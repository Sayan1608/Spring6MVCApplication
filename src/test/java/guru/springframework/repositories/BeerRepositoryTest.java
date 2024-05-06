package guru.springframework.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import guru.springframework.entities.Beer;
import guru.springframework.model.BeerStyle;

@DataJpaTest
class BeerRepositoryTest {
	
	@Autowired
	BeerRepository beerRepository;

	@Test
	void testSaveBeer() {
		Beer savedBeer = beerRepository.save(Beer.builder()
				.beerName("My Beer")
				.beerstyle(BeerStyle.ALE)
				.upc("1246133112132")
				.price(BigDecimal.valueOf(100))
				.build());
		
		beerRepository.flush();
		
		assertThat(savedBeer).isNotNull();
		assertThat(savedBeer.getId()).isNotNull();
	}

}
