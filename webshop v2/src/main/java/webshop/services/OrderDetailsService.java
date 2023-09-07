package webshop.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import webshop.entities.OrderDetails;
import webshop.entities.OrderStatus;
import webshop.entities.Product;
import webshop.entities.User;
import webshop.entities.UserOrder;
import webshop.repositories.OrderDetailsRepository;
import webshop.repositories.UserOrderRepository;
import webshop.repositories.ProductRepository;

@Service
public class OrderDetailsService {
    
    private final OrderDetailsRepository orderDetailsRepository;
    private final UserOrderRepository userOrderRepository;
    private final ProductRepository productRepository;

    public OrderDetailsService(OrderDetailsRepository orderDetailsRepository,
                               UserOrderRepository userOrderRepository,
                               ProductRepository productRepository) {
        this.orderDetailsRepository = orderDetailsRepository;
        this.userOrderRepository = userOrderRepository;
        this.productRepository = productRepository;
    }

    public OrderDetails addOrderDetails(OrderDetails orderDetails) {
        UserOrder userOrder = userOrderRepository.findById(orderDetails.getOrder().getId())
                .orElseThrow(() -> new RuntimeException("Order not found"));
        Product product = productRepository.findById(orderDetails.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        orderDetails.setOrder(userOrder);
        orderDetails.setProduct(product);
     

        return orderDetailsRepository.save(orderDetails);
    }
    
    public List<OrderDetails> getOrderDetailsByOrderId(Long orderId) {
        return orderDetailsRepository.findByOrderId(orderId);
    }
    public List<OrderDetails> getAllOrderDetails() {
        return orderDetailsRepository.findAll();
    }
    public void deleteOrderDetail(Long orderDetailId) {
        if (!orderDetailsRepository.existsById(orderDetailId)) {
            throw new RuntimeException("Order detail not found");
        }
        orderDetailsRepository.deleteById(orderDetailId);
    }
    
    public List<OrderDetails> getOrderDetailsByUserId(Long Id) {
        return orderDetailsRepository.findByOrderUserId(Id);
    }

    public List<Product> getTopSellingProducts() {
    	return orderDetailsRepository.findTopSellingProducts(OrderStatus.DONE);
    }

    public List<User> getTopSpendingUsers() {
    	return orderDetailsRepository.findTopSpendingUsers(OrderStatus.DONE);
    }
    public OrderDetails getOrderDetailById(Long orderDetailId) {
        return orderDetailsRepository.findById(orderDetailId)
            .orElseThrow(() -> new RuntimeException("Order detail not found"));
    }
    public List<Product> findProductsByDay()   {
    	return orderDetailsRepository.findProductsByDay(OrderStatus.DONE, LocalDate.now());
    }
    public OrderDetails saveOrderDetail(OrderDetails orderDetails) {
        return orderDetailsRepository.save(orderDetails);
    }

	public OrderDetails findOrderDetailsByOrderAndProductAndSizeId(Long id, Long productId, String sizeId) {
		return orderDetailsRepository.findOrderDetailsByOrderAndProductAndSizeId(id, productId, sizeId);
	}

   
    
   
    

   
}
