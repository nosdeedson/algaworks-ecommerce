package com.ejs.criteria;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.junit.Assert;
import org.junit.Test;

import com.ejs.entityManager.EntityManagerTest;
import com.ejs.model.Categoria;
import com.ejs.model.Cliente;
import com.ejs.model.Cliente_;
import com.ejs.model.ItemPedido;
import com.ejs.model.ItemPedido_;
import com.ejs.model.Pagamento;
import com.ejs.model.Pagamento_;
import com.ejs.model.Pedido;
import com.ejs.model.Pedido_;
import com.ejs.model.Produto;
import com.ejs.model.Produto_;
import com.ejs.model.enums.StatusPagamento;
import com.ejs.model.enums.StatusPedido;

public class PedidoCriteriaTest extends EntityManagerTest {
	
	@Test
	public void findByProduto() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Pedido> query = builder.createQuery(Pedido.class);
		Root<Pedido> root = query.from(Pedido.class);
		Join<Pedido, ItemPedido> joinItemPedido = root.join("itensPedido");
		Join<ItemPedido, Produto> joinProduto = joinItemPedido.join("produto");
		query.select(root);
		query.where(builder.equal(joinProduto.get(Produto_.id), 1));
		
		TypedQuery<Pedido> typedQuery = entityManager.createQuery(query);
		List<Pedido> pedidos = typedQuery.getResultList();
		Assert.assertFalse(pedidos.isEmpty());
		pedidos.forEach(p -> System.out.println("id: " + p.getId()));
		
	}
	
	@Test
	public void usingFunctionsAgregations() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
		Root<Pedido> root = query.from(Pedido.class);
		query.multiselect(
					builder.count(root.get(Pedido_.id)),
					builder.sum(root.get(Pedido_.total)),
					builder.avg(root.get(Pedido_.total)),
					builder.max(root.get(Pedido_.total)),
					builder.min(root.get(Pedido_.total))
				);
		
		TypedQuery<Object[]> typedQuery = entityManager.createQuery(query);
		List<Object[]> list = typedQuery.getResultList();
		list.forEach(l -> {
			System.out.println("count: " + l[0] + 
						", sum: " + l[1] +
						", avg: " + l[2] +
						", max: " + l[3] +
						", min: " + l[4]
					);
		});
		Assert.assertFalse(list.isEmpty());
	}

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
	
	@Test
	public void pagination() {
		int page = 1;
		int size = 2;
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Pedido> query = builder.createQuery(Pedido.class);
		Root<Pedido> root = query.from(Pedido.class);
		query.select(root);
		
		TypedQuery<Pedido> typedQuery = entityManager.createQuery(query);
		/* quantity of elements per page (page or max results) multiplied by actuall page minus one
		 * 
		 */
		typedQuery.setFirstResult(page * (page - 1));
		typedQuery.setMaxResults(size);
		
		List<Pedido> pedidos = typedQuery.getResultList();
		pedidos.forEach(p -> System.out.println(p.getId()));
		Assert.assertFalse(pedidos.isEmpty());
	}
	
	@Test 
	public void usingAnd() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Pedido> query = builder.createQuery(Pedido.class);
		Root<Pedido> root = query.from(Pedido.class);
		query.select(root);
		query.where(
					builder.and(
								builder.or(
											builder.equal(root.get(Pedido_.status), StatusPedido.AGUARDANDO),
											builder.equal(root.get(Pedido_.status), StatusPedido.PAGO)
										),
								builder.greaterThan(root.get(Pedido_.total), new BigDecimal(799))
							)
				);
		
		TypedQuery<Pedido> typedQuery = entityManager.createQuery(query);
		List<Pedido> pedidos = typedQuery.getResultList();
		pedidos.forEach(p -> System.out.println("id: " + p.getId()));
		
		Assert.assertFalse(pedidos.isEmpty());
	}
	
	@Test
	public void finValueUsingBetween() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Tuple> query = builder.createQuery(Tuple.class);
		Root<Pedido> root = query.from(Pedido.class);
		query.multiselect(
					root.get(Pedido_.id).alias("id"),
					root.get(Pedido_.total).alias("total")
				);
		query.where(builder.between(root.get(Pedido_.total), new BigDecimal(1000), new BigDecimal(3000)));
		
		TypedQuery<Tuple> typedQuery = entityManager.createQuery(query);
		List<Tuple> list = typedQuery.getResultList();
		list.forEach(l -> System.out.println("id: " + l.get("id") + " total: " + l.get("total")));
		Assert.assertFalse(list.isEmpty());
	}
	
	@Test
	public void findByDataBetween() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Pedido> query = builder.createQuery(Pedido.class);
		Root<Pedido> root = query.from(Pedido.class);
		query.select(root);
		query.where(builder.between(root.get(Pedido_.DATA_CRIACAO), LocalDateTime.now().minusMonths(5), LocalDateTime.now()));
		
		TypedQuery<Pedido> typedQuery = entityManager.createQuery(query);
		List<Pedido> pedidos = typedQuery.getResultList();
		pedidos.forEach(p -> System.out.println("id " + p.getId()));
		Assert.assertFalse(pedidos.isEmpty());
		
	}
	
	@Test
	public void findByDataInicialAndFinal() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Pedido> query = builder.createQuery(Pedido.class);
		Root<Pedido> root = query.from(Pedido.class);
		query.select(root);
		List<Predicate> predicates = new ArrayList<Predicate>();
		LocalDateTime inicial = LocalDateTime.of(2022, 1, 1, 0, 0).minusDays(1L);
		LocalDateTime dataFinal = LocalDateTime.now();
		predicates.add(builder.greaterThanOrEqualTo(root.get(Pedido_.DATA_CRIACAO), inicial));
		predicates.add(builder.lessThanOrEqualTo(root.get(Pedido_.DATA_CRIACAO), dataFinal));
		query.where(predicates.toArray(new Predicate[predicates.size()]));
		TypedQuery<Pedido> typedQuery = entityManager.createQuery(query);
		List<Pedido> pedidos = typedQuery.getResultList();
		pedidos.forEach(p -> System.out.println("id " +  p.getId()));
		Assert.assertFalse(pedidos.isEmpty());
		
	}
	
	@Test
	public void findClienteUsingFetch() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Pedido> query = builder.createQuery(Pedido.class);
		Root<Pedido> root = query.from(Pedido.class);
		root.fetch("cliente");
		root.fetch("notaFiscal", JoinType.LEFT);
		root.fetch("pagamento", JoinType.LEFT);
		query.select(root);
		TypedQuery<Pedido> typedQuery = entityManager.createQuery(query);
		List<Pedido> pedidos = typedQuery.getResultList();
		pedidos.forEach(p ->{
			System.out.println(p.getCliente().getNome());
		});
		Assert.assertFalse(pedidos.isEmpty());
	}
	
	@Test
	public void usingLeftOuterJoin() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Pedido> query = builder.createQuery(Pedido.class);
		Root<Pedido> root = query.from(Pedido.class);
		Join<Pedido, Pagamento> joinPagamento = root.join("pagamento", JoinType.LEFT);
		query.select(root);
		query.where(
					joinPagamento.get(Pagamento_.status).in(Arrays.asList(StatusPagamento.PROCESSANDO, StatusPagamento.RECEBIDO))
				);
		TypedQuery<Pedido> typedQuery = entityManager.createQuery(query);
		List<Pedido>  pedidos = typedQuery.getResultList();
		pedidos.forEach(p -> System.out.println("pagamento " +p.getPagamento().getId()));
		Assert.assertFalse(pedidos.isEmpty());
		
	}
	
	@Test
	public void usingLeftOuterJoinAgain() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Pedido> query = builder.createQuery(Pedido.class);
		Root<Pedido> root = query.from(Pedido.class);
		// não precica criar o Join<x, y> ao memos que precise usar algum atributo do join 
