package com.algaworks.brewer.repository.helper.estilo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.algaworks.brewer.model.Estilo;

public interface EstilosQueries {
	public Page<Estilo> filtrar(Estilo estilo, Pageable pageable);

}
