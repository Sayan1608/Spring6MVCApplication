package guru.springframework.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BeerDto {
	private UUID id;
	private Integer version;
	@NotBlank
	@NotNull
	private String beerName;
	@NotNull
	private BeerStyle beerstyle;
	
	@NotNull
	@NotBlank
	private String upc;
	private Integer quantityOnHand;
	
	@NotNull
	@Positive
	private BigDecimal price;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;
	
}