//		Join<Pedido, Pagamento> joinPagamento = root.join("pagamento", JoinType.LEFT);
		root.join("pagamento", JoinType.LEFT);
		query.where(root.get("status").in(Arrays.asList(StatusPedido.AGUARDANDO), StatusPedido.PAGO));
		
		TypedQuery<Pedido> typedQuery = entityManager.createQuery(query);
		
		List<Pedido> pedidos = typedQuery.getResultList();
		
		pedidos.forEach(p -> System.out.println(p.getId()));
		
		Assert.assertTrue(pedidos.size() == 4);
	}
	
	@Test
	public void findNomeProdutoQtd() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
		Root<Pedido> root = query.from(Pedido.class);
		Join<Pedido, ItemPedido> joinItemPedido = root.join("itensPedido");
		Join<ItemPedido, Produto> joinProduto = joinItemPedido.join("produto");
		query.multiselect(
					builder.count(joinProduto.get(Produto_.id)),
					joinProduto.get(Produto_.nome)
				);
		query.where(builder.equal(root.get(Cliente_.id), 1));
		query.groupBy(joinProduto.get(Produto_.nome));
		TypedQuery<Object[]> typedQuery = entityManager.createQuery(query);
		List<Object[]> list = typedQuery.getResultList();
		list.forEach(l -> System.out.println("Produto: "+ l[1] + ", QTD: " + l[0]));
		Assert.assertFalse(list.isEmpty());
	}
	
	@Test
	public void findProdutosDoCliente() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Produto> query = builder.createQuery(Produto.class);
		Root<Pedido> root = query.from(Pedido.class);
		Join<Pedido, ItemPedido> joinItemPedido = root.join(Pedido_.ITENS_PEDIDO);
		Join<ItemPedido, Produto> joinProduto = joinItemPedido.join(ItemPedido_.PRODUTO);
		query.select(joinProduto);
		query.distinct(true);
		query.where(
					builder.equal(root.get(Pedido_.CLIENTE).get(Cliente_.ID), 1)
				);
		TypedQuery<Produto> typedQuery = entityManager.createQuery(query);
		List<Produto> produtos = typedQuery.getResultList();
		produtos.forEach(p -> System.out.println("Produto: " + p.getNome()));
		Assert.assertFalse(produtos.isEmpty());
	}
	
	@Test
	public void usingJoinWithOn() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Pedido> query = builder.createQuery(Pedido.class);
		Root<Pedido> root = query.from(Pedido.class);
		Join<Pedido, Pagamento> joinPagamento = root.join(Pedido_.PAGAMENTO, JoinType.INNER);
		query.select(root);
		joinPagamento.on(builder.equal(joinPagamento.get(Pagamento_.status), StatusPagamento.PROCESSANDO));
		TypedQuery<Pedido> typedQuery = entityManager.createQuery(query);
		List<Pedido> pedidos = typedQuery.getResultList();
		pedidos.forEach(p -> System.out.println("id: " + p.getId()));
		Assert.assertFalse(pedidos.isEmpty());
	}
	
	@Test
	public void findPagamento() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Pagamento> query = builder.createQuery(Pagamento.class);
		Root<Pedido> root = query.from(Pedido.class);
		Join<Pedido, Pagamento> joinPagamento = root.join("pagamento", JoinType.LEFT);
		
		query.select(joinPagamento);
		query.where(builder.equal(joinPagamento.get(Pagamento_.STATUS), StatusPagamento.PROCESSANDO));
		
		TypedQuery<Pagamento> typedQuery = entityManager.createQuery(query);
		List<Pagamento> list = typedQuery.getResultList();
		
		Assert.assertFalse(list.isEmpty());
		list.forEach(l -> System.out.println("Id : "+l.getId()));
	}
	
	@Test
	public void findCliente() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Cliente> query = builder.createQuery(Cliente.class);
		Root<Pedido> root = query.from(Pedido.class);
		Join<Pedido, Cliente> joinCliente = root.join(Pedido_.CLIENTE);
		
		query.select(joinCliente);
		query.where(builder.equal(joinCliente.get(Cliente_.id), 1));
		
		TypedQuery<Cliente> typedQuery = entityManager.createQuery(query);
		Cliente cliente = typedQuery.getSingleResult();
		
		Assert.assertNotNull(cliente);
		
		System.out.println("nome " + cliente.getNome());
	}
	
	@Test
	public void findById() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Pedido> query = builder.createQuery(Pedido.class);
		Root<Pedido> root = query.from(Pedido.class);
		
		query.select(root);
		query.where(builder.equal(root.get(Cliente_.id), 1));
		TypedQuery<Pedido> typedQuery = entityManager.createQuery(query);
		
		var pedido = typedQuery.getSingleResult();
		
		Assert.assertNotNull(pedido);
		System.out.println("id " +  pedido.getId());
	
	}
	
	@Test
	public void agruparResultadoComFuncoes() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
		Root<Pedido> root = query.from(Pedido.class);
		
		Expression<Integer> anoCriacaoPedido = builder.function("year", Integer.class, root.get(Pedido_.DATA_CRIACAO));
		
		Expression<Integer> mesCriacaoPedido = builder.function("month", Integer.class, root.get(Pedido_.DATA_CRIACAO));
		
		Expression<String> nomeMesCriacaoPedido = builder.function("monthname", String.class, root.get(Pedido_.DATA_CRIACAO));	
		
		Expression<String> anoMesConcat = builder.concat(
				builder.concat(nomeMesCriacaoPedido, "/")
				, anoCriacaoPedido.as(String.class));
		
		query.multiselect(
					anoMesConcat,
					builder.sum(root.get(Pedido_.TOTAL))
				);
		
		query.groupBy(anoCriacaoPedido, mesCriacaoPedido, root.get(Pedido_.DATA_CRIACAO));
		
		TypedQuery<Object[]> typedQuery = entityManager.createQuery(query);
		List<Object[]> list = typedQuery.getResultList();
		
		list.forEach(arr ->{
			System.out.println("mes: " + arr[0] + ", total: " + arr[1]);
		});
		Assert.assertFalse(list.isEmpty());
		
	}
	
	@Test
	public void usingCaseWhenParaPegarStatusDoPedido() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
		Root<Pedido> root = query.from(Pedido.class);
		
		/* não funciona porque o JPA tenta passar parâmetros para a query sql, mesmo se passássemos não funionaria
		 * JPA não reconhece,
		 *  devido ao uso de atributos da classe
		 *  */
