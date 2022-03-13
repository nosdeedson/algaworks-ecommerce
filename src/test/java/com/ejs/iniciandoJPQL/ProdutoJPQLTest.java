package com.ejs.iniciandoJPQL;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.TypedQuery;

import org.junit.Assert;
import org.junit.Test;

import com.ejs.entityManager.EntityManagerTest;
import com.ejs.model.Produto;

public class ProdutoJPQLTest extends EntityManagerTest {
	
	// Todos os produtos que foram vendidos pelo preço atual
	
	@Test
	public void produtosVendidosPrecoAtual() {
		
		String jpql = "SELECT DISTINCT prod FROM Pedido p inner join p.itensPedido ip"
				+ " INNER JOIN ip.produto prod WHERE ip.precoProduto = ALL "
				+ "( SELECT precoProduto FROM ItemPedido ip2 WHERE ip2.produto = prod ) ";
		
		TypedQuery<Produto> typedQuery = entityManager.createQuery(jpql, Produto.class);
		
		List<Produto> produtos = typedQuery.getResultList();
		
		produtos.forEach(prod -> System.out.println(prod.getNome()));
		
		Assert.assertFalse(produtos.size() != 2);
	}
	
	@Test
	public void findSeProdutoFoiVendidoDepoisAlta() {
		String jpql = "SELECT p FROM Produto p WHERE EXISTS "
				+ " ( SELECT p FROM Pedido p INNER JOIN p.itensPedido ip "
				+ " INNER JOIN ip.produto prod "
				+ " WHERE ip.precoProduto = 799)";
		
		TypedQuery<Produto> typedQuery = entityManager.createQuery(jpql, Produto.class);
		
		List<Produto> produtos = typedQuery.getResultList();
		
		produtos.forEach(prod -> { System.out.println(prod.getNome()); });
		
		Assert.assertFalse(produtos.size() > 0);
	}
	
	@Test
	public void findByPrecoDiferente() {
		String jpql = "SELECT p FROM Produto p "
				+ " WHERE p.preco <> 499";
			
		TypedQuery<Produto> typedQuery = entityManager.createQuery(jpql, Produto.class);
		
		List<Produto> lista = typedQuery.getResultList();
		
		Assert.assertFalse(lista.isEmpty());
	}
	
	@Test
	public void findByPrecoBetween() {
		String jpql = "SELECT p FROM Produto p "
				+ " WHERE p.preco BETWEEN :precoInicial AND :precoFinal";
			
		TypedQuery<Produto> typedQuery = entityManager.createQuery(jpql, Produto.class);
		typedQuery.setParameter("precoInicial", new BigDecimal(400));		
		typedQuery.setParameter("precoFinal", new BigDecimal(1500));
		
		List<Produto> lista = typedQuery.getResultList();
		
		Assert.assertFalse(lista.isEmpty());
	}

	@Test
	public void findProdutoWithoutFoto() {
		String jpql = "SELECT p FROM Produto p WHERE p.foto is null"; // could be is not null
		
		TypedQuery<Produto> typedQuery = entityManager.createQuery(jpql, Produto.class);
		
		List<Produto> lista = typedQuery.getResultList();
				
		Assert.assertFalse(lista.isEmpty());
	}
	
	@Test
	public void findProdutoWithoutCategoria() {
		String jpql = "SELECT p FROM Produto p WHERE p.categorias is EMPTY"; // could be is not empty
		
		TypedQuery<Produto> typedQuery = entityManager.createQuery(jpql, Produto.class);
		
		List<Produto> lista = typedQuery.getResultList();
				
		Assert.assertTrue(lista.isEmpty());
	}
}
