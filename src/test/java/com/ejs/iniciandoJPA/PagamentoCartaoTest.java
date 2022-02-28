package com.ejs.iniciandoJPA;

import org.junit.Assert;
import org.junit.Test;

import com.ejs.entityManager.EntityManagerTest;
import com.ejs.model.Pagamento;
import com.ejs.model.PagamentoCartao;
import com.ejs.model.Pedido;
import com.ejs.model.enums.StatusPagamento;

public class PagamentoCartaoTest extends EntityManagerTest {

	@Test
	public void save(){
		Pedido pedido = entityManager.find(Pedido.class, 2);
		
		Pagamento pc = new PagamentoCartao();
		((PagamentoCartao) pc).setNumeroCartao("123456");
		pc.setPedido(pedido);
		pc.setStatus(StatusPagamento.PROCESSANDO);
		
		entityManager.getTransaction().begin();
		entityManager.persist(pc);
		entityManager.getTransaction().commit();
		entityManager.clear();
		
		Pedido pedidovalidar = entityManager.find(Pedido.class, 2);
		
		Assert.assertNotNull(pedidovalidar.getPagamento());
		
		Pedido pedido2 = entityManager.find(Pedido.class, 1);
		
		Pagamento pc2 = new PagamentoCartao();
		((PagamentoCartao) pc2).setNumeroCartao("123456");
		pc2.setPedido(pedido2);
		pc2.setStatus(StatusPagamento.PROCESSANDO);
		
		entityManager.getTransaction().begin();
		entityManager.persist(pc2);
		entityManager.getTransaction().commit();
		entityManager.clear();
		
		Pedido pedidovalidar2 = entityManager.find(Pedido.class, 1);
		
		Assert.assertNotNull(pedidovalidar2.getPagamento());
	}
	
	
	
	
	
	
	
	
	
	
	
}
