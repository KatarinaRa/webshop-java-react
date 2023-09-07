package webshop.controllers;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import java.io.File;

import webshop.DTO.SizeAndQuantityDTO;
import webshop.entities.Color;
import webshop.entities.Gender;
import webshop.entities.Product;
import webshop.entities.ProductCategory;
import webshop.entities.ProductSize;
import webshop.entities.Size;
import webshop.repositories.ColorRepository;
import webshop.repositories.GenderRepository;
import webshop.repositories.ProductSizeRepository;
import webshop.repositories.SizeRepository;
import webshop.services.ProductService;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {

	private final ProductService service;
	private ProductSizeRepository productSizeRepository;
	private ColorRepository colorRepository;
	private GenderRepository genderRepository;
	private SizeRepository sizeRepository;
	
    public ProductController(ProductService service,ProductSizeRepository productSizeRepository, ColorRepository colorRepository,GenderRepository genderRepository,SizeRepository sizeRepository ) {
        this.service = service;
        this.productSizeRepository = productSizeRepository;
        this.colorRepository = colorRepository;
        this.genderRepository = genderRepository;
        this.sizeRepository = sizeRepository;
    }

    @GetMapping("/products")
    public Page<Product> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size) {
    	System.out.println("usao");
        System.out.println("Getting all products, page: " + page + ", size: " + size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = service.getAllProducts(pageable);
        System.out.println("Fetched " + products.getContent().size() + " products");
        return products;
    }

    @PostMapping("/products")
    public ResponseEntity<Product> addProduct(
        @RequestParam("name") String name,
        @RequestParam("image") MultipartFile imageFile,
        @RequestParam("description") String description,
        @RequestParam("price") double price,
        @RequestParam("color") Color color,
        @RequestParam("gender") Gender gender,
        @RequestParam("category") ProductCategory category,
        @RequestParam("sizeQuantities") String sizeQuantitiesJson) {

    	 try {
             String folder = "C:\\Users\\katar\\Desktop\\zavrsni\\webshop v2\\upload";
             byte[] bytes = imageFile.getBytes();

             MessageDigest md = MessageDigest.getInstance("MD5");
             byte[] hashBytes = md.digest(bytes);
             StringBuilder sb = new StringBuilder();
             for (byte b : hashBytes) {
                 sb.append(String.format("%02x", b));
             }
             String uniqueHash = sb.toString();

            
             String extension = imageFile.getOriginalFilename().substring(imageFile.getOriginalFilename().lastIndexOf("."));

         
             Path path = Paths.get(folder + File.separator + uniqueHash + extension);
             Files.write(path, bytes);

            
			Map<String, Integer> sizeQuantities = new ObjectMapper().readValue(sizeQuantitiesJson, Map.class);
            List<ProductSize> productSizes = new ArrayList<>();

            for (Map.Entry<String, Integer> entry : sizeQuantities.entrySet()) {
                String sizeId = entry.getKey();
                Integer quantity = entry.getValue();
                Size size = sizeRepository.findById(Long.parseLong(sizeId)).orElseThrow(() -> new Exception("Size with ID " + sizeId + " not found."));
                ProductSize productSize = new ProductSize();
                productSize.setSize(size);
                productSize.setQuantity(quantity);
                productSizes.add(productSize);
            }

            Product createdProduct = service.addProduct(name, uniqueHash + extension, description, price, color, gender, category, productSizes);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    	@GetMapping("/products/images/{filename:.+}")
    	public ResponseEntity<Resource> posluziDatoteku(@PathVariable String filename) {
    	    try {
    	        Path file = Paths.get("C:\\Users\\katar\\Desktop\\zavrsni\\webshop v2\\upload" + File.separator + filename);
    	        Resource resource = new UrlResource(file.toUri());

    	        String mimeType = Files.probeContentType(file);
    	        if (mimeType == null) {
    	            mimeType = "image/jpeg";
    	        }

    	        if (resource.exists() || resource.isReadable()) {
    	            return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType)).body(resource);
    	        } else {
    	            return ResponseEntity.notFound().build();
    	        }
    	    } catch (Exception e) {
    	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    	    }
    	}

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long id) {
        Product product = service.getProductById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    	
    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") Long id, @RequestBody Product product) {
        try {
            Product existingProduct = service.getProductById(id);
            if (existingProduct == null) {
                return ResponseEntity.notFound().build();
            }

            if (product.getName() != null)
                existingProduct.setName(product.getName());
            if (product.getDescription() != null)
                existingProduct.setDescription(product.getDescription());
            if (product.getPrice() != null)
                existingProduct.setPrice(product.getPrice());
            if (product.getColor() != null) {
                Color color = colorRepository.findById(product.getColor().getId())
                        .orElseThrow(() -> new Exception("Boja s ID-om " + product.getColor().getId() + " nije pronađena."));
                existingProduct.setColor(color);
            }

            if (product.getGender() != null) {
                Gender gender = genderRepository.findById(product.getGender().getId())
                        .orElseThrow(() -> new Exception("Spol s ID-om " + product.getGender().getId() + " nije pronađen."));
                existingProduct.setGender(gender);
            }

            if (product.getProductSizes() != null && !product.getProductSizes().isEmpty()) {
                List<ProductSize> updatedProductSizes = new ArrayList<>();

                for (ProductSize incomingSize : product.getProductSizes()) {
                    Size size = sizeRepository.findById(incomingSize.getSize().getId())
                            .orElseThrow(() -> new Exception("Size with ID " + incomingSize.getSize().getId() + " not found."));
                    ProductSize productSize = new ProductSize();
                    productSize.setSize(size);
                    productSize.setQuantity(incomingSize.getQuantity());

                    updatedProductSizes.add(productSize);
                }

                existingProduct.setProductSizes(updatedProductSizes);
            }

            Product updatedProduct = service.updateProduct(id, existingProduct);
            return ResponseEntity.ok(updatedProduct);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) {
        boolean success = service.delete(id);
        if (success) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/products/{id}/sizes")
    public ResponseEntity<List<Size>> getSizesForProduct(@PathVariable("id") Long id) {
        try {
            System.out.println("Dohvaćam proizvod s ID-jem: " + id);
            Product product = service.getProductById(id);
            
            if (product == null) {
                System.out.println("Proizvod nije pronađen");
                return ResponseEntity.notFound().build();
            }
            
            List<ProductSize> productSizes = productSizeRepository.findByProductId(id);
            System.out.println("Dohvaćene veličine proizvoda: " + productSizes.size());
            
            List<Size> sizes = new ArrayList<>();
            for (ProductSize ps : productSizes) {
                sizes.add(ps.getSize());
            }
            System.out.println("Dohvaćene veličine: " + sizes.size());

            return ResponseEntity.ok(sizes);
            
        } catch (Exception e) {
            e.printStackTrace(); 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    @GetMapping("/products/{productId}/size/{sizeId}/isAvailable")
    public ResponseEntity<Boolean> checkProductQuantity(
        @PathVariable("productId") Long productId,
        @PathVariable("sizeId") Long sizeId,
        @RequestParam("quantity") int requestedQuantity) {
        
        try {
            boolean isAvailable = service.isQuantityAvailable(productId, sizeId, requestedQuantity);
            
            if (isAvailable) {
                return ResponseEntity.ok(true);
            } else {
                return ResponseEntity.ok(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    

    
   
   }



    







