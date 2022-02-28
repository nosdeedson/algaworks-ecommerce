package com.ejs.iniciandoJPA;

import org.junit.Assert;
import org.junit.Test;

import com.ejs.entityManager.EntityManagerTest;
import com.ejs.model.Cliente;
import com.ejs.model.ItemPedido;
import com.ejs.model.ItemPedidoId;
import com.ejs.model.Pedido;
import com.ejs.model.Produto;
import com.ejs.model.enums.StatusPedido;

public class ItemPedidoTest extends EntityManagerTest {
	
	
	@Test
	public void save() {
		Cliente cliente = entityManager.find(Cliente.class, 1);
		Produto produto = entityManager.find(Produto.class, 1);
		entityManager.getTransaction().begin();
		Pedido pedido = new Pedido();
		
		pedido.setCliente(cliente);
		pedido.setStatus(StatusPedido.AGUARDANDO);
		pedido.setTotal(produto.getPreco());
		
		entityManager.persist(pedido);
		entityManager.flush();
				
		ItemPedido itemPedido = new ItemPedido();
		
//		itemPedido.setPedidoId(pedido.getId()); chave composta
//		itemPedido.setProdutoId(produto.getId()); chave composta 
		itemPedido.setPedido(pedido);
		itemPedido.setProduto(produto);
		itemPedido.setId(new ItemPedidoId(pedido.getId(), produto.getId()));
		
		entityManager.persist(itemPedido);
			
		entityManager.getTransaction().commit();
		
		entityManager.clear();
		
		 Pedido pedidoVerificacao = entityManager.find(Pedido.class, pedido.getId());
	     Assert.assertNotNull(pedidoVerificacao);
	     Assert.assertFalse(pedidoVerificacao.getItensPedido().isEmpty());
	}
	
	@Test
	public void findItemPedido() {
		ItemPedido itemPedido = entityManager.find(ItemPedido.class,
				new ItemPedidoId(1, 1));
		
		Assert.assertNotNull(itemPedido);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
