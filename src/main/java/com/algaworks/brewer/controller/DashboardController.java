package com.algaworks.brewer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.algaworks.brewer.repository.Cervejas;
import com.algaworks.brewer.repository.Clientes;
import com.algaworks.brewer.repository.Vendas;

@Controller
public class DashboardController {

	@Autowired
	private Vendas vendas;
	
	@Autowired
	private Clientes clientes;
	
	@Autowired
	private Cervejas cervejas;
	
	@GetMapping("/")
	public ModelAndView dashboard(){
		ModelAndView mv = new ModelAndView("Dashboard");
		
		//mv.addObject("valorEstoque", cervejas.v());
		mv.addObject("valorItensEstoque", cervejas.valorItensEstoque());
		
		mv.addObject("totalClientes", clientes.count());
		mv.addObject("vendasNoAno", vendas.valorTotalNoAno());
		mv.addObject("vendasNoMes", vendas.valorTotalNoMes());
		mv.addObject("ticketMedio", vendas.valorTicketMedioNoAno());

		return mv;
	}
	
	
}
