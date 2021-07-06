package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 * This class provide the necessary methods to access the CustomerAuthEntity
 */
@Repository
public class CustomerAuthDao {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Save a Customer Auth Details in the Database
     *
     * @param customerAuthEntity Customer Auth Details to be Saved
     * @return Saved Customer Auth Details
     */
    public CustomerAuthEntity createCustomerAuth(CustomerAuthEntity customerAuthEntity) {
        entityManager.persist(customerAuthEntity);
        return customerAuthEntity;
    }

    /**
     * Update a Customer Auth Details in the Database
     *
     * @param customerAuthEntity Customer Auth Details to be Updated
     * @return Updated Customer Auth Details
     */
    public CustomerAuthEntity customerLogout(CustomerAuthEntity customerAuthEntity) {
        entityManager.merge(customerAuthEntity);
        return customerAuthEntity;
    }

    /**
     * Get the Customer Auth Details based on the AccessToken
     *
     * @param accessToken AccessToken
     * @return Customer Auth details matching the AccessToken or NULL
     */
    public CustomerAuthEntity getCustomerAuthByAccessToken(String accessToken) {
        try {
            CustomerAuthEntity customerAuthEntity = entityManager.createNamedQuery("getCustomerAuthByAccessToken", CustomerAuthEntity.class).setParameter("access_Token", accessToken).getSingleResult();
            return customerAuthEntity;
        } catch (NoResultException nre) {
            return null;
        }
    }
}