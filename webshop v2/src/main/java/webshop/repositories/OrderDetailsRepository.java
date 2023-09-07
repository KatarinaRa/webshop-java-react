package webshop.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import webshop.entities.OrderDetails;
import webshop.entities.OrderStatus;
import webshop.entities.Product;
import webshop.entities.User;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails,Long> {
	
	List<OrderDetails> findByOrderId(Long orderId);
	
	List<OrderDetails> findByOrderUserId(Long Id);
	
	@Query("SELECT od.product FROM OrderDetails od WHERE od.order.status = ?1 GROUP BY od.product ORDER BY SUM(od.quantity) DESC")
	List<Product> findTopSellingProducts(OrderStatus status);
	
	@Query("SELECT od.order.user FROM OrderDetails od WHERE od.order.status = ?1 GROUP BY od.order.user ORDER BY SUM(od.price * od.quantity) DESC")
	List<User> findTopSpendingUsers(OrderStatus status);

	@Query("SELECT o FROM OrderDetails o WHERE o.order.id = :orderId AND o.product.id = :productId AND o.sizeId = :sizeId")
	OrderDetails findOrderDetailsByOrderAndProductAndSizeId(@Param("orderId") Long orderId, @Param("productId") Long productId, @Param("sizeId") String sizeId);
	
	@Query(value = "SELECT od.product FROM OrderDetails od WHERE  od.order.status = ?1 AND od.orderTime = ?2")
	List<Product> findProductsByDay(OrderStatus status, LocalDate targetDate);
	

}
