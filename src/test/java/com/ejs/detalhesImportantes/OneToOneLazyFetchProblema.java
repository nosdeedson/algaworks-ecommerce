package com.ejs.detalhesImportantes;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.ejs.entityManager.EntityManagerTest;
import com.ejs.model.Pedido;

public class OneToOneLazyFetchProblema extends EntityManagerTest {

	@Test
	public void mostrandoProblema() {
		/*
		 * As buscas abaixo trazem pagamento e nota fiscal 
		 * para o fetch lazy funcionar é necessário implementar "PersistentAttributeInterceptable" do JPA
		 */
		Pedido pedido = entityManager.find(Pedido.class, 1);
		Assert.assertNotNull(pedido);
		
		// PARA TRAZER NOTA FISCAL E PAGAMETO É NECESSARIO USAR O JOIN FETCH NA STRING
		String sql = "select p from Pedido p";
		
		List<Pedido> pedidos = entityManager.createQuery(sql, Pedido.class).getResultList();
		Assert.assertFalse(pedidos.isEmpty());
	}
}
