package webshop.repositories;



import org.springframework.data.jpa.repository.JpaRepository;
import webshop.entities.Product;



public interface ProductRepository extends JpaRepository<Product,Long> {
	

}
