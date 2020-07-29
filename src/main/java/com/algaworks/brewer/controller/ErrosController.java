package com.algaworks.brewer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrosController {

	
	@GetMapping("/404")
	public String paginaNaoEcontrada(){
		return "404";
	}
	@GetMapping("/500")
	public String erroServidor(){
		return "500";
	}
}
