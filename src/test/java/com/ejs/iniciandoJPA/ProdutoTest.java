package com.ejs.iniciandoJPA;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.Assert;
import org.junit.Test;

import com.ejs.entityManager.EntityManagerTest;
import com.ejs.model.CaracteristicaProduto;
import com.ejs.model.Categoria;
import com.ejs.model.Produto;

public class ProdutoTest extends EntityManagerTest{
	
	@Test
	public void updateProdutoCascadeCategoria() {
	
		/*to work is needed to add de cascade all in the class produto in the atribute categoria*/
		Produto produto = entityManager.find(Produto.class, 2);
		produto.setPreco(new BigDecimal(1500));
		List<Categoria> categorias = new ArrayList<Categoria>();
		produto.getCategorias().forEach(cat -> {
			Categoria categoriaPai = cat;
			Categoria categoriaFilha = new Categoria("Tablet");
			categoriaFilha.setCategoriaPai(categoriaPai);
			categorias.add(categoriaFilha);
		});
		produto.setCategorias(categorias);
		entityManager.getTransaction().begin();
		produto = entityManager.merge(produto);
		entityManager.getTransaction().commit();
		entityManager.clear();
		
		Assert.assertEquals("Tablet", produto.getCategorias().get(0).getNome());
	}
	
	@Test
	public void saveProdutoCascadeCategoria() {
		Categoria categoriaPai = new Categoria("Esportes");
		
		Categoria categoriaFilha = new Categoria("Futebol");
		categoriaFilha.setCategoriaPai(categoriaPai);
		
		Produto produto = new Produto();
		produto.setCategorias(Arrays.asList(categoriaFilha));
		produto.setDescricao("bola de futebol");
		produto.setNome("bola");
		produto.setPreco(BigDecimal.TEN);
		
		categoriaFilha.setProdutos(Arrays.asList(produto));
		
		entityManager.getTransaction().begin();
		entityManager.persist(categoriaPai);
		entityManager.persist(produto);
		entityManager.getTransaction().commit();
		
		entityManager.clear();
		
		Produto produtoValidar = entityManager.find(Produto.class, produto.getId());
		
		Assert.assertNotNull(produtoValidar.getCategorias());
		Assert.assertFalse(produto.getCategorias().get(0).getCategoriaPai() == null);
	}
	
		
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
		Produto produto = entityManager.find(Produto.class, 2);
		entityManager.getTransaction().begin();
		entityManager.remove(produto);
		entityManager.getTransaction().commit();
		entityManager.clear();
		
		Produto deletar = entityManager.find(Produto.class, 2);
		Assert.assertNull(deletar);
		
		Categoria categoria = entityManager.find(Categoria.class, 1);
		Assert.assertNotNull(categoria);
		
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void saveFoto() {
		Produto produto = entityManager.find(Produto.class, 1);
		
		ClassLoader loader = getClass().getClassLoader();
		
		try {
			produto.setFoto(loader.getSystemResourceAsStream("teste.png").readAllBytes());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		entityManager.getTransaction().begin();
		entityManager.getTransaction().commit();
		entityManager.clear();
		
		produto= entityManager.find(Produto.class, 1);
		Assert.assertNotNull(produto.getFoto());
		
//        try {
//            OutputStream out = new FileOutputStream(
//                    Files.createFile(Paths.get("/home/edson/Imagens/foto.jpg")).toFile());
//            out.write(produto.getFoto());
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
      
	}
	
	@Test
	public void setTags() {
		Produto produto = entityManager.find(Produto.class, 1);
		produto.setTags(Arrays.asList("e-book","livro-digital"));
		entityManager.getTransaction().begin();
		entityManager.getTransaction().commit();
		entityManager.clear();
		
		produto = entityManager.find(Produto.class, 1);
		
		Assert.assertFalse(produto.getTags().isEmpty());
	}
	
	@Test
	public void setCaracteristicas() {
		Produto produto = entityManager.find(Produto.class, 1);
		produto.setCaracteristica(Arrays.asList(new CaracteristicaProduto("tela", "320X600"), 
				new CaracteristicaProduto("Processador", "não tenho ideia")));
				
		entityManager.getTransaction().begin();
		entityManager.getTransaction().commit();
		entityManager.clear();
		produto = entityManager.find(Produto.class, 1);
		Assert.assertNotNull(produto.getCaracteristica());
	}

}
