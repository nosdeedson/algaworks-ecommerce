package com.ejs.iniciandoJPA;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.ejs.entityManager.EntityManagerTest;
import com.ejs.model.Categoria;
import com.ejs.model.Produto;

public class CategoriaTest extends EntityManagerTest {

	@Test
	public void save() {
		Categoria categoriaPai = entityManager.find(Categoria.class, 1);
		
		Categoria categoria = new Categoria();
		categoria.setNome("Celulares");
		categoria.setCategoriaPai(categoriaPai);
		
		entityManager.getTransaction().begin();
		entityManager.persist(categoriaPai);
		entityManager.persist(categoria);
		entityManager.getTransaction().commit();
		entityManager.clear();
		
		Categoria validarPai = entityManager.find(Categoria.class, categoriaPai.getId());
		Assert.assertFalse(validarPai.getCategorias().isEmpty());
		
		Categoria validarFilha = entityManager.find(Categoria.class, categoria.getId());
		Assert.assertNotNull(validarFilha.getCategoriaPai());
	}
	
	@Test
	public void addRelationProdutoCategoria() {
		Categoria categoria = entityManager.find(Categoria.class, 1);
		Produto produto = entityManager.find(Produto.class, 1);
		
		produto.setCategorias(Arrays.asList(categoria));
		
		entityManager.getTransaction().begin();
		entityManager.persist(produto);
		entityManager.getTransaction().commit();
		
		entityManager.clear();
		
		Produto produtoValidar = entityManager.find(Produto.class, produto.getId());
		
		Assert.assertFalse(produtoValidar.getCategorias().isEmpty());
	}
	
	
	
	
	
}
