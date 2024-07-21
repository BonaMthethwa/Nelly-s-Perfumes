package com.perfumes.perfumeswebapp.Services;

import com.perfumes.perfumeswebapp.model.Cart;
import com.perfumes.perfumeswebapp.model.CartItem;
import com.perfumes.perfumeswebapp.model.Product;

import jakarta.servlet.http.HttpSession;

import com.perfumes.perfumeswebapp.Repositories.CartRepository;
import com.perfumes.perfumeswebapp.Repositories.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    public Cart findByUserId(String userId) {
        return cartRepository.findByUserId(userId);
    }

    @Autowired
    private HttpSession httpSession;

    public Cart getOrCreateCart(HttpSession session) {

        String cartId = (String) httpSession.getAttribute("cartId");
        if (cartId != null) {

            return cartRepository.findById(cartId).orElseGet(this::createCart);
        } else {
            Cart cart = createCart();
            httpSession.setAttribute("cartId", cart.getId());
            return cart;
        }
    }

    public Cart createCart() {
        Cart cart = new Cart();
        return cartRepository.save(cart);
    }

    public void addItemToCart(String cartId, String productId, double productPrice, String productImage, int quantity) {
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            List<CartItem> items = cart.getItems();

            if (items == null) {
                items = new ArrayList<>();
                cart.setItems(items);
            }

            Product product = productRepository.findById(productId).orElseThrow();
            CartItem cartItem = new CartItem();
            cartItem.setProductName(product.getProductName());
            cartItem.setPrice(product.getPrice());
            cartItem.setImage(product.getImage());
            cartItem.setQuantity(quantity);
            items.add(cartItem);
            cart.setTotalPrice(calculateTotalPrice(items));
            cartRepository.save(cart);
        }
    }

    public void removeItemFromCart(String cartId, String itemId) {
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            List<CartItem> items = cart.getItems();

            items.removeIf(item -> item.getId().equals(itemId));

            cart.setTotalPrice(calculateTotalPrice(items));

            cartRepository.save(cart);
        }
    }

    private double calculateTotalPrice(List<CartItem> items) {
        double totalPrice = 0;
        for (CartItem item : items) {
            totalPrice += item.getPrice() * item.getQuantity();
        }
        return totalPrice;
    }

    public List<CartItem> getCartItems(String cartId) {
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            return cart.getItems();
        } else {
            return Collections.emptyList();
        }
    }

}
