package com.ejs.iniciandoJPA;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

import com.ejs.entityManager.EntityManagerTest;
import com.ejs.model.Cliente;
import com.ejs.model.ItemPedido;
import com.ejs.model.NotaFiscal;
import com.ejs.model.Pedido;
import com.ejs.model.Produto;
import com.ejs.model.enums.SexoCliente;
import com.ejs.model.enums.StatusPedido;

public class ClienteTest extends EntityManagerTest{
	
	@Test
	public void findById() {
		Cliente cliente = entityManager.find(Cliente.class, 1);
		Assert.assertNotNull(cliente);
	}
	
	@Test
	public void remove() {
		Cliente deletar = entityManager.find(Cliente.class, 2);
		System.out.println(deletar.getNome());
		entityManager.getTransaction().begin();
		entityManager.remove(deletar);
		entityManager.getTransaction().commit();
		deletar = entityManager.find(Cliente.class, 2);
		Assert.assertNull(deletar);
	}
	
	@Test
	public void save() {
		Cliente cliente = new Cliente("Lucineia my crush", SexoCliente.FEMININO);
		entityManager.getTransaction().begin();
		cliente = entityManager.merge(cliente);
		entityManager.getTransaction().commit();
		
		Assert.assertNotNull(cliente);
	}
	
	
	@Test
	public void update() {
		Cliente cliente = entityManager.find(Cliente.class, 1);
		System.out.println(cliente.getNome());
		System.out.println("\n");
		cliente.setNome("Jussara souza");
		entityManager.getTransaction().begin();
		cliente = entityManager.merge(cliente);
		entityManager.getTransaction().commit();
		Assert.assertNotNull(cliente);
	}
	
	@Test
	public void verificaSeTemPedidos() {
		Cliente cliente = entityManager.find(Cliente.class, 1);
		
		Pedido pedido = new Pedido();
		pedido.setStatus(StatusPedido.AGUARDANDO);
		pedido.setCliente(cliente);
		pedido.setTotal(BigDecimal.TEN);
		
		NotaFiscal nf = new NotaFiscal();
		nf.setPedido(pedido);
		nf.setXml("teste");
		
		pedido.setNotaFiscal(nf);
		
		Produto produto = entityManager.find(Produto.class, 1);
		
		ItemPedido ip = new ItemPedido();
		ip.setPedido(pedido);
		ip.setProduto(produto);
		ip.setQuantidade(1);
		
		entityManager.getTransaction().begin();
		entityManager.persist(pedido);
		entityManager.persist(nf);
		entityManager.persist(ip);
		entityManager.getTransaction().commit();
		entityManager.clear();
		
		Cliente validar = entityManager.find(Cliente.class, 1);
		Assert.assertFalse(validar.getPedidos().isEmpty());
	}

	
}
