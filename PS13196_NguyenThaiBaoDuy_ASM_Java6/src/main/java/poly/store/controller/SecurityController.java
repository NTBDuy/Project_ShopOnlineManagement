package poly.store.controller;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import poly.store.dao.AccountDAO;
import poly.store.dao.AuthorityDAO;
import poly.store.entity.Account;
import poly.store.entity.Authority;
import poly.store.entity.Role;
import poly.store.service.SendMailService;
import poly.store.service.UserService;

@Controller
public class SecurityController {
	@Autowired AccountDAO adao;
	@Autowired AuthorityDAO auDao;
	@Autowired BCryptPasswordEncoder pe;
	@Autowired HttpSession session;
	@Autowired SendMailService sendMailService;
	
	@RequestMapping("/security/login/form")
	public String login(Model model) {
		model.addAttribute("message","Vui lòng đăng nhập!");
		return "security/login";
	}
	

	@RequestMapping("/security/changePass")
	public String change(Model model) {
		return "security/changePass";
	}

	@PostMapping("/security/changePass")
	public String changeP(Model model, @RequestParam("mk_1") String mk1, @RequestParam("mk_2") String mk2) {
		if(mk1.equals(mk2)) {
			String use = String.valueOf(session.getAttribute("use"));
			Account ac = adao.findByUsername(use);
			ac.setPassword(pe.encode(mk1));
			adao.save(ac);
			model.addAttribute("message", "Thay đổi mật khẩu thành công!");
			return "security/changePass";
		} else {
			model.addAttribute("message", "2 mật khẩu không giống nhau!");
			return "security/changePass";
		}
	}
	
	@RequestMapping("/security/confirm")
	public String confirm(Model model) {
		return "security/confirm";
	}
	
	@PostMapping("/security/confirm")
	public String confirmm(Model model, @RequestParam("otp") String otp) {
		if (otp.equals(String.valueOf(session.getAttribute("otp")))) {
			model.addAttribute("message", "Đúng OTP!");
			return "security/changePass";
		} else {
			model.addAttribute("message", "SAI OTP!");
			return "security/confirm";
		}
	}
	
	@RequestMapping("/security/forgot")
	public String forgot() {
		return "security/forgot";
	}
	
	@PostMapping("/security/forgot")
	public String forgotPass(Model model, @RequestParam("email") String mail) {
		Boolean chkMail = adao.existsByEmail(mail);
		if(!chkMail==false) {
			Account acc = adao.findByEmail(mail);
			String use = acc.getUsername();
			session.setAttribute("use", use);
			session.removeAttribute("otp");
			int random_otp = (int) Math.floor(Math.random() * (999999 - 100000 + 1) + 100000);
			session.setAttribute("otp", random_otp);
			String body = "<div>\r\n" + "        <h3>Mã OTP của bạn là: <span style=\"color:red; font-weight: bold;\">"
					+ random_otp + "</span></h3>\r\n" + "    </div>";
			sendMailService.queue(mail, "OTP xác nhận tài khoản", body);
			model.addAttribute("message", "Đã gửi mail tới bạn, hãy kiểm tra nhé!");
			return "security/confirm";
		} else {
			model.addAttribute("message", "Email không tồn tại!");
			return "security/forgot";
		}
	}
	
	@RequestMapping("/security/register")
	public String register() {
		return "security/register";
	}
	
	@PostMapping("/security/register")
	public String create(Model model, Account item, @RequestParam("username") String username, 
			@RequestParam("email") String email,
			@RequestParam("password") String pass) {
		boolean chkUser = adao.existsById(username);
		boolean chkEmail = adao.existsByEmail(email);
		if(chkEmail==true) {
			model.addAttribute("message", "Email đã tồn tại!");
		} else if (chkUser==true) {
			model.addAttribute("message", "Tên tài khoản đã tồn tại!");
		} else {
			item.setUsername(username);
			item.setPassword(pe.encode(pass));
			item.setPhoto("user.png");
			item.setEmail(email);
			adao.save(item);
			Authority au = new Authority();
			au.setAccount(item);
			Role ro = new Role();
			ro.setId("CUST");
			au.setRole(ro);
			auDao.save(au);		
			String body = "Bạn vừa đăng kí tài khoản thành công!";
			sendMailService.queue(email, "Đăng kí thành công!", body);
			model.addAttribute("message", "Tạo tài khoản thành công!");
		}
		return "security/register";
	}
	
	@RequestMapping("/security/login/success")
	public String loginSuccess(Model model) {
		model.addAttribute("message","Đăng nhập thành công");
		return "forward:/index";
	}
	
	@RequestMapping("/security/login/error")
	public String loginError(Model model) {
		model.addAttribute("message","Sai thông tin đăng nhập");
		return "security/login";
	}
	
	@RequestMapping("/security/unauthoried")
	public String unauthoried(Model model) {
		model.addAttribute("message","Không có quyền truy xuất!");
		return "security/login";
	}
	
	@RequestMapping("/security/logoff/success")
	public String logoffSuccess(Model model) {
		model.addAttribute("message","Bạn đã đăng xuất");
		return "security/login";
	}
	
	// OAuth2
	@Autowired UserService userService;
	@RequestMapping("/oauth2/login/success")
	public String success(Model model, OAuth2AuthenticationToken oauth2) {
		userService.loginFromOAuth2(oauth2);
		model.addAttribute("message","Đăng nhập thành công");
		return "forward:/index";
	}
}
