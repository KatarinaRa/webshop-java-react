package webshop.repositories;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import webshop.entities.OrderStatus;
import webshop.entities.UserOrder;

public interface UserOrderRepository extends JpaRepository <UserOrder,Long> {
	
	List<UserOrder> findByUserId(Long id);
	UserOrder findTopByUserIdOrderByIdDesc(Long id);
}
