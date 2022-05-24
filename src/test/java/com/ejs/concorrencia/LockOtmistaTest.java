package com.ejs.concorrencia;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ejs.model.Produto;

public class LockOtmistaTest {

	protected static EntityManagerFactory entityManagerFactory;
	
	
	@BeforeClass
	public static void setUpBeforeClass() {
		entityManagerFactory = Persistence.createEntityManagerFactory("Ecommerce-PU");
	}
	
	@AfterClass
	public static void tearDownAfterClass() {
		entityManagerFactory.close();
	}
	
	private static void log(Object obj, Object... args) {
		System.out.println(
					String.format("[Log " + System.currentTimeMillis() + "] " + obj, args)
				);
	}
	
	private static void esperar(int segundos) {
		try {
			Thread.sleep(segundos * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void usarLockOtimista() {
		Runnable runnable = () ->{
			log("Runnable 1 vai carregar produdo 1");
			
			EntityManager entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();
			Produto produto = entityManager.find(Produto.class, 1, LockModeType.OPTIMISTIC);
			log("Runnable 1 vai esperar 3 segundos");
			esperar(3);
			produto.setDescricao("Runnable 1 editou");
			entityManager.getTransaction().commit();
			log("Runnable 1 salva alteração");
			entityManager.close();
		};
		
		Runnable runnable2 = () ->{
			log("Runnable 2 vai carregar o produto 1");
			EntityManager entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();
			Produto produto = entityManager.find(Produto.class, 1);
			
			log("Runnable 2 vai esperar 1 segundo");
			esperar(1);
			produto.setDescricao("Runnable 2 editou");
			log("Runnable 2 salva alteração");
			entityManager.getTransaction().commit();
			entityManager.close();
		};
		
		Thread thread = new Thread(runnable);
		Thread thread2 = new Thread(runnable2);
		
		thread.start();
		thread2.start();
		
		try {
			thread.join();
			thread2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		Produto produtoAlterado = entityManager.find(Produto.class, 1);
		entityManager.close();
		Assert.assertEquals("Runnable 2 editou", produtoAlterado.getDescricao());
		System.out.println(" PRODUTO VERSAO " + produtoAlterado.getVersao());
		System.out.println("TESTE FINALIZADO METODO: " + produtoAlterado.getDescricao());
	}
	
}
