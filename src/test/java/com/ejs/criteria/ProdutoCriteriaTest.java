package com.ejs.criteria;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.junit.Assert;
import org.junit.Test;

import com.ejs.DTO.ProdutoDTO;
import com.ejs.entityManager.EntityManagerTest;
import com.ejs.model.Categoria;
import com.ejs.model.Categoria_;
import com.ejs.model.ItemPedido;
import com.ejs.model.ItemPedido_;
import com.ejs.model.Produto;
import com.ejs.model.Produto_;

public class ProdutoCriteriaTest extends EntityManagerTest {
	
	@Test
	public void precoBetween() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Produto> query = builder.createQuery(Produto.class);
		Root<Produto> root = query.from(Produto.class);
		
		Predicate[] predicates = new Predicate[2];
		predicates[0] = builder.greaterThanOrEqualTo(root.get(Produto_.preco), new BigDecimal(900));
		predicates[1]= builder.lessThanOrEqualTo(root.get(Produto_.preco), new BigDecimal(3500));
		
		query.where(predicates);
		TypedQuery<Produto> typedQuery = entityManager.createQuery(query);
		List<Produto> produtos = typedQuery.getResultList();
		
		produtos.forEach(p -> System.out.println(p.getNome()));
		
		Assert.assertFalse(produtos.isEmpty());
		
	}
	
	@Test
	public void produtoSemCategoria() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Produto> query = builder.createQuery(Produto.class);
		Root<Produto> root = query.from(Produto.class);
		
		query.select(root);
		query.where(builder.isEmpty(root.get("categorias")));
		TypedQuery<Produto> typedQuery = entityManager.createQuery(query);
		List<Produto> produtos = typedQuery.getResultList();
		
		produtos.forEach(p -> {
			Assert.assertTrue(p.getCategorias().isEmpty());
			System.out.println(p.getCategorias().isEmpty());
		});
		
		produtos.forEach(p -> {
			System.out.println(p.getNome());
		});
	}

	
	
	@Test
	public void produtoComCategorias() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Produto> query = builder.createQuery(Produto.class);
		Root<Produto> root = query.from(Produto.class);
		root.fetch("categorias");
		query.select(root);
		query.where(builder.isNotEmpty(root.get(Produto_.CATEGORIAS)));
		TypedQuery<Produto> typedQuery = entityManager.createQuery(query);
		List<Produto> produtos = typedQuery.getResultList();
		
		produtos.forEach(p ->{
			p.getCategorias().forEach(c ->{
				System.out.println(c.getNome());
			});
			Assert.assertTrue(p.getCategorias().size() > 0);
		});
		
	}
	
	@Test
	public void produtoComFoto() {
		/*
		 * don't use isNull ou isNotNull with list like produto.categorias
		 */
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Produto> query = builder.createQuery(Produto.class);
		Root<Produto> root = query.from(Produto.class);
		query.select(root);
		query.where(builder.isNotNull(root.get(Produto_.FOTO)));
		TypedQuery<Produto> typedQuery = entityManager.createQuery(query);
		List<Produto> produtos = typedQuery.getResultList();
		
		produtos.forEach(p -> {
			Assert.assertNotNull(p.getFoto());
			System.out.println(p.getNome());
		});
		
	}
	
	@Test 
	public void produtoSemFoto() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Produto> query = builder.createQuery(Produto.class);
		Root<Produto> root = query.from(Produto.class);
		query.select(root);
		query.where(builder.isNull(root.get(Produto_.FOTO)));
		TypedQuery<Produto> typedQuery = entityManager.createQuery(query);
		List<Produto> produtos = typedQuery.getResultList();
		produtos.forEach(p -> {
			System.out.println(p.getNome());
			Assert.assertNull(p.getFoto());
		});
	}
	
	@Test
	public void usandoTuple() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Tuple> query = builder.createQuery(Tuple.class);
		Root<Produto> root = query.from(Produto.class);
		query.multiselect(
					root.get(Produto_.id).alias("id"),
					root.get(Produto_.nome).alias("nome")
				);
		query.orderBy(builder.asc(root.get(Produto_.id)));
		TypedQuery<Tuple> typedQuery = entityManager.createQuery(query);
		List<Tuple> list = typedQuery.getResultList();
		list.forEach(l ->{
			System.out.println("id: " + l.get("id") + ", nome: " + l.get("nome"));
		});
		
		Assert.assertFalse(list.isEmpty());
	}
	
	@Test
	public void getProdutoDTO() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ProdutoDTO> query = builder.createQuery(ProdutoDTO.class);
		Root<Produto> root = query.from(Produto.class);
		/*one way*/
