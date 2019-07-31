package com.tuan.exercise.sprdict.jpa.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class AccountDaoImpl implements AccountDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public String isAccountValid(String username, String password) {
        String role = null;

        String queryStr = "select acc.role from Account as acc "
                + "where acc.username = :username and acc.password = :password";
        TypedQuery<String> query = em.createQuery(queryStr, String.class)
                .setParameter("username", username)
                .setParameter("password", password);

        List<String> rows = query.getResultList();

        if (!rows.isEmpty()) {
            role = rows.get(0);
        }

        return role;
    }
}
