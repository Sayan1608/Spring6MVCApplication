package guru.springframework.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import guru.springframework.model.CustomerDto;
import guru.springframework.services.BeerService;
import guru.springframework.services.CustomerService;
import guru.springframework.services.CustomerServiceImpl;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	
	@MockBean
	CustomerService customerService;
	
	CustomerServiceImpl customerServiceImpl;
	
	@BeforeEach
	void setUp() {
		customerServiceImpl = new CustomerServiceImpl();
	}

	@Test
	void testListCustomers() throws Exception {
		
		given(customerService.listCustomers()).willReturn(customerServiceImpl.listCustomers());
		
		mockMvc.perform(get(CustomerController.CUSTOMER_PATH).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.length()", is(3)));
	}

	@Test
	void testGetCustomerById() throws Exception {
		CustomerDto testCustomer = customerServiceImpl.listCustomers().get(0);
		
		given(customerService.getCustomerById(testCustomer.getId())).willReturn(Optional.of(testCustomer));
		
		mockMvc.perform(get(CustomerController.CUSTOMER_PATH_ID, testCustomer.getId()).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.id", is(testCustomer.getId().toString())))
		.andExpect(jsonPath("$.customerName", is(testCustomer.getCustomerName())));
	}
	
	@Test
	void testCreateNewCustomer() throws Exception {
		CustomerDto customer = customerServiceImpl.listCustomers().get(0);
		
		given(customerService.addNewCustomer(any(CustomerDto.class))).willReturn(customerServiceImpl.listCustomers().get(1));
		
		mockMvc.perform(post(CustomerController.CUSTOMER_PATH).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(customer)))
		.andExpect(status().isCreated())
		.andExpect(header().exists("Location"));
		
	}
	
	@Test
	void testUpdateCustomer() throws JsonProcessingException, Exception {
		CustomerDto customer = customerServiceImpl.listCustomers().get(0);
		
		
		given(customerService.updateCustomer(any(), any())).willReturn(Optional.of(customer));
		
		mockMvc.perform(put(CustomerController.CUSTOMER_PATH_ID, customer.getId())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(customer)))
		.andExpect(status().isOk());
		
		verify(customerService).updateCustomer(any(UUID.class), any(CustomerDto.class));
		
	}
	
	@Test
	void testDeleteCustomer() throws Exception {
		CustomerDto customer = customerServiceImpl.listCustomers().get(0);
		
		given(customerService.deleteBeerById(any())).willReturn(true);
		
		mockMvc.perform(delete(CustomerController.CUSTOMER_PATH_ID, customer.getId())
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
		
		ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
		
		verify(customerService).deleteBeerById(uuidArgumentCaptor.capture());
		
		assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
		
		
	}
	
	@Test
	void getCustomerByIdNotFound() throws Exception {
		
//		given(customerService.getCustomerById(any(UUID.class))).willThrow(NotFoundException.class);
		given(customerService.getCustomerById(any(UUID.class))).willReturn(Optional.empty());
		
		mockMvc.perform(get(CustomerController.CUSTOMER_PATH_ID, UUID.randomUUID())
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
	}
	

}