//		query.multiselect(
//					root.get(Produto_.id),
//					root.get(Produto_.nome)
//				);
		
		/*another way*/
		query.select(
					builder.construct(ProdutoDTO.class,
							root.get(Produto_.id), root.get(Produto_.nome))
				);
		query.orderBy(builder.desc(root.get(Produto_.id)));
		TypedQuery<ProdutoDTO> typedQuery = entityManager.createQuery(query);
		List<ProdutoDTO> dtos = typedQuery.getResultList();
		dtos.forEach(d ->{
			System.out.println("id: " + d.getId()+ ", nome: " + d.getNome());
		});
		Assert.assertFalse(dtos.isEmpty());
	}
	
	@Test
	public void multSelectCriteria() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
		
		Root<Produto> root = query.from(Produto.class);
		
		query.multiselect(root.get("id"), root.get("nome"));
		
		TypedQuery<Object[]> typedquery = entityManager.createQuery(query);
		
		List<Object[]> lista = typedquery.getResultList();
		
		lista.forEach(item -> System.out.println("id " +  item[0] +  ", nome " +  item[1]));
		
		Assert.assertFalse(lista.isEmpty());
	}
	
	@Test
	public void findAll() {
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Produto> query = builder.createQuery(Produto.class);
		
		Root<Produto> root = query.from(Produto.class);
		
		query.select(root);
		
		TypedQuery<Produto> typedQuery = entityManager.createQuery(query);
		
		List<Produto> lista = typedQuery.getResultList();
		
		lista.forEach(produto -> System.out.println(produto.getNome()));
		
		Assert.assertFalse(lista.isEmpty());
		
	}
	
	@Test
	public void produtoMaisCaro() {
//      O produto ou os produtos mais caros da base.
//     String jpql = "select p from Produto p where " +
//             " p.preco = (select max(preco) from Produto)";
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Produto> query = builder.createQuery(Produto.class);
		Root<Produto> root = query.from(Produto.class);
		
		Subquery<BigDecimal> subQuery = query.subquery(BigDecimal.class);
		Root<Produto> rootSubQuery = subQuery.from(Produto.class);
		subQuery.select(builder.max(rootSubQuery.get(Produto_.PRECO)));
		
		query.select(root);
		query.where(builder.equal(root.get(Produto_.PRECO), subQuery));
		TypedQuery<Produto> typedQuery = entityManager.createQuery(query);
		List<Produto> produtos = typedQuery.getResultList();
		produtos.forEach(p -> System.out.println("nome: " + p.getNome() + "preço: " +p.getPreco()));
		Assert.assertFalse(produtos.isEmpty());
		
	}
	
	@Test
	public void produtosVendidos() {
//      Todos os produtos que já foram vendidos.
//      String jpql = "select p from Produto p where exists " +
//              " (select 1 from ItemPedido ip2 join ip2.produto p2 where p2 = p)";
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Produto> query = builder.createQuery(Produto.class);
		Root<Produto> root = query.from(Produto.class);
		
		Subquery<Integer> subQuery = query.subquery(Integer.class);
		Root<ItemPedido> subQueryRoot = subQuery.from(ItemPedido.class);
		subQuery.select(builder.literal(1));
		subQuery.where(builder.equal(subQueryRoot.get(ItemPedido_.produto), root));
		
		query.select(root);
		query.where(builder.exists(subQuery));
		
		TypedQuery<Produto> typedQuery = entityManager.createQuery(query);
		List<Produto> produtos = typedQuery.getResultList();
		produtos.forEach(p -> System.out.println("id: " + p.getId()));
		Assert.assertFalse(produtos.isEmpty());
	}
	
	@Test
	public void produtosNaoVendidos() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Produto> query = builder.createQuery(Produto.class);
		Root<Produto> root = query.from(Produto.class);
		
		Subquery<Integer> subQuery = query.subquery(Integer.class);
		Root<ItemPedido> subQueryRoot = subQuery.from(ItemPedido.class);
		subQuery.select(subQueryRoot.get(ItemPedido_.produto).get(Produto_.id));
		subQuery.where(builder.equal(subQueryRoot.get(ItemPedido_.produto), root));
		
		query.select(root);
		
		query.where(builder.not(root.get(Produto_.id).in(subQuery)));
		
		TypedQuery<Produto> typedQuery = entityManager.createQuery(query);
		List<Produto> produtos = typedQuery.getResultList();
		produtos.forEach(p -> System.out.println("id: " + p.getId()));
		Assert.assertFalse(produtos.isEmpty());
	}
	
	@Test
	public void produtoscomAlteracaoNoPreco() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Produto> query = builder.createQuery(Produto.class);
		Root<Produto> root = query.from(Produto.class);
		
		Subquery<Integer> subquery = query.subquery(Integer.class);
		Root<ItemPedido> rootSubquery = subquery.from(ItemPedido.class);
		Join<ItemPedido, Produto> joinProduto = rootSubquery.join(ItemPedido_.PRODUTO);
		
		subquery.select(builder.literal(1));
		subquery.where(
					builder.equal(root, joinProduto),
					builder.greaterThan(root.get("preco"), rootSubquery.get("precoProduto"))
				);
		
		query.select(root);
		query.where(builder.exists(subquery));
		
		TypedQuery<Produto> typedQuery = entityManager.createQuery(query);
		List<Produto> produtos = typedQuery.getResultList();
		
		Assert.assertFalse(produtos.isEmpty());
		produtos.forEach(p -> System.out.println("id " + p.getId() + ", nome " + p.getNome()));
	}
	
	@Test
	public void produtosNaoVendidosAposAlta() {
//  Todos os produtos não foram vendidos mais depois que encareceram
//  String jpql = "select p from Produto p where " +
//          " p.preco > ALL (select precoProduto from ItemPedido where produto = p)";
//          " and exists (select 1 from ItemPedido where produto = p)";
	
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Produto> query = builder.createQuery(Produto.class);
		Root<Produto> root = query.from(Produto.class);
		
		Subquery<BigDecimal> subquery = query.subquery(BigDecimal.class);
		Root<ItemPedido> rootSubquery = subquery.from(ItemPedido.class);
		subquery.select(rootSubquery.get(ItemPedido_.PRECO_PRODUTO));
		subquery.where(
					builder.equal(rootSubquery.get(ItemPedido_.PRODUTO), root)
				);
		
		query.select(root);
		query.where(
					builder.greaterThan(
							root.get(Produto_.PRECO), builder.all(subquery)),
					builder.exists(subquery)
				);
		
		
		TypedQuery<Produto> typedQuery = entityManager.createQuery(query);
		List<Produto> produtos = typedQuery.getResultList();
		produtos.forEach(p -> System.out.println("id " + p.getId() + " nome " + p.getNome()));
		Assert.assertFalse(produtos.isEmpty());
	}
	
	@Test
	public void produtosNaoAumentaramEForamVendidos() {
//  Todos os produtos que SEMPRE foram vendidos pelo preco atual.
//  String jpql = "select p from Produto p where " +
//          " p.preco = ALL (select precoProduto from ItemPedido where produto = p)";
	
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Produto> query = builder.createQuery(Produto.class);
		Root<Produto> root = query.from(Produto.class);
		
		Subquery<Integer> subquery = query.subquery(Integer.class);
		Root<ItemPedido> rootSubquery = subquery.from(ItemPedido.class);
		
		subquery.select(rootSubquery.get(ItemPedido_.PRODUTO).get(Produto_.ID));
		subquery.where(
					builder.equal(rootSubquery.get(ItemPedido_.PRODUTO), root),
					builder.not(builder.greaterThan(root.get(Produto_.PRECO), rootSubquery.get(ItemPedido_.PRECO_PRODUTO)))
				);
		
		query.select(root);
		query.where(
					root.get(Produto_.id).in(subquery)
				);
		
		TypedQuery<Produto> typedQuery = entityManager.createQuery(query);
		List<Produto> produtos = typedQuery.getResultList();
		
		produtos.forEach(p -> System.out.println("id " + p.getId() + " nome " + p.getNome()));
		Assert.assertFalse(produtos.isEmpty());
	}
	
	@Test
	public void produtosVendidoSemprePeloMesmoPreco() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Produto> query = builder.createQuery(Produto.class);
		Root<Produto> root = query.from(Produto.class);
		
		Subquery<BigDecimal> subquery = query.subquery(BigDecimal.class);
		Root<ItemPedido> rootSubquery= subquery.from(ItemPedido.class);
		
		subquery.select(rootSubquery.get(ItemPedido_.PRECO_PRODUTO));
		subquery.where(
					builder.equal(rootSubquery.get(ItemPedido_.PRODUTO), root)
				);
		
		query.select(root);
		query.where(root.get(Produto_.PRECO).in(subquery));
		
		TypedQuery<Produto> typedQuery = entityManager.createQuery(query);
		List<Produto> produtos = typedQuery.getResultList();
		produtos.forEach(p -> System.out.println("id: " + p.getId() + ", nome: " + p.getNome()));
		Assert.assertFalse(produtos.isEmpty());
	}
	
	@Test
	public void atualizar() {
		entityManager.getTransaction().begin();
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaUpdate<Produto> criteriaUpdate = builder.createCriteriaUpdate(Produto.class);
		Root<Produto> root = criteriaUpdate.from(Produto.class);
		
		criteriaUpdate.set(root.get(Produto_.preco), builder.prod(root.get(Produto_.preco), new BigDecimal("1.1")));
		
		Subquery<Integer> subquery = criteriaUpdate.subquery(Integer.class);
		Root<Produto> rootSubquery = subquery.correlate(root);
		Join<Produto, Categoria> joinCategoria = rootSubquery.join(Produto_.CATEGORIAS);
		subquery.select(builder.literal(1));
		subquery.where(builder.equal(joinCategoria.get(Categoria_.ID), 2));
		
		criteriaUpdate.where(builder.exists(subquery));
		
		Query query = entityManager.createQuery(criteriaUpdate);
	    int itensAtualizaos = query.executeUpdate();
		entityManager.getTransaction().commit();
		
		Assert.assertTrue(itensAtualizaos != 0);
	}
	
	@Test
	public void deletar() {
		entityManager.getTransaction().begin();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaDelete<Produto> criteriaDelete = builder.createCriteriaDelete(Produto.class);
		Root<Produto> root = criteriaDelete.from(Produto.class);
		criteriaDelete.where(builder.between(root.get(Produto_.ID), 8, 12));
		
		Query query = entityManager.createQuery(criteriaDelete);
		int deletados = query.executeUpdate();
		Assert.assertTrue(deletados > 0);
		entityManager.getTransaction().commit();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
