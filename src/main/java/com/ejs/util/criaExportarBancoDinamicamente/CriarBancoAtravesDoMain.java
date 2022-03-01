package com.ejs.util.criaExportarBancoDinamicamente;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class CriarBancoAtravesDoMain {
	
	public static void main(String[] args) {
		
		/*Esta classe passa para o entityManagerFactory as propriedades necessárias para criação do banco de dados 
		 * não precisa retirá-las do persistence.xml, o atributo (propriedades do tipo Map) tem prioridade sobre 
		 * as propriedades do arquivo*/
		
		Map<String, String> propriedades = new HashMap<>();

		propriedades.put("javax.persistence.schema-generation.database.action", "drop-and-create");

		propriedades.put("javax.persistence.schema-generation.create-source", "metadata-then-script");
		propriedades.put("javax.persistence.schema-generation.drop-source", "metadata-then-script");

		propriedades.put("javax.persistence.schema-generation.create-script-source",
				"META-INF/banco-de-dados/script-criacao.sql");
		propriedades.put("javax.persistence.schema-generation.drop-script-source",
				"META-INF/banco-de-dados/script-remocao.sql");

		propriedades.put("javax.persistence.sql-load-script-source", "META-INF/banco-de-dados/dados-iniciais.sql");

		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("Ecommerce-PU",
				propriedades);

		entityManagerFactory.close();
	}

}
