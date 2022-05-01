package com.ejs.consultasNativas;

import java.util.List;

import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

import org.junit.Assert;
import org.junit.Test;

import com.ejs.entityManager.EntityManagerTest;
import com.ejs.model.Cliente;

public class StoredProceduresTest extends EntityManagerTest{
	
	@Test
	public void listandoClientesQueMaisConsomem() {
		StoredProcedureQuery storedProcedureQuery = 
				entityManager.createNamedStoredProcedureQuery("compraram_acima_media");
		storedProcedureQuery.setParameter("ano", 2021);
		@SuppressWarnings("unchecked")
		List<Cliente> clientes = storedProcedureQuery.getResultList();
		
		Assert.assertFalse(clientes.isEmpty());
		clientes.forEach(c -> System.out.println(c.getNome()));
	}
	
	@Test
	public void ajustandoValorProduto() {
		StoredProcedureQuery storedProcedureQuery =
				entityManager.createStoredProcedureQuery("ajustar_preco_produto");
		storedProcedureQuery.registerStoredProcedureParameter("produto_id", Integer.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("percentual_ajuste", Double.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("preco_ajustado", Double.class, ParameterMode.OUT);
		
		storedProcedureQuery.setParameter("produto_id", 1);
		storedProcedureQuery.setParameter("percentual_ajuste", 0.1);
		
		Double precoAjustado = (Double) storedProcedureQuery.getOutputParameterValue("preco_ajustado");
		System.out.println(precoAjustado);
		Assert.assertEquals(Long.valueOf("878,9"), precoAjustado);
	}
	
	@Test
	public void listandoClientesQueMaisConsumiramComProcedure() {
		StoredProcedureQuery storedProcedureQuery = 
				entityManager.createStoredProcedureQuery("compraram_acima_media", Cliente.class);
		storedProcedureQuery.registerStoredProcedureParameter("ano", Integer.class, ParameterMode.IN);
		storedProcedureQuery.setParameter("ano", 2021);
		
		@SuppressWarnings("unchecked")
		List<Cliente> clientes = storedProcedureQuery.getResultList();
		
		Assert.assertFalse(clientes.isEmpty());
		clientes.forEach(c -> System.out.println(c.getNome()));
	}
	
	@Test
	public void usandoInAndOutParametros() {
		StoredProcedureQuery storedProcedureQuery = entityManager.createStoredProcedureQuery("buscar_nome_produto");
		storedProcedureQuery.registerStoredProcedureParameter("produto_id", Integer.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("produto_nome", String.class, ParameterMode.OUT);
		
		storedProcedureQuery.setParameter("produto_id", 1);
		String nome = (String) storedProcedureQuery.getOutputParameterValue("produto_nome");
		System.out.println(nome);
		
		Assert.assertEquals(nome, "Kindle");
	}

}
