package com.algaworks.sistemausuarios;

import com.algaworks.sistemausuarios.dto.UsuarioDTO;
import com.algaworks.sistemausuarios.model.Configuracao;
import com.algaworks.sistemausuarios.model.Usuario;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class ConsultaComCriteria {

    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence
                .createEntityManagerFactory("Usuarios-PU");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        //primeiraConsulta(entityManager);
        //escolhendoORetorno(entityManager);
        //fazendoProjecoes(entityManager);
        //passandoParametros(entityManager);
        // fazendoJoins(entityManager);
        // fazendoJoins(entityManager);
        // fazendoLeftJoin(entityManager);
        // carregamentoComJoinFetch(entityManager);
        // filtrandoRegistros(entityManager);
        // utilizandoOperadoresLogicos(entityManager);
        // ultilizandoOperadorIn(entityManager);
            ordenandoResultados(entityManager);
          //  paginandoResultados(entityManager);

        entityManager.close();
        entityManagerFactory.close();
    }

    private static void paginandoResultados(EntityManager entityManager) {
        String jpql = "select u from Usuario u";
        TypedQuery<Usuario> query = entityManager.createQuery(jpql, Usuario.class);
        query.setFirstResult(0).setMaxResults(2);
        List<Usuario> usuarios = query.getResultList();
        usuarios.forEach(u -> System.out.println(u.getId() + " " + u.getNome()));
    }

    private static void ordenandoResultados(EntityManager entityManager) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Usuario> criteriaQuery = criteriaBuilder.createQuery(Usuario.class);
        Root<Usuario> root = criteriaQuery.from(Usuario.class);

        criteriaQuery.select(root);
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get("nome")));

        TypedQuery<Usuario> query = entityManager.createQuery(criteriaQuery);
        List<Usuario> usuarios = query.getResultList();
        usuarios.forEach(u -> System.out.println(u.getId() + " " + u.getNome()));

//        String jpql = "select u from Usuario u order by u.nome";
//        TypedQuery<Usuario> query = entityManager.createQuery(jpql, Usuario.class);
//        List<Usuario> usuarios = query.getResultList();
//        usuarios.forEach(u -> System.out.println(u.getId() + " " + u.getNome()));
    }

    private static void ultilizandoOperadorIn(EntityManager entityManager) {
        String jpql = "select u from Usuario u where u.id in (:ids)";
        TypedQuery<Usuario> query = entityManager.createQuery(jpql, Usuario.class);
        query.setParameter("ids", Arrays.asList(1, 2));
        List<Usuario> usuarios = query.getResultList();
        usuarios.forEach(u -> System.out.println(u.getId() + " " + u.getNome()));
    }

    private static void utilizandoOperadoresLogicos(EntityManager entityManager) {
        String jpql = "select u from Usuario u where u.ultimoAcesso > :ontem and u.ultimoAcesso < :hoje" +
                " or u.ultimoAcesso is null";
        TypedQuery<Usuario> query = entityManager.createQuery(jpql, Usuario.class);
        query.setParameter("ontem", LocalDateTime.now().minusDays(1));
        query.setParameter("hoje", LocalDateTime.now());
        List<Usuario> usuarios = query.getResultList();
        usuarios.forEach(u -> System.out.println(u.getId() + " " + u.getNome()));
    }

    private static void filtrandoRegistros(EntityManager entityManager) {
        // LIKE, IS NULL, IS EMPTY, BETWEEN, >, <, >=, <=, =, <>
        // LIKE select u from Usuario u where u.nome like :nome
        // IS NULL = select u from Usuario u where u.senha is null
        // IS EMPTY = select d from Dominio d where d.usuarios is empty
        // BETWEEN

        String jpql = "select u from Usuario u where u.ultimoAcesso between :ontem and :hoje";
        // OU POSSO FAZER ASSIM E NAO PRECISA PASSAR O '%' NO PARAMETER
        // String jpql = "select u from Usuario u where u.nome like concat (:nome, '%')";

        TypedQuery<Usuario> query = entityManager.createQuery(jpql, Usuario.class);

        //  query.setParameter("nome", "Cal%");
        query.setParameter("ontem", LocalDateTime.now().minusDays(1));
        query.setParameter("hoje", LocalDateTime.now());

        List<Usuario> usuarios = query.getResultList();
        usuarios.forEach(u -> System.out.println(u.getId() + " " + u.getNome()));
    }

    private static void carregamentoComJoinFetch(EntityManager entityManager) {
        String jpql = "select u from Usuario u join fetch u.configuracao join fetch u.dominio d";
        TypedQuery<Usuario> query = entityManager.createQuery(jpql, Usuario.class);
        List<Usuario> usuarios = query.getResultList();
        usuarios.forEach(u -> System.out.println(u.getId() + " " + u.getNome()));
    }

    public static void fazendoLeftJoin(EntityManager entityManager) {
        String jpql = "select u, c from Usuario u left join u.configuracao c";
        TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);
        List<Object[]> resultList = query.getResultList();
        resultList.forEach(obj -> {
            String out = ((Usuario) obj[0]).getNome();
            if (obj[1] == null) {
                out += ", Null";
            } else {
                out += ", " + ((Configuracao) obj[1]).getId();
            }

            System.out.println(out);
        });
    }

    public static void fazendoJoins(EntityManager entityManager) {
        String jpql = "select u from Usuario u join u.dominio d where d.id = 1";
        TypedQuery<Usuario> query = entityManager.createQuery(jpql, Usuario.class);
        List<Usuario> usuarios = query.getResultList();
        usuarios.forEach(u -> System.out.println(u.getId() + " " + u.getNome()));
    }

    private static void passandoParametros(EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Usuario> criteriaQuery = criteriaBuilder.createQuery(Usuario.class);
        Root<Usuario> root = criteriaQuery.from(Usuario.class);

        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.equal(root.get("id"), 1));

        TypedQuery<Usuario> typedQuery = entityManager.createQuery(criteriaQuery);
        Usuario usuarios = typedQuery.getSingleResult();
        System.out.println(usuarios.getId() + " " + usuarios.getNome());

