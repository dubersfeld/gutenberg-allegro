package com.dub.gutenberg.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DefaultController {
	
	@Value("${startServerUrl}")
	private String startServerUrl;
	
	@Value("${stopServerUrl}")
	private String stopServerUrl;
	
	@Value("${excludeServerUrl}")
	private String excludeServerUrl;
	
	@Value("${includeServerUrl}")
	private String includeServerUrl;
	
	@RequestMapping({"/", "/index", "/backHome"})
	public String home(ModelMap model) {
		
		model.addAttribute("startServerUrl", startServerUrl);
		model.addAttribute("stopServerUrl", stopServerUrl);
		model.addAttribute("excludeServerUrl", excludeServerUrl);
		model.addAttribute("includeServerUrl", includeServerUrl);
	
		return "dashboard";
	}

}
