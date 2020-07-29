package com.algaworks.brewer.session;

import org.junit.Test;

import com.algaworks.brewer.model.Cerveja;
import com.algaworks.brewer.session.TabelaItensVenda;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Before;

public class TabelaItensVendaTest {

	private TabelaItensVenda tabelaItensVenda;
	
	@Before
	public void setUp(){
		this.tabelaItensVenda = new TabelaItensVenda("1");
	}
	@Test
	public void deveCalcularValorTotalSemItens() throws Exception{
		assertEquals(BigDecimal.ZERO,tabelaItensVenda.getValorTotal());
	}
	
	@Test
	public void deveCalcularValorTotalComUmItem() throws Exception{
		Cerveja cerveja = new Cerveja();
		BigDecimal valor = new BigDecimal("8.90");
		cerveja.setValor(valor);
		
		tabelaItensVenda.adicionarItem(cerveja, 1);
		
		assertEquals(valor,tabelaItensVenda.getValorTotal());

	}
	
	@Test
	public void deveCalcularValorTotalComVariosItem() throws Exception{
		Cerveja c1 = new Cerveja();
		c1.setCodigo(1L);
		BigDecimal v1 = new BigDecimal("8.90");
		c1.setValor(v1);
		
		Cerveja c2 = new Cerveja();
		c2.setCodigo(2l);
		BigDecimal v2 = new BigDecimal("4.99");
		c2.setValor(v2);
		
		tabelaItensVenda.adicionarItem(c1, 1);
		tabelaItensVenda.adicionarItem(c2, 2);

		assertEquals(new BigDecimal("18.88"),tabelaItensVenda.getValorTotal());

	}
	@Test
	public void deveManterTamanhoDaListaParaMesmasCervejas() throws Exception {
		Cerveja c1 = new Cerveja();
		c1.setCodigo(1l);
		c1.setValor(new BigDecimal("8.90"));
		tabelaItensVenda.adicionarItem(c1, 1);
		tabelaItensVenda.adicionarItem(c1, 1);
		assertEquals(1, tabelaItensVenda.total());
	}
	
	@Test
	public void deveAlterarQuantidadeItem() throws Exception{
		Cerveja c1 = new Cerveja();
		c1.setCodigo(1l);
		c1.setValor(new BigDecimal("4.50"));
		tabelaItensVenda.adicionarItem(c1, 1);
		tabelaItensVenda.alterarQuantidadeItens(c1, 3);
		
		assertEquals(new BigDecimal("13.50"), tabelaItensVenda.getValorTotal());


	}
	@Test
	public void deveExcluirItem() throws Exception{
		Cerveja c1 = new Cerveja();
		c1.setCodigo(1L);
		BigDecimal v1 = new BigDecimal("8.90");
		c1.setValor(v1);
		
		Cerveja c2 = new Cerveja();
		c2.setCodigo(2l);
		BigDecimal v2 = new BigDecimal("4.99");
		c2.setValor(v2);
		
		Cerveja c3 = new Cerveja();
		c3.setCodigo(3l);
		BigDecimal v3 = new BigDecimal("4.99");
		c3.setValor(v3);
		
		tabelaItensVenda.adicionarItem(c1, 1);
		tabelaItensVenda.adicionarItem(c2, 1);
		tabelaItensVenda.adicionarItem(c3, 1);

		tabelaItensVenda.excluirItem(c1);
		
		assertEquals(2, tabelaItensVenda.total());
		assertEquals(new BigDecimal("9.98"),tabelaItensVenda.getValorTotal());


	}
}
