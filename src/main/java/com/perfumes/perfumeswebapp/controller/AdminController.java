package com.perfumes.perfumeswebapp.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.perfumes.perfumeswebapp.Repositories.AdminRepository;
import com.perfumes.perfumeswebapp.Repositories.UserRepository;
import com.perfumes.perfumeswebapp.Services.CartService;
import com.perfumes.perfumeswebapp.Services.OrderService;
import com.perfumes.perfumeswebapp.Services.ProductService;
import com.perfumes.perfumeswebapp.model.Admin;
import com.perfumes.perfumeswebapp.model.Cart;
import com.perfumes.perfumeswebapp.model.Order;
import com.perfumes.perfumeswebapp.model.Product;
import com.perfumes.perfumeswebapp.model.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller

public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/welcomeAdmin")
    public String welcomePage() {
        return "welcomeAdmin";
    }

    @GetMapping("/loginAdmin")
    public String loginAdminPage(@RequestParam(name = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid email or password");
        }
        return "loginAdmin";
    }

    @PostMapping("/loginAdmin")
    public String login(@RequestParam String email, @RequestParam String password, Model model) {
        Optional<Admin> adminOptional = adminRepository.findByEmail(email);
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            if (admin.getPassword().equals(password)) {

                return "redirect:/adminCategories";
            }
        }

        model.addAttribute("error", "Invalid email or password");
        return "loginAdmin";
    }

    @GetMapping("/adminCategories")
    public String getCategories() {
        return "adminCategories";
    }

    @GetMapping("/viewUsers")
    public String viewUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "viewUsers";
    }

    @PostMapping("/addProduct")
    public String addProduct(@RequestParam("productName") String productName,
            @RequestParam("description") String description,
            @RequestParam("price") double price,
            @RequestParam("image") MultipartFile image,
            @RequestParam("category") String category,
            Model model) {
        try {

            String uploadPath = "C:/Users/User/Desktop/perfumeswebapp/src/main/resources/static/images/";

            String imageName = image.getOriginalFilename();
            File imageFile = new File(uploadPath + imageName);
            image.transferTo(imageFile);

            model.addAttribute("successMessage", "Product added successfully.");

            productService.addProduct(productName, description, price, imageFile.getName(), category);
            return "redirect:/adminCategories";
        } catch (IOException e) {
            e.printStackTrace();

            return "errorPage";
        }
    }

    @GetMapping("/addProduct")
    public String showAddProductForm() {
        return "addProduct";
    }

    @GetMapping("/errorPage")
    public String errorPage() {
        return "errorPage";
    }

    @GetMapping("/viewProducts")
    public String viewProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "viewProducts";
    }

    @PostMapping("/deleteProduct")
    public String deleteProductByName(@RequestParam("productName") String productName, Model model) {
        try {
            productService.deleteProductByName(productName);
            model.addAttribute("successMessage", "Product deleted successfully");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error deleting product");
            e.printStackTrace();
        }
        return "redirect:/viewProducts";
    }

    @GetMapping("/deleteProduct")
    public String deleteProduct() {
        return "deleteProduct";
    }

    @Autowired
    private CartService cartService;

    @GetMapping("/viewOrders")
    public String viewOrders(Model model, HttpSession session) {
        session = initializeSession();
        User user = getCurrentUser();
        Cart cart = cartService.getOrCreateCart(session);
        Order order = orderService.createOrderFromCart(cart, user, session);
        List<Order> orders = orderService.findOrdersWithUser(user, order, session);

        model.addAttribute("orders", orders);

        return "viewOrders";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, Model model) {

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/welcomeAdmin";
    }

    @Autowired
    private HttpServletRequest request;

    private HttpSession initializeSession() {
        HttpSession session = request.getSession(true);
        return session;
    }

    private User getCurrentUser() {
        HttpSession session = request.getSession(true);
        if (session != null) {
            return (User) session.getAttribute("user");
        }
        return null;
    }
}
