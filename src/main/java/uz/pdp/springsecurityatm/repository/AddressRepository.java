package uz.pdp.springsecurityatm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurityatm.entity.Address;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
}