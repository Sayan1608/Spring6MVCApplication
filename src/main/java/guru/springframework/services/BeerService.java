package guru.springframework.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;

import guru.springframework.model.BeerDto;

public interface BeerService {
	Optional<BeerDto> getBeerById(UUID id);

	List<BeerDto> listBeers();

	BeerDto saveNewBeer(BeerDto beer);

	Optional<BeerDto> updateBeer(UUID id, BeerDto beer);
	
	Boolean deleteBeerById(UUID id);
}
