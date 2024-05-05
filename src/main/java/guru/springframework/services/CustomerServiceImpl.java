package guru.springframework.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import guru.springframework.model.CustomerDto;

@Service
public class CustomerServiceImpl implements CustomerService {
	
	private Map<UUID, CustomerDto> customerMap;
	
	public CustomerServiceImpl() {
		CustomerDto customer1 = CustomerDto.builder()
				.id(UUID.randomUUID())
				.customerName("Tony Stark")
				.createdDate(LocalDateTime.of(2023, 1, 12, 9, 0, 0)) 
				.lastModifiedDate(LocalDateTime.now())
				.build();
		
		CustomerDto customer2 = CustomerDto.builder()
				.id(UUID.randomUUID())
				.customerName("Mitchel Johnson")
				.createdDate(LocalDateTime.of(2023, 10, 12, 9, 0, 0)) 
				.lastModifiedDate(LocalDateTime.now())
				.build();
		
		CustomerDto customer3 = CustomerDto.builder()
				.id(UUID.randomUUID())
				.customerName("John Doe")
				.createdDate(LocalDateTime.of(2023, 11, 11, 9, 0, 0)) 
				.lastModifiedDate(LocalDateTime.now())
				.build();
		
		customerMap = new HashMap<>();
		customerMap.put(customer1.getId(), customer1);
		customerMap.put(customer2.getId(), customer2);
		customerMap.put(customer3.getId(), customer3);
	}
	
	

	@Override
	public List<CustomerDto> listCustomers() {
		return new ArrayList<>(customerMap.values());
	}

	@Override
	public Optional<CustomerDto> getCustomerById(UUID id) {
		return Optional.of(customerMap.get(id));
	}



	@Override
	public CustomerDto addNewCustomer(CustomerDto customer) {
		CustomerDto savedCustomer = CustomerDto.builder()
				.id(UUID.randomUUID())
				.customerName(customer.getCustomerName())
				.createdDate(LocalDateTime.now())
				.lastModifiedDate(LocalDateTime.now())
				.version(customer.getVersion())
				.build();
		
		customerMap.put(savedCustomer.getId(), savedCustomer);
		
		return savedCustomer;
	}



	@Override
	public Optional<CustomerDto> updateCustomer(UUID id, CustomerDto customer) {
		CustomerDto existing = customerMap.get(id);
		existing.setCustomerName(customer.getCustomerName());
		existing.setVersion(customer.getVersion());
		existing.setLastModifiedDate(LocalDateTime.now());
		
		CustomerDto updated = existing;
		return Optional.of(updated);
	}



	@Override
	public Boolean deleteBeerById(UUID id) {
		customerMap.remove(id);
		return true;
	}

}
