package poly.store.controller;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


import poly.store.dao.AccountDAO;
import poly.store.rest.controller.AccountRestController;
import poly.store.service.OrderService;


@Controller
public class HomeController {
	@Autowired
	BCryptPasswordEncoder pe;
	
	@Autowired
	OrderService orderService;
	
	@Autowired 
	AccountRestController accRest;
	
	@Autowired
	AccountDAO adao;
	
	
	@RequestMapping({"/index","/home/index","/"})
	public String index() {	
		return "layout/index";
	}
	
	@RequestMapping("/contact")
	public String contact() {
		return "layout/contact";
	}
	
	@RequestMapping({"/admin","/admin/home/index"})
	public String admin() {
		return "redirect:/assets/admin/index.html";
	}
	
	
	@RequestMapping({"/profile","/order/list"})
	public String profile(Model model,
			HttpServletRequest req) {
		String username =  req.getRemoteUser();
		model.addAttribute("orders",orderService.findByUsername(username));
		return "profile/profile";
	}
}
