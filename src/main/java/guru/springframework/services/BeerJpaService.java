package guru.springframework.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import guru.springframework.entities.Beer;
import guru.springframework.mappers.BeerMapper;
import guru.springframework.model.BeerDto;
import guru.springframework.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Primary
public class BeerJpaService implements BeerService {
	private final BeerRepository beerRepository;
	private final BeerMapper beerMapper;

	@Override
	public Optional<BeerDto> getBeerById(UUID id) {
		return Optional.ofNullable(beerMapper.beerToBeerDto(beerRepository.findById(id).orElse(null)));
	}

	@Override
	public List<BeerDto> listBeers() {
		
//		Function<Beer, BeerDto> func = (t) -> beerMapper.beerToBeerDto(t);
	
		return beerRepository.findAll()
				.stream()
				.map(beerMapper :: beerToBeerDto)
				.collect(Collectors.toList());
	}

	@Override
	public BeerDto saveNewBeer(BeerDto beer) {
		return beerMapper.beerToBeerDto(beerRepository.save(beerMapper.beerDtoToBeer(beer)));
	}

	@Override
	public Optional<BeerDto> updateBeer(UUID id, BeerDto beer) {
		
		AtomicReference<Optional<BeerDto>> atomicReference = new AtomicReference<>();
		
		beerRepository.findById(id).ifPresentOrElse (foundBeer ->{
			foundBeer.setBeerName(beer.getBeerName());
			foundBeer.setBeerstyle(beer.getBeerstyle());
			foundBeer.setUpc(beer.getUpc());
			foundBeer.setPrice(beer.getPrice());
			foundBeer.setCreatedDate(beer.getCreatedDate());
			
			beerRepository.save(foundBeer);
			
			atomicReference.set(Optional.of(beerMapper.beerToBeerDto(beerRepository.save(foundBeer))));
		}, ()->{
			atomicReference.set(Optional.empty());
		});
		
		return atomicReference.get();
	}

	@Override
	public Boolean deleteBeerById(UUID id) {
		if(beerRepository.existsById(id)) {
			beerRepository.deleteById(id);
			return true;
		}
		return false;
	}

}
