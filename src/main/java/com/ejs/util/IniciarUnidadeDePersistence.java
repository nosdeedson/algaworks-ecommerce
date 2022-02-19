package com.ejs.util;

import com.ejs.model.Produto;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class IniciarUnidadeDePersistence {
    public static void main(String[] args) {
        EntityManagerFactory entityManagetFactory = Persistence.createEntityManagerFactory("Ecommerce-PU");
        EntityManager entityManager = entityManagetFactory.createEntityManager();
        Produto produto = entityManager.find(Produto.class, 1);
        System.out.println(produto.getNome());
        entityManager.close();
        entityManagetFactory.close();
    }
}
