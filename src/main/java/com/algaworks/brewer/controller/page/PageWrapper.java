package com.algaworks.brewer.controller.page;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

public class PageWrapper<T> {
	private Page<T> page1;
	private UriComponentsBuilder uriBuilder;
	
	public PageWrapper(Page<T> page, HttpServletRequest httpServletRequest){
		this.page1 = page;
		//this.uriBuilder = ServletUriComponentsBuilder.fromRequest(httpServletRequest);
		String httpUrl = httpServletRequest.getRequestURL().append(
				httpServletRequest.getQueryString() != null ? "?" + httpServletRequest.getQueryString(): "")
				.toString().replaceAll("\\+","%20").replaceAll("excluido", "");
		this.uriBuilder = UriComponentsBuilder.fromHttpUrl(httpUrl);
	}
	
	public List<T> getConteudo(){
		return page1.getContent();
	}
	public boolean isVazia(){
		return page1.getContent().isEmpty();
	}
	
	public int getAtual(){
		return page1.getNumber();
	}
	public boolean isPrimeira(){
		return page1.isFirst();
	}
	public boolean isUltima(){
		return page1.isLast();
	}
	public int getTotal(){
		return page1.getTotalPages();
	}
	public String urlParaPagina(int pagina){
		return uriBuilder.replaceQueryParam("page", pagina).build(true).encode().toUriString();
	}
	public String urlOrdenada(String propriedade){
		UriComponentsBuilder uriBuilderOrder = UriComponentsBuilder.fromUriString(uriBuilder.build(true).encode().toUriString());
		String valorSort = String.format("%s,%s", propriedade, inverterDirecao(propriedade));
		
		return uriBuilderOrder.replaceQueryParam("sort", valorSort).build(true).encode().toUriString();		
	}
	public String inverterDirecao(String propriedade){
		String direcao = "asc";
		Order order = page1.getSort() != null ? page1.getSort().getOrderFor(propriedade) : null;
		if(order != null){
			direcao = Sort.Direction.ASC.equals(order.getDirection()) ? "desc" : "asc";
		}
		return direcao;
	}
	
	public Boolean descendente(String propriedade){
		return inverterDirecao(propriedade).equals("asc");
	}
	public boolean ordenada(String propriedade){
		Order order = page1.getSort() != null ? page1.getSort().getOrderFor(propriedade) : null;

		if(order==null){
			return false;
		}
		return page1.getSort().getOrderFor(propriedade) != null ? true : false;
	}
}
