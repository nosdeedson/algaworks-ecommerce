package com.ejs.iniciandoJPQL;

import java.util.Date;
import java.util.List;

import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.junit.Assert;
import org.junit.Test;

import com.ejs.entityManager.EntityManagerTest;
import com.ejs.model.NotaFiscal;

public class NotaFiscalJPQLTest extends EntityManagerTest{

	@Test
	public void findByDataEmissao() {
		String jpql = "SELECT nf FROM NotaFiscal nf WHERE dataEmissao <= :dataEmissao";
		
		TypedQuery<NotaFiscal> typedQuery = entityManager.createQuery(jpql, NotaFiscal.class);
		
		typedQuery.setParameter("dataEmissao", new Date(), TemporalType.TIMESTAMP);
		
		List<NotaFiscal> lista = typedQuery.getResultList();
		
		Assert.assertTrue(lista.size() == 1);
	}
}
