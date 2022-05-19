package com.ejs.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Cache;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.CacheStoreMode;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ejs.model.Pedido;

public class CacheTest {
	
protected static EntityManagerFactory entityManagerFactory;
	
	@BeforeClass
	public static void setUpBeforeClass() {
		entityManagerFactory = Persistence.createEntityManagerFactory("Ecommerce-PU");
	}
	
	@AfterClass
	public static void tearDownAfterClass() {
		entityManagerFactory.close();
	}
	
	public void esperar(int segundos) throws InterruptedException {
		Thread.sleep(segundos * 1000);
	}
	
	@Test 
	public void ehCache() throws InterruptedException {
		Cache cache = entityManagerFactory.getCache();
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityManager entityManager2 = entityManagerFactory.createEntityManager();
		List<Pedido> pedidos =  entityManager.createQuery("select p from Pedido p", Pedido.class).getResultList();
		
		System.out.println("imprimindo primeiro lista");
		System.out.println(pedidos.get(0).getId());
		
		System.out.println("imprimindo o pedido dois");
		System.out.println(entityManager2.find(Pedido.class, 2).getId());
		
		Assert.assertTrue(cache.contains(Pedido.class, 2));
		
		esperar(2);
	
		Assert.assertFalse(cache.contains(Pedido.class, 2));
		entityManager.close();
		entityManager2.close();
	}
	
	@Test
	public void cacheDinanico() {
		// javax.persistence.cache.retrieveMode CacheRetrieveMode
        // javax.persistence.cache.storeMode CacheStoreMode
		
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityManager entityManager1 = entityManagerFactory.createEntityManager();
		
		/*Toda consulta com esta entidade não vai salvar no cache de segundo nível*/
		entityManager.setProperty("javax.persistence.cache.storeMode", CacheStoreMode.BYPASS);
		
		System.out.println("buscando todos os pedidos #######################################");
		
		/*Sobreescreve a configuração do cache de segundo nível da linha acima*/
		entityManager.createQuery("select p from Pedido p", Pedido.class)
			/* BYPASS não adiciona os resultados no cache de segundo nível*/
//			.setHint("javax.persistence.cache.storeMode", CacheRetrieveMode.BYPASS)
			/*USE adiciona os resultados no cache de segundo nível*/
			.setHint("javax.persistence.cache.storeMode", CacheRetrieveMode.USE)
			.getResultList();
		System.out.println("buscando pedido 2 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		
		Map<String, Object> properties = new HashMap<String, Object>();
		
		/* faz com que o JPA consulte o banco*/
//		properties.put("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
		
		
		/* faz com que o JPA use o cache de segundo nível*/
		properties.put("javax.persistence.cache.retrieveMode", CacheRetrieveMode.USE);
		
		entityManager1.find(Pedido.class, 2, properties);
		entityManager.close();
		entityManager1.close();
	}
	
	@Test
	public void testeCache() {
		Cache cache = entityManagerFactory.getCache();
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.createQuery("select p from Pedido p", Pedido.class).getResultList();
		
		Assert.assertTrue(cache.contains(Pedido.class, 1));
		entityManager.close();
	}
	
	@Test
	public void verificaSeEntidadeEstaNoCache() {
		Cache cache = entityManagerFactory.getCache();
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.createQuery("select p from Pedido p", Pedido.class).getResultList();
		
		Assert.assertTrue(cache.contains(Pedido.class, 1));
		Assert.assertTrue(cache.contains(Pedido.class, 2));
		entityManager.close();
	}
	
	@Test
	public void removerDoCache() {
		
		Cache cache = entityManagerFactory.getCache();
		EntityManager entityManager1 = entityManagerFactory.createEntityManager();
		EntityManager entityManager2 = entityManagerFactory.createEntityManager();
		
		System.out.println("Buscando com o entityManager1");
		List<Pedido> pedidos = entityManager1.createQuery("select p from Pedido p", Pedido.class).getResultList();
		System.out.println("id " + pedidos.get(0).getId());
		
		/*tira do cache apenas o pedido de id 1*/
//		cache.evict(Pedido.class, 1);
		
		/*tira todos os pedidos do cache*/
//		cache.evict(Pedido.class);
		
		/*limpa todo o cache */
		cache.evictAll();
		
		System.out.println("Buscando com o entityManager2");
		Pedido pedido1 = entityManager2.find(Pedido.class, 1);
		Pedido pedido2 = entityManager2.find(Pedido.class, 2);
		System.out.println("id " + pedido1.getId());
		System.out.println("id " + pedido2.getId());
		entityManager1.close();
		entityManager2.close();
	}
	
	@Test
	public void buscarDaListaNoCache() {
		EntityManager entityManager1 = entityManagerFactory.createEntityManager();
		EntityManager entityManager2 = entityManagerFactory.createEntityManager();
		
		System.out.println("Buscando com o entitymanager1");
		List<Pedido> lista = entityManager1
				.createQuery("SELECT p FROM Pedido p", Pedido.class)
				.getResultList();
		System.out.println("id: " + lista.get(0).getId());
		
		/*
		 * A primeira busca (acima) é feita e os resulstados são salvos no cache de segundo nível
		 * A segunda busca (abaixo) não tem requisição ao banco de dados o pesquisa é feita na lista 
		 * que está no cache de segundo nível.
		 */
		System.out.println("Buscando com o entitymanager2");
		Pedido pedido1 = entityManager2.find(Pedido.class, 1);
		System.out.println("id: " + pedido1.getId());
		entityManager1.close();
		entityManager2.close();
	}
	
	@Test
	public void buscarDoCache() {
		EntityManager entityManager1 = entityManagerFactory.createEntityManager();
		EntityManager entityManager2 = entityManagerFactory.createEntityManager();
		
		System.out.println("Buscando com o entitymanager1");
		Pedido pedido = entityManager1.find(Pedido.class, 1);
		System.out.println("id: " + pedido.getId());
		
		/*
		 * Devido ao uso do cache de segundo nível não é feita a segunda consulta no banco de dados
		 * A configuração do cache de segundo nível é feita pela API hibernate-testing
		 * A API implementa a especificação de cache de segundo nível
		 */
		System.out.println("Buscando com o entitymanager2");
		Pedido pedido1 = entityManager2.find(Pedido.class, 1);
		System.out.println("id: " + pedido1.getId());
		entityManager1.close();
		entityManager2.close();
	}

}