//		query.multiselect(
//					root.get(Pedido_.id),
//					builder.selectCase(root.get(Pedido_.status))
//					.when(StatusPedido.AGUARDANDO, "Está aguardando.")
//					.when(StatusPedido.PAGO, "foi pago")
//					.otherwise(root.get(Pedido_.status))
//				);
		
		/*funciona pelo simples fato de usar string ao invés dos atributos da classe */
		query.multiselect(
					root.get(Pedido_.id),
					builder.selectCase(root.get(Pedido_.STATUS))
						.when(StatusPedido.AGUARDANDO.toString(), "Está aguardando")
						.when(StatusPedido.PAGO.toString(), "Está pago")
						.otherwise(root.get(Pedido_.STATUS))
				);
		
		TypedQuery<Object[]> typedQuery = entityManager.createQuery(query);
		List<Object[]> list = typedQuery.getResultList();
		
		list.forEach(l ->{
			System.out.println("id: " + l[0] + ", status: " + l[1]);
		});
		Assert.assertFalse(list.isEmpty());
		
	}
	
	@Test
	public void usingCaseWhenParaPegarTipoPagamento() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
		Root<Pedido> root = query.from(Pedido.class);
		
		/* não funciona porque o JPA tenta passar parâmetros a query sql, mesmo se passássemos não funcionaria, JPA não reconhece
		 *  pois da forma feita usamos as classes PagamentoBoleto e PagamentoCartao 
		 *  */
