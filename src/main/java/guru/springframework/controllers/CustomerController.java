package guru.springframework.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import guru.springframework.model.CustomerDto;
import guru.springframework.services.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
public class CustomerController {
	
	public static final String CUSTOMER_PATH = "/api/v1/customers";
	public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{customerId}";
	
	
	private final CustomerService customerService;
	
	@GetMapping(CUSTOMER_PATH)
	public List<CustomerDto> listCustomers(){
		return customerService.listCustomers();
	}
	
	@GetMapping(CUSTOMER_PATH_ID)
	public CustomerDto getCustomerById(@PathVariable("customerId") UUID id) {
		return customerService.getCustomerById(id).orElseThrow(NotFoundException::new);
	}
	
	@PostMapping(CUSTOMER_PATH)
	public ResponseEntity<String> addNewCustomer(@RequestBody CustomerDto customer){
		CustomerDto savedCustomer = customerService.addNewCustomer(customer);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", CUSTOMER_PATH +"/" +savedCustomer.getId());
		return new ResponseEntity<String>("Customer Added Successfully", headers, HttpStatus.CREATED);
	}
	
	@PutMapping(CUSTOMER_PATH_ID)
	public ResponseEntity<String> updateCustomer(@PathVariable("customerId") UUID id, @RequestBody CustomerDto customer){
		Optional<CustomerDto> updatedCustomer = customerService.updateCustomer(id, customer);
		if(updatedCustomer.isEmpty()) {
			throw new NotFoundException();
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", CUSTOMER_PATH);
		return new ResponseEntity<String>("Customer Updated Successfully", headers, HttpStatus.OK);
	}
	
	@DeleteMapping(CUSTOMER_PATH_ID)
	public ResponseEntity<String> deleteCustomerById(@PathVariable("customerId") UUID id){
		log.debug("deleting customer with id : " + id);
		if(!customerService.deleteBeerById(id)) {
			throw new NotFoundException();
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", "/api/v1/customers/"+id);
		return new ResponseEntity<String>("Customer Deleted Successfully", headers, HttpStatus.OK);
	}
	
	
}
