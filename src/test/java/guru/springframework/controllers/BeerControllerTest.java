package guru.springframework.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.math.BigInteger;
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
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import guru.springframework.entities.Beer;
import guru.springframework.model.BeerDto;
import guru.springframework.services.BeerService;
import guru.springframework.services.BeerServiceImpl;

@WebMvcTest(BeerController.class)
class BeerControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@MockBean
	BeerService beerService;
	
	BeerServiceImpl beerServiceImpl;
	
	@BeforeEach
	void setUp() {
		beerServiceImpl = new BeerServiceImpl();
	}
	

	@Test
	void testGetBeerById() throws Exception {
		BeerDto testBeer = beerServiceImpl.listBeers().get(0);
		
//		given(beerService.getBeerById(any(UUID.class))).willReturn(testBeer);
		
		given(beerService.getBeerById(testBeer.getId())).willReturn(Optional.of(testBeer));
		
		mockMvc.perform(get(BeerController.BEER_PATH_ID, testBeer.getId())
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
		.andExpect(jsonPath("$.beerName", is(testBeer.getBeerName())));
	}
	
	@Test
	void testListBeers() throws Exception {
		given(beerService.listBeers()).willReturn(beerServiceImpl.listBeers());
		
		mockMvc.perform(get(BeerController.BEER_PATH)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.length()", is(3)));
		
	}
	
	@Test
	void testCreateNewBeer() throws Exception {
		
		BeerDto testBeer = beerServiceImpl.listBeers().get(0);
		testBeer.setVersion(null);
		testBeer.setId(null);
		
		given(beerService.saveNewBeer(any(BeerDto.class))).willReturn(beerServiceImpl.listBeers().get(1));
		
		mockMvc.perform(post(BeerController.BEER_PATH).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(testBeer)))
		.andExpect(status().isCreated())
		.andExpect(header().exists("Location"));
		
		verify(beerService).saveNewBeer(any(BeerDto.class));
		
//		ObjectMapper objectMapper = new ObjectMapper();
		
//		objectMapper.findAndRegisterModules();
		
//		System.out.println(objectMapper.writeValueAsString(testBeer));
	}
	
	@Test
	void testUpdateBeer() throws JsonProcessingException, Exception {
		BeerDto beer = beerServiceImpl.listBeers().get(0);
		
		given(beerService.updateBeer(any(), any())).willReturn(Optional.of(beer));
		
		mockMvc.perform(put(BeerController.BEER_PATH_ID, beer.getId())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(beer)))
		.andExpect(status().isOk());
		
		verify(beerService).updateBeer(any(UUID.class), any(BeerDto.class));
	}
	
	@Test
	void testDeleteBeer() throws JsonProcessingException, Exception {
		BeerDto beer = beerServiceImpl.listBeers().get(0);
		
		given(beerService.deleteBeerById(any())).willReturn(true);
		
		mockMvc.perform(delete(BeerController.BEER_PATH_ID, beer.getId())
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
		
		ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
		
		verify(beerService).deleteBeerById(uuidArgumentCaptor.capture());
		
		assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
		
		
	}
	
	@Test
	void getBeerByIdNotFound() throws Exception {
		
//		given(beerService.getBeerById(any(UUID.class))).willThrow(NotFoundException.class);
		
		given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.empty());
		
		mockMvc.perform(get(BeerController.BEER_PATH_ID, UUID.randomUUID())
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
	}
	
	@Test
	void testBlankOrNullBeerName() throws JsonProcessingException, Exception {
		BeerDto beerDto = BeerDto.builder()
				.price(BigDecimal.valueOf(0))
				.build();
		
		given(beerService.saveNewBeer(any(BeerDto.class))).willReturn(beerServiceImpl.listBeers().get(1));
		
		MvcResult mvcResult = mockMvc.perform(post(BeerController.BEER_PATH)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(beerDto)))
		.andExpect(jsonPath("$.length()", is(6)))		
		.andExpect(status().isBadRequest()).andReturn();
		
		System.out.println(mvcResult.getResponse().getContentAsString());
	}
	
	@Test
	void testUpdateBlankBeerName() throws JsonProcessingException, Exception {
		BeerDto beer = beerServiceImpl.listBeers().get(0);
		beer.setBeerName("");
		
		given(beerService.updateBeer(any(), any())).willReturn(Optional.of(beer));
		
		mockMvc.perform(put(BeerController.BEER_PATH_ID, beer.getId())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(beer)))
		.andExpect(status().isBadRequest());
		
	}
	
	

}
