package webshop.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import webshop.entities.ProductCategory;
import webshop.services.ProductCategoryService;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    public ProductCategoryController(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    @GetMapping("/productscategory")
    public List<ProductCategory> getAllProductCategories() {
        return productCategoryService.getAllProductCategories();
    }

    @PostMapping("/productscategory")
    public ProductCategory addProductCategory(@RequestBody ProductCategory productCategory) {
        return productCategoryService.addProductCategory(productCategory);
    }

    @PutMapping("/productscategory/{productCategoryId}")
    public ProductCategory updateProductCategory(@PathVariable("productCategoryId") Long productCategoryId,
                                                 @RequestBody ProductCategory updatedProductCategory) {
        return productCategoryService.updateProductCategory(productCategoryId, updatedProductCategory);
    }

    @DeleteMapping("/productscategory/{productCategoryId}")
    public void deleteProductCategory(@PathVariable("productCategoryId") Long productCategoryId) {
        productCategoryService.delete(productCategoryId);
    }
    @GetMapping("/productscategory/{id}")
    public ResponseEntity<ProductCategory> getProductCategroyById(@PathVariable("id") Long id){
    	ProductCategory productCategory = productCategoryService.getProductCategoryById(id);
    	
    	if (productCategory != null) {
    		return ResponseEntity.ok(productCategory);
    	}else {
    		return ResponseEntity.notFound().build();
    	}
    		
    }
}