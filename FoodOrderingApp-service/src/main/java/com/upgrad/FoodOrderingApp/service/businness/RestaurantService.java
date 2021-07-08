package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantCategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.InvalidRatingException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantDao restaurantDao;

    @Autowired
    private AddressDao addressDao;

    @Autowired
    private RestaurantCategoryDao restaurantCategoryDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public List<RestaurantEntity> getAllRestaurants(){
        return restaurantDao.getAllRestaurants();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity getRestaurantAddress(Integer restaurantId) {

        return addressDao.getAddressByRestaurantId(restaurantId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<RestaurantEntity> getAllRestaurantsByName(String restaurantName) {
        return restaurantDao.getAllRestaurantsByName(restaurantName);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public RestaurantEntity getRestaurantByUuid(String restaurantUuid) throws RestaurantNotFoundException {
        if(restaurantUuid.isEmpty())
            throw new RestaurantNotFoundException("RNF-002","Restaurant id field should not be empty");
        RestaurantEntity restaurantEntity = restaurantDao.getRestaurantByUuid(restaurantUuid);
        if(restaurantEntity==null)
            throw new RestaurantNotFoundException("RNF-001","No restaurant by this id");
        return restaurantEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<RestaurantCategoryEntity> getCategories(RestaurantEntity restaurantEntity) {
       return restaurantCategoryDao.getRestaurantCategories(restaurantEntity.getId());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<RestaurantEntity> getRestaurantByCategory(String categoryId) throws CategoryNotFoundException {
        if(categoryId.isEmpty())
            throw new CategoryNotFoundException("CNF-001","Category id field should not be empty");
        List<RestaurantCategoryEntity> restaurantCategoryEntities = restaurantCategoryDao.getRestaurantByCategoryUuid(categoryId);
        if(restaurantCategoryEntities.size()==0)
            throw new CategoryNotFoundException("CNF-002","No category by this id");
        List<RestaurantEntity> restaurantEntities = new ArrayList<>();
        for(int i=0;i<restaurantCategoryEntities.size();i++){
            restaurantEntities.add(restaurantCategoryEntities.get(i).getRestaurantEntity());
        }
        return restaurantEntities;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public RestaurantEntity editRestaurantEntity(String restaurantUuid,
                                                 Double customerRating,
                                                 String accessToken)
            throws AuthorizationFailedException, InvalidRatingException, RestaurantNotFoundException {

//        CustomerService customerService = new CustomerService();
//        CustomerEntity customerEntity = customerService.getCustomer(accessToken);

        RestaurantEntity restaurantEntity = this.getRestaurantByUuid(restaurantUuid);

        if(customerRating<1 || customerRating>5)
            throw new InvalidRatingException("IRE-001","Restaurant should be in the range of 1 to 5");

        Double noOfCustomerRated = restaurantEntity.getNoOfCustomerRated().doubleValue();
        Double restaurantRating =  restaurantEntity.getCustomerRating().doubleValue();
        restaurantRating=restaurantRating*noOfCustomerRated;
        noOfCustomerRated = noOfCustomerRated+1;
        restaurantRating = (restaurantRating+customerRating) / noOfCustomerRated ;
        restaurantEntity.setNoOfCustomerRated(noOfCustomerRated.intValue());

        restaurantEntity.setCustomerRating(BigDecimal.valueOf(restaurantRating).setScale(2,RoundingMode.HALF_UP));

        return restaurantDao.editRestaurant(restaurantEntity);


    }
}
