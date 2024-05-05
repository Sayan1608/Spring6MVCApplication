package guru.springframework.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import guru.springframework.model.CustomerDto;

public interface CustomerService {
	
	List<CustomerDto> listCustomers();
	Optional<CustomerDto> getCustomerById(UUID id);
	CustomerDto addNewCustomer(CustomerDto customer);
	Optional<CustomerDto> updateCustomer(UUID id, CustomerDto customer);
	Boolean deleteBeerById(UUID id);

}
