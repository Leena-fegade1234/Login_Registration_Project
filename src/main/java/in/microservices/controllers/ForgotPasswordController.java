package in.microservices.controllers;

import java.io.UnsupportedEncodingException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import in.microservices.entities.Customer;
import in.microservices.services.CustomerServices;
import in.microservices.services.Utility;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import net.bytebuddy.utility.RandomString;


@Controller
public class ForgotPasswordController {

	@Autowired
    private JavaMailSender mailSender;
     
    @Autowired
    private CustomerServices customerService;
     
    @GetMapping("/forgot_password")
    public String showForgotPasswordForm(Model model) {
    	model.addAttribute("pageTitle", "Forgot Password");
    	return "forgot_password_form";
		
 
    }
 
    @PostMapping("/forgot_password")
    public String processForgotPassword(HttpServletRequest request, Model model) {
    	String email = request.getParameter("email");
        String token = RandomString.make(30);
         
        System.out.println("Email :"  +email);
        System.out.println("Token:"  +token);
        try {
            customerService.updateResetPasswordToken(token, email);
            String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;
            sendEmail(email, resetPasswordLink);
            model.addAttribute("message", "We have sent a reset password link to your email. Please check.");
             
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
        } 
    	return "forgot_password_form";
		
    }
     
    private void sendEmail(String email, String resetPasswordLink) throws UnsupportedEncodingException, MessagingException {
    	
    	String recipientEmail = "djjagtap123@gmail.com";
    	 MimeMessage message = mailSender.createMimeMessage();              
    	    MimeMessageHelper helper = new MimeMessageHelper(message);
    	     
    	    helper.setFrom("djjagtap123@gmail.com");
    	    helper.setTo(recipientEmail);
    	     
    	    String subject = "Here's the link to reset your password";
    	     
    	    String link = "http://contextpath/reset_password?token=random_token";
			String content = "<p>Hello,</p>"
    	            + "<p>You have requested to reset your password.</p>"
    	            + "<p>Click the link below to change your password:</p>"
    	            + "<p><a href=\"" + link  + "\">Change my password</a></p>"
    	            + "<br>"
    	            + "<p>Ignore this email if you do remember your password, "
    	            + "or you have not made the request.</p>";
    	     
    	    helper.setSubject(subject);
    	     
    	    helper.setText(content, true);
    	     
    	    mailSender.send(message);
		
	}

     
     
    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
    	Customer customer = customerService.getByResetPasswordToken(token);
        model.addAttribute("token", token);
         
        if (customer == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        }
         
    	return "reset_password_form";
 
    }
     
    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest request, Model model) {
    	
    	 String token = request.getParameter("token");
    	    String password = request.getParameter("password");
    	     
    	    Customer customer = customerService.getByResetPasswordToken(token);
    	    model.addAttribute("title", "Reset your password");
    	     
    	    if (customer == null) {
    	        model.addAttribute("message", "Invalid Token");
    	        return "message";
    	    } else {           
    	        customerService.updatePassword(customer, password);
    	         
    	        model.addAttribute("message", "You have successfully changed your password.");
    	    }
    	 return "message";
 
    }
}
