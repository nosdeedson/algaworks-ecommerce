package com.ejs.iniciandoJPA;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

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
		nf.setXml("teste".getBytes());
		
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
	
	@Test
	public void addContato() {
		Cliente cliente = entityManager.find(Cliente.class, 1);
		cliente.setContatos(Collections.singletonMap("email", "fernando@email.com"));
		cliente.setDataNascimento(LocalDateTime.of(1989, 07, 01, 0, 0));
		entityManager.getTransaction().begin();
		entityManager.getTransaction().commit();
		entityManager.clear();
		
		cliente = entityManager.find(Cliente.class, 1);
		Assert.assertEquals("fernando@email.com", cliente.getContatos().get("email"));
	}
	
	@Test
	public void saveWithXML() throws Exception {
		
       Pedido pedido = entityManager.find(Pedido.class, 1);
		
		NotaFiscal nf = new NotaFiscal();
		nf.setPedido(pedido);
		nf.setXml(carregarNota());
		entityManager.getTransaction().begin();
		entityManager.persist(nf);
		entityManager.getTransaction().commit();
		entityManager.clear();
		
		nf = entityManager.find(NotaFiscal.class, 1);
		Assert.assertNotNull(nf.getXml());
	}
	
	private byte[] carregarNota() throws Exception {
		ClassLoader loader = getClass().getClassLoader();
		try {
			return loader.getResourceAsStream("nf.xml").readAllBytes();
		} catch (Exception e) {
			throw 	new RuntimeException(e);
		}
	}

	
}
