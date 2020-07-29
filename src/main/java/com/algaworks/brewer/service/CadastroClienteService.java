package com.algaworks.brewer.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.brewer.model.Cliente;
import com.algaworks.brewer.repository.Clientes;
import com.algaworks.brewer.service.exception.CpfClienteJaCadastradoException;

@Service
public class CadastroClienteService {

	@Autowired
	private Clientes clientes;
	
	
	//private ApplicationEventPublisher publisher;
	
	@Transactional
	public void salvar(Cliente cliente) {
		Optional<Cliente> clienteExistente = clientes.findByCpfOuCnpj(cliente.getCpfOuCnpjSemFormatacao());
		if(clienteExistente.isPresent()){
			throw new CpfClienteJaCadastradoException("CPF/CNPJ Já cadastrado");
		}
		clientes.save(cliente);
		//publisher.publishEvent(new CervejaSalvaEvent(cerveja));

	}
}
