package com.perfumes.perfumeswebapp.Services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perfumes.perfumeswebapp.Repositories.OrderRepository;

import com.perfumes.perfumeswebapp.model.Cart;
import com.perfumes.perfumeswebapp.model.CartItem;
import com.perfumes.perfumeswebapp.model.Order;
import com.perfumes.perfumeswebapp.model.OrderItem;
import com.perfumes.perfumeswebapp.model.OrderStatus;
import com.perfumes.perfumeswebapp.model.User;

import jakarta.servlet.http.HttpSession;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemService orderItemService;

    public List<Order> findOrdersWithUser(User user, Order order, HttpSession session) {
        session.setAttribute("user", user);
        return orderRepository.findByUser(user);
    }

    public Order createOrderFromCart(Cart cart, User user, HttpSession session) {
        double total = calculateTotal(cart);
        Order order = new Order();
        order.setCart(cart);
        order.setUser(user);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setCreatedDate(new Date());
        order.setTotal(total);

        Order savedOrder = orderRepository.save(order);

        createOrderItemsFromCart(cart, savedOrder, user);

        cart.clearCart();

        return savedOrder;
    }

    private double calculateTotal(Cart cart) {
        double total = 0.0;
        for (CartItem cartItem : cart.getItems()) {
            total += cartItem.getPrice() * cartItem.getQuantity();
        }
        return total;
    }

    private void createOrderItemsFromCart(Cart cart, Order order, User user) {

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductName(cartItem.getProductName());
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setImage(cartItem.getImage());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setId(order.getOrderId());
            orderItems.add(orderItem);
        }
        orderItemService.createOrderItems(orderItems);
    }

    public void updateOrderStatus(String orderId, OrderStatus orderStatus) {
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        order.setOrderStatus(orderStatus);
        orderRepository.save(order);
    }

    public class OrderNotFoundException extends RuntimeException {
        public OrderNotFoundException() {
            super("Order not found");
        }
    }
}
