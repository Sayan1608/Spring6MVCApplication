package guru.springframework.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import guru.springframework.mappers.CustomerMapper;
import guru.springframework.model.CustomerDto;
import guru.springframework.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Primary
public class CustomerJpaService implements CustomerService {

	private final CustomerRepository customerRepository;
	private final CustomerMapper customerMapper;

	@Override
	public List<CustomerDto> listCustomers() {
		return customerRepository.findAll().stream().map(customerMapper::customerToCustomerDto)
				.collect(Collectors.toList());
	}

	@Override
	public Optional<CustomerDto> getCustomerById(UUID id) {
		return Optional.ofNullable(customerMapper.customerToCustomerDto(customerRepository.findById(id).orElse(null)));
	}

	@Override
	public CustomerDto addNewCustomer(CustomerDto customer) {
		return customerMapper
				.customerToCustomerDto(customerRepository.save(customerMapper.customerDtoToCustomer(customer)));
	}

	@Override
	public Optional<CustomerDto> updateCustomer(UUID id, CustomerDto customer) {
		
		AtomicReference<Optional<CustomerDto>> atomicReference = new AtomicReference<>();
		
		customerRepository.findById(id).ifPresentOrElse(foundCustomer ->{
			foundCustomer.setCustomerName(customer.getCustomerName());
			foundCustomer.setCreatedDate(customer.getCreatedDate());
			 
			atomicReference.set(Optional.of(customerMapper.customerToCustomerDto(customerRepository.save(foundCustomer))));
		}, ()->{
			atomicReference.set(Optional.empty());
		});
		
		return atomicReference.get();
	}

	@Override
	public Boolean deleteBeerById(UUID id) {
		if(customerRepository.existsById(id)) {
			customerRepository.deleteById(id);
			return true;
		}
		return false;
	}

}
