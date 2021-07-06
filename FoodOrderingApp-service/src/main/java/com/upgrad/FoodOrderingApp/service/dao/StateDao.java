package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class StateDao {

    @PersistenceContext
    private EntityManager entityManager;

    public StateEntity getStateByUuid(final String uuid) {
        try {
            return entityManager.createNamedQuery("StateByUuid", StateEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public List<StateEntity> getAllStates() {
        try {
            return entityManager.createNamedQuery("getAllStates", StateEntity.class).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }
}

