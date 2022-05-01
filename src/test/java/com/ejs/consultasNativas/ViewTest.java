package com.ejs.consultasNativas;

import java.util.List;

import javax.persistence.Query;

import org.junit.Test;

import com.ejs.entityManager.EntityManagerTest;
import com.ejs.model.Cliente;

public class ViewTest extends EntityManagerTest {
	
	@Test
	public void executarView() {
		
		String sql = "SELECT cli.id, cli.nome, sum(ped.total) "
				+ " FROM pedido ped "
				+ " JOIN view_clientes_acima_media cli ON cli.id=ped.cliente_id "
				+ " GROUP BY ped.cliente_id";
		
		Query query = entityManager.createNativeQuery(sql);
		
		@SuppressWarnings("unchecked")
		List<Object[]> lista = query.getResultList();
		lista.stream().forEach(l ->{
			System.out.println(String.format("Cliente => id: %s, nome %s", l));
		});
		
	}
	
	@Test
	public void executarViewRetornandoCliente() {
		String sql = "SELECT * FROM view_clientes_acima_media";
		Query query = entityManager.createNativeQuery(sql, Cliente.class);
		@SuppressWarnings("unchecked")
		List<Cliente> clientes = query.getResultList();
		clientes.forEach(c ->{
			System.out.println(String.format("Cliente => Id: %s, nome %s", c.getId(), c.getNome()));
		});
	}
	

}
