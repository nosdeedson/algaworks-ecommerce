package com.ejs.criteria;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.junit.Assert;
import org.junit.Test;

import com.ejs.entityManager.EntityManagerTest;
import com.ejs.model.Categoria;
import com.ejs.model.ItemPedido;
import com.ejs.model.Produto;

public class CategoriaCriteriaTest extends EntityManagerTest {

	@Test
	public void totalVendidoPorCategoria() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
		Root<ItemPedido> root = query.from(ItemPedido.class);
		Join<ItemPedido, Produto> joinProduto = root.join("produto");
		Join<Produto, Categoria> joinProdutoCategoria = joinProduto.join("categorias");
		
		query.groupBy(joinProdutoCategoria.get("nome"));
		query.multiselect(
					joinProdutoCategoria.get("nome"),
					builder.sum(root.get("precoProduto"))
				);
		
		TypedQuery<Object[]> typedQuery = entityManager.createQuery(query);
	
		List<Object[]> list = typedQuery.getResultList();
		
		list.forEach(arr ->{
			System.out.println("Nome categoria: " + arr[0] 
						+ ", total vendas: " + arr[1]
					);
		});
		Assert.assertFalse(list.isEmpty());
	}
	
	@Test
	public void produtosPorCategoria() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
		Root<Categoria> root = query.from(Categoria.class);
		Join<Categoria, Produto> joinProduto = root.join("produtos", JoinType.LEFT);
		
		query.groupBy(root.get("nome"));
		query.multiselect(
					root.get("nome"),
					builder.count(joinProduto.get("id"))
				);
		
		TypedQuery<Object[]> typedQuery = entityManager.createQuery(query);
		
		List<Object[]> list = typedQuery.getResultList();
		list.forEach(arr -> 
					System.out.println("Nome categoria: " + arr[0] 
							+ ", QTD produtos: " + arr[1])
				);
		
		Assert.assertFalse(list.isEmpty());
		
	}
}
