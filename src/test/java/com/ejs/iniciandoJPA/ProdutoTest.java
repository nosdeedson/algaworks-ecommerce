package com.ejs.iniciandoJPA;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

import com.ejs.entityManager.EntityManagerTest;
import com.ejs.model.Produto;

public class ProdutoTest extends EntityManagerTest{
		
	@Test
	public void consultandoProduto() {
		Produto produto = entityManager.find(Produto.class, 1);
		Assert.assertNotNull(produto);
//		Assert.assertEquals("Kindle", produto.getNome());
	}
	
	@Test
	public void atualizandoProduto() {
		Produto produto = entityManager.find(Produto.class, 1);
		produto.setNome("teste");
		System.out.println(produto.getNome());		
		entityManager.refresh(produto);
		System.out.println(produto.getNome());
		Assert.assertEquals("Kindle", produto.getNome());
	}
	
	@Test
	public void atualizarProduto() {
		Produto produto = entityManager.find(Produto.class, 1);
		
		produto.setNome("teste");
		System.out.println(produto.getNome());	
		entityManager.getTransaction().begin();
		entityManager.merge(produto);
		entityManager.getTransaction().commit();
		System.out.println(produto.getNome());
		entityManager.refresh(produto);
		System.out.println(produto.getNome());
		Assert.assertEquals("teste", produto.getNome());
	}
	
	@Test
	public void inserirProduto() {
		Produto produto = new Produto();
		produto.setDescricao("teste inserçao");
		produto.setNome("TV tela plana");
		produto.setPreco(new BigDecimal(3000));
		entityManager.getTransaction().begin();
		entityManager.persist(produto);
		entityManager.getTransaction().commit();
		entityManager.clear();
		Produto produtoVerificacao = entityManager.find(Produto.class, produto.getId());
		Assert.assertNotNull(produtoVerificacao);
	}
	
	@Test
	public void removerProduto() {
		Produto produto = new Produto();
		produto.setId(2);
		produto.setDescricao("teste inserçao");
		produto.setNome("TV tela plana");
		produto.setPreco(new BigDecimal(3000));
		entityManager.getTransaction().begin();
		entityManager.persist(produto);
		entityManager.flush();
		entityManager.getTransaction().commit();
		Produto deletar = entityManager.find(Produto.class, 2);
		entityManager.getTransaction().begin();
		entityManager.remove(deletar);
		entityManager.getTransaction().commit();
		deletar = entityManager.find(Produto.class, 2);
		
		Assert.assertNull(deletar);
	}
	

}
