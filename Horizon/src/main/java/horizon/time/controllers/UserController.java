package horizon.time.controllers;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import horizon.time.BaseObject;
import horizon.time.dto.UserDto;
import horizon.time.persistence.model.User;
import horizon.time.services.UserService;

@Controller
public class UserController extends BaseObject {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/login")
	public String showLogin(UserDto userDto) {
		return "login";
	}

	@PostMapping(path = "/login")
	public String login(@RequestParam(name = "username", required = true, defaultValue = "") String name,
			@RequestParam(name = "password", required = true, defaultValue = "") String password, Model model) {

		if (name.isBlank())
			model.addAttribute("name", name);

		return "login";
	}

	@RequestMapping(value = "/register")
	public String registerView(final UserDto userDto) {
		return "register";
	}

	@PostMapping(value = "/register")
	public String registerPost(@Valid final UserDto userDto, BindingResult bindingResult, Model model,
			Principal principal, Authentication authentication, HttpServletRequest request) {
		if (bindingResult.hasErrors()) {
			logger.info("bindingResult.hasErrors");
			return "register";
		} else {
			try {
				final User userReg = userService.registerNewUserAccount(userDto);
				if (!userReg.isNew()) {
					userService.authWithoutPassword(userReg);
					return "redirect:/console";
				} else
					return "register";
			} catch (Exception ex) {
				bindingResult.rejectValue("username", "register.exists", "register.exists");
				return "register";
			}
		}
	}
}
