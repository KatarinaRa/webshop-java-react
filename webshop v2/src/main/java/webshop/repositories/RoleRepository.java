package webshop.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import webshop.entities.ERole;
import webshop.entities.Role;

public interface RoleRepository extends JpaRepository<Role,Long> {
	
	Optional<Role> findByName(ERole name);

}
