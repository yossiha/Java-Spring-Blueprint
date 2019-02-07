package horizon.time.controllers;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import horizon.time.BaseObject;

@Controller
public class SecurityController extends BaseObject {
	@RequestMapping(value = "/console")
	public String console(Model model, Principal principal, Authentication authentication, HttpServletRequest request) {
		model.addAttribute("principal", principal);
		model.addAttribute("userEmail", principal.getName());
		model.addAttribute("authentication", authentication);
		return "console";
	}
}
