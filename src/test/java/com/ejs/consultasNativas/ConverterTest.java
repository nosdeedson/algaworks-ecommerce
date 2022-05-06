package com.ejs.consultasNativas;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

import com.ejs.entityManager.EntityManagerTest;
import com.ejs.model.Produto;

public class ConverterTest extends EntityManagerTest {
	
	@Test
	public void converterTest() {
		Produto p = new Produto();
		p.setAtivo(Boolean.TRUE);
		p.setDescricao("celular galaxi funciona até estragar");
		p.setNome("Galaxi s5");
		p.setPreco(new BigDecimal(2000));
		entityManager.getTransaction().begin();
		entityManager.persist(p);
		entityManager.getTransaction().commit();
		
		Produto produtoBD = entityManager.find(Produto.class, 4);
		Assert.assertTrue(produtoBD.getAtivo());
		
		System.out.println(p.getNome());
	}

}
