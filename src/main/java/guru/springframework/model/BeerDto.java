package guru.springframework.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BeerDto {
	private UUID id;
	private Integer version;
	private String beerName;
	private BeerStyle beerstyle;
	private String upc;
	private Integer quantityOnHand;
	private BigDecimal price;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;
	
}
