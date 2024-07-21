package com.perfumes.perfumeswebapp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.perfumes.perfumeswebapp.Repositories.CartRepository;
import com.perfumes.perfumeswebapp.Repositories.UserRepository;
import com.perfumes.perfumeswebapp.Services.AddressService;
import com.perfumes.perfumeswebapp.Services.CartService;
import com.perfumes.perfumeswebapp.Services.CustomUserDetailsService;
import com.perfumes.perfumeswebapp.Services.OrderService;
import com.perfumes.perfumeswebapp.Services.ProductService;

import com.perfumes.perfumeswebapp.model.Cart;
import com.perfumes.perfumeswebapp.model.CartItem;
import com.perfumes.perfumeswebapp.model.Order;
import com.perfumes.perfumeswebapp.model.Product;
import com.perfumes.perfumeswebapp.model.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @GetMapping("/")
    public String welcomePage() {
        return "welcome";
    }

    @GetMapping("/home")
    public String homePage(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "home";
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(name = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid email or password");
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model) {

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(password)) {

                return "redirect:/home";
            }
        }

        model.addAttribute("error", "Invalid email or password");

        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String name, @RequestParam String surname,
            @RequestParam String email, @RequestParam String password, Model model) {

        User user = new User(name, surname, email, password);

        userRepository.save(user);

        return "redirect:/login";
    }

    @GetMapping("/allProducts")
    public String viewProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "allProducts";
    }

    @GetMapping("/ladies")
    public String viewLadiesProducts(Model model) {
        List<Product> products = productService.getProductsByCategory("/ladies");
        model.addAttribute("products", products);
        return "ladies";
    }

    @GetMapping("/men")
    public String viewMenProducts(Model model) {
        List<Product> products = productService.getProductsByCategory("/men");
        model.addAttribute("products", products);
        return "men";
    }

    @GetMapping("/unisex")
    public String viewUnisexProducts(Model model) {
        List<Product> products = productService.getProductsByCategory("/unisex");
        model.addAttribute("products", products);
        return "unisex";
    }

    @GetMapping("/cart")
    public String viewCart(Model model, HttpSession session) {

        Cart cart = cartService.getOrCreateCart(session);
        List<CartItem> cartItems = cartService.getCartItems(cart.getId());

        model.addAttribute("cartItems", cartItems);

        return "cart";
    }

    @PostMapping("/addToCart")
    public String addToCart(@RequestParam("productId") String productId,
            @RequestParam("productPrice") double productPrice,
            @RequestParam("productImage") String productImage,
            @RequestParam("quantity") int quantity, HttpSession session) {

        Cart cart = cartService.getOrCreateCart(session);

        cartService.addItemToCart(cart.getId(), productId, productPrice, productImage, quantity);

        return "redirect:/cart";
    }

    @Autowired
    private CartRepository cartRepository;

    @PostMapping("/cart/removeItem")
    public String removeItemFromCart(
            @RequestParam("itemId") String itemId, @RequestParam("cartId") String cartId) {
        cartRepository.deleteById(itemId);
        return "redirect:/cart";
    }

    @GetMapping("/creditCardPayment")
    public String showCreditCardPaymentPage() {
        return "creditCardPayment";
    }

    @PostMapping("/creditCardPayment")
    public String processCreditCardPayment(@RequestParam("cardnumber") String cardNumber,
            @RequestParam("expdate") String expiryDate,
            @RequestParam("cvv") String cvv,
            @RequestParam("amount") double amount,
            HttpSession session) {

        Cart cart = cartService.getOrCreateCart(session);
        session = initializeSession();
        User user = getCurrentUser();
        orderService.createOrderFromCart(cart, user, session);
        return "redirect:/orderConfirmation";

    }

    @GetMapping("/orderConfirmation")
    public String placeOrder() {
        return "orderConfirmation";
    }

    @GetMapping("/uploadProofOfPayment")
    public String uploadProofOfPaymentPage() {
        return "uploadProofOfPayment";
    }

    @PostMapping("/uploadProofOfPayment")
    public String postMethodName(Model model, HttpSession session) {

        Cart cart = cartService.getOrCreateCart(session);
        session = initializeSession();
        User user = getCurrentUser();
        orderService.createOrderFromCart(cart, user, session);

        return "orderConfirmation";
    }

    @GetMapping("/checkout")
    public String getCheckoutPage() {
        return "checkout";
    }

    @PostMapping("/address")
    public String saveAddress(@RequestParam("street") String street,
            @RequestParam("city") String city,
            @RequestParam("code") String code,
            @RequestParam("country") String country,
            HttpSession session) {

        session = initializeSession();
        User user = getCurrentUser();
        Cart cart = cartService.getOrCreateCart(session);
        Order order = orderService.createOrderFromCart(cart, user, session);
        addressService.saveAddress(street, city, code, country, user, order, session);

        return "redirect:/home";
    }

    @GetMapping("/address")
    public String saveAdress() {
        return "address";
    }

    @GetMapping("/forgotPassword")
    public String showForgotPasswordForm() {
        return "forgotPassword";
    }

    @PostMapping("/forgotPassword")
    public String submitEmail(@RequestParam("email") String email, Model model) {

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            model.addAttribute("email", email);
            return "redirect:/updatePassword";
        } else {

            model.addAttribute("error", "Email not found");
            return "forgotPassword";
        }
    }

    @GetMapping("/updatePassword")
    public String showUpdatePasswordForm() {
        return "updatePassword";
    }

    @PostMapping("/updatePassword")
    public String updatePassword(@RequestParam("email") String email, @RequestParam("password") String password,
            Model model) {
        System.out.println("Email received in updatePassword controller method: " + email);
        userDetailsService.updatePassword(email, password);
        return "redirect:/login";
    }

    @Autowired
    private HttpServletRequest request;

    private HttpSession initializeSession() {
        HttpSession session = request.getSession(true);
        return session;
    }

    private User getCurrentUser() {
        HttpSession session = request.getSession(false);
        return (User) session.getAttribute("user");

    }

    @GetMapping("/search")
    public String searchProducts() {

        return "search";
    }

    @GetMapping("/logoutUser")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/welcome";
    }
}