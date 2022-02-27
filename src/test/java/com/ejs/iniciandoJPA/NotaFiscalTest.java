package com.ejs.iniciandoJPA;

import org.junit.Assert;
import org.junit.Test;

import com.ejs.entityManager.EntityManagerTest;
import com.ejs.model.NotaFiscal;
import com.ejs.model.Pedido;

public class NotaFiscalTest extends EntityManagerTest{

	@Test
	public void save() {
		Pedido pedido = entityManager.find(Pedido.class, 1);
		
		NotaFiscal nf = new NotaFiscal();
		nf.setPedido(pedido);
		nf.setXml("teste");
		entityManager.getTransaction().begin();
		entityManager.persist(nf);
		entityManager.getTransaction().commit();
		entityManager.clear();
		
		Pedido pedidovalidar = entityManager.find(Pedido.class, 1);
		Assert.assertNotNull(pedidovalidar.getNotaFiscal());
	}
}
