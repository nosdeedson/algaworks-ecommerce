package com.ejs.iniciandoJPQL;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.TypedQuery;

import org.junit.Test;

import com.ejs.DTO.ProdutoDTO;
import com.ejs.entityManager.EntityManagerTest;
import com.ejs.model.ItemPedido;
import com.ejs.model.Pedido;

public class IniciandoJPQLTest extends EntityManagerTest {
	
	@Test
	public void fetchProjetar() {
		String jpql = "SELECT p FROM Pedido p "
				+ " left join fetch p.pagamento pag "
				+ " left join fetch p.notaFiscal n "
				+ " inner join fetch p.cliente c";
		
		TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);
		
		List<Pedido> lista = typedQuery.getResultList();
		
		System.out.println(lista.size());
	}
	
	@Test
	public void leftOuterJoinProjetar() {
		
		/*as I used the filter 'on' the result will be all the 'pedido' because the JPA will bring all
		 * 'pedidos' that have 'pagamento' and those that don't have 'pagamento*/		
		String jpql = "SELECT p FROM Pedido p LEFT JOIN p.pagamento pag on pag.status='PROCESSANDO'";
		
		TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);
		
		List<Pedido> lista = typedQuery.getResultList();
		
		System.out.println(lista.size());
		
		/* here JPA will consider just the 'pedidos' that have a 'pagamento' because I used the where clause
		 * so just the line where we have 'pedidos' and 'pagamento' related could evaluate the status=PROCESSANDO
		 * the other lines where 'pagamento' is null the comparation can be done, therefore the result is false*/
		
		String jpql2 = "SELECT p FROM Pedido p LEFT JOIN p.pagamento pag where pag.status='PROCESSANDO'";
		
		TypedQuery<Pedido> typedQuery2 = entityManager.createQuery(jpql2, Pedido.class);
		
		List<Pedido> lista2 = typedQuery2.getResultList();
		
		System.out.println(lista2.size());
		
	}
	
	@Test
	public void projetarPedidosDoCliente() {
		String jpql = "SELECT p FROM Cliente c INNER JOIN c.pedidos p where p.status= 'AGUARDANDO'";
		TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);
		
		List<Pedido> lista = typedQuery.getResultList();
		
		List<String> nomes = lista.stream().map(Pedido::getCliente)
						.map(c -> {return c.getNome();}).collect(Collectors.toList());
		
		nomes.forEach(System.out::println);
	}

	@Test
	public void projetarProdutoDTO() {
		
		String jpql = "SELECT new com.ejs.DTO.ProdutoDTO(id, nome) FROM Produto";
		
		TypedQuery<ProdutoDTO> typedQuery = entityManager.createQuery(jpql, ProdutoDTO.class);
		
		List<ProdutoDTO> lista = typedQuery.getResultList();
		
		lista.forEach(p -> System.out.println(p.getId() + "," + p.getNome()));
	}
	
	@Test
	public void projetarPedido() {
		String jpql = "SELECT p FROM Pedido p INNER JOIN p.itensPedido ip";
		
		TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);
		
		List<Pedido> lista = typedQuery.getResultList();
		
		for ( Pedido p : lista) {
			System.out.println(p.getId());
			
			for ( ItemPedido ip : p.getItensPedido()) {
				System.out.println("pedido id: "+ ip.getId().getPedidoId()+ "produto id: " + ip.getId().getProdutoId());
			}
		}
	}
	
}
