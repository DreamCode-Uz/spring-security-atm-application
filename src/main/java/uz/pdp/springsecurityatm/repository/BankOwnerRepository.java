package uz.pdp.springsecurityatm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.springsecurityatm.entity.Bank;
import uz.pdp.springsecurityatm.entity.BankOwner;

import java.util.UUID;

@Repository
public interface BankOwnerRepository extends JpaRepository<BankOwner, UUID> {
    boolean existsByBankAndEmail(Bank bank, String email);

    boolean existsByEmail(String email);
}
