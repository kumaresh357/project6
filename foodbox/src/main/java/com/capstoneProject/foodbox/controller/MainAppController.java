package com.capstoneProject.foodbox.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.capstoneProject.foodbox.model.Admin;
import com.capstoneProject.foodbox.model.Cart;
import com.capstoneProject.foodbox.model.Customer;
import com.capstoneProject.foodbox.model.Product;
import com.capstoneProject.foodbox.model.Purchase;
import com.capstoneProject.foodbox.service.ProductService;
import com.capstoneProject.foodbox.service.PurchaseService;

@Controller
public class MainAppController {
	@Autowired
	private ProductService productService;

	@Autowired
	private PurchaseService purchaseService;

	@GetMapping("/")
	public String viewHomePage(Model model,HttpSession session) {
		model.addAttribute("action", session.getAttribute("action"));
		session.setAttribute("action", null);
		if(session.getAttribute("productList")==null) {
			session.setAttribute("productList", productService.getAllProducts());
			session.setAttribute("searchH", null);
		}
		return "home";
	}

	@GetMapping("/goHome")
	public String goHome(Model model,HttpSession session) {
		model.addAttribute("action", session.getAttribute("action"));
		session.setAttribute("action", null);
		session.setAttribute("productList", productService.getAllProducts());
		session.setAttribute("searchH", null);
		return "home";
	}

	@PostMapping("/searchHome")
	public String searchHome(@RequestParam("keyword") String keyword,Model model,HttpSession session) {
		model.addAttribute("action", session.getAttribute("action"));
		session.setAttribute("action", null);
		List<Product> productList = productService.homeSearch(keyword);
		if(productList.isEmpty()) {
			session.setAttribute("action", "Currently no products for searched");
			session.setAttribute("productList", null);
			return "redirect:/";
		}
		session.setAttribute("productList", productList);
		session.setAttribute("searchH", "yes");
		return "home";
	}


	@GetMapping("/register")
	public String register(Model model) {
		Customer customer = new Customer();
		model.addAttribute("customer", customer);
		return "newCustomer";
	}

	@GetMapping("/login")
	public String customerLogin(Model model) {
		Customer customer = new Customer();
		model.addAttribute("customer", customer);
		return "customerLogin";
	}

	@GetMapping("/adminLogin")
	public String adminLogin(Model model) {
		Admin admin = new Admin();
		model.addAttribute("admin",admin);
		return "adminLogin";
	}

	@GetMapping("/addCart/{id}")
	public String selectProduct(@PathVariable("id") int id,HttpSession session,Model model) {
		if(session.getAttribute("customerLogin")==null) {
			session.setAttribute("action", "Login or Register to start shopping");
			return "redirect:/";
		}
		else {
			session.setAttribute("product", productService.getProductById(id));
			Cart cart = new Cart();
			model.addAttribute("cart", cart);
			return "addCart";
		}
	}

	@GetMapping("/viewOrders/{email}")
	public String customerOrders(@PathVariable(name = "email") String email, Model model,HttpSession session) {
		List<Purchase> sPurchase = purchaseService.getByEmail(email);
		if(!sPurchase.isEmpty()) {
		model.addAttribute("sPurchase", sPurchase);
		return "ViewOrders";
		}
		else {
			session.setAttribute("action", "No Active Orders/Purchases by Customer");
			return "redirect:/";
		}
	}
}