//		query.multiselect(
//					root.get(Pedido_.id),
//					builder.selectCase(root.get(Pedido_.pagamento).type())
//						.when(PagamentoBoleto.class, "Pago com boleto")
//						.when(PagamentoCartao.class, "pago com cartao")
//						.otherwise("não identificado")
//				);
		
		/*
		 * Funciona porque agora o JPA consegue reconhecer o @DiscriminatorValue "boleto", "cartao" de cada subClasse
		 * e não é mais passado na query sql parâmetros, mas sim o valor discriminatório de cada classe, ou seja,
		 * o valor referente a coluna tipo_pagamento como referência para comparação
		 */
		query.multiselect(
					root.get(Pedido_.id),
					builder.selectCase(root.get(Pedido_.pagamento).type().as(String.class))
							.when("boleto", "Pagamento com boleto")
							.when("cartao", "Pagamento com cartao")
							.otherwise("não identificado")
				);
		
		TypedQuery<Object[]> typedQuery = entityManager.createQuery(query);
		List<Object[]> list = typedQuery.getResultList();
		list.forEach(l ->{
			System.out.println("id: " + l[0] + ", status: " + l[1]);
		});
		Assert.assertFalse(list.isEmpty());
		
	}
	
	@Test
	public void findByClientes() {
		
		Cliente c1 = entityManager.find(Cliente.class, 1);
		Cliente c2 = entityManager.find(Cliente.class, 2);
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Pedido>  query = builder.createQuery(Pedido.class);
		Root<Pedido> root = query.from(Pedido.class);
		query.select(root);
		query.where(root.get(Pedido_.CLIENTE).in(Arrays.asList(c1, c2)));
		TypedQuery<Pedido> typedQuery = entityManager.createQuery(query);
		List<Pedido> pedidos = typedQuery.getResultList();
		
		pedidos.forEach(p -> {
			System.out.println(p.getCliente().getNome());
			Assert.assertNotNull(p.getCliente());
		});
	}
	
	@Test
	public void findByDistinctClientes() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Pedido> query = builder.createQuery(Pedido.class);
		Root<Pedido> root = query.from(Pedido.class);
		root.join("itensPedido");
		query.select(root);
		query.distinct(true);
		TypedQuery<Pedido> typedQuery = entityManager.createQuery(query);
		List<Pedido> pedidos = typedQuery.getResultList();
		pedidos.forEach(p ->{
			System.out.println("id: " + p.getId());
		});
		Assert.assertFalse(pedidos.isEmpty());
	}
	
	@Test
	public void pedidoAcimaDaMediaVendas() {
//      Todos os pedidos acima da média de vendas
//     String jpql = "select p from Pedido p where " +
//             " p.total > (select avg(total) from Pedido)";
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Pedido> query = builder.createQuery(Pedido.class);
		Root<Pedido> root = query.from(Pedido.class);
		
		Subquery<BigDecimal> subQuery = query.subquery(BigDecimal.class);
		Root<Pedido> rootSubQuery = subQuery.from(Pedido.class);
		subQuery.select(builder.avg(rootSubQuery.get(Pedido_.TOTAL)).as(BigDecimal.class));
		query.where(
					builder.greaterThan(root.get(Pedido_.TOTAL), subQuery)
				);
		query.select(root);
		
		TypedQuery<Pedido> typedQuery = entityManager.createQuery(query);
		List<Pedido> pedidos = typedQuery.getResultList();
		pedidos.forEach(p -> System.out.println("id " + p.getId()));
		Assert.assertFalse(pedidos.isEmpty());
	}
	

	@Test
	public void pedidosComPrecosAcimaDe1000() {
//      String jpql = "select p from Pedido p where p.id in " +
//      " (select p2.id from ItemPedido i2 " +
//      "      join i2.pedido p2 join i2.produto pro2 where pro2.preco > 100)";
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Pedido> query = builder.createQuery(Pedido.class);
		Root<Pedido> root = query.from(Pedido.class);
		
		Subquery<Integer> subQuery = query.subquery(Integer.class);
		Root<ItemPedido> subQueryRoot = subQuery.from(ItemPedido.class);
		Join<ItemPedido, Pedido> joinSubQueryPedido = subQueryRoot.join("pedido");
		Join<ItemPedido, Produto> joinSubQueryProduto = subQueryRoot.join("produto");
		subQuery.select(joinSubQueryPedido.get(Pedido_.id));
		subQuery.where(builder.greaterThan(joinSubQueryProduto.get(Produto_.preco), new BigDecimal(1000)));
		
		query.where(root.get(Pedido_.id).in(subQuery));
		query.select(root);
		
		TypedQuery<Pedido> typedQuery = entityManager.createQuery(query);
		List<Pedido> pedidos = typedQuery.getResultList();
		pedidos.forEach(p -> System.out.println("id: "  + p.getId()));
		Assert.assertFalse(pedidos.isEmpty());
	}
	
	@Test
	public void pedidosComProdutosDaCategoria2() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Pedido> query = builder.createQuery(Pedido.class);
		Root<Pedido> root = query.from(Pedido.class);
		
		Subquery<Integer> subquery = query.subquery(Integer.class);
		Root<ItemPedido> rootSubQuery = subquery.from(ItemPedido.class);
		Join<ItemPedido, Produto> joinItemPedidoProduto = rootSubQuery.join("produto");
		Join<Produto, Categoria> joinProdutoCategoria = joinItemPedidoProduto.join("categorias");
		
		subquery.select(rootSubQuery.get(ItemPedido_.PEDIDO).get(Pedido_.ID));
		subquery.where(
					builder.equal(joinProdutoCategoria.get("id"), Integer.valueOf(2))
				);
		
		query.select(root);
		query.where(root.get(Pedido_.id).in(subquery));
		
		TypedQuery<Pedido> typedQuery = entityManager.createQuery(query);
		List<Pedido> pedidos = typedQuery.getResultList();
		pedidos.forEach(p -> System.out.println("id: " + p.getId()));
		Assert.assertFalse(pedidos.isEmpty());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
