package uz.pdp.springsecurityatm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurityatm.entity.ATM;

public interface ATMRepository extends JpaRepository<ATM, Long> {
}