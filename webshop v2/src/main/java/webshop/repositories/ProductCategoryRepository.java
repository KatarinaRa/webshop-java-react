package webshop.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import webshop.entities.ProductCategory;


public interface ProductCategoryRepository extends JpaRepository <ProductCategory,Long>{
	
	List<ProductCategory> findAll();

	ProductCategory save(ProductCategory productCategory);

    void deleteById(Long id);

}
