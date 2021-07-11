package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class CustomerAuthDao {

    @PersistenceContext private EntityManager entityManager;

    /**
     * This method stores authorization access token in the database
     *
     * @param customerAuthEntity the CustomerAuthEntity object from which new authorization will be
     *     created
     */
    public void createCustomerAuthToken(CustomerAuthEntity customerAuthEntity) {
        entityManager.persist(customerAuthEntity);
    }

    /**
     *
     * @param accessToken access-token obtained during successful login.
     * @return CustomerAuthEntity or null of token not found in database. This method helps to find
     *     the customer using the access token. ======= This method helps to find the customer using
     *     the access token.
     *     <p>>>>>>>> Closes #8 added code for /address/{address_id} end point api to delete the
     *     address of a customer if no orders placed using the given address
     * @param accessToken the access token which will be searched in database to find the customer.
     * @return CustomerAuthEntity object if given access token exists in the database.
     */
    public CustomerAuthEntity getCustomerAuthByToken(final String accessToken) {
        try {
            return entityManager
                    .createNamedQuery("customerAuthByToken", CustomerAuthEntity.class)
                    .setParameter("accessToken", accessToken)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * This method updates the customers logout time in the database.
     *
     * @param updatedCustomerAuthEntity CustomerAuthEntity object to update.
     */
    public void updateCustomerAuth(final CustomerAuthEntity updatedCustomerAuthEntity) {
        entityManager.merge(updatedCustomerAuthEntity);
    }
}