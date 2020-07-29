package com.algaworks.brewer.service.exception;

public class UsuarioJaCadastradoException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public UsuarioJaCadastradoException(String message){
		super(message);
	}
}
