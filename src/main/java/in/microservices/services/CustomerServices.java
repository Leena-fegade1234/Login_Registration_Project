package in.microservices.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import in.microservices.entities.Customer;
import in.microservices.repository.CustomerRepository;
import jakarta.transaction.Transactional;


@Service
@Transactional
public class CustomerServices implements UserDetailsService {

	@Autowired
    private CustomerRepository repo;
	
	public void updateResetPasswordToken(String token, String email) throws Exception {
        Customer customer = repo.findByEmail(email);
        if (customer != null) {
            customer.setResetPasswordToken(token);
            repo.save(customer);
        } else {
            throw new Exception("Could not find any customer with the email " + email);
        }
    }
     
    public Customer getByResetPasswordToken(String token) {
        return repo.findByResetPasswordToken(token);
    }
     
    public void updatePassword(Customer customer, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        customer.setPassword(encodedPassword);
         
        customer.setResetPasswordToken(null);
        repo.save(customer);
    }
    
    public UserDetails loadUserByUsername(String username) {
       Customer customer = repo.findByEmail(username);
        if (customer == null) {
            try {
				throw new Exception("Customer not found");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return new CustomUserDetails(customer);
    }
}
