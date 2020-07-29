package com.algaworks.brewer.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.brewer.controller.page.PageWrapper;
import com.algaworks.brewer.model.Cidade;
import com.algaworks.brewer.repository.Cidades;
import com.algaworks.brewer.repository.Estados;
import com.algaworks.brewer.repository.filter.CidadeFilter;
import com.algaworks.brewer.service.CadastroCidadeService;
import com.algaworks.brewer.service.exception.CidadeJaCadastradoException;
import com.algaworks.brewer.service.exception.ImpossivelExcluirEntidadeException;

@Controller
@RequestMapping("/cidades")
public class CidadesController {

	@Autowired
	private Cidades cidades;
	
	@Autowired
	private Estados estados;
	
	@Autowired
	private CadastroCidadeService cadastroCidadeService;
	
	@RequestMapping("/nova")
	public ModelAndView nova(Cidade cidade) {
		ModelAndView mv = new ModelAndView("cidade/CadastroCidade");
		mv.addObject("estados", estados.findAll());

		return mv;
	}

	@PostMapping({"/nova", "{\\d+}"})
	@CacheEvict(value = "cidades", key = "#cidade.estado.codigo", condition = "#cidade.temEstado()")
	public ModelAndView salvar(@Valid Cidade cidade, Model model, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return nova(cidade);
		}
		
		try {
			cadastroCidadeService.salvar(cidade);
		} catch (CidadeJaCadastradoException e) {
			result.rejectValue("nome", e.getMessage(), e.getMessage());
			return nova(cidade);
		}
		
		attributes.addFlashAttribute("mensagem", "Cidade salva com sucesso!");
		return new ModelAndView("redirect:/cidades/nova");
	}
	@Cacheable("cidades")
	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Cidade> pesquisarPorCodigoEstado(
			@RequestParam(name = "estado", defaultValue="-1") Long codigoEstado){
		try{
			Thread.sleep(100);
		}catch (InterruptedException e) {}
			return cidades.findByEstadoCodigo(codigoEstado);
		
	}
	@GetMapping
	public ModelAndView pesquisar(CidadeFilter cidadeFilter,
			BindingResult result, @PageableDefault(size = 5) Pageable pageable, HttpServletRequest http) {
		ModelAndView mv = new ModelAndView("cidade/PesquisaCidade");
		mv.addObject("estados", estados.findAll());
		
		
		PageWrapper<Cidade> pagina = new PageWrapper<>(cidades.filtrar(cidadeFilter, pageable), http);
		mv.addObject("pagina", pagina);
		return mv;
	}

	@DeleteMapping("/{codigo}")
	public ResponseEntity<?> excluir(@PathVariable("codigo") Cidade cidade) {
		try {
			this.cadastroCidadeService.excluir(cidade);
		} catch (ImpossivelExcluirEntidadeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok().build();
	}
	@GetMapping("/{codigo}")
	public ModelAndView atualizar(@PathVariable("codigo") Cidade cidade) {
		ModelAndView mv = this.nova(cidade);
		mv.addObject(this.cidades.findByCodigoFetchingEstado(cidade.getCodigo()));
		return mv;
	}
	
}
