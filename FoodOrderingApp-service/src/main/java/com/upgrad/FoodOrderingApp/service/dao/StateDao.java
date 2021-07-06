package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * StateDao class provides the database access for all the required endpoints inside the address
 * controller
 */
@Repository
public class StateDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * This method helps to fetch the existing State by using StateUUID.
     *
     * @param uuid the state UUID which will be searched in database to find existing state.
     * @return StateEntity object if given state exists in database.
     */
    public StateEntity getStateByUuid(final String uuid) {
        try {
            return entityManager.createNamedQuery("StateByUuid", StateEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * This method fetch all states from database.
     *
     * @return List<StateEntity> object.
     */
    public List<StateEntity> getAllStates() {
        try {
            return entityManager.createNamedQuery("getAllStates", StateEntity.class).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }
}

