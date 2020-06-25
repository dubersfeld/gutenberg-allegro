package com.dub.gutenberg.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dub.gutenberg.services.ClusterService;

@RestController
public class ClusterController {
	
	@Autowired
	private ClusterService clusterService;

	@RequestMapping(value = "/startNode",
			method = RequestMethod.POST)
	public String startNode(@RequestBody IpForm ipForm) {
			
		try {
			clusterService.startNode(ipForm.getIp());
		} catch (IOException e) {
			System.out.println("/startNode exception caught "
					+ e);
		}
		return "STARTED";		
	}
	
	@RequestMapping(value = "/stopNode",
			method = RequestMethod.POST)
	public String stopNode(@RequestBody IpForm ipForm) {
			
		try {
			clusterService.stopNode(ipForm.getIp());
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("/stopNode exception caught "
					+ e);	
		}
		return "STOPPED";		
	}
	

	@RequestMapping(value = "/excludeNode",
			method = RequestMethod.POST)
	public String excludeNode(@RequestBody IpForm ipForm) {
			
		// first find an active node
		try {
			clusterService.excludeNode(ipForm.getIp());
		} catch (IOException e) {
			System.out.println("/excludeNode exception caught "
					+ e);
			//e.printStackTrace();
		}
		return "EXCLUDED";		
	}
	

	@RequestMapping(value = "/includeNode",
			method = RequestMethod.POST)
	public String includeNode(@RequestBody IpForm ipForm) {
			
		try {
			clusterService.includeNode(ipForm.getIp());
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("/includeNode exception caught "
					+ e);
		}
		return "INCLUDED";		
	}
}
