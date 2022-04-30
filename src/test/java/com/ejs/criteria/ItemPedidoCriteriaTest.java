package com.ejs.criteria;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import org.junit.Assert;
import org.junit.Test;

import com.ejs.entityManager.EntityManagerTest;
import com.ejs.model.Categoria;
import com.ejs.model.Categoria_;
import com.ejs.model.ItemPedido;
import com.ejs.model.ItemPedido_;
import com.ejs.model.Produto;
import com.ejs.model.Produto_;

public class ItemPedidoCriteriaTest extends EntityManagerTest {

	@Test
	public void mediaTotalVendidoPorCategoria() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
		Root<ItemPedido> root = query.from(ItemPedido.class);
		Join<ItemPedido, Produto> joinProduto = root.join("produto");
		Join<Produto, Categoria> joinCategoria = joinProduto.join("categorias");
		
		query.multiselect(
					joinCategoria.get(Categoria_.nome),
					builder.sum(joinProduto.get(Produto_.preco)),
					builder.avg(joinProduto.get(Produto_.PRECO))
				);
		
		query.groupBy(joinCategoria.get(Categoria_.nome));
		query.having(
				builder.greaterThan(
						builder.avg(root.get(ItemPedido_.precoProduto)).as(BigDecimal.class), 
						new BigDecimal(1000)
						)
				);
		
		TypedQuery<Object[]> typedQuery = entityManager.createQuery(query);
		
		List<Object[]> list = typedQuery.getResultList();
		
		list.forEach(l ->{
			System.out.println("nome: " + l[0] + ", total: " + l[1] + ", media: " + l[2]);
		});
		
		Assert.assertFalse(list.isEmpty());
		
	}	
}
