package uz.pdp.springsecurityatm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.springsecurityatm.entity.ATM;

@Repository
public interface ATMRepository extends JpaRepository<ATM, Long> {
}