var Brewer = Brewer || {};

Brewer.MascaraCpfCnpj = (function(){
	
	function MascaraCpfCnpj(){
		this.radioTipoPessoa = $(".js-radio-tipo-pessoa");
		this.labelCpfCnpj = $('[for=cpfOuCnpj]');
		this.inputCpfOuCnpj = $('#cpfOuCnpj');
	}
	MascaraCpfCnpj.prototype.iniciar = function(){
		this.radioTipoPessoa.on('change', onTipoPessoaAlterado.bind(this));
		var tipoPessoaSelecionada = this.radioTipoPessoa.filter(':checked')[0];
		if(tipoPessoaSelecionada){
			aplicarMascara.call(this, $(tipoPessoaSelecionada));
		}
	}
	
	function onTipoPessoaAlterado(evento){
		var tipoPessoaSelecionada = $(evento.currentTarget);
		aplicarMascara.call(this, tipoPessoaSelecionada);
		this.inputCpfOuCnpj.val('');
	}
	
	function aplicarMascara(tipoPessoaSelecionada){
		this.labelCpfCnpj.text(tipoPessoaSelecionada.data('documento'));
		this.inputCpfOuCnpj.mask(tipoPessoaSelecionada.data('mascara'));
		this.inputCpfOuCnpj.removeAttr('disabled');
	}
	
	return MascaraCpfCnpj;
}());

$(function(){
	var mascaraCpfCnpj = new Brewer.MascaraCpfCnpj();
	mascaraCpfCnpj.iniciar();
});