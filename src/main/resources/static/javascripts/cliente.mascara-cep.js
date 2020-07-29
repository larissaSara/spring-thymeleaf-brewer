var Brewer = Brewer || {};

Brewer.MascaraCep = (function(){
	
	function MascaraCep(){
		this.inputCep = $('#jscep');
	}
	MascaraCep.prototype.iniciar = function(){
		this.inputCep.mask(this.inputCep.data('mascara'));
		console.log(this.inputCep.data('mascara'));

	}
	
	return MascaraCep;
}());

$(function(){
	var mascaraCep = new Brewer.MascaraCep();
	mascaraCep.iniciar();
});