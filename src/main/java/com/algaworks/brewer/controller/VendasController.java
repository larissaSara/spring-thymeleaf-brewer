package com.algaworks.brewer.controller;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.brewer.controller.page.PageWrapper;
import com.algaworks.brewer.controller.validator.VendaValidator;
import com.algaworks.brewer.dto.VendaMes;
import com.algaworks.brewer.dto.VendaOrigem;
import com.algaworks.brewer.mail.Mailer;
import com.algaworks.brewer.model.Cerveja;
import com.algaworks.brewer.model.ItemVenda;
import com.algaworks.brewer.model.Origem;
import com.algaworks.brewer.model.Sabor;
import com.algaworks.brewer.model.StatusVenda;
import com.algaworks.brewer.model.TipoPessoa;
import com.algaworks.brewer.model.Venda;
import com.algaworks.brewer.repository.Cervejas;
import com.algaworks.brewer.repository.Vendas;
import com.algaworks.brewer.repository.filter.CervejaFilter;
import com.algaworks.brewer.repository.filter.VendaFilter;
import com.algaworks.brewer.security.UsuarioSistema;
import com.algaworks.brewer.service.CadastroVendaService;
import com.algaworks.brewer.session.TabelasItensSession;

import org.thymeleaf.util.StringUtils;


@Controller
@RequestMapping("/vendas")
public class VendasController {
	
	@Autowired
	private Cervejas cervejas;
	
	@Autowired
	private Vendas vendas;
	
	@Autowired
	private TabelasItensSession tabelaItensVenda;
	
	@Autowired
	private CadastroVendaService cadastroVendaService;
	
	@Autowired
	private VendaValidator vendaValidator;
	
	@Autowired
	private Mailer mailer;
	
	@InitBinder("venda")
	public void inicializarValidator(WebDataBinder binder){
		binder.setValidator(vendaValidator);
	}
	
