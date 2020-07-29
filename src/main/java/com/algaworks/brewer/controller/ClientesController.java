package com.algaworks.brewer.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.brewer.controller.page.PageWrapper;
import com.algaworks.brewer.model.Cerveja;
import com.algaworks.brewer.model.Cliente;
import com.algaworks.brewer.model.TipoPessoa;
import com.algaworks.brewer.repository.Clientes;
import com.algaworks.brewer.repository.Estados;
import com.algaworks.brewer.repository.filter.ClienteFilter;
import com.algaworks.brewer.service.CadastroClienteService;
import com.algaworks.brewer.service.exception.CpfClienteJaCadastradoException;

@Controller
@RequestMapping("/clientes")
public class ClientesController {
	
	@Autowired
	private Estados estadosrepository;
	
	@Autowired
	private Clientes clientes;
	
	@Autowired
	private CadastroClienteService cadastroClienteService;
	


	@RequestMapping("/novo")
	public ModelAndView novo(Cliente cliente) {
		ModelAndView mv = new ModelAndView("cliente/CadastroCliente");
		mv.addObject("tiposPessoa", TipoPessoa.values());
		mv.addObject("estados", estadosrepository.findAll());

		return mv;
	}
		
	@PostMapping("/novo")
	public ModelAndView salvar(@Valid Cliente cliente, 
			BindingResult result, RedirectAttributes attributes){
		if(result.hasErrors()){
			return novo(cliente);
		}
		try{
			cadastroClienteService.salvar(cliente);
		}catch (CpfClienteJaCadastradoException e) {
			result.rejectValue("cpfOuCnpj",e.getMessage(), e.getMessage());
			return novo(cliente);
		}
		attributes.addFlashAttribute("mensagem", "Cliente salvo com sucesso!");
		return new ModelAndView("redirect:/clientes/novo");
	}
	
	@GetMapping
	public ModelAndView pesquisar(ClienteFilter clienteFilter, BindingResult result,
			@PageableDefault(size = 5) Pageable pageable, HttpServletRequest http){
		ModelAndView mv = new ModelAndView("cliente/PesquisaCliente");
		PageWrapper<Cliente> pagina = new PageWrapper<>(clientes.filtrar(clienteFilter, pageable), http);
		mv.addObject("pagina", pagina);
		return mv;
	}
	
	@RequestMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
	public @ResponseBody List<Cliente> pesquisar(String nome){
		validarTamanhoNome(nome);
		return clientes.findByNomeStartingWithIgnoreCase(nome);
	}
	
	
	private void validarTamanhoNome(String nome) {
		if(StringUtils.isEmpty(nome) || nome.length() < 3){
			throw new IllegalArgumentException();
		}
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Void> tratarIllegalArgumentException(IllegalArgumentException e){
		return ResponseEntity.badRequest().build();
	}
}
