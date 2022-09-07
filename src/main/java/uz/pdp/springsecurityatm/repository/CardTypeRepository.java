package uz.pdp.springsecurityatm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.springsecurityatm.entity.CardType;
import uz.pdp.springsecurityatm.entity.enums.CardName;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardTypeRepository extends JpaRepository<CardType, UUID> {
    Optional<CardType> findCardTypeByType(CardName type);
}
