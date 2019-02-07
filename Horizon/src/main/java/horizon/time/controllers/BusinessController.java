package horizon.time.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import horizon.time.BaseObject;
import horizon.time.components.JsonComponent;
import horizon.time.dto.BusinessDto;
import horizon.time.persistence.model.Business;
import horizon.time.services.BusinessService;
import horizon.time.services.MediaService;

@Controller
@RequestMapping(value = "/business")
public class BusinessController extends BaseObject {
	@Autowired
	private BusinessService businessService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private MediaService mediaService;

	/**
	 * View a business listing
	 * 
	 * @return
	 */
	@RequestMapping(value = "/view/{businessId}")
	public String businessView(@PathVariable Long businessId, Model model) {
		try {
			model.addAttribute("business", businessService.ownerGetFirstBusiness(businessId));
		} catch (Exception e) {
			return "redirect:/console";
		}

		return "business/view";
	}

	/**
	 * Upload a new image to the database.
	 * 
	 * @param type       - the type of image that is being set (cover, logo,
	 *                   profile, etc..)
	 * @param businessId
	 * @param model
	 * @param file       - uploaded file
	 * @return
	 */
	@PostMapping(value = "/setimage{type}/{businessId}", produces = "application/json;charset=UTF-8", consumes = "multipart/form-data")
	public @ResponseBody String businessSetImage(@PathVariable String type, @PathVariable Long businessId,
			@RequestParam("image-data") MultipartFile file) {
		JsonComponent jsonComponent = new JsonComponent();

		try {
			mediaService.setImage(file, type, businessId);
			jsonComponent.addMessageSuccess("message", "Image uploaded successfully");

			return jsonComponent.getJson();
		} catch (Exception e) {
			e.printStackTrace();
			jsonComponent.addMessageFailure("message", "Failure uploading image");

			return jsonComponent.getJson();
		}
	}

	@GetMapping(value = "/deleteimage/{imageId}", produces = "application/json;charset=UTF-8")
	public @ResponseBody String business(@PathVariable Long imageId) {
		JsonComponent jsonComponent = new JsonComponent();

		try {
			mediaService.deleteImage(imageId);
			jsonComponent.addMessageSuccess("message", "Image deleted successfully");

			return jsonComponent.getJson();
		} catch (Exception e) {
			e.printStackTrace();
			jsonComponent.addMessageFailure("message", "Failure deleting image");

			return jsonComponent.getJson();
		}
	}

	/**
	 * Edit - Display a business listing, or save one if a POST was sent
	 * 
	 * @return
	 */
	@RequestMapping(value = "/edit/{businessId}")
	public String businessEdit(@PathVariable Long businessId, @Valid BusinessDto businessDto,
			BindingResult bindingResult, Model model, HttpServletRequest request) {
		try {
			final Business business = businessService.ownerGetFirstBusiness(businessId);

			if (request.getMethod().equals("GET")) {
				businessDto = modelMapper.map(business, BusinessDto.class);
				model.addAttribute("businessDto", businessDto);
				return "business/edit";
			} else if (business.getId() != businessDto.getId()) {// checking permissions
				return "redirect:/console/";
			} else if (bindingResult.hasErrors()) {
				return "business/edit";
			}

			try {
				Business savedBusiness = businessService.ownerUpdateBusiness(businessDto);
				return "redirect:/business/view/" + savedBusiness.getId();
			} catch (Exception ex) {
				return "redirect:/console/";
			}
		} catch (Exception e) {
			return "redirect:/console/";
		}
	}

	/**
	 * Add business page - view
	 * 
	 * @return
	 */
	@RequestMapping(value = "/add")
	public String businessAdd(final BusinessDto businessDto) {
		return "business/add";
	}

	/**
	 * Add business page - post reply from user
	 * 
	 * @return
	 */
	@PostMapping(value = "/add")
	public String registerPost(@Valid final BusinessDto businessDto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "business/add";
		} else {
			try {
				final Business businessReg = businessService.registerNewBusiness(businessDto);

				if (!businessReg.isNew()) {
					return "redirect:/business/view/" + businessReg.getId();
				} else {
					return "business/add";
				}
			} catch (Exception ex) {
				return "business/add";
			}
		}
	}
}
