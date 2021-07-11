package com.upgrad.FoodOrderingApp.service.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

@Repository
public class ItemDao {

    @PersistenceContext private EntityManager entityManager;

    /**
     * This method gets top five popular items of a restaurant.
     *
     * @param restaurant Restaurant whose top five items are to be queried.
     * @return top five items
     */
    public List<ItemEntity> getOrdersByRestaurant(RestaurantEntity restaurant) {
        List<ItemEntity> items =
                entityManager
                        .createNamedQuery("topFivePopularItemsByRestaurant", ItemEntity.class)
                        .setParameter(0, restaurant.getId())
                        .getResultList();
        if (items != null) {
            return items;
        }
        return Collections.emptyList();
    }

    /**
     * Fetch the item based on UUID.
     *
     * @param itemUUID UUID of the item to be fetched.
     * @return ItemEntity if found in database else null
     */
    public ItemEntity getItemByUUID(String itemUUID) {
        try {
            ItemEntity item =
                    entityManager
                            .createNamedQuery("itemByUUID", ItemEntity.class)
                            .setParameter("itemUUID", itemUUID)
                            .getSingleResult();
            return item;
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * This method gets Items for a given category in a restaurant
     *
     * @param restaurantUuid Restaurant whose items are to be queried, categoryUuid Category to be
     *     queried. * @return List of ItemEntity
     * @return List of ItemEntity
     */
    public List<ItemEntity> getAllItemsInCategoryInRestaurant(
            final String restaurantUuid, final String categoryUuid) {
        List<ItemEntity> items =
                entityManager
                        .createNamedQuery("getAllItemsInCategoryInRestaurant", ItemEntity.class)
                        .setParameter("restaurantUuid", restaurantUuid)
                        .setParameter("categoryUuid", categoryUuid)
                        .getResultList();
        return items;
    }
}