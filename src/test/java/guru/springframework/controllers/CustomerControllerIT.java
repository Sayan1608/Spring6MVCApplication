package guru.springframework.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import guru.springframework.entities.Customer;
import guru.springframework.mappers.CustomerMapper;
import guru.springframework.model.CustomerDto;
import guru.springframework.repositories.BeerRepository;
import guru.springframework.repositories.CustomerRepository;
import jakarta.transaction.Transactional;

@SpringBootTest
class CustomerControllerIT {

	@Autowired
	CustomerController customerController;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	CustomerMapper customerMapper;

	@Test
	@Transactional
	@Rollback
	void testEmptyList() {
		customerRepository.deleteAll();

		List<CustomerDto> customers = customerController.listCustomers();

		assertThat(customers.size()).isEqualTo(0);
	}

	@Test
	void testListCustomers() {
		List<CustomerDto> customers = customerController.listCustomers();

		assertThat(customers.size()).isEqualTo(3);
	}

	@Test
	void testGetCustomerById() {
		Customer customer = customerRepository.findAll().get(0);

		CustomerDto customerDto = customerController.getCustomerById(customer.getId());

		assertThat(customerDto).isNotNull();
	}

	@Test
	void testCustomerIdNotFound() {

		assertThrows(NotFoundException.class, () -> customerController.getCustomerById(UUID.randomUUID()));

	}

	@Test
	@Transactional
	@Rollback
	void testSaveCustomer() {
		CustomerDto customerDto = CustomerDto.builder().customerName("New Customer").build();

		ResponseEntity<String> responseEntity = customerController.addNewCustomer(customerDto);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
		assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

		String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");

		UUID customerUUID = UUID.fromString(locationUUID[4]);

		assertThat(customerRepository.findById(customerUUID).get()).isNotNull();
	}

	@Test
	@Transactional
	@Rollback
	void testUpdateCustomer() {
		Customer customer = customerRepository.findAll().get(0);

		CustomerDto customerDto = customerMapper.customerToCustomerDto(customer);

		customerDto.setId(null);
		customerDto.setVersion(null);

		final String customerName = "UPDATED";

		customerDto.setCustomerName(customerName);

		ResponseEntity<String> responseEntity = customerController.updateCustomer(customer.getId(), customerDto);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
		assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

		Customer updatedCustomer = customerRepository.findById(customer.getId()).get();

		assertThat(updatedCustomer.getCustomerName()).isEqualTo(customerName);
	}

	@Test
	void testUpdateCustomerNotFound() {
		assertThrows(NotFoundException.class,
				() -> customerController.updateCustomer(UUID.randomUUID(), CustomerDto.builder().build()));

	}

	@Test
	@Transactional
	@Rollback
	void deleteCustomerById() {
		Customer customer = customerRepository.findAll().get(0);

		ResponseEntity<String> responseEntity = customerController.deleteCustomerById(customer.getId());

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));

		assertThat(customerRepository.findById(customer.getId())).isEmpty();

	}
	
	@Test
	void testDeleteCustomerIdNotFound() {
		assertThrows(NotFoundException.class, ()-> customerController.deleteCustomerById(UUID.randomUUID()));
	}

}
