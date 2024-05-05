package guru.springframework.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDto {
	private UUID id;
	private String customerName;
	private Integer version;
	private LocalDateTime createdDate;
	private LocalDateTime lastModifiedDate;
	
}
