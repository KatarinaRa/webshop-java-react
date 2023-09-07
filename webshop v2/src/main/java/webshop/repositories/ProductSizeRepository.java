package webshop.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import webshop.entities.ProductSize;

public interface ProductSizeRepository extends JpaRepository<ProductSize ,Long> {
	
	List<ProductSize> findByProductId(Long id);
	Optional<ProductSize> findByProductIdAndSizeId(Long productId, Long sizeId);
	
	
	
}
