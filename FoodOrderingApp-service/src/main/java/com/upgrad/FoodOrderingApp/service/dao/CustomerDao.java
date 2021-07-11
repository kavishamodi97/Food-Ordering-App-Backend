package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/** This class provides the database access for all the endpoints in the customer controller. */
@Repository
public class CustomerDao {

    @PersistenceContext private EntityManager entityManager;

    /**
     * This method saves the details of the new customer in database.
     *
     * @param customerEntity for creating new customer.
     * @return CustomerEntity object.
     */
    public CustomerEntity saveCustomer(final CustomerEntity customerEntity) {
        entityManager.persist(customerEntity);
        return customerEntity;
    }

    /**
     * This method helps finds the customer by using contact number.
     *
     * @param contactNumber to find the customer is already registered with this number
     * @return CustomerEntity if the contact number exists in the database
     */
    public CustomerEntity getCustomerByContactNumber(final String contactNumber) {
        try {
            return entityManager
                    .createNamedQuery("customerByContactNumber", CustomerEntity.class)
                    .setParameter("contactNumber", contactNumber)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * This method updates the customer details in the database.
     *
     * @param customerEntity CustomerEntity object to update.
     * @return Updated CustomerEntity object.
     */
    public CustomerEntity updateCustomer(final CustomerEntity customerEntity) {
        entityManager.merge(customerEntity);
        return customerEntity;
    }
}