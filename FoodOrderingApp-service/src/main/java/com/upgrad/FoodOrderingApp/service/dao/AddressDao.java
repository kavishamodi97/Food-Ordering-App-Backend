package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;

/**
 * AddressDao class provides the database access for all the endpoints inside the address controller
 */
@Repository
public class AddressDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Creates the address entity using the given AddressEntity.
     *
     * @param addressEntity contains the address details.
     * @return AddressEntity object.
     */
    public AddressEntity createCustomerAddress(final AddressEntity addressEntity) {
        entityManager.persist(addressEntity);
        return addressEntity;
    }

    /**
     * This method fetches all the addresses of a given customer.
     *
     * @param customer whose detals to be fetched.
     * @return List of CustomerAddressEntity type object.
     */
    public List<CustomerAddressEntity> customerAddressByCustomer(CustomerEntity customer) {
        List<CustomerAddressEntity> addresses =
                entityManager
                        .createNamedQuery("customerAddressByCustomer", CustomerAddressEntity.class)
                        .setParameter("customer", customer)
                        .getResultList();
        if (addresses == null) {
            return Collections.emptyList();
        }
        return addresses;
    }

    /*
     * This method fetches the address from Database based on address UUID.
     *
     * @param addressUUID UUID of the address to be fetched.
     * @return AddressEntity
     */
    public AddressEntity getAddressByUUID(final String addressUUID) {
        try {
            return entityManager
                    .createNamedQuery("addressByUUID", AddressEntity.class)
                    .setParameter("addressUUID", addressUUID)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Deletes the given address entity.
     *
     * @param addressEntity Address to delete from database.
     * @return AddressEntity object.
     */
    public AddressEntity deleteAddress(final AddressEntity addressEntity) {
        entityManager.remove(addressEntity);
        return addressEntity;
    }

    /**
     * Updated the given address entity by setting active to 0 if orders placed with given address.
     *
     * @param addressEntity Address to update.
     * @return AddressEntity object.
     */
    public AddressEntity updateAddress(final AddressEntity addressEntity) {
        return entityManager.merge(addressEntity);
    }
}