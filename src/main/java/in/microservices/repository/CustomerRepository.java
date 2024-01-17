package in.microservices.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import in.microservices.entities.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long>{

	@Query("SELECT c FROM Customer c WHERE c.email = ?1")
	public Customer findByEmail(String username);
	
	public Customer findByResetPasswordToken(String token);
	
	

	
}
