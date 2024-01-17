package in.microservices.tests;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import in.microservices.entities.Customer;
import in.microservices.repository.CustomerRepository;


@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CustomerRepositoryTests {

	
	    @Autowired
	    private TestEntityManager entityManager;
	     
	    @Autowired
	    private CustomerRepository repo;
	    
	    @Test
	    public void testCreateUser() {
	       Customer customer = new Customer();
	       customer.setEmail("leena@gmail.com");
	       customer.setPassword("leena#");
	       customer.setFirstName("Leena");
	       customer.setLastName("Patil");
	         
	       Customer savedcustomer = repo.save(customer);
	         
	       Customer existCustomer = entityManager.find(Customer.class, savedcustomer.getId());
	         
	        assertThat(customer.getEmail()).isEqualTo(existCustomer.getEmail());
	         
	    }
}
