package guru.springframework.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import guru.springframework.model.BeerDto;
import guru.springframework.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BeerServiceImpl implements BeerService {
	
	private Map<UUID, BeerDto> beerMap;
	
	public BeerServiceImpl() {
		BeerDto beer1 = BeerDto.builder()
				.id(UUID.randomUUID())
				.beerName("Heiniken")
				.beerstyle(BeerStyle.ALE)
				.price(BigDecimal.valueOf(157.25))
				.upc("UPC1")
				.version(1)
				.createdDate(LocalDateTime.now())
				.updatedDate(LocalDateTime.now())
				.build();
		
		BeerDto beer2 = BeerDto.builder()
				.id(UUID.randomUUID())
				.beerName("Carlsberg")
				.beerstyle(BeerStyle.GOSE)
				.price(BigDecimal.valueOf(185.25))
				.upc("UPC1")
				.version(1)
				.createdDate(LocalDateTime.now())
				.updatedDate(LocalDateTime.now())
				.build();
		
		BeerDto beer3 = BeerDto.builder()
				.id(UUID.randomUUID())
				.beerName("Budweiser")
				.beerstyle(BeerStyle.LAGER)
				.price(BigDecimal.valueOf(160.25))
				.upc("UPC1")
				.version(1)
				.createdDate(LocalDateTime.now())
				.updatedDate(LocalDateTime.now())
				.build();
		
		this.beerMap = new HashMap<>();
		beerMap.put(beer1.getId(), beer1);
		beerMap.put(beer2.getId(), beer2);
		beerMap.put(beer3.getId(), beer3);
		
	}
	
	@Override
	public List<BeerDto> listBeers(){
		return new ArrayList<>(beerMap.values());
	}
	

	@Override
	public Optional<BeerDto> getBeerById(UUID id) {
		log.debug("getBeerById method in BeerServiceImpl is called " + id);
		return Optional.of(beerMap.get(id));
	}

	@Override
	public BeerDto saveNewBeer(BeerDto beer) {
		BeerDto savedBeer = BeerDto.builder()
				.id(UUID.randomUUID())
				.beerName(beer.getBeerName())
				.beerstyle(beer.getBeerstyle())
				.createdDate(LocalDateTime.now())
				.updatedDate(LocalDateTime.now())
				.upc(beer.getUpc())
				.build();
		
		beerMap.put(savedBeer.getId(), savedBeer);
		
		return savedBeer;
		
	}

	@Override
	public Optional<BeerDto> updateBeer(UUID id, BeerDto beer) {
		
		BeerDto existing = beerMap.get(id);
		existing.setBeerName(beer.getBeerName());
		existing.setBeerstyle(beer.getBeerstyle());
		existing.setPrice(beer.getPrice());
		existing.setUpdatedDate(beer.getUpdatedDate());
		existing.setUpc(beer.getUpc());
		existing.setQuantityOnHand(beer.getQuantityOnHand());
		existing.setVersion(beer.getVersion());
		
		BeerDto updated = existing;
		
		return Optional.of(updated);
		
	}

	@Override
	public Boolean deleteBeerById(UUID id) {
		beerMap.remove(id);
		return true;
		
	}

}
