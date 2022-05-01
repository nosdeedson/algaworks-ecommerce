package com.ejs.consultasNativas;

import java.util.List;

import javax.persistence.Query;

import org.junit.Assert;
import org.junit.Test;

import com.ejs.DTO.ProdutoDTO;
import com.ejs.entityManager.EntityManagerTest;
import com.ejs.model.Categoria;
import com.ejs.model.Produto;

public class ConsultasNativas extends EntityManagerTest {
	
	@Test
	public void listaCategorias() {
		Query query = entityManager.createNamedQuery("categoria.listar");
		@SuppressWarnings("unchecked")
		List<Categoria> categorias = query.getResultList();
		categorias.forEach(c -> System.out.println(String.format("Categoria => Id: %s, nome: %s", c.getId(), c.getNome())));
		Assert.assertFalse(categorias.isEmpty());
	}
	
	@Test
	public void buscarProdutosComNamedQuery() {
		Query query = entityManager.createNamedQuery("queryNativa.produto.listar");
		@SuppressWarnings("unchecked")
		List<Produto> produtos = query.getResultList();
		Assert.assertFalse(produtos.isEmpty());
		produtos.forEach(p -> System.out.println(String.format("Id: %s, nome %s", p.getId(), p.getNome())));
	}
	
	@Test
	public void buscaProdutoDTO() {
		String sql = "SELECT id, nome FROM produto";
		Query query = entityManager.createNativeQuery(sql, "produto.ProdutoDTO");
		@SuppressWarnings("unchecked")
		List<ProdutoDTO> produtos = query.getResultList();
		Assert.assertFalse(produtos.isEmpty());
		produtos.forEach(p -> System.out.println(String.format("produtoDto => id: %s, nome %s", p.getId(), p.getNome())));
	}

	@Test
	public void findById() {
		String sql = "SELECT * FROM produto WHERE id= :id";
		Query query = entityManager.createNativeQuery(sql, Produto.class);
		query.setParameter("id", 1);
		Produto produto = (Produto) query.getSingleResult();
		System.out.println(produto.getNome());
		Assert.assertNotNull(produto);
	}
	
	@Test
	public void select() {
		String sql = "SELECT id, nome FROM produto";
		
		Query query = entityManager.createNativeQuery(sql);
		@SuppressWarnings("unchecked")
		List<Object[]> produtos = query.getResultList();
		
		produtos.stream().forEach(arr -> System.out.println(arr[0] + ", "  +arr[1]));
	}
	
	@Test
	public void selectProdutos() {
		String sql = "SELECT * FROM produto";
		Query query = entityManager.createNativeQuery(sql, Produto.class);
		@SuppressWarnings("unchecked")
		List<Produto> produtos = query.getResultList();
		produtos.forEach(p -> System.out.println(String.format("Produto => Id: %s, Nome: %s", p.getId(), p.getNome())));
		
		Assert.assertFalse(produtos.isEmpty());
	}
}
