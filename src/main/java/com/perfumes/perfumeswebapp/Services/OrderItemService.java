package com.perfumes.perfumeswebapp.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perfumes.perfumeswebapp.Repositories.OrderItemRepository;
import com.perfumes.perfumeswebapp.model.OrderItem;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    public List<OrderItem> getAllOrderItems() {
        return orderItemRepository.findAll();
    }

    public OrderItem getOrderItemById(String id) {
        return orderItemRepository.findById(id).orElse(null);
    }

    public OrderItem createOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    public List<OrderItem> createOrderItems(List<OrderItem> orderItems) {

        for (OrderItem orderItem : orderItems) {
            orderItemRepository.save(orderItem);
        }
        return orderItems;
    }

    public OrderItem updateOrderItem(String id, OrderItem updatedOrderItem) {
        OrderItem existingOrderItem = orderItemRepository.findById(id).orElse(null);
        if (existingOrderItem != null) {
            existingOrderItem.setProductName(updatedOrderItem.getProductName());
            existingOrderItem.setPrice(updatedOrderItem.getPrice());
            existingOrderItem.setImage(updatedOrderItem.getImage());
            existingOrderItem.setQuantity(updatedOrderItem.getQuantity());
            return orderItemRepository.save(existingOrderItem);
        }
        return null;
    }

    public void deleteOrderItem(String id) {
        orderItemRepository.deleteById(id);
    }
}
