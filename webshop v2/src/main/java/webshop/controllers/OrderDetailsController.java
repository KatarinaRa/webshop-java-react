package webshop.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import webshop.config.services.CustomUserDetails;
import webshop.entities.OrderDetails;
import webshop.entities.OrderStatus;
import webshop.entities.Product;
import webshop.entities.ProductSize;
import webshop.entities.User;
import webshop.entities.UserOrder;
import webshop.repositories.ProductSizeRepository;
import webshop.services.OrderDetailsService;
import webshop.services.ProductService;
import webshop.services.UserOrderService;
import webshop.services.UserService;


@RestController
@RequestMapping("/orderDetails")
@CrossOrigin(origins = {"*"})
public class OrderDetailsController {

    private final OrderDetailsService service;
    private final ProductService productService;
    private final UserOrderService userOrderService;
    private final UserService userService;
    private final ProductSizeRepository productSizeRepository;

    public OrderDetailsController(OrderDetailsService service, ProductService productService, UserOrderService userOrderService, UserService userService, ProductSizeRepository productSizeRepository) {
        this.service = service;
        this.productService = productService;
        this.userOrderService = userOrderService;
        this.userService = userService;
        this.productSizeRepository = productSizeRepository;
    }

    @PostMapping
    public ResponseEntity<String> addOrderDetails(@RequestBody OrderDetails orderDetails, @RequestParam Long orderId, @RequestParam Long productId, @RequestParam String sizeId) {
        UserOrder order = userOrderService.getOrderById(orderId);
        Product product = productService.getProductById(productId); 

        if (order != null && product != null) {
        
           OrderDetails existingOrderDetails = service.findOrderDetailsByOrderAndProductAndSizeId(orderId, productId, sizeId);
            
            if (existingOrderDetails != null) {
            	boolean flag = productService.isQuantityAvailable(productId, Long.parseLong(sizeId), existingOrderDetails.getQuantity() + 1);
            	if(flag == false) {
            		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient quantity available");
            	}
                existingOrderDetails.setQuantity(existingOrderDetails.getQuantity() + 1);
                OrderDetails updatedOrderDetails = service.addOrderDetails(existingOrderDetails);
                return ResponseEntity.status(HttpStatus.OK).body("Ok");
            } else { 
            	boolean flag = productService.isQuantityAvailable(productId, Long.parseLong(sizeId), 1);
            	if(flag == false) {
            		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient quantity available");
            	}
                orderDetails.setOrder(order);
                orderDetails.setProduct(product);
                orderDetails.setSizeId(sizeId);
                orderDetails.setOrderTime(LocalDate.now());
                OrderDetails createdOrderDetails = service.addOrderDetails(orderDetails);
                return ResponseEntity.status(HttpStatus.CREATED).body("Created");
            }
            
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    

    @GetMapping("/{orderId}")
    public ResponseEntity<List<OrderDetails>> getOrderDetailsByOrderId(@PathVariable Long orderId) {
        UserOrder order = userOrderService.getOrderById(orderId);

        if (order != null) {
            List<OrderDetails> orderDetails = service.getOrderDetailsByOrderId(orderId);
            return ResponseEntity.ok(orderDetails);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{orderDetailId}")
    public ResponseEntity<?> deleteOrderDetail(@PathVariable Long orderDetailId) {
        try {
            service.deleteOrderDetail(orderDetailId);
            return ResponseEntity.ok("Order detail deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<OrderDetails>> getAllOrderDetails() {
        List<OrderDetails> details = service.getAllOrderDetails();
        return new ResponseEntity<>(details, HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/user/{Id}")
    public ResponseEntity<List<UserOrder>> getOrdersByUserId(@PathVariable Long Id) {
        List<UserOrder> orders = userOrderService.getOrderByUserId(Id);
        if (orders != null && !orders.isEmpty()) {
            return ResponseEntity.ok(orders);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @GetMapping("/details/user/{Id}")
    public ResponseEntity<List<OrderDetails>> getOrderDetailsByUserId(@PathVariable Long Id) {
        List<OrderDetails> details = service.getOrderDetailsByUserId(Id);
        List<OrderDetails> doneOrders = new ArrayList<>();
        for(OrderDetails detail : details) {
        	if(detail.getOrder().getStatus() == OrderStatus.DONE) {
        		doneOrders.add(detail);
        	}
        }
        if (doneOrders != null && !doneOrders.isEmpty()) {
            return ResponseEntity.ok(doneOrders);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    @GetMapping("/top-selling-products")
    public List<Product> getTopSellingProducts() {
        return service.getTopSellingProducts();
    }

    @GetMapping("/top-spending-users")
    public List<User> getTopSpendingUsers() {
        return service.getTopSpendingUsers();
    }
    
    @GetMapping("/products-by-day")
    public List<Product> getPurchasedProductsByDay(){
    	return service.findProductsByDay();
    }
    
    @PutMapping("/{orderDetailId}")
    public ResponseEntity<?> updateOrderDetail(@PathVariable Long orderDetailId, @RequestBody OrderDetails updatedOrderDetails) {
        try {
            OrderDetails existingOrderDetails = service.getOrderDetailById(orderDetailId);

            if (existingOrderDetails == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            existingOrderDetails.setQuantity(updatedOrderDetails.getQuantity());
       
            service.saveOrderDetail(existingOrderDetails);

            return ResponseEntity.ok("Order detail updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
     
    }
    
    @PostMapping("/complite")
    public void compliteOrder() {
    	CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long userId = userDetails.getId();
		List<UserOrder> userOrders = userOrderService.getOrderByUserId(userId);
		for(UserOrder userOrder : userOrders) {
			if(userOrder.getStatus() == OrderStatus.IN_PROGRESS)
			{
				List<OrderDetails> orderDetails = service.getOrderDetailsByOrderId(userOrder.getId());
				for(OrderDetails orderDetail : orderDetails) {
					Optional<ProductSize> optionalProductSize = productSizeRepository.findByProductIdAndSizeId(orderDetail.getProduct().getId(), Long.parseLong(orderDetail.getSizeId()));
					optionalProductSize.get().setQuantity(optionalProductSize.get().getQuantity()-orderDetail.getQuantity());
					productSizeRepository.save(optionalProductSize.get());
				}
				userOrder.setStatus(OrderStatus.DONE);
				userOrderService.saveOrder(userOrder);
			}
		}
    }

  

}
