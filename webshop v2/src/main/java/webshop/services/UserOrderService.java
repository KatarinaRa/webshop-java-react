package webshop.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import webshop.entities.OrderStatus;
import webshop.entities.User;
import webshop.entities.UserOrder;
import webshop.repositories.UserOrderRepository;
import webshop.repositories.UserRepository;

@Service
public class UserOrderService {
	
	private final UserOrderRepository userOrderRepository;
	private final UserRepository userRepository;
	
	@Autowired
	public UserOrderService (UserOrderRepository userOrderRepository, UserRepository userRepository) { 
		this.userOrderRepository = userOrderRepository;
		this.userRepository = userRepository; 
	}
	
	public List<UserOrder> getAllOrders() {
        return userOrderRepository.findAll();
    }
	
	public UserOrder getOrderById(Long id) {
		return userOrderRepository.findById(id).orElse(null);
	}

	public List<UserOrder> getOrderByUserId(Long id) {
		return userOrderRepository.findByUserId(id);
	}

	public UserOrder findOrderByUserId(Long id) {
		return userOrderRepository.findTopByUserIdOrderByIdDesc(id);
	}
	

	public List<UserOrder> getOrdersByUsername(String username) {
		Optional<User> user = userRepository.findByUsername(username);
		if (user.isPresent()) {
			return userOrderRepository.findByUserId(user.get().getId());
		}
		return List.of();
	}
	
	public UserOrder saveOrder(UserOrder userOrder) {
		return userOrderRepository.save(userOrder);
	}
	
	public void deleteOrder(Long id) {
		userOrderRepository.deleteById(id);
	}
}
