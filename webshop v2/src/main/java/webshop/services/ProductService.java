package webshop.services;

import org.springframework.stereotype.Service;

import webshop.entities.Color;
import webshop.entities.Gender;
import webshop.entities.Product;
import webshop.entities.ProductCategory;
import webshop.entities.ProductSize;
import webshop.entities.Size;
import webshop.repositories.ProductRepository;
import webshop.repositories.ProductSizeRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository repository;
    private final ProductSizeRepository productSizeRepository;  

    public ProductService(ProductRepository repository, ProductSizeRepository productSizeRepository) {
        this.repository = repository;
        this.productSizeRepository = productSizeRepository;
    }
    
    public Page<Product> getAllProducts(Pageable pageable) {
        return repository.findAll(pageable);
    }
    
    public List<ProductSize> getAllProductSizes() {
        return productSizeRepository.findAll();
    }


    public Product addProduct(String name, String imageURL, String description, double price,
            Color color, Gender gender, ProductCategory category, List<ProductSize> productSizes) {
        
        Product product = new Product();
        product.setName(name);
        product.setImageURL(imageURL);
        product.setDescription(description);
        product.setPrice(price);
        product.setColor(color);
        product.setGender(gender);
        product.setCategory(category);
        
    
        Product savedProduct = repository.save(product);

   
        for (ProductSize productSize : productSizes) {
            productSize.setProduct(savedProduct);
            productSizeRepository.save(productSize);
        }

        return savedProduct;
    }


    

    public Product getProductById(Long productId) {
        Optional<Product> optionalProduct = repository.findById(productId);
        if (optionalProduct.isPresent()) {
            return optionalProduct.get();
        } else {
            throw new IllegalArgumentException("Proizvod s ID-om " + productId + " ne postoji.");
        }
    }

    public Product updateProduct(Long Id, Product updatedProduct) {
        Product existingProduct = repository.findById(Id)
                .orElseThrow(() -> new IllegalArgumentException("Ne postoji proizvod s ID-om: " + Id));

        if (updatedProduct.getName() != null) {
        	System.out.println(updatedProduct.getName());
            existingProduct.setName(updatedProduct.getName());
            System.out.println(existingProduct.getName());
            
        }
        if (updatedProduct.getDescription() != null) {
            existingProduct.setDescription(updatedProduct.getDescription());
        }
        if (updatedProduct.getImageURL() != null) {
            existingProduct.setImageURL(updatedProduct.getImageURL());
        }
        if (updatedProduct.getPrice() != null) {
            existingProduct.setPrice(updatedProduct.getPrice());
        }
        if (updatedProduct.getColor() != null) {
            existingProduct.setColor(updatedProduct.getColor());
        }
        if (updatedProduct.getSize() != null) {
            existingProduct.setSize(updatedProduct.getSize());
        }
        if (updatedProduct.getGender() != null) {
            existingProduct.setGender(updatedProduct.getGender());
        }
        if (updatedProduct.getCategory() != null) {
            existingProduct.setCategory(updatedProduct.getCategory());
        }
        if (updatedProduct.getProductSizes() != null) {
            existingProduct.setProductSizes(updatedProduct.getProductSizes());
        }
       


        return repository.save(existingProduct);
    }


    public boolean delete(Long id) {
        try {
            repository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean isQuantityAvailable(Long productId, Long sizeId, int requestedQuantity) {
        Optional<ProductSize> optionalProductSize = productSizeRepository.findByProductIdAndSizeId(productId, sizeId);
        
        if (!optionalProductSize.isPresent()) {
            return false;
        }
        
        ProductSize productSize = optionalProductSize.get();
        return productSize.getQuantity() >= requestedQuantity;
    }    
    
}
