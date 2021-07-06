package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 * CustomerAddressDao class provides the database access for all the required endpoints inside the
 * customer and address controllers.
 */
@Repository
public class CustomerAddressDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Creates mapping between the customer and the address entity.
     *
     * @param customerAddressEntity Customer and the address to map.
     * @return CustomerAddressEntity object.
     */
    public CustomerAddressEntity createCustomerAddress(final CustomerAddressEntity customerAddressEntity) {
        entityManager.persist(customerAddressEntity);
        return customerAddressEntity;
    }

    /**
     * fetches the address of a customer using given address.
     *
     * @param address address to fetch.
     * @return CustomerAddressEntity type object.
     */
    public CustomerAddressEntity customerAddressByAddress(final AddressEntity address) {
        try {

            return entityManager.createNamedQuery("getCustomerAddressByAddress", CustomerAddressEntity.class).setParameter("address", address).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
