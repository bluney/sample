package bluney.sample.sample.service;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import bluney.sample.sample.HomeController;

/**
 * Handles requests for the ajax request.
 */
@Controller
public class AjaxController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/resource/ajax/dashboard.html", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
	
		return "/ajax/dashboard";
//		return "/loginForm";
	}
}
