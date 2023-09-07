package webshop.services;

import java.util.List;

import org.springframework.stereotype.Service;

import webshop.entities.ProductCategory;
import webshop.repositories.ProductCategoryRepository;

@Service
public class ProductCategoryService {
	
	private final ProductCategoryRepository repository;

	public ProductCategoryService(ProductCategoryRepository repository) {
		this.repository = repository;
	}

	public ProductCategory addProductCategory(ProductCategory productCategory) {
		return repository.save(productCategory);
	}

	public List<ProductCategory> getAllProductCategories() {
		return repository.findAll();
	}

	public ProductCategory updateProductCategory(Long productCategoryId, ProductCategory updatedProductCategory) {
		ProductCategory existingProductCategory = repository.findById(productCategoryId)
				.orElseThrow(() -> new IllegalArgumentException("Ne postoji kategorija proizvoda s ID-om: " + productCategoryId));

		existingProductCategory.setName(updatedProductCategory.getName());
	

		return repository.save(existingProductCategory);
	}

	public void delete(Long productCategoryId) {
		repository.deleteById(productCategoryId);
	}
	public ProductCategory getProductCategoryById(Long id) {
        return repository.findById(id).orElse(null);
    }
}