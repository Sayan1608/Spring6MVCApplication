package guru.springframework.bootstrap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import guru.springframework.repositories.BeerRepository;
import guru.springframework.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;

@DataJpaTest
@RequiredArgsConstructor
class BootStrapDataTest {
	
	@Autowired
	private  BeerRepository beerRepository;
	
	@Autowired
	private  CustomerRepository customerRepository;
	
	private BootStrapData bootStrapData;
	
	@BeforeEach
	public void setUp() {
		bootStrapData = new BootStrapData(beerRepository, customerRepository);
	}
	
	

	@Test
	void test() throws Exception {
		bootStrapData.run(null);
		
		assertThat(beerRepository.count()).isEqualTo(3);
		assertThat(customerRepository.count()).isEqualTo(3);
	}

}
