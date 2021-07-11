package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 * This class provide the necessary methods to access the CustomerEntity
 */
@Repository
public class CustomerDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Save a Customer Details in the Database
     *
     * @param customerEntity Customer Details to be Saved
     * @return Saved Customer Details
     */
    public CustomerEntity createCustomer(CustomerEntity customerEntity) {
        entityManager.persist(customerEntity);
        return customerEntity;
    }

    /**
     * Update the Customer Details
     *
     * @param customerToBeUpdated Customer Details to be Updated
     * @return Updated Customer Details
     */
    public CustomerEntity updateCustomer(CustomerEntity customerToBeUpdated) {
        entityManager.merge(customerToBeUpdated);
        return customerToBeUpdated;
    }

    /**
     * Get the Customer Details based on the UUID
     *
     * @param uuid UUID of the Customer
     * @return Customer details matching the UUID or NULL
     */
    public CustomerEntity getCustomerByUuid(final String uuid) {
        try {
            CustomerEntity customer = entityManager.createNamedQuery("customerByUuid", CustomerEntity.class).setParameter("uuid", uuid).getSingleResult();
            return customer;
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Get the Customer Details based on the Contact Number
     *
     * @param contact_number Contact Number of the Customer
     * @return Customer details matching the Contact Number or NULL
     */
    public CustomerEntity getCustomerByContactNumber(final String contact_number) {
        try {
            CustomerEntity customer = entityManager.createNamedQuery("customerByContactNumber", CustomerEntity.class).setParameter("contact_number", contact_number).getSingleResult();
            return customer;
        } catch (NoResultException nre) {
            return null;
        }
    }
}