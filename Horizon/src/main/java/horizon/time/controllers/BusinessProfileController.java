package horizon.time.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import horizon.time.BaseObject;
import horizon.time.persistence.model.Business;
import horizon.time.services.BusinessProfileService;

//TODO stub
@Controller
@RequestMapping(value = "/site/")
public class BusinessProfileController extends BaseObject {
	// @Autowired
	// private BusinessService businessService;

	@Autowired
	private BusinessProfileService businessProfileService;

	/**
	 * View a business listing
	 * 
	 * @return
	 */
	@RequestMapping(value = "/profile/{businessSlug}")
	public String businessView(@PathVariable String businessSlug, Model model) {
		try {
			Business business = businessProfileService.getBySlug(businessSlug);
			business.getMedia();
			model.addAttribute("business", business);
		} catch (Exception e) {
			return "redirect:/console";
		}
		return "business/view";
	}
}
