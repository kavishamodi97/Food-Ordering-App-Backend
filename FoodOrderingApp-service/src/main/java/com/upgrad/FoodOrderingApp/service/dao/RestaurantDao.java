package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RestaurantDao {
    @PersistenceContext
    private EntityManager entityManager;

    public List<RestaurantEntity> getAllRestaurants(){
        return entityManager.createNamedQuery("getAllRestaurants",RestaurantEntity.class).getResultList();
    }

    public List<RestaurantEntity> getAllRestaurantsByName(String restaurantName) {
        return entityManager.createNamedQuery("getAllRestaurantsByName",RestaurantEntity.class)
                .setParameter("restaurantName",restaurantName).getResultList();
    }
}
