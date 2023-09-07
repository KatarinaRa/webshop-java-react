package webshop.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import webshop.entities.Size;


public interface SizeRepository extends JpaRepository <Size,Long> {
	
	List<Size> findAll();
	
	Optional<Size> findById(Long Id);

	Size save(Size size);

    void deleteById(Long id);

}
