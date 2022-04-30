package com.ejs.criteria;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.junit.Assert;
import org.junit.Test;

import com.ejs.entityManager.EntityManagerTest;
import com.ejs.model.Cliente;
import com.ejs.model.Cliente_;
import com.ejs.model.Pedido;
import com.ejs.model.Pedido_;

public class ClienteCriteriaTest extends EntityManagerTest {

	@Test
	public void totalVendasPorCliente() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
		Root<Cliente> root = query.from(Cliente.class);
		Join<Cliente, Pedido> joinPedido = root.join("pedidos");
		
		query.groupBy(root.get(Cliente_.nome));
		query.multiselect(
					root.get(Cliente_.nome),
					builder.sum(joinPedido.get(Pedido_.total))
				);
		
		TypedQuery<Object[]> typedQuery = entityManager.createQuery(query);
		
		List<Object[]> list = typedQuery.getResultList();
		
		list.forEach(arr -> System.out.println(
					"Cliente: " + arr[0] + ", total compras: " + arr[1]
				));
		
		Assert.assertFalse(list.isEmpty());
		
	}
	
	@Test
	public void orderByNome() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Cliente> query = builder.createQuery(Cliente.class);
		Root<Cliente> root = query.from(Cliente.class);
		
//		query.orderBy(builder.asc(root.get("nome")));
		query.orderBy(builder.desc(root.get("nome")), builder.asc(root.get("id")) );
		
		query.select(root);
		TypedQuery<Cliente> typedQuery = entityManager.createQuery(query);
		List<Cliente> clientes = typedQuery.getResultList();
		clientes.forEach(cli -> System.out.println("nome " +cli.getNome() + " id " + cli.getId()));
	}
	
	@Test
	public void finbByNomeLike() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Cliente> query = builder.createQuery(Cliente.class);
		
		Root<Cliente> root = query.from(Cliente.class);
		query.where(builder.like(root.get("nome"), "%a%"));
		
		TypedQuery<Cliente> typedQuery = entityManager.createQuery(query);
		
		List<Cliente> clientes = typedQuery.getResultList();
		
		clientes.forEach(c -> System.out.println(c.getNome()));
		
		Assert.assertTrue(clientes.size() == 2);
		
	}
	
	@Test
	public void clienteById() {
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Cliente> query = builder.createQuery(Cliente.class);
		
		Root<Cliente> root = query.from(Cliente.class);
		
		query.where(builder.equal(root.get("id"), 1));
		
		TypedQuery<Cliente> typedQuery = entityManager.createQuery(query);
		
		Cliente cliente = typedQuery.getSingleResult();
		
		System.out.println("id " +  cliente.getNome());
		
		Assert.assertNotNull(cliente);
	}
	
	@Test
	public void melhoresClientes() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Cliente> query = builder.createQuery(Cliente.class);
		Root<Cliente> root = query.from(Cliente.class);
		
		Subquery<BigDecimal> subquery = query.subquery(BigDecimal.class);
		Root<Pedido> subQueryRoot = subquery.from(Pedido.class);

		subquery.select(builder.sum(subQueryRoot.get(Pedido_.total)));
		subquery.where(builder.equal(root, subQueryRoot.get(Pedido_.CLIENTE)));
		
		query.select(root);
		query.where(builder.greaterThan(subquery, new BigDecimal(7000)));
		
		TypedQuery<Cliente> typedQuery = entityManager.createQuery(query);
		List<Cliente> clientes = typedQuery.getResultList();
		
		clientes.forEach(c -> System.out.println(c.getNome()));
		Assert.assertFalse(clientes.isEmpty());
	}
	
	@Test
	public void clientesComMaisDe2Pedidos() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Cliente> query = builder.createQuery(Cliente.class);
		Root<Cliente> root = query.from(Cliente.class);
		
		Subquery<Integer> subquery = query.subquery(Integer.class);
		Root<Pedido> rootSubQuery = subquery.from(Pedido.class);
		subquery.select(rootSubQuery.get(Pedido_.cliente).get(Cliente_.id));
		subquery.where(
					builder.equal(root, rootSubQuery.get(Pedido_.cliente))		
				);
		subquery.groupBy(rootSubQuery.get(Pedido_.cliente));
		subquery.having(
					builder.greaterThan(builder.count(rootSubQuery.get(Pedido_.id)), 1L)
				);
				
		query.select(root);
		query.where(root.get(Cliente_.id).in(subquery));
		TypedQuery<Cliente> typedQuery = entityManager.createQuery(query);
		List<Cliente> clientes = typedQuery.getResultList();
		Assert.assertFalse(clientes.isEmpty());
		
		clientes.forEach(c -> System.out.println(c.getNome()));
	}	
	
}

