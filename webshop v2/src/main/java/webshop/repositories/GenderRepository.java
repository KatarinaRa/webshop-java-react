package webshop.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import webshop.entities.Gender;

public interface GenderRepository extends JpaRepository <Gender,Long> {
	
	public List<Gender> findAll();
	void deleteById(Long id);

}
