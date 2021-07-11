package com.upgrad.FoodOrderingApp.service.businness;

import java.util.List;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.InvalidRatingException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RestaurantService {

    @Autowired private RestaurantDao restaurantDao;

    /**
     * This method gets the restaurant details.
     *
     * @param uuid UUID of the restaurant.
     * @return
     * @throws RestaurantNotFoundException if restaurant with UUID doesn't exist in the database.
     */
    public RestaurantEntity restaurantByUUID(String uuid) throws RestaurantNotFoundException {
        RestaurantEntity restaurant = restaurantDao.restaurantByUUID(uuid);
        if (restaurant == null) {
            throw new RestaurantNotFoundException("RNF-001", "No restaurant by this id");
        }
        return restaurant;
    }

    /**
     * Gets all the restaurants in DB.
     *
     * @return List of RestaurantEntity
     */
    public List<RestaurantEntity> restaurantsByRating() {

        return restaurantDao.restaurantsByRating();
    }

    /**
     * Gets restaurants in DB based on search string.
     *
     * @return List of RestaurantEntity
     */
    public List<RestaurantEntity> restaurantsByName(final String search)
            throws RestaurantNotFoundException {
        if (search == null || search.isEmpty()) {
            throw new RestaurantNotFoundException("RNF-003", "Restaurant name field should not be empty");
        }

        List<RestaurantEntity> relevantRestaurantEntities = restaurantDao.restaurantsByName(search);

        return relevantRestaurantEntities;
    }

    /**
     * Gets all the restaurants in DB based on Category Uuid
     *
     * @return List of RestaurantEntity
     */
    public List<RestaurantEntity> restaurantByCategory(final String categoryUuid)
            throws CategoryNotFoundException {
        if (categoryUuid == null) {
            throw new CategoryNotFoundException("CNF-001", "Category id field should not be empty");
        }

        List<RestaurantEntity> restaurantEntities = restaurantDao.restaurantByCategory(categoryUuid);
        if (restaurantEntities == null) {
            throw new CategoryNotFoundException("CNF-002", "No category by this id");
        }

        return restaurantEntities;
    }

    /**
     * Updates the customer rating for a restaurant
     *
     * @param restaurantEntity Restaurant whose rating is to be done, customerRating as provided by
     *     customer
     * @return RestaurantEntity
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public RestaurantEntity updateRestaurantRating(
            final RestaurantEntity restaurantEntity, final Double customerRating)
            throws InvalidRatingException {
        if (Double.valueOf(customerRating) < 1 || Double.valueOf(customerRating) > 5) {
            throw new InvalidRatingException("IRE-001", "Restaurant should be in the range of 1 to 5");
        }

        Double currentRating = restaurantEntity.getCustomerRating();
        Integer numberCustomersRated = restaurantEntity.getNumberCustomersRated();

        Double newRating =
                ((currentRating * numberCustomersRated) + currentRating) / (numberCustomersRated + 1);

        restaurantEntity.setCustomerRating(newRating);
        restaurantEntity.setNumberCustomersRated(numberCustomersRated + 1);

        return restaurantDao.updateRestaurantEntity(restaurantEntity);
    }
}