package com.algaworks.brewer.dto;

public class VendaOrigem {

	private String mes;
	private Integer total_nacional;
	private Integer total_internacional;
	
	public VendaOrigem(){
		
	}
	
	public VendaOrigem(String mes, Integer total_nacional, Integer total_internacional) {
		this.mes = mes;
		this.total_nacional = total_nacional;
		this.total_internacional = total_internacional;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public Integer getTotal_nacional() {
		return total_nacional;
	}

	public void setTotal_nacional(Integer total_nacional) {
		this.total_nacional = total_nacional;
	}

	public Integer getTotal_internacional() {
		return total_internacional;
	}

	public void setTotal_internacional(Integer total_internacional) {
		this.total_internacional = total_internacional;
	}
	
	
}
