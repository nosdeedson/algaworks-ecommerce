package com.ejs.iniciandoJPA;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.ejs.entityManager.EntityManagerTest;
import com.ejs.model.Cliente;
import com.ejs.model.ItemPedido;
import com.ejs.model.ItemPedidoId;
import com.ejs.model.Pedido;
import com.ejs.model.Produto;
import com.ejs.model.enums.StatusPedido;

public class PedidoTest extends EntityManagerTest{

	@Test
	public void save() {
		Cliente cliente = entityManager.find(Cliente.class, 1);
				
		Pedido pedido = new Pedido();
		pedido.setStatus(StatusPedido.AGUARDANDO);
		pedido.setCliente(cliente);
		
		Produto produto = entityManager.find(Produto.class, 1);
		Produto produto2 = entityManager.find(Produto.class, 2);
		
		ItemPedido ip = new ItemPedido();
		ip.setProduto(produto);
		ip.setQuantidade(1);
		
		ItemPedido ip2 = new ItemPedido();
		ip2.setProduto(produto2);
		ip2.setQuantidade(1);
		
		
		for (ItemPedido itemPedido : Arrays.asList(ip, ip2)) {
			pedido.setTotal(itemPedido.getProduto().getPreco()
					.multiply(new BigDecimal(itemPedido.getQuantidade())));
		}
		
		entityManager.getTransaction().begin();
		Pedido pedidoBD = entityManager.merge(pedido);
		ip.setId(new ItemPedidoId(pedidoBD.getId(), produto.getId()));
		ip.setPedido(pedidoBD);
		ip2.setPedido(pedidoBD);
		ip2.setId(new ItemPedidoId(pedidoBD.getId(), produto.getId()));
		entityManager.persist(ip);
		entityManager.persist(ip2);
		entityManager.getTransaction().commit();
		entityManager.clear();
		
		ItemPedido itemPedidoValidar = entityManager.find(ItemPedido.class, new ItemPedidoId(2, 1));
		Assert.assertNotNull(itemPedidoValidar);
		
		Pedido pedidoValidar = entityManager.find(Pedido.class, 2);
		Assert.assertNotNull(pedidoValidar.getItensPedido());
				
	}
	
	@Test
	public void find() {
		Pedido pedido = entityManager.find(Pedido.class, 2);
		Assert.assertNotNull(pedido.getItensPedido());
	}
	
	@Test
	public void remove() {
		Pedido pedido = entityManager.find(Pedido.class, 1);
		entityManager.getTransaction().begin();
		entityManager.remove(pedido);
		entityManager.getTransaction().commit();
		entityManager.clear();
		Pedido pedidoValidar = entityManager.find(Pedido.class, 1);
		Assert.assertNull(pedidoValidar);
	}
	
}
