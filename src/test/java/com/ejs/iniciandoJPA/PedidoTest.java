package com.ejs.iniciandoJPA;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

import org.junit.Assert;
import org.junit.Test;

import com.ejs.entityManager.EntityManagerTest;
import com.ejs.model.Cliente;
import com.ejs.model.ItemPedido;
import com.ejs.model.ItemPedidoId;
import com.ejs.model.Pedido;
import com.ejs.model.Pedido_;
import com.ejs.model.Produto;
import com.ejs.model.enums.SexoCliente;
import com.ejs.model.enums.StatusPedido;

public class PedidoTest extends EntityManagerTest{
	
	@Test
	public void removePedidoAndItens() {
		
		/**/
		
		Pedido pedido = entityManager.find(Pedido.class, 1);
		
		Integer idPedido = pedido.getId();
		
		entityManager.getTransaction().begin();
		entityManager.remove(pedido);
		entityManager.getTransaction().commit();
		entityManager.clear();
		
		pedido = entityManager.find(Pedido.class, idPedido);
		
		Assert.assertNull(pedido);
		
	}

	@Test
	public void save() {
		Cliente cliente = entityManager.find(Cliente.class, 1);
				
		Pedido pedido = new Pedido();
		pedido.setStatus(StatusPedido.AGUARDANDO);
		pedido.setCliente(cliente);
		
		Produto produto = entityManager.find(Produto.class, 1);
		Produto produto2 = entityManager.find(Produto.class, 2);
		
		ItemPedido ip = new ItemPedido();
		ip.setId(new ItemPedidoId());
		ip.setProduto(produto);
		ip.setQuantidade(1);
		/*the cascade will work if I set the 'pedido' in the 'itemPedido' otherwise as 'pedido' is null the JPA doesn't know how to relate
		 *  'pedido' with the 'itemPedido'*/
		ip.setPedido(pedido);
		
		ItemPedido ip2 = new ItemPedido();
		ip2.setId(new ItemPedidoId());
		ip2.setProduto(produto2);
		ip2.setQuantidade(1);
		ip2.setPedido(pedido);
		
		for (ItemPedido itemPedido : Arrays.asList(ip, ip2)) {
			pedido.setTotal(itemPedido.getProduto().getPreco()
					.multiply(new BigDecimal(itemPedido.getQuantidade())));
		}
		
		pedido.setItensPedido(Arrays.asList(ip, ip2));
		
		entityManager.getTransaction().begin();
		entityManager.persist(pedido);
		entityManager.getTransaction().commit();
		entityManager.clear();
		
		ItemPedido itemPedidoValidar = entityManager.find(ItemPedido.class, new ItemPedidoId(4, 1));
		Assert.assertNotNull(itemPedidoValidar);
		
		Pedido pedidoValidar = entityManager.find(Pedido.class, pedido.getId());
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
	
	@Test
	public void savePedidoWithNewCliente() {
		
		Cliente cliente = new Cliente();
		cliente.setCpf("111");
		cliente.setDataCriacao(LocalDateTime.now());
		cliente.setDataNascimento(LocalDateTime.of(1980, 6, 30, 0, 0));
		cliente.setNome("Jose souza");
		cliente.setSexo(SexoCliente.MASCULINO);
				
		Pedido pedido = new Pedido();
		pedido.setStatus(StatusPedido.AGUARDANDO);
		pedido.setCliente(cliente);
		
		Produto produto = entityManager.find(Produto.class, 1);
		Produto produto2 = entityManager.find(Produto.class, 2);
		
		ItemPedido ip = new ItemPedido();
		ip.setId(new ItemPedidoId());
		ip.setProduto(produto);
		ip.setQuantidade(1);
		/*the cascade will work if I set the 'pedido' in the 'itemPedido' otherwise as 'pedido' is null the JPA doesn't know how to relate
		 *  'pedido' with the 'itemPedido'*/
		ip.setPedido(pedido);
		
		ItemPedido ip2 = new ItemPedido();
		ip2.setId(new ItemPedidoId());
		ip2.setProduto(produto2);
		ip2.setQuantidade(1);
		ip2.setPedido(pedido);
		
		for (ItemPedido itemPedido : Arrays.asList(ip, ip2)) {
			pedido.setTotal(itemPedido.getProduto().getPreco()
					.multiply(new BigDecimal(itemPedido.getQuantidade())));
		}
		pedido.setItensPedido(Arrays.asList(ip, ip2));
		
		entityManager.getTransaction().begin();
		entityManager.persist(pedido);
		entityManager.getTransaction().commit();
		entityManager.clear();
		
		Pedido pedidoValidar = entityManager.find(Pedido.class, pedido.getId());
		
		Assert.assertNotNull(pedidoValidar.getCliente());
		
		Assert.assertFalse(pedidoValidar.getItensPedido().isEmpty());
		
	}
	
}
