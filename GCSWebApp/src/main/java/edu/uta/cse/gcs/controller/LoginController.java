package edu.uta.cse.gcs.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.uta.cse.gcs.util.Constants;
import edu.uta.cse.gcs.util.GoogleCloudStorageManager;


@Controller
public class LoginController {

	
	private static Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
	/**
	 * This method 
	 * @param map : ModelMap object, for adding model data objects in the view
	 * @return : redirects to "Login.jsp"
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(ModelMap map) {
		LOGGER.info("Redirecting to Login page");
		return "Login";
	}
	
	/**
	 * Would be invoked to perform access denied action is the user login fails 
	 * @param map : ModelMap object, for adding model data objects in the view
	 * @return : redirects to "Login.jsp"
	 */
	@RequestMapping(value = "/accessDenied", method = RequestMethod.GET)
	public String loginError(ModelMap map) {
		map.addAttribute("error", "Invalid Email id or Password");
		return "Login";
	}
	

	/** Invoked once login is successful.This method would get perform actions such as: get details displayed on
	 *  dash-board & store variables in session.
	 * @param map : ModelMap object, for adding model data objects in the view
	 * @param principal : Principal object - Spring Security object, for retrieving the logged in user's user name from the current session.
	 * @param session : HttpSession object for storing the session
	 * @return : redirects to "home.jsp"
	 */
	@RequestMapping(value = "/loginSuccess")
	public String loginSuccess(ModelMap map, Principal principal,
			HttpSession session) {
		LOGGER.info("Logged in username : "+ principal.getName());
		session.setAttribute("userName", principal.getName());
		
		List<String> objectsInBucket = new ArrayList<>();
		objectsInBucket=GoogleCloudStorageManager.listBucket(Constants.BUCKET_NAME);
		map.addAttribute("objectsInBucket", objectsInBucket);
		if(objectsInBucket.size()<0){
			map.addAttribute("emptyBucket", "No objects in - "+Constants.BUCKET_NAME);
		}
		return "Home";

	}
	
}
