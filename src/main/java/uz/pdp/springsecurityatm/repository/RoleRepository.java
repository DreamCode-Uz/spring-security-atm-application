package uz.pdp.springsecurityatm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.springsecurityatm.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
}
