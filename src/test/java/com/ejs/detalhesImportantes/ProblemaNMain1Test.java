package com.ejs.detalhesImportantes;

import java.util.List;

import javax.persistence.EntityGraph;

import org.junit.Assert;
import org.junit.Test;

import com.ejs.entityManager.EntityManagerTest;
import com.ejs.model.Pedido;

public class ProblemaNMain1Test extends EntityManagerTest {
	 @Test
	    public void resolverComEntityGraph() {
	        EntityGraph<Pedido> entityGraph = entityManager.createEntityGraph(Pedido.class);
	        entityGraph.addAttributeNodes("cliente", "notaFiscal", "pagamento");

	        /*
	         * loadgraph resolve o problema do n+1, que consiste em um lista de objetos faz-se outra para trazer os objetos relacionados
	         */
	        List<Pedido> lista = entityManager
	                .createQuery("select p from Pedido p ", Pedido.class)
	                .setHint("javax.persistence.loadgraph", entityGraph)
	                .getResultList();

	        Assert.assertFalse(lista.isEmpty());
	    }

	 	/*
	 	 * para resolver o problema do n+1 basta usar o 'join fetch'
	 	 */
	    @Test
	    public void resolverComFetch() {
	        List<Pedido> lista = entityManager
	                .createQuery("select p from Pedido p " +
	                        " join fetch p.cliente c " +
	                        " join fetch p.pagamento pag " +
	                        " join fetch p.notaFiscal nf", Pedido.class)
	                .getResultList();

	        Assert.assertFalse(lista.isEmpty());
	    }
}
