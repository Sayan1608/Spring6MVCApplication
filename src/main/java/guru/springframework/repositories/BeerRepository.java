package guru.springframework.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import guru.springframework.entities.Beer;

public interface BeerRepository extends JpaRepository<Beer, UUID> {

}
