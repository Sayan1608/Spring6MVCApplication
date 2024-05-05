package guru.springframework.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import guru.springframework.entities.Customer;

@DataJpaTest
class CustomerRepositoryTest {
	
	@Autowired
	CustomerRepository customerRepository;

	@Test
	void testSaveCustomer() {
		Customer savedCustomer = customerRepository.save(Customer.builder()
				.customerName("My Customer")
				.build());
		
		assertThat(savedCustomer).isNotNull();
		assertThat(savedCustomer.getId()).isNotNull();
	}

}