	@GetMapping("/nova")
	public ModelAndView nova(Venda venda){
		ModelAndView mv = new ModelAndView("venda/CadastroVenda");
		
		setUuid(venda);
		mv.addObject("itens", venda.getItens());
		mv.addObject("valorFrete", venda.getValorFrete());
		mv.addObject("valorDesconto", venda.getValorDesconto());
		mv.addObject("valorTotalItens", tabelaItensVenda.getValorTotal(venda.getUuid()));
		return mv;

	}
	
	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable("codigo") Long codigo){
		Venda venda = vendas.buscarComItens(codigo);
		setUuid(venda);
		
		for(ItemVenda item : venda.getItens()){
			tabelaItensVenda.adicionarItem(venda.getUuid(), item.getCerveja(), item.getQuantidade());
		}
		ModelAndView mv =nova(venda);
		mv.addObject(venda);
		
		return mv;
	}
	
	@PostMapping(value= "/nova", params="cancelar")
	public ModelAndView cancelar(Venda venda, 
			BindingResult result, RedirectAttributes attributes,
			@AuthenticationPrincipal UsuarioSistema usuariosistema){
		
		try{
			cadastroVendaService.cancelar(venda);
			
		}catch (AccessDeniedException e) {
			return new ModelAndView("/403");
		}
	
		attributes.addFlashAttribute("mensagem", 
				String.format("Venda nº %d cancelada com sucesso!", venda.getCodigo()));
		return new ModelAndView("redirect:/vendas/" +venda.getCodigo());
	}
	
	@PostMapping(value = "/nova", params = "salvar")
	public ModelAndView salvar(Venda venda, BindingResult result, RedirectAttributes attributes, @AuthenticationPrincipal UsuarioSistema usuariosistema){
		validarVenda(venda, result);
		if(result.hasErrors()){
			return nova(venda);
		}
		venda.setUsuario(usuariosistema.getUsuario());
		
		cadastroVendaService.salvar(venda);
		attributes.addFlashAttribute("mensagem", 
				String.format("Venda nº %d salva com sucesso!", venda.getCodigo()));
		return new ModelAndView("redirect:/vendas/nova");
	}
	
	@GetMapping("/totalPorMes")
	public @ResponseBody List<VendaMes> listarTotalVendaPorMes(){
		return vendas.totalPorMes();
	}
	
	@GetMapping("/totalPorOrigem")
	public @ResponseBody List<VendaOrigem> listarTotalVendaPorOrigem(){
		return vendas.totalPorOrigem();
	}
	
	
	@PostMapping(value= "/nova", params="emitir")
	public ModelAndView emitir(Venda venda, BindingResult result, RedirectAttributes attributes, @AuthenticationPrincipal UsuarioSistema usuariosistema){
		validarVenda(venda, result);
		if(result.hasErrors()){
			return nova(venda);
		}
		venda.setUsuario(usuariosistema.getUsuario());
		
		cadastroVendaService.emitir(venda);
		attributes.addFlashAttribute("mensagem", 
				String.format("Venda n° %d emitida com sucesso!", venda.getCodigo()));
		return new ModelAndView("redirect:/vendas/nova");
	}

	
	
	@PostMapping(value= "/nova", params="enviarEmail")
	public ModelAndView enviarEmail(Venda venda, BindingResult result, RedirectAttributes attributes, @AuthenticationPrincipal UsuarioSistema usuariosistema){
		validarVenda(venda, result);
		if(result.hasErrors()){
			return nova(venda);
		}
		venda.setUsuario(usuariosistema.getUsuario());
		
		cadastroVendaService.salvar(venda);
		
		mailer.enviar(venda);
		attributes.addFlashAttribute("mensagem", String.format("Venda nº "
				+ "%d salva com sucesso e "
				+ "e-mail enviado", venda.getCodigo()));
		return new ModelAndView("redirect:/vendas/nova");
	}
	
	@PostMapping("/item")
	public ModelAndView adicionarItem(Long codigoCerveja, String uuid){
		Cerveja cerveja = cervejas.findOne(codigoCerveja);
		tabelaItensVenda.adicionarItem(uuid,cerveja, 1);
		return mvTabelaItensVenda(uuid);
	}
	
	@GetMapping
	public ModelAndView pesquisar(VendaFilter vendaFilter,
			BindingResult result, @PageableDefault(size = 5) Pageable pageable, HttpServletRequest http) {
		ModelAndView mv = new ModelAndView("venda/PesquisaVenda");
		
		mv.addObject("todosStatus", StatusVenda.values());
		mv.addObject("tiposPessoa", TipoPessoa.values());

		PageWrapper<Venda> pagina = new PageWrapper<>(vendas.filtrar(vendaFilter, pageable), http);
		mv.addObject("pagina", pagina);
		return mv;
	}
	
	@PutMapping("/item/{codigoCerveja}")
	public ModelAndView alterarQuantidadeItem(@PathVariable("codigoCerveja") Cerveja cerveja, 
			Integer quantidade, String uuid){
		tabelaItensVenda.alterarQuantidadeItens(uuid,cerveja, quantidade);
		return mvTabelaItensVenda(uuid);
	}
	
	@DeleteMapping("/item/{uuid}/{codigoCerveja}")
	public ModelAndView excluirItem(@PathVariable("codigoCerveja") Cerveja cerveja, @PathVariable String uuid){
		tabelaItensVenda.excluirItem(uuid, cerveja);
		return mvTabelaItensVenda(uuid);
	}
	private ModelAndView mvTabelaItensVenda(String uuid) {
		ModelAndView mv = new ModelAndView("venda/TabelaItensVenda");
		mv.addObject("itens", tabelaItensVenda.getItens(uuid));
		mv.addObject("valorTotal", tabelaItensVenda.getValorTotal(uuid));
		
		return mv;
	}
	private void validarVenda(Venda venda, BindingResult result) {
		venda.adicionarItens(tabelaItensVenda.getItens(venda.getUuid()));
		venda.calcularValorTotal();
		
		vendaValidator.validate(venda, result);
	}
	
	private void setUuid(Venda venda){
		if(StringUtils.isEmpty(venda.getUuid())){
			venda.setUuid(UUID.randomUUID().toString());
		}
	}
}
