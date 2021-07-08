package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import org.springframework.stereotype.Repository;


import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RestaurantCategoryDao {
 @PersistenceContext
    private EntityManager entityManager;

    public List<RestaurantCategoryEntity> getRestaurantCategories(Integer restaurantId) {
        try {
            return entityManager.createNamedQuery("getRestaurantCategories", RestaurantCategoryEntity.class).setParameter("restaurantId", restaurantId).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }
}