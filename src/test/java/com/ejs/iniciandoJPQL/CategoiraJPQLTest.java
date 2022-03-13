package com.ejs.iniciandoJPQL;

import java.util.Arrays;
import java.util.List;

import javax.persistence.TypedQuery;

import org.junit.Assert;
import org.junit.Test;

import com.ejs.entityManager.EntityManagerTest;
import com.ejs.model.Categoria;

public class CategoiraJPQLTest extends EntityManagerTest{
	
	@Test
	public void usingIn() {
		String jqpl = "SELECT distinct c FROM Pedido p INNER JOIN ItemPedido ip on ip.pedido=p"
				+ " INNER JOIN Produto prod on ip.produto=prod"
				+ " INNER JOIN Categoria c on c in(:categorias)";
//				+ " WHERE c in (:categorias)";
		
		Categoria cat = new Categoria();
		cat.setId(2);
		List<Categoria> categorias = Arrays.asList(cat);
		
		TypedQuery<Categoria> typedQuery = entityManager.createQuery(jqpl, Categoria.class);
		typedQuery.setParameter("categorias", categorias);
		
		categorias = typedQuery.getResultList();
		
		Assert.assertFalse(categorias.isEmpty());
		
		categorias.forEach(c -> System.out.println(c.getNome()));
		
	}
	
	@Test
	public void groupBy() {
		
//		produtos por categoria
//		String jpql = "Select c.nome as nomeCategoria, count(p.id) qtd from Categoria c inner join "
//				+ " c.produtos p group by c.nome ";
		
//		vendas por ano mes
//		String jpql = "Select concat( year(p.dataCriacao),' ', function('monthname', p.dataCriacao)), "
//				+ " sum(p.total) from Pedido p "
//				+ " group by year(p.dataCriacao) , month(p.dataCriacao), concat( year(p.dataCriacao),' ', function('monthname', p.dataCriacao))";
//		vendas por categoria
//		String jpql = "Select c.nome, sum(prod.preco * ip.quantidade) from ItemPedido ip inner join ip.produto prod"
//				+ " inner join prod.categorias c group by c.nome";
		
//		vendas por cliente
//		String jpql = "select c.nome, sum(p.total) from Pedido p inner join p.cliente c"
//				+ " group by c.nome";
		
//		Vendas por categoria e dia
		String jpql = "select concat(c.nome, ' dia:', day(p.dataCriacao)), sum(p.total) from Pedido p"
				+ " inner join p.itensPedido ip inner join ip.produto prod inner join prod.categorias c"
				+ " group by concat(c.nome, ' ', day(p.dataCriacao)), c.nome";
		
		TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);
		
		List<Object[]> lista = typedQuery.getResultList();
		
		lista.forEach(l -> { System.out.println( l[0] + " - " + l[1]);});
	}
	
	@Test
	public void trimAndLength() {
		String jpql = "SELECT c.nome, length(c.nome) FROM Categoria c";
		
		TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);
		
		List<Object[]> lista = typedQuery.getResultList();
		
//		String jpql2 = "SELECT trim(c.nome), c.nome FROM Categoria c"; // trim retira os espaços vazios antes e depois do nome
//		
//		TypedQuery<Object[]> typedQuery2 = entityManager.createQuery(jpql2, Object[].class);
//		
//		List<Object[]> lista2 = typedQuery2.getResultList();
		
		lista.forEach(item -> System.out.println(item[0] + " - " + item[1]));
		
		Assert.assertTrue(lista.size() > 0);
	}

	@Test
	public void paginationCategoria() {
		String jpql = "SELECT c FROM Categoria c";
		
		int maxResult = 2;
		int page = 1;
		// FIRST_RESULT = MAX_RESULTS * (pagina - 1)
		int firstResult = maxResult * (page - 1);
		
		TypedQuery<Categoria> typedQuery = entityManager.createQuery(jpql, Categoria.class);
		typedQuery.setFirstResult(firstResult);
		typedQuery.setMaxResults(maxResult);
		
		List<Categoria> lista = typedQuery.getResultList();
		
		Assert.assertTrue(lista.size() == 2);
		
		lista.forEach(categoria ->{
			System.out.println(categoria.getNome());
		});
		
	}
}
