package com.ejs.iniciandoJPA;

import org.junit.Assert;
import org.junit.Test;

import com.ejs.entityManager.EntityManagerTest;
import com.ejs.model.ItemPedido;
import com.ejs.model.ItemPedidoId;
import com.ejs.model.Pedido;

public class OrphanRemovalTest extends EntityManagerTest {
	
	@Test
	public void removePedidoAndOrphans() {
		Pedido pedido = entityManager.find(Pedido.class, 1);
		
		entityManager.getTransaction().begin();
		entityManager.remove(pedido);
		entityManager.getTransaction().commit();
		
		entityManager.clear();
		
		ItemPedido itemPedido = entityManager.find(ItemPedido.class, new ItemPedidoId(1,1));
		
		Assert.assertNull(itemPedido);
	}

}
