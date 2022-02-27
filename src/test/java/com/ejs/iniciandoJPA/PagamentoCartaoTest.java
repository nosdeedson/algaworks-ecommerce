package com.ejs.iniciandoJPA;

import org.junit.Assert;
import org.junit.Test;

import com.ejs.entityManager.EntityManagerTest;
import com.ejs.model.PagamentoCartao;
import com.ejs.model.Pedido;
import com.ejs.model.enums.StatusPagamento;

public class PagamentoCartaoTest extends EntityManagerTest {

	@Test
	public void save(){
		Pedido pedido = entityManager.find(Pedido.class, 1);
		
		PagamentoCartao pc = new PagamentoCartao();
		pc.setNumero("123456");
		pc.setPedido(pedido);
		pc.setStatus(StatusPagamento.PROCESSANDO);
		
		entityManager.getTransaction().begin();
		entityManager.persist(pc);
		entityManager.getTransaction().commit();
		entityManager.clear();
		
		Pedido pedidovalidar = entityManager.find(Pedido.class, 1);
		
		Assert.assertNotNull(pedidovalidar.getPagamentoCartao());
	}
	
	
	
	
	
	
	
	
	
	
	
}
