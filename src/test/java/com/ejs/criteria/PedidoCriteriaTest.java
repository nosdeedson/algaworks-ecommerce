package com.ejs.criteria;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.junit.Assert;
import org.junit.Test;

import com.ejs.entityManager.EntityManagerTest;
import com.ejs.model.Pedido;
import com.ejs.model.Pedido_;

public class PedidoCriteriaTest extends EntityManagerTest {

	@Test
	public void usandoFuncoesNativas() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
		Root<Pedido> root = query.from(Pedido.class);
		
		query.multiselect(
					root.get("id"),
					builder.function("dayname", String.class, root.get("dataCriacao") ) // dayname function native of the mysql
				);
		
		query.where(builder.isTrue(
					builder.function("acima_media_faturamento", Boolean.class, root.get(Pedido_.total))
				));
		
		TypedQuery<Object[]> typedQuery = entityManager.createQuery(query);
		
		List<Object[]> list = typedQuery.getResultList();
		list.forEach(arr -> System.out.println(arr[0] + " dayname: " + arr[1]));
		
		Assert.assertFalse(list.isEmpty());
	}
}
