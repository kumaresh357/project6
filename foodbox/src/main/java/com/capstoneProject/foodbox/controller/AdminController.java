package com.capstoneProject.foodbox.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.capstoneProject.foodbox.model.Admin;
import com.capstoneProject.foodbox.service.AdminService;

@Controller
public class AdminController {
	@Autowired
	private AdminService adminService;

	@PostMapping("/verifyLogin")
	public String verifyLogin(@RequestParam(name="username") String username,@RequestParam(name="password") String password,HttpSession session,Model model) {
		if(!username.isEmpty() || !password.isEmpty()) {
			if(adminService.loginVerify(username,password)) {
				session.setAttribute("uname", username);
				return "adminDashboard";
			}
			else {
				model.addAttribute("action","Username or password wrong");
				return "adminLogin";
			}
		}
		else {
			model.addAttribute("action", "Fields must not be empty");
			return "adminLogin";
		}
	}

	@GetMapping("/getDashboard")
	public String getDashboard() {
		return "adminDashboard";
	}

	@GetMapping("/changePassword")
	public String changeAdminPassword(HttpSession session, Model model)	{
		String username=(String) session.getAttribute("uname");
		Admin admin = adminService.getAdmin(username);
		model.addAttribute("admin", admin);
		return "changePassword";
	}

	@PostMapping("/updatePassword")
	public String updatePassword(@RequestParam(name="oldPassword") String oldPassword,@RequestParam(name="newPassword") String newPassword,HttpSession session,Model model) {
		String username=(String) session.getAttribute("uname");
		Admin admin = adminService.getAdmin(username);
		if(oldPassword.equals(admin.getPassword())) {
			admin.setPassword(newPassword);
			adminService.updatePassword(admin);
			model.addAttribute("action", "Password changed Successfully");
			return "adminDashboard";
		}
		else {
			model.addAttribute("action", "Old Password not matching");
			return "changePassword";
		}
	}

	@GetMapping("/logout")
	public String adminLogout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
}

