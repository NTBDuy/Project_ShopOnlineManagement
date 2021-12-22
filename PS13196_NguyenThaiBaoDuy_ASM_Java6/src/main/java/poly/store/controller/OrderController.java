package poly.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import poly.store.service.OrderService;

@Controller
public class OrderController {
	@Autowired
	OrderService orderService;
	
	@RequestMapping("/order/checkout")
	public String checkout() {
		return "order/checkout";
	}

//	@RequestMapping("/order/list")
//	public String list(Model model,
//			HttpServletRequest req) {
//		String username =  req.getRemoteUser();
//		model.addAttribute("orders",orderService.findByUsername(username));
//		return "profile/profile";
//	}
	
	@RequestMapping("/order/detail/{id}")
	public String detail(@PathVariable("id")Long id,
			Model model) {
		model.addAttribute("order",orderService.findById(id));
		return "order/detail";
	}
	
}
