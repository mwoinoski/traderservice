package com.acme.acmetrade.endpoints;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EchoEndpoint {
	
	@RequestMapping("/echo")
	public String echo(@RequestParam("str") String str) {
		return "echo: "+str;
	}

}
