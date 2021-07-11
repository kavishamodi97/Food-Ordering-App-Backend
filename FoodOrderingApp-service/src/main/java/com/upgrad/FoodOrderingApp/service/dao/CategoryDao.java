package com.upgrad.FoodOrderingApp.service.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryDao {

    @PersistenceContext private EntityManager entityManager;

    /**
     * This method fetches CategoryEntity from database based Category UUID.
     *
     * @param categoryUuid
     * @return CategoryEntity or null if there is no category in database by given categoryUuid.
     */
    public CategoryEntity getCategoryByUuid(final String categoryUuid) {
        try {
            return entityManager
                    .createNamedQuery("categoryByUuid", CategoryEntity.class)
                    .setParameter("uuid", categoryUuid)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * This method fetches all CategoryEntity from db
     *
     * @param
     * @return List of categoryEntity
     */
    public List<CategoryEntity> getAllCategoriesOrderedByName() {

        return entityManager
                .createNamedQuery("getAllCategoriesOrderedByName", CategoryEntity.class)
                .getResultList();
    }

    /**
     * This method fetches all CategoryEntity from db for given restaurant
     *
     * @param restaurantUuid
     * @return List of categoryEntity
     */
    public List<CategoryEntity> getCategoriesByRestaurant(final String restaurantUuid) {
        try {
            return entityManager
                    .createNamedQuery("getCategoriesByRestaurant", CategoryEntity.class)
                    .setParameter("restaurantUuid", restaurantUuid)
                    .getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }
}