package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.RestaurantDetailsResponse;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @CrossOrigin
    @RequestMapping(path = "/restaurant", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<RestaurantDetailsResponse>> getAllRestaurants(){
        final List<RestaurantEntity> restaurantEntityList = restaurantService.getAllRestaurants();
        List<RestaurantDetailsResponse> restaurantDetailsResponsesList = new ArrayList<>();
        for(int i=0;i<restaurantEntityList.size();i++){
            RestaurantDetailsResponse restaurantDetailsResponse = new RestaurantDetailsResponse();
            RestaurantEntity restaurantEntityTmp = restaurantEntityList.get(i);
            restaurantDetailsResponse.id(UUID.fromString(restaurantEntityTmp.getUuid()));
            restaurantDetailsResponse.restaurantName(restaurantEntityTmp.getRestaurantName());
            restaurantDetailsResponse.photoURL(restaurantEntityTmp.getPhotoUrl());
            restaurantDetailsResponse.customerRating(restaurantEntityTmp.getCustomerRating());
            restaurantDetailsResponse.averagePrice(restaurantEntityTmp.getAveragePrice());
            restaurantDetailsResponse.numberCustomersRated(restaurantEntityTmp.getNoOfCustomerRated());
            restaurantDetailsResponsesList.add(restaurantDetailsResponse);
        }
        return new ResponseEntity<>(restaurantDetailsResponsesList,HttpStatus.OK);
    }


}
