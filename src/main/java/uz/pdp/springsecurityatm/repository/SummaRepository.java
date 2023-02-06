package uz.pdp.springsecurityatm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.springsecurityatm.entity.Summa;

@Repository
public interface SummaRepository extends JpaRepository<Summa, Integer> {
}
