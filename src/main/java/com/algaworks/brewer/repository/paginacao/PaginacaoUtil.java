package com.algaworks.brewer.repository.paginacao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PaginacaoUtil {

	public void preparar(Criteria criteria, Pageable pageable){

		int paginaAtual = pageable.getPageNumber();
		int totalRegistroPorPagina = pageable.getPageSize();
		int primeiroRegistro = paginaAtual * totalRegistroPorPagina; 
				
		criteria.setFirstResult(primeiroRegistro);
		criteria.setMaxResults(totalRegistroPorPagina);
		
		Sort sort = pageable.getSort();
		if(sort !=null){
			Sort.Order order = sort.iterator().next();
			String property = order.getProperty();
			criteria.addOrder(order.isAscending() ? Order.asc(property) : Order.desc(property));
		}
	}
}
