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

import guru.springframework.entities.Beer;
import guru.springframework.mappers.BeerMapper;
import guru.springframework.model.BeerDto;
import guru.springframework.repositories.BeerRepository;
import jakarta.transaction.Transactional;

@SpringBootTest
class BeerControllerIT {

	@Autowired
	BeerController beerController;

	@Autowired
	BeerRepository beerRepository;
	
	@Autowired
	BeerMapper beerMapper;

	@Test
	void testListBeers() {
		List<BeerDto> listBeers = beerController.listBeers();

		assertThat(listBeers.size()).isEqualTo(3);
	}

	@Rollback
	@Transactional
	@Test
	void testEmptyList() {
		beerRepository.deleteAll();

		List<BeerDto> listBeers = beerController.listBeers();

		assertThat(listBeers.size()).isEqualTo(0);
	}

	@Test
	void testBeerById() {
		Beer beer = beerRepository.findAll().get(0);

		BeerDto beerDto = beerController.getBeerById(beer.getId());

		assertThat(beerDto).isNotNull();
	}

	@Test
	void testBeerIdNotFound() {
		assertThrows(NotFoundException.class, () -> beerController.getBeerById(UUID.randomUUID()));

	}

	@Transactional
	@Rollback
	@Test
	void testSaveBeer() {
		BeerDto beer = BeerDto.builder().beerName("New Beer").build();

		ResponseEntity<String> responseEntity = beerController.addNewBeer(beer);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
		assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

		String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
		UUID savedUUID = UUID.fromString(locationUUID[4]);

		Beer savedBeer = beerRepository.findById(savedUUID).get();

		assertThat(savedBeer).isNotNull();

	}

	@Test
	@Transactional
	@Rollback
	void testUpdateBeer() {
		Beer beer = beerRepository.findAll().get(0);
		BeerDto beerDto = beerMapper.beerToBeerDto(beer);
		
		beerDto.setId(null);
		beerDto.setVersion(null);
		
		final String beerName = "UPDATED";
		
		beerDto.setBeerName(beerName);
		
		ResponseEntity<String> responseEntity = beerController.updateBeer(beer.getId(), beerDto);
		
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
		assertThat(responseEntity.getHeaders().getLocation()).isNotNull();
		
		Beer updatBeer = beerRepository.findById(beer.getId()).get();
		
		assertThat(updatBeer.getBeerName()).isEqualTo(beerName);

	}

	@Test
	void testUpdateBeerIdNotFound() {
		BeerDto beerDto = beerController.listBeers().get(0);
		assertThrows(NotFoundException.class, () -> beerController.updateBeer(UUID.randomUUID(), beerDto));
	}
	
	@Test
	@Transactional
	@Rollback
	void deleteBeerById() {
		Beer beer = beerRepository.findAll().get(0);
		ResponseEntity<String> responseEntity = beerController.deleteBeerById(beer.getId());
		
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
		assertThat(responseEntity.getHeaders().getLocation()).isNotNull();
		
		assertThat(beerRepository.findById(beer.getId())).isEmpty();
	}
	
	@Test
	void deleteBeerIdNotFound() {
		assertThrows(NotFoundException.class, ()->{
			beerController.deleteBeerById(UUID.randomUUID());
		});
	}

}
