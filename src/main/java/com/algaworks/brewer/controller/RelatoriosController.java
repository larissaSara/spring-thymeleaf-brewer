package com.algaworks.brewer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.algaworks.brewer.dto.PeriodoRelatorio;

@Controller
@RequestMapping("/relatorios")
public class RelatoriosController {

	@GetMapping("/vendasEmitidas")
	public ModelAndView relatorioVendasEmitidas(){
		ModelAndView mv = new ModelAndView("relatorio/RelatorioVendasEmitidas");
		mv.addObject(new PeriodoRelatorio());
		
		return mv;
	}
}
