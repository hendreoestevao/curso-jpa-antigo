package com.algaworks.sistemausuarios;

import com.algaworks.sistemausuarios.dto.UsuarioDTO;
import com.algaworks.sistemausuarios.model.Dominio;
import com.algaworks.sistemausuarios.model.Usuario;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class ConsultasComJPQL {

    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence
                .createEntityManagerFactory("Usuarios-PU");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        // primeiraConsulta(entityManager);
        // escolhendoORetorno(entityManager);
        //fazendoProjecoes(entityManager);
        passandoParametros(entityManager);

        entityManager.close();
        entityManagerFactory.close();
    }

    private static void passandoParametros(EntityManager entityManager) {
        String jpql = "select u from Usuario u where u.id = :id";
        TypedQuery<Usuario> query = entityManager.createQuery(jpql, Usuario.class);
        query.setParameter("id", 1);
        Usuario usuario = query.getSingleResult();
        System.out.println(usuario.getId() + " , " + usuario.getNome());
    }


    public static void fazendoProjecoes(EntityManager entityManager) {
        String jpqlArr = "select id, login, nome from Usuario";
        TypedQuery<Object[]> typedQueryArr = entityManager.createQuery(jpqlArr, Object[].class);
        List<Object[]> resultList = typedQueryArr.getResultList();
        resultList.forEach(obj -> System.out.printf("%s, %s, %s%n", obj));

        String jpqlDto = "select new com.algaworks.sistemausuarios.dto.UsuarioDTO(id, login, nome) " +
                "from Usuario";
        TypedQuery<UsuarioDTO> typedQueryDto = entityManager.createQuery(jpqlDto, UsuarioDTO.class);
        List<UsuarioDTO> resultListDto = typedQueryDto.getResultList();
        resultListDto.forEach(u -> System.out.println("DTO: ID: " + u.getId() + " Login: " + u.getLogin() + " Nome: " + u.getNome()));

    }


    private static void escolhendoORetorno(EntityManager entityManager) {
        String jpql = "select u.dominio from Usuario u where u.id = 2";
        TypedQuery<Dominio> typedQuery = entityManager.createQuery(jpql, Dominio.class);
        Dominio dominio = typedQuery.getSingleResult();
        System.out.println(dominio.getId() + " , " + dominio.getNome());

        String jpqlNom = "select u.nome from Usuario u";
        TypedQuery<String> typedQueryNom = entityManager.createQuery(jpqlNom, String.class);
        List<String> listaNom = typedQueryNom.getResultList();
        listaNom.forEach(System.out::println);
    }


    public static void primeiraConsulta(EntityManager entityManager) {

        String jpql = "select u from Usuario u";
        TypedQuery<Usuario> typedQuery = entityManager.createQuery(jpql, Usuario.class);
        List<Usuario> lista = typedQuery.getResultList();
        lista.forEach(u -> System.out.println(u.getId() + " , " + u.getNome()));

        String jpqlSingle = "select u from Usuario u where u.id = 1";
        TypedQuery<Usuario> querySingle = entityManager.createQuery(jpqlSingle, Usuario.class);
        Usuario usuario = querySingle.getSingleResult();
        System.out.println(usuario.getId() + " , " + usuario.getNome());

        String jpqlQueryCast = "select u from Usuario u where u.id = 1";
        Query query = entityManager.createQuery(jpqlQueryCast);
        Usuario usuario2 = (Usuario) query.getSingleResult();
        System.out.println(usuario2.getId() + " , " + usuario2.getNome());

    }
}
