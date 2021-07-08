package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * AddressDao class provides the database access for all the endpoints inside the address controller
 */
@Repository
public class AddressDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Creates the address entity using AddressEntity.
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
     * @param customer whose details  to be fetched.
     * @return List of CustomerAddressEntity type object.
     */
    public List<CustomerAddressEntity> getCustomerAddressByCustomer(CustomerEntity customer) {
        try {
            return entityManager.createNamedQuery("getCustomerAddressByCustomer", CustomerAddressEntity.class).setParameter("customer", customer).getResultList();
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
    public AddressEntity deleteAddress(AddressEntity addressEntity) {
        entityManager.remove(addressEntity);
        return addressEntity;
    }

    /*
     * This method fetches the address from Database based on address UUID.
     *
     * @param addressUUID UUID of the address to be fetched.
     * @return AddressEntity
     */
    public AddressEntity getAddressByUuid(final String addressUuid) {
        try {
            return entityManager.createNamedQuery("getAddressByUuid", AddressEntity.class).setParameter("addressUuid", addressUuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public AddressEntity getAddressByRestaurantId(Integer restaurantId) {
        try {
            return entityManager.createNamedQuery("getRestaurantAddressById", AddressEntity.class).setParameter("restaurantId", restaurantId).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
