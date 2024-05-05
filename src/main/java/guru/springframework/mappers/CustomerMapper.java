package guru.springframework.mappers;

import org.mapstruct.Mapper;

import guru.springframework.entities.Customer;
import guru.springframework.model.CustomerDto;

@Mapper
public interface CustomerMapper {
	
	Customer customerDtoToCustomer(CustomerDto customerDto);
	
	CustomerDto customerToCustomerDto(Customer customer);

}
