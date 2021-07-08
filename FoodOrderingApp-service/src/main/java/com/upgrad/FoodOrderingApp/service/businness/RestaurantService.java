package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantDao restaurantDao;

    @Autowired
    private AddressDao addressDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public List<RestaurantEntity> getAllRestaurants(){
        return restaurantDao.getAllRestaurants();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity getRestaurantAddress(Integer restaurantId) {

        return addressDao.getAddressByRestaurantId(restaurantId);
    }

    public List<RestaurantEntity> getAllRestaurantsByName(String restaurantName) {
        return restaurantDao.getAllRestaurantsByName(restaurantName);
    }
}
