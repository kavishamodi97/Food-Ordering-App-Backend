package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AddressDao {

    @PersistenceContext
    private EntityManager entityManager;

    public AddressEntity createCustomerAddress(final AddressEntity addressEntity) {
        entityManager.persist(addressEntity);
        return addressEntity;
    }

    public List<CustomerAddressEntity> getCustomerAddressByCustomer(CustomerEntity customer) {
        try {
            return entityManager.createNamedQuery("getCustomerAddressByCustomer", CustomerAddressEntity.class).setParameter("customer", customer).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public AddressEntity deleteAddress(AddressEntity addressEntity) {
        entityManager.remove(addressEntity);
        return addressEntity;
    }

    public AddressEntity getAddressByUuid(final String addressUuid) {
        try {
            return entityManager.createNamedQuery("getAddressByUuid", AddressEntity.class).setParameter("addressUuid", addressUuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
