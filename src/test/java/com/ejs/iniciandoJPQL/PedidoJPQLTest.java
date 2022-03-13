package com.ejs.iniciandoJPQL;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.persistence.TypedQuery;

import org.junit.Assert;
import org.junit.Test;

import com.ejs.entityManager.EntityManagerTest;
import com.ejs.model.Categoria;
import com.ejs.model.ItemPedido;
import com.ejs.model.Pedido;
import com.ejs.model.enums.StatusPagamento;

public class PedidoJPQLTest extends EntityManagerTest{
	
	@Test
	public void usingIn() {
		
		Categoria categoria = new Categoria();
		categoria.setId(2);
		List<Categoria> categorias = Arrays.asList(categoria);
		
		String jpql = "SELECT distinct p FROM Pedido p INNER JOIN p.itensPedido ip "
				+ "	INNER JOIN ip.produto prod INNER JOIN prod.categorias cats"
				+ " WHERE cats in (:categorias)";
		
		TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);
		typedQuery.setParameter("categorias", categorias);
		
		List<Pedido> lista = typedQuery.getResultList();
		
		System.out.println(lista.size());
		
		Assert.assertFalse(lista.isEmpty());
		
		lista.forEach(l -> {
			System.out.println("id " + l.getId());
			l.getItensPedido().forEach(i -> System.out.println(i.getProduto().getCategorias().get(0).getNome()));
		});
	}
	
	@Test
	public void teste() {
		String jpql = "SELECT p FROM Pedido p INNER JOIN ItemPedido ip on ip.pedido=p ";
		
		TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);
		
		List<Pedido> lista = typedQuery.getResultList();
		
		lista.forEach(System.out::println);
	}
	
	@Test
	public void useFunctionCreatedInMysql() {
		String jpql = "SELECT function('dayname', p.dataCriacao) FROM Pedido p "
				+ "WHERE FUNCTION( 'acima_media_faturamento', p.total) = 1";
		
		TypedQuery<String> typedQuery = entityManager.createQuery(jpql, String.class);
		
		List<String> lista = typedQuery.getResultList();
		
		lista.forEach(System.out::println);
	}
	
	@Test
	public void findByDataCriacao() {
		String jpql = "SELECT p FROM Pedido p "
				+ " WHERE p.dataCriacao BETWEEN :dataInicial AND :dataFinal";
		
		TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);
		typedQuery.setParameter("dataInicial", LocalDateTime.now().minusMonths(3L));		
		typedQuery.setParameter("dataFinal", LocalDateTime.now());
		
		List<Pedido> lista = typedQuery.getResultList();
		
		Assert.assertFalse(lista.isEmpty());
	}
	
	
	@Test
	public void buscandoPedidoUsingPotion() {
		String jpql = "SELECT p FROM Pedido p JOIN p.pagamento pag "
				+ " WHERE p.id = ?1 AND pag.status = ?2";
		
		TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);
		typedQuery.setParameter(1, 2);
		typedQuery.setParameter(2, StatusPagamento.PROCESSANDO);
		
		List<Pedido> lista = typedQuery.getResultList();
		
		Assert.assertFalse(lista.isEmpty());
	}
	
	@Test
	public void buscandoPedidoUsingNameAtribute() {
		String jpql = "SELECT p FROM Pedido p JOIN p.pagamento pag "
				+ " WHERE p.id = :pedidoId AND pag.status = :status";
		
		TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);
		typedQuery.setParameter("pedidoId", 2);
		typedQuery.setParameter("status", StatusPagamento.PROCESSANDO);
		
		List<Pedido> lista = typedQuery.getResultList();
		
		Assert.assertFalse(lista.isEmpty());
	}
	
	
	@Test
	public void buscandoPedidoByProduto() {
		String jpql = "SELECT ip FROM ItemPedido ip INNER JOIN FETCH ip.produto prod "
				+ " WHERE prod.id =1";
		
		TypedQuery<ItemPedido> typedQuery = entityManager.createQuery(jpql, ItemPedido.class);
		
		List<ItemPedido> lista = typedQuery.getResultList();
		
		lista.forEach(item -> {
			System.out.println(item.getProduto().getNome());
		});
	}

}
