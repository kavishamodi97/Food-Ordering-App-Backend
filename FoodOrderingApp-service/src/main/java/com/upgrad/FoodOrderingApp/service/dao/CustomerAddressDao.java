package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class CustomerAddressDao {

    @PersistenceContext
    private EntityManager entityManager;

    public CustomerAddressEntity createCustomerAddress(final CustomerAddressEntity customerAddressEntity) {
        entityManager.persist(customerAddressEntity);
        return customerAddressEntity;
    }

    public CustomerAddressEntity customerAddressByAddress(final AddressEntity address) {
        try {

            return entityManager.createNamedQuery("getCustomerAddressByAddress", CustomerAddressEntity.class).setParameter("address", address).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
