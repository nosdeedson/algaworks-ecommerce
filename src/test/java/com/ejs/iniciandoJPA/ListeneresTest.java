package com.ejs.iniciandoJPA;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.ejs.entityManager.EntityManagerTest;
import com.ejs.model.Cliente;
import com.ejs.model.ItemPedido;
import com.ejs.model.Pedido;
import com.ejs.model.Produto;
import com.ejs.model.enums.StatusPedido;

public class ListeneresTest extends EntityManagerTest {
	
	@Test
	public void carregarEntidades() {
		Produto produto = entityManager.find(Produto.class, 1);
		Pedido pedido = entityManager.find(Pedido.class, 1);
	}

	@Test
	public void testingListenerOfPedido() {
		System.out.println("checking during update");
		
		Cliente cliente = entityManager.find(Cliente.class, 1);
		
		Pedido pedido2 =new Pedido();
		pedido2.setCliente(cliente);
		pedido2.setStatus(StatusPedido.PAGO);
		Produto produto = entityManager.find(Produto.class, 1);

		ItemPedido ip = new ItemPedido();
		ip.setPedido(pedido2);
		ip.setProduto(produto);
		ip.setQuantidade(1);
		pedido2.setItensPedido(Arrays.asList(ip));
		
		entityManager.getTransaction().begin();
		entityManager.persist(pedido2);
		entityManager.flush();
		entityManager.getTransaction().commit();
		
		entityManager.clear();
		
		Pedido pedido2Validar = entityManager.find(Pedido.class, pedido2.getId());
		Assert.assertNull(pedido2Validar.getNotaFiscal());
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
