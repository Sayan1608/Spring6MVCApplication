package guru.springframework.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import guru.springframework.model.BeerDto;
import guru.springframework.services.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@AllArgsConstructor
@Slf4j
public class BeerController {
	
	public static final String BEER_PATH = "/api/v1/beers";
	public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";
	
	private final BeerService beerService;
	
	@GetMapping(BEER_PATH)
	public List<BeerDto> listBeers(){
		log.debug("Listing beers...");
		return beerService.listBeers();
	}
	
	@GetMapping(BEER_PATH_ID)
	public BeerDto getBeerById(@PathVariable("beerId") UUID id) {
		log.debug("getBeerById method in BeerController is called " + id);
		return  beerService.getBeerById(id).orElseThrow(NotFoundException::new);
	}
	
	@PostMapping(BEER_PATH)
	public ResponseEntity<String> addNewBeer(@Validated @RequestBody BeerDto beer){
		log.debug("addNewBeer method in BeerController is called");
		BeerDto savedBeer = beerService.saveNewBeer(beer);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", BEER_PATH + "/" + savedBeer.getId().toString());
		return new ResponseEntity<String>("Beer Added Successfully", headers, HttpStatus.CREATED) ;
	}
	
	@PutMapping(BEER_PATH_ID)
	public ResponseEntity<String> updateBeer(@PathVariable("beerId") UUID id, @Validated @RequestBody BeerDto beer){
		log.debug("updating beer with id: " + id );
		Optional<BeerDto> updatedBeer = beerService.updateBeer(id, beer);
		if(updatedBeer.isEmpty()) {
			throw new NotFoundException();
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", BEER_PATH);
		return new ResponseEntity<String>("Beer Updated Successfully", headers, HttpStatus.OK) ;
	}
	
	@DeleteMapping(BEER_PATH_ID)
	public ResponseEntity<String> deleteBeerById(@PathVariable("beerId") UUID id){
		log.debug("deleting beer with id : " + id);
		if(!beerService.deleteBeerById(id)) {
			throw new NotFoundException();
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", BEER_PATH +"/"+id.toString());
		return new ResponseEntity<String>("Beer Deleted Successfully", headers, HttpStatus.OK);
	}
	
	/*@ExceptionHandler(NotFoundException.class)
	public ResponseEntity handleNotFoundException() {
		return ResponseEntity.notFound().build();
	}*/

}
