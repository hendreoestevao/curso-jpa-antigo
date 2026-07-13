package com.java360.cadastrocliente;

import com.java360.cadastrocliente.model.Cliente;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Exemplo {

    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory =
                Persistence.createEntityManagerFactory("Clientes-PU");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        //Busca
//        Cliente cliente = entityManager.find(Cliente.class, 1);

        //Inserção
//        Cliente cliente = new Cliente();
//        cliente.setNome("Autopeças Estradas sem id");
//
//        entityManager.getTransaction().begin();
//        entityManager.persist(cliente);
//        entityManager.getTransaction().commit();

        //Remoção
//        Cliente cliente = entityManager.find(Cliente.class, 3);
//        entityManager.getTransaction().begin();
//        entityManager.remove(cliente);
//        entityManager.getTransaction().commit();

        //Atualizacao
//        Cliente cliente = entityManager.find(Cliente.class, 1);
//        entityManager.getTransaction().begin();
//        cliente.setNome(cliente.getNome() + " Alterado");
//        entityManager.getTransaction().commit();

        //Atualização com ID
        Cliente cliente = new Cliente();
        cliente.setId(1);
        cliente.setNome("Armazem Estrela");

        entityManager.getTransaction().begin();
        entityManager.merge(cliente);
        entityManager.getTransaction().commit();


        entityManager.close();
        entityManagerFactory.close();
    }
}
