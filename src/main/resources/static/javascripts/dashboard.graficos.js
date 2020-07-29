var Brewer = Brewer || {};

Brewer.GraficoVendaPorMes = (function() {
	
	function GraficoVendaPorMes() {
		this.ctx = $('#graficoVendasPorMes')[0].getContext('2d');
	}
	
	GraficoVendaPorMes.prototype.iniciar = function() {
		
		$.ajax({
			url: 'vendas/totalPorMes',
			method: 'GET',
			success: onDadosRecebidos.bind(this)
		})
	}
	function onDadosRecebidos(vendaMes){
		var meses = [];
		var valores = [];
		vendaMes.forEach(function(obj){
			meses.unshift(obj.mes);
			valores.unshift(obj.total)
		});
		
		var graficoVendasPorMes = new Chart(this.ctx, {
		    type: 'line',
		    data: {
		    	labels: meses,
		    	datasets: [{
		    		label: 'Vendas por mês',
		    		backgroundColor: "rgba(26,179,148,0.5)",
	                pointBorderColor: "rgba(26,179,148,1)",
	                pointBackgroundColor: "#fff",
	                data: valores
		    	}]
		    },
		});
	}
	
	return GraficoVendaPorMes;
	
}());

Brewer.GraficoVendaPorOrigem = (function() {
	
	function GraficoVendaPorOrigem() {
		this.ctx = $('#graficoVendaPorOrigem')[0].getContext('2d');
	}
	
	GraficoVendaPorOrigem.prototype.iniciar = function() {
		
		$.ajax({
			url: 'vendas/totalPorOrigem',
			method: 'GET',
			success: onDadosRecebidos.bind(this)
		})
	}
	function onDadosRecebidos(vendaOrigem){
		var meses = [];
		var cervejasNacionais = [];
		var cervejasInternacionais = [];
		
		vendaOrigem.forEach(function(obj){
			meses.unshift(obj.mes);
			cervejasNacionais.unshift(obj.total_nacional);
			cervejasInternacionais.unshift(obj.total_internacional);
		});
		
		var graficoVendaPorOrigem = new Chart(this.ctx, {
		    type: 'bar',
		    data: {
		    	labels: meses,
		    	datasets: [{
		    		label: 'Nacional',
		    		backgroundColor: "rgba(220,220,220,0.5)",
	                data: cervejasNacionais
		    	},
		    	{
		    		label: 'Internacional',
		    		backgroundColor: "rgba(26,179,148,0.5)",
	                data: cervejasInternacionais
		    	}]
		    },
		});
	}
	
	return GraficoVendaPorOrigem;
	
}());

$(function() {
	var graficoVendaPorMes = new Brewer.GraficoVendaPorMes();
	graficoVendaPorMes.iniciar();
	
	var graficoVendaPorOrigem = new Brewer.GraficoVendaPorOrigem();
	graficoVendaPorOrigem.iniciar();
});