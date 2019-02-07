package horizon.time.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import horizon.time.BaseObject;
import horizon.time.persistence.model.Business;
import horizon.time.services.MinisiteService;

/**
 * 
 * @TODO stub, create a minisite controller after you create a profile
 *       controller.
 *
 */
@Controller
@RequestMapping(value = "/site/")
public class MinisiteController extends BaseObject {
	@Autowired
	private MinisiteService minisiteService;

	/**
	 * View a business listing
	 * 
	 * @return
	 */
	@RequestMapping(value = "/{businessSlug}")
	public String businessView(@PathVariable String businessSlug, Model model) {
		try {
			Business business = minisiteService.getBySlug(businessSlug);
			model.addAttribute("business", business);
		} catch (Exception e) {
			return "business/notfound";
		}
		return "business/view";
	}
}