//        String jpql = "select u from Usuario u where u.id = :id";
//        TypedQuery<Usuario> query = entityManager.createQuery(jpql, Usuario.class);
//        query.setParameter("id", 1);
//        Usuario usuario = query.getSingleResult();
//        System.out.println(usuario.getId() + " , " + usuario.getNome());
    }


    public static void fazendoProjecoes(EntityManager entityManager) {

//        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//
//        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
//        Root<Usuario> root = criteriaQuery.from(Usuario.class);
//
//        criteriaQuery.multiselect(root.get("id"), root.get("login"), root.get("nome"));
//
//        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);
//        List<Object[]> usuarios = typedQuery.getResultList();
//        usuarios.forEach(obj -> System.out.printf("%s, %s, %s%n", obj));


        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<UsuarioDTO> criteriaQuery = criteriaBuilder.createQuery(UsuarioDTO.class);
        Root<Usuario> root = criteriaQuery.from(Usuario.class);

        criteriaQuery.select(criteriaBuilder.construct(UsuarioDTO.class, root.get("id"), root.get("login"), root.get("nome")));

        TypedQuery<UsuarioDTO> typedQuery = entityManager.createQuery(criteriaQuery);
        List<UsuarioDTO> resultListDto = typedQuery.getResultList();
        resultListDto.forEach(u -> System.out.println("DTO: ID: " + u.getId() + " Login: " + u.getLogin() + " Nome: " + u.getNome()));



        /*
        String jpqlArr = "select id, login, nome from Usuario";
        TypedQuery<Object[]> typedQueryArr = entityManager.createQuery(jpqlArr, Object[].class);
        List<Object[]> resultList = typedQueryArr.getResultList();
        resultList.forEach(obj -> System.out.printf("%s, %s, %s%n", obj));

        String jpqlDto = "select new com.algaworks.sistemausuarios.dto.UsuarioDTO(id, login, nome) " +
                "from Usuario";
        TypedQuery<UsuarioDTO> typedQueryDto = entityManager.createQuery(jpqlDto, UsuarioDTO.class);
        List<UsuarioDTO> resultListDto = typedQueryDto.getResultList();
        resultListDto.forEach(u -> System.out.println("DTO: ID: " + u.getId() + " Login: " + u.getLogin() + " Nome: " + u.getNome()));
        */
    }


    private static void escolhendoORetorno(EntityManager entityManager) {

         /*
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Dominio> criteriaQuery = criteriaBuilder.createQuery(Dominio.class);
        Root<Usuario> root = criteriaQuery.from(Usuario.class);

        criteriaQuery.select(root.get("dominio"));

        TypedQuery<Dominio> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Dominio> usuarios = typedQuery.getResultList();
        usuarios.forEach(d -> System.out.println(d.getId() + " , " + d.getNome()));


        String jpql = "select u.dominio from Usuario u where u.id = 2";
        TypedQuery<Dominio> typedQuery = entityManager.createQuery(jpql, Dominio.class);
        Dominio dominio = typedQuery.getSingleResult();
        System.out.println(dominio.getId() + " , " + dominio.getNome());
        */

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> criteriaQuery = criteriaBuilder.createQuery(String.class);
        Root<Usuario> root = criteriaQuery.from(Usuario.class);

        criteriaQuery.select(root.get("nome"));

        TypedQuery<String> typedQuery = entityManager.createQuery(criteriaQuery);
        List<String> resultList = typedQuery.getResultList();
        resultList.forEach(u -> System.out.println("Nome: " + u));

        /*
        String jpqlNom = "select u.nome from Usuario u";
        TypedQuery<String> typedQueryNom = entityManager.createQuery(jpqlNom, String.class);
        List<String> listaNom = typedQueryNom.getResultList();
        listaNom.forEach(System.out::println);
        */

    }


    public static void primeiraConsulta(EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Usuario> criteriaQuery = criteriaBuilder.createQuery(Usuario.class);
        Root<Usuario> root = criteriaQuery.from(Usuario.class);

        criteriaQuery.select(root);

        TypedQuery<Usuario> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Usuario> usuarios = typedQuery.getResultList();
        usuarios.forEach(u -> System.out.println(u.getId() + " , " + u.getNome()));

    }
}
