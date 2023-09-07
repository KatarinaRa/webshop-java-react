package webshop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import webshop.entities.UserOrder;
import webshop.services.OrderDetailsService;
import webshop.services.UserOrderService;
import webshop.services.UserService;
import webshop.entities.OrderDetails;
import webshop.entities.OrderStatus;
import webshop.entities.User;
import webshop.config.services.CustomUserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Controller
@CrossOrigin(origins = { "http://localhost:3000" })
@RequestMapping("/orders")
public class UserOrderController {

	private final OrderDetailsService orderDetailsService;
	private final UserOrderService userOrderService;
	private final UserService userService;

	@Autowired
	public UserOrderController(UserOrderService userOrderService, UserService userService,
			OrderDetailsService orderDetailsService) {
		this.orderDetailsService = orderDetailsService;
		this.userOrderService = userOrderService;
		this.userService = userService;
	}

	@GetMapping
	public ResponseEntity<List<UserOrder>> getAllOrders() {
		List<UserOrder> orders = userOrderService.getAllOrders();
		return new ResponseEntity<>(orders, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<List<OrderDetails>> getOrderById(@PathVariable Long id) {
		List<UserOrder> orders = userOrderService.getOrderByUserId(id);

		if (orders == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			List<OrderDetails> orderDetails = new ArrayList<>();
			for (UserOrder order : orders) {
				if(order.getStatus() == OrderStatus.IN_PROGRESS) {
					orderDetails.addAll(orderDetailsService.getOrderDetailsByOrderId(order.getId()));
				}
			}
			return new ResponseEntity<>(orderDetails, HttpStatus.OK);
		}
	}

	@PostMapping
	public ResponseEntity<UserOrder> saveOrder(@RequestBody UserOrder userOrder) {
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long userId = userDetails.getId();
		UserOrder user_order = userOrderService.findOrderByUserId(userId);
		if (user_order != null && user_order.getStatus() == OrderStatus.IN_PROGRESS) {
			return new ResponseEntity<>(user_order, HttpStatus.CREATED);
		}

		User user = userService.getUserById(userId);
		userOrder.setUser(user);
		userOrder.setStatus(OrderStatus.IN_PROGRESS);
		UserOrder savedOrder = userOrderService.saveOrder(userOrder);
		return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")

	public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
		userOrderService.deleteOrder(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}