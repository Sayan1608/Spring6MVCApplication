package guru.springframework.bootstrap;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import guru.springframework.entities.Beer;
import guru.springframework.entities.Customer;
import guru.springframework.model.BeerDto;
import guru.springframework.model.BeerStyle;
import guru.springframework.model.CustomerDto;
import guru.springframework.repositories.BeerRepository;
import guru.springframework.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BootStrapData implements CommandLineRunner{
	
	@Autowired
	private final BeerRepository beerRepository;
	@Autowired
	private final CustomerRepository customerRepository;

	@Override
	public void run(String... args) throws Exception {
		loadBeerData();
		loadCustomerData();
	}

	public void loadBeerData() {
		if(beerRepository.count() == 0) {
			
			Beer beer1 = Beer.builder()
					.beerName("Heiniken")
					.beerstyle(BeerStyle.ALE)
					.price(BigDecimal.valueOf(157.25))
					.upc("UPC1")
					.createdDate(LocalDateTime.now())
					.updatedDate(LocalDateTime.now())
					.build();
			
			Beer beer2 = Beer.builder()
					.beerName("Carlsberg")
					.beerstyle(BeerStyle.GOSE)
					.price(BigDecimal.valueOf(185.25))
					.upc("UPC1")
					.createdDate(LocalDateTime.now())
					.updatedDate(LocalDateTime.now())
					.build();
			
			Beer beer3 = Beer.builder()
					.beerName("Budweiser")
					.beerstyle(BeerStyle.LAGER)
					.price(BigDecimal.valueOf(160.25))
					.upc("UPC1")
					.createdDate(LocalDateTime.now())
					.updatedDate(LocalDateTime.now())
					.build();
			
			beerRepository.save(beer1);
			beerRepository.save(beer2);
			beerRepository.save(beer3);
		}
		
		
	}
	
	public void loadCustomerData() {
		if(customerRepository.count() == 0) {
			Customer customer1 = Customer.builder()
					.customerName("Tony Stark")
					.createdDate(LocalDateTime.of(2023, 1, 12, 9, 0, 0)) 
					.lastModifiedDate(LocalDateTime.now())
					.build();
			
			Customer customer2 = Customer.builder()
					.customerName("Mitchel Johnson")
					.createdDate(LocalDateTime.of(2023, 10, 12, 9, 0, 0)) 
					.lastModifiedDate(LocalDateTime.now())
					.build();
			
			Customer customer3 = Customer.builder()
					.customerName("John Doe")
					.createdDate(LocalDateTime.of(2023, 11, 11, 9, 0, 0)) 
					.lastModifiedDate(LocalDateTime.now())
					.build();
			
			customerRepository.saveAll(Arrays.asList(customer1, customer2, customer3));
		}
		
		
	}

}
