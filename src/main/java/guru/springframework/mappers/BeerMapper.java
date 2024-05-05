package guru.springframework.mappers;

import org.mapstruct.Mapper;

import guru.springframework.entities.Beer;
import guru.springframework.model.BeerDto;

@Mapper
public interface BeerMapper {
	
	Beer beerDtoToBeer(BeerDto beerDto);
	
	BeerDto beerToBeerDto(Beer beer);
	
	

}
