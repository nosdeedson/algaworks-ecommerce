package com.ejs.iniciandoJPQL;

import java.util.List;

import javax.persistence.TypedQuery;

import org.junit.Assert;
import org.junit.Test;

import com.ejs.entityManager.EntityManagerTest;
import com.ejs.model.Cliente;

public class ClienteJPQLTest extends EntityManagerTest{
	
	@Test
	public void subQuery() {
		String jpql = "SELECT c.nome FROM Cliente c "
				+ " WHERE c.id in"
				+ " ( SELECT c.id FROM Pedido p INNER JOIN p.cliente c2 "
				+ " where c2=c "
				+ " group by c.id "
				+ " having count(c.id) > 0 )";
		
		TypedQuery<String> typedQuery = entityManager.createQuery(jpql, String.class);
		
		List<String> clientes = typedQuery.getResultList();

		clientes.forEach(System.out::println);
		
		Assert.assertFalse(clientes.size() < 1);
		
	}

	@Test
	public void findClienteUsingLike() {
		String jpql = "SELECT c FROM Cliente c WHERE c.nome LIKE CONCAT('%', :nome, '%')";
		
		TypedQuery<Cliente> typedQuery = entityManager.createQuery(jpql, Cliente.class);
		
		typedQuery.setParameter("nome", "a");
		
		List<Cliente> lista = typedQuery.getResultList();
		
		lista.forEach(cliente -> System.out.println(cliente.getNome()));
		
		Assert.assertFalse(lista.isEmpty());
	}
}
