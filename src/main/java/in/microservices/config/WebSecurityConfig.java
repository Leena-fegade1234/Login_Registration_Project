package in.microservices.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import in.microservices.services.CustomerServices;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
	@Autowired
    private DataSource dataSource;
	
	
	@Bean
    public CustomerServices customerServices() {
        return new CustomerServices();
    }
	
	@Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	
	@Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customerServices());
        authProvider.setPasswordEncoder(passwordEncoder());
         
        return authProvider;
    }
	     
	

	@Autowired
	protected void configure(AuthenticationManagerBuilder  auth) throws Exception{
		auth.authenticationProvider(authenticationProvider());
	}
	     
	@Lazy
	@Bean
	public SecurityFilterChain securityConfig(HttpSecurity  http) throws Exception {
		
		
		http.authorizeHttpRequests( (req) -> req
				.requestMatchers("/users").authenticated()
				.anyRequest().permitAll()
				.and()
				).formLogin()
		           .usernameParameter("email")
		           .defaultSuccessUrl("/users",true)
		           .permitAll()
		       .and()
		           
		       .logout().logoutSuccessUrl("/logout").permitAll();
		return http.build();
		
	}


}
