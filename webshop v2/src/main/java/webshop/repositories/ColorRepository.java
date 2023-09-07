package webshop.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import webshop.entities.Color;

public interface ColorRepository extends JpaRepository<Color,Long> {
	
	List<Color> findAll();
	void deleteById(Long id);

}
