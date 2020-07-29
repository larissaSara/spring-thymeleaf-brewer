package com.algaworks.brewer.service;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.brewer.model.Cerveja;
import com.algaworks.brewer.repository.Cervejas;
import com.algaworks.brewer.service.exception.ImpossivelExcluirEntidadeException;
import com.algaworks.brewer.storage.FotoStorage;

@Service
public class CadastroCervejaService {

	@Autowired
	private Cervejas cervejas;
	
	@Autowired
	private FotoStorage fotoStorage;

	
	@Transactional
	public void salvar(Cerveja cerveja) {
		cervejas.save(cerveja);

	}
	
	@Transactional
	public void excluir(Cerveja cerveja){
		try{
			String foto = cerveja.getFoto();
			cervejas.delete(cerveja);
			cervejas.flush();
			fotoStorage.excluir(foto);
		}
		catch(PersistenceException e){
			throw new 
			ImpossivelExcluirEntidadeException("Impossivel apagar cerveja. Já foi usada em uma venda");
		}
	}
	
	
	
}
