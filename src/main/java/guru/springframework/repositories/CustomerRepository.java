package guru.springframework.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import guru.springframework.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

}
