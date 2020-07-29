package com.algaworks.brewer.service;

import java.util.Optional;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.brewer.model.Cidade;
import com.algaworks.brewer.repository.Cidades;
import com.algaworks.brewer.service.exception.CidadeJaCadastradoException;
import com.algaworks.brewer.service.exception.ImpossivelExcluirEntidadeException;

@Service
public class CadastroCidadeService {
	
	@Autowired
	private Cidades cidades;
	
	
	@Transactional
	public void salvar(Cidade cidade) {
		Optional<Cidade> cidadeExistente = cidades.findByNome(cidade.getNome());
		if(cidadeExistente.isPresent()){
			throw new CidadeJaCadastradoException("Cidade já cadastrada");
		}
		cidades.save(cidade);
		

	}

	@Transactional
	public void excluir(Cidade cidade) {
		try {
			this.cidades.delete(cidade);
			this.cidades.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidadeException("Impossível apagar cidade. O registro está sendo usado.");
		}
	}
}
