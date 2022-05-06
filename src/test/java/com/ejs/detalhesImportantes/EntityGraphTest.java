package com.ejs.detalhesImportantes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityGraph;
import javax.persistence.Subgraph;
import javax.persistence.TypedQuery;

import org.junit.Assert;
import org.junit.Test;

import com.ejs.entityManager.EntityManagerTest;
import com.ejs.model.Cliente;
import com.ejs.model.Pedido;
import com.ejs.model.Pedido_;

public class EntityGraphTest extends EntityManagerTest {
	
	@Test
	public void usandoEntityGraphRefinarBusca02() {
		EntityGraph<Pedido> entityGraph = entityManager.createEntityGraph(Pedido.class);
		entityGraph.addAttributeNodes(Pedido_.DATA_CRIACAO, Pedido_.STATUS);
		
//		Subgraph<Cliente> subgraph = entityGraph.addSubgraph("cliente", Cliente.class);
		Subgraph<Cliente> subgraph = entityGraph.addSubgraph(Pedido_.CLIENTE);
		subgraph.addAttributeNodes("nome", "cpf");
		
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("javax.persistence.fetchgraph", entityGraph);
		
		TypedQuery<Pedido> typedQuery = entityManager.createQuery("select p from Pedido p", Pedido.class);
		typedQuery.setHint("javax.persistence.fetchgraph", entityGraph);
		List<Pedido> lista = typedQuery.getResultList();
		
		Assert.assertFalse(lista.isEmpty());
	
	}
	
	@Test
	public void usandoEntityGraphRefinarBusca() {
		EntityGraph<Pedido> entityGraph = entityManager.createEntityGraph(Pedido.class);
		entityGraph.addAttributeNodes("dataCriacao", "status", "cliente", "notaFiscal");
		
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("javax.persistence.fetchgraph", entityGraph);
//		properties.put("javax.persistence.loadgraph", entityGraph);
		
		Pedido pedido = entityManager.find(Pedido.class, 1, properties);
		
		Assert.assertNotNull(pedido);
		
		System.out.println(pedido.getCliente().getNome());
		
//		TypedQuery<Pedido> typedQuery = entityManager.createQuery("select p from Pedido p", Pedido.class);
//		typedQuery.setHint("javax.persistence.fetchgraph", entityGraph);
//		List<Pedido> lista = typedQuery.getResultList();
//		
//		Assert.assertFalse(lista.isEmpty());
		
		
		
	}

}
