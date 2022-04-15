package com.ejs.criteria;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import org.junit.Assert;
import org.junit.Test;

import com.ejs.entityManager.EntityManagerTest;
import com.ejs.model.ItemPedido;
import com.ejs.model.ItemPedido_;
import com.ejs.model.Pedido;
import com.ejs.model.Pedido_;
import com.ejs.model.Produto_;

public class PedidoCriteriaTest extends EntityManagerTest {

	@Test
	public void findPedidoWithProdutoEspecifico() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Pedido> query = builder.createQuery(Pedido.class);
		Root<Pedido> root = query.from(Pedido.class);
		@SuppressWarnings("unchecked")
		Join<Pedido, ItemPedido> joinItemPedido = (Join<Pedido, ItemPedido>) root.fetch(Pedido_.itensPedido);
		query.where(builder.equal(joinItemPedido.get(ItemPedido_.produto).get(Produto_.id), 1));		
		query.select(root);
		TypedQuery<Pedido> typedQuery = entityManager.createQuery(query);
		List<Pedido> pedidos = typedQuery.getResultList();
		
		Assert.assertNotNull(pedidos);
		
		pedidos.forEach(pedido ->{
			System.out.print("Pedido id: " + pedido.getId());
			pedido.getItensPedido().forEach(item -> {				
				System.out.println(", produto nome: " + item.getProduto().getNome());
			});
		});
	}
	
	@Test
	public void functionOfAgregation() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
		Root<Pedido> root = query.from(Pedido.class);
		
		query.multiselect(
					builder.count(root.get("id")),
					builder.avg(root.get("total")),
					builder.sum(root.get("total")),
					builder.max(root.get("total")),
					builder.min(root.get("total"))
				);
		
		TypedQuery<Object[]> typedQuery = entityManager.createQuery(query);
		List<Object[]> list = typedQuery.getResultList();
		
		list.forEach( arr ->
					System.out.println(
							"count: " + arr[0] 
							+ " avg: " + arr[1]
							+ " sum: " + arr[2]
							+ " max: " + arr[3]
							+ " min: " + arr[4]
						)
				);
		Assert.assertFalse(list.isEmpty());
	}
}
