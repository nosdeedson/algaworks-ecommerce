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

public class LockPessimistaTest {
	
	private static EntityManagerFactory entityManagerFactory;
	
	@BeforeClass
	public static void setUpBeforeClass() {
		entityManagerFactory = Persistence.createEntityManagerFactory("Ecommerce-PU");
	}
	
	@AfterClass
	public static void tearDownClass() {
		entityManagerFactory.close();
	}
	
	private void log(Object obj, Object... args) {
		System.out.println(
					String.format("[Log " + obj + " ]", args)
				);
	}

	private void esperar(int segundos) {
		try {
			Thread.sleep(segundos* 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void lockPessimista() {
		Runnable runnable = () ->{
			log("Runnable 01 está iniciando");
			EntityManager entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();
			/*
			 * Se apenas runnable 1 setar o lock pessimista apenas esta thread poderá alterar o produto;
			 * Se outra thread abrir uma transaction a que finalizar primeiro é que vai editar produto
			 */
			Produto  produto = entityManager.find(Produto.class, 1, LockModeType.PESSIMISTIC_READ);
			esperar(3);
			
			produto.setDescricao("Runnable 01 " + System.currentTimeMillis());
			log("Runable 1 commit produto");
			try {
				entityManager.getTransaction().commit();
			} catch (Exception e) {
				System.out.println("/n/n" + e.getMessage() + "\n\n Runnable 01");
			}
			entityManager.close();
			log("Runnable 01 salvou produto");
			
		};
		
		Runnable runnable2 = () ->{
			log("Runnable 02 está iniciando");
			EntityManager entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();
			Produto  produto = entityManager.find(Produto.class, 1, LockModeType.PESSIMISTIC_READ);
			esperar(1);
			
			produto.setDescricao("Runnable 02 " + System.currentTimeMillis());
			log("Runable 2 commit produto");
			try {
				entityManager.getTransaction().commit();
			} catch (Exception e) {
				System.out.println("/n/n" + e.getMessage() + "\n\n Runnable 02");
			}
			entityManager.close();
			log("Runnable 02 salvou produto");
			
		};
		
		Thread thread = new Thread(runnable);
		Thread thread2 = new Thread(runnable2);
		
		thread.start();
		esperar(1);
		thread2.start();
		
		try {
			thread.join();
			thread2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		Produto produtoAlterado = entityManager.find(Produto.class, 1);
		
		System.out.println(produtoAlterado.getDescricao());
		
		Assert.assertTrue(produtoAlterado.getDescricao().startsWith("Runnable 01 "));
		
		log("Método finalizou");
	}
	
	@Test
	public void lockWritePessimista() {
		Runnable runnable = () ->{
			log("Runnable 01 está iniciando");
			EntityManager entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();
			/*
			 * Se apenas Runnable 1 tiver o pessimistic write as outras transações não vão poder commitar
			 * pois serão travadas, apos runnable 1 commitar elas serão desbloqueadas, mas não poderam 
			 * alterar produto porque estão com uma versão antiga
			 * Se ambas as thread usarem pessimistic write, a segunda será travada no select 
			 * possibilitando que a segunda thread edita produto, pois esta pegará produto após o mesmo ser
			 * editado pela thread 1
			 */
			Produto  produto = entityManager.find(Produto.class, 1, LockModeType.PESSIMISTIC_WRITE);
			esperar(3);
			
			produto.setDescricao("Runnable 01 " + System.currentTimeMillis());
			log("Runable 1 commit produto");
			try {
				entityManager.getTransaction().commit();
			} catch (Exception e) {
				System.out.println("/n/n" + e.getMessage() + "\n\n Runnable 01");
			}
			entityManager.close();
			log("Runnable 01 salvou produto");
			
		};
		
		Runnable runnable2 = () ->{
			log("Runnable 02 está iniciando");
			EntityManager entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();
			Produto  produto = entityManager.find(Produto.class, 1, LockModeType.PESSIMISTIC_WRITE);
			esperar(1);
			
			produto.setDescricao("Runnable 02 " + System.currentTimeMillis());
			log("Runable 2 commit produto");
			try {
				entityManager.getTransaction().commit();
			} catch (Exception e) {
				System.out.println("/n/n" + e.getMessage() + "\n\n Runnable 02");
			}
			entityManager.close();
			log("Runnable 02 salvou produto");
			
		};
		
		Thread thread = new Thread(runnable);
		Thread thread2 = new Thread(runnable2);
		
		thread.start();
		esperar(1);
		thread2.start();
		
		try {
			thread.join();
			thread2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		Produto produtoAlterado = entityManager.find(Produto.class, 1);
		
		System.out.println(produtoAlterado.getDescricao());
		
		Assert.assertTrue(produtoAlterado.getDescricao().startsWith("Runnable 02"));
		
		log("Método finalizou");
	}
	
	@Test
	public void misturandoLockPessimista() {
		Runnable runnable = () ->{
			log("Runnable 01 está iniciando");
			EntityManager entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();
			/*
			 * Usando os locks com está a seguna thread será travada no select, apoś finalizar a 
			 * thread 1, ela realizará todo o processo normalmente
			 */
			Produto  produto = entityManager.find(Produto.class, 1, LockModeType.PESSIMISTIC_READ);
			esperar(3);
			
			produto.setDescricao("Runnable 01 " + System.currentTimeMillis());
			log("Runable 1 commit produto");
			try {
				entityManager.getTransaction().commit();
			} catch (Exception e) {
				System.out.println("/n/n" + e.getMessage() + "\n\n Runnable 01");
			}
			entityManager.close();
			log("Runnable 01 salvou produto");
			
		};
		
		Runnable runnable2 = () ->{
			log("Runnable 02 está iniciando");
			EntityManager entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();
			Produto  produto = entityManager.find(Produto.class, 1, LockModeType.PESSIMISTIC_WRITE);
			esperar(1);
			
			produto.setDescricao("Runnable 02 " + System.currentTimeMillis());
			log("Runable 2 commit produto");
			try {
				entityManager.getTransaction().commit();
			} catch (Exception e) {
				System.out.println("/n/n" + e.getMessage() + "\n\n Runnable 02");
			}
			entityManager.close();
			log("Runnable 02 salvou produto");
			
		};
		
		Thread thread = new Thread(runnable);
		Thread thread2 = new Thread(runnable2);
		
		thread.start();
		esperar(1);
		thread2.start();
		
		try {
			thread.join();
			thread2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		Produto produtoAlterado = entityManager.find(Produto.class, 1);
		
		System.out.println(produtoAlterado.getDescricao());
		
		Assert.assertTrue(produtoAlterado.getDescricao().startsWith("Runnable 02"));
		
		log("Método finalizou");
	}
	

	@Test
	public void misturandoLockPessimista2() {
		Runnable runnable = () ->{
			log("Runnable 01 está iniciando");
			EntityManager entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();
			/*
			 * Usando os locks com está a seguna thread será travada no select, e como ela solicitou
			 * um lock para update o select não será executado
			 */
			Produto  produto = entityManager.find(
					Produto.class, 1, LockModeType.PESSIMISTIC_READ);
			log("runnable 1 espera 3 segundos");
			esperar(3);
			
			produto.setDescricao("Runnable 01 " + System.currentTimeMillis());
			log("Runable 1 commit produto");
			try {
				entityManager.getTransaction().commit();
			} catch (Exception e) {
				System.out.println("/n/n" + e.getMessage() + "\n\n Runnable 01");
			}
			entityManager.close();
			log("Runnable 01 salvou produto");
			
		};
		
		Runnable runnable2 = () ->{
			log("Runnable 02 está iniciando");
			EntityManager entityManager2 = entityManagerFactory.createEntityManager();
			entityManager2.getTransaction().begin();
			Produto  produto = entityManager2.find(
					Produto.class, 1, LockModeType.PESSIMISTIC_WRITE);
			produto.setDescricao("Runnable 02 " + System.currentTimeMillis());
			esperar(1);
			
			log("Runable 2 commit produto");
			try {
				entityManager2.getTransaction().commit();
			} catch (Exception e) {
				System.out.println("/n/n" + e.getMessage() + "\n\n Runnable 02");
			}
			entityManager2.close();
			log("Runnable 02 salvou produto");
			
		};
		
		Thread thread = new Thread(runnable);
		Thread thread2 = new Thread(runnable2);
		
		thread.start();
		esperar(1);
		thread2.start();
		
		try {
			thread.join();
			thread2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		EntityManager entityManager3 = entityManagerFactory.createEntityManager();
		Produto produtoAlterado = entityManager3.find(Produto.class, 1);
		
		System.out.println(produtoAlterado.getDescricao());
		
		Assert.assertTrue(produtoAlterado.getDescricao().startsWith("Runnable 02"));
		
		log("Método finalizou");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
