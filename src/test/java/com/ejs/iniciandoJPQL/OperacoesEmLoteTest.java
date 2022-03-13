package com.ejs.iniciandoJPQL;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.stream.Collectors;

import javax.persistence.Query;

import org.junit.Test;

import com.ejs.entityManager.EntityManagerTest;
import com.ejs.model.Produto;

public class OperacoesEmLoteTest extends EntityManagerTest {
	
	private static final int LIMITE_INSERCOES = 4;
	
	@Test 
	public void removeProdutos() {
		String jpql = "DELETE Produto p WHERE p.id between 16 AND 20";
		
		Query query = entityManager.createQuery(jpql);
		entityManager.getTransaction().begin();
		query.executeUpdate();
		entityManager.getTransaction().commit();
	}
	
	@Test
	public void updateProdutos() {
		String jpql = "UPDATE Produto p set p.preco= (p.preco + p.preco * 0.1)"
				+ " WHERE p.id between 20 and 27 ";
		
		Query query = entityManager.createQuery(jpql);
		entityManager.getTransaction().begin();
		query.executeUpdate();
		entityManager.getTransaction().commit();
	}
	
	@Test
	public void inserirEmLote() {
		
		
		InputStream input = OperacoesEmLoteTest.class.getClassLoader()
				.getResourceAsStream("produtos/importar.txt");
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		
		entityManager.getTransaction().begin();
		
		int contador = 0;
		
		for ( String linha : reader.lines().collect(Collectors.toList())) {
			if( linha.isBlank()) {
				continue;
			}
			
			String[] produtoColuna = linha.split(";");
			Produto produto = new Produto();
			produto.setNome(produtoColuna[0]);
			produto.setDescricao(produtoColuna[1]);
			produto.setPreco(new BigDecimal(produtoColuna[2]));
			
			entityManager.persist(produto);
			
			if(++contador == LIMITE_INSERCOES) {
				entityManager.flush();
				entityManager.clear();
				System.out.println(contador);
				contador = 0;
				System.out.println("----------------------------------------------------");
			}
		}
		entityManager.getTransaction().commit();
	}

}
