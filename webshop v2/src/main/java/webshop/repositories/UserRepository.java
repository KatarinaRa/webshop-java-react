package webshop.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import webshop.entities.User;


public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByUsername(String username);
	Boolean existsByUsername(String username);
	Boolean existsByEmail(String email);
	public List<User> findByEmail(String email);
	public User findUserById(Long Id);
	void findUserByAddress(String address);
	
    

}
