package com.ejs.iniciandoJPQL;

import java.util.List;

import javax.persistence.TypedQuery;

import org.junit.Assert;
import org.junit.Test;

import com.ejs.entityManager.EntityManagerTest;
import com.ejs.model.Pedido;
import com.ejs.model.Produto;

public class NamedQueryTest extends EntityManagerTest {
	
	@Test
	public void findAllPedido() {
		TypedQuery<Pedido> typedQuery = entityManager.createNamedQuery("PedidoFindAll", Pedido.class);
		List<Pedido> pedidos = typedQuery.getResultList();
		pedidos.forEach(p -> System.out.println("Id " + p.getId()));
		
		Assert.assertFalse(pedidos.isEmpty());
	}
	
	@Test
	public void findProdutoByCategoria() {
		TypedQuery<Produto> typedQuery = entityManager.createNamedQuery("ProdutoByCategoria", Produto.class);
		typedQuery.setParameter("categoria", 2);
		List<Produto> produtos = typedQuery.getResultList();
		produtos.forEach(p -> System.out.println(p.getNome()));
		
		Assert.assertFalse(produtos.isEmpty());
	}
	
	@Test
	public void usingNamedQuery() {
		TypedQuery<Produto> typedQuery = entityManager.createNamedQuery("Produto.listar", Produto.class);
		List<Produto> produtos = typedQuery.getResultList();
		produtos.forEach(p -> System.out.println(p.getNome()));
		
		Assert.assertFalse(produtos.isEmpty());
	}

}
