package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.RestaurantDetailsResponse;
import com.upgrad.FoodOrderingApp.api.model.RestaurantDetailsResponseAddress;
import com.upgrad.FoodOrderingApp.api.model.RestaurantDetailsResponseAddressState;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


            RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress();
            AddressEntity addressEntity =  restaurantService.getRestaurantAddress(restaurantEntityTmp.getId());
            restaurantDetailsResponseAddress.city(addressEntity.getCity());
            restaurantDetailsResponseAddress.flatBuildingName(addressEntity.getFlatBuilNo());
            restaurantDetailsResponseAddress.id(UUID.fromString(addressEntity.getUuid()));
            restaurantDetailsResponseAddress.locality(addressEntity.getLocality());
            restaurantDetailsResponseAddress.pincode(addressEntity.getPincode());


            RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState();
            StateEntity stateEntity = addressEntity.getState();
            restaurantDetailsResponseAddressState.id(UUID.fromString(stateEntity.getUuid()));
            restaurantDetailsResponseAddressState.stateName(stateEntity.getStateName());
            restaurantDetailsResponseAddress.state(restaurantDetailsResponseAddressState);

            restaurantDetailsResponse.address(restaurantDetailsResponseAddress);

            restaurantDetailsResponsesList.add(restaurantDetailsResponse);
        }
        return new ResponseEntity<>(restaurantDetailsResponsesList,HttpStatus.OK);
    }



//
//    get restaurant by restaurant Names
//
//
//
//

    @CrossOrigin
    @RequestMapping(path = "/restaurant/name/{restaurant_name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<RestaurantDetailsResponse>>
                   getAllRestaurantsByName(@PathVariable("restaurant_name") final String RestaurantName){
        final List<RestaurantEntity> restaurantEntityList = restaurantService.getAllRestaurantsByName(RestaurantName);
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


            RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress();
            AddressEntity addressEntity =  restaurantService.getRestaurantAddress(restaurantEntityTmp.getId());
            restaurantDetailsResponseAddress.city(addressEntity.getCity());
            restaurantDetailsResponseAddress.flatBuildingName(addressEntity.getFlatBuilNo());
            restaurantDetailsResponseAddress.id(UUID.fromString(addressEntity.getUuid()));
            restaurantDetailsResponseAddress.locality(addressEntity.getLocality());
            restaurantDetailsResponseAddress.pincode(addressEntity.getPincode());


            RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState();
            StateEntity stateEntity = addressEntity.getState();
            restaurantDetailsResponseAddressState.id(UUID.fromString(stateEntity.getUuid()));
            restaurantDetailsResponseAddressState.stateName(stateEntity.getStateName());
            restaurantDetailsResponseAddress.state(restaurantDetailsResponseAddressState);

            restaurantDetailsResponse.address(restaurantDetailsResponseAddress);

            restaurantDetailsResponsesList.add(restaurantDetailsResponse);
        }
        return new ResponseEntity<>(restaurantDetailsResponsesList,HttpStatus.OK);
    }

//
//
//    Get all restaurants by restaurantId -> UUid
//
//
@CrossOrigin
@RequestMapping(path = "/api/restaurant/{restaurant_id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public ResponseEntity<RestaurantDetailsResponse>
getAllRestaurantsByUuid(@PathVariable("restaurant_id") final String restaurantId) throws RestaurantNotFoundException {
    final RestaurantEntity restaurantEntity = restaurantService.getRestaurantByUuid(restaurantId);
        RestaurantDetailsResponse restaurantDetailsResponse = new RestaurantDetailsResponse();
        restaurantDetailsResponse.id(UUID.fromString(restaurantEntity.getUuid()));
        restaurantDetailsResponse.restaurantName(restaurantEntity.getRestaurantName());
        restaurantDetailsResponse.photoURL(restaurantEntity.getPhotoUrl());
        restaurantDetailsResponse.customerRating(restaurantEntity.getCustomerRating());
        restaurantDetailsResponse.averagePrice(restaurantEntity.getAveragePrice());
        restaurantDetailsResponse.numberCustomersRated(restaurantEntity.getNoOfCustomerRated());


        RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress();
        AddressEntity addressEntity =  restaurantService.getRestaurantAddress(restaurantEntity.getId());
        restaurantDetailsResponseAddress.city(addressEntity.getCity());
        restaurantDetailsResponseAddress.flatBuildingName(addressEntity.getFlatBuilNo());
        restaurantDetailsResponseAddress.id(UUID.fromString(addressEntity.getUuid()));
        restaurantDetailsResponseAddress.locality(addressEntity.getLocality());
        restaurantDetailsResponseAddress.pincode(addressEntity.getPincode());


        RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState();
        StateEntity stateEntity = addressEntity.getState();
        restaurantDetailsResponseAddressState.id(UUID.fromString(stateEntity.getUuid()));
        restaurantDetailsResponseAddressState.stateName(stateEntity.getStateName());
        restaurantDetailsResponseAddress.state(restaurantDetailsResponseAddressState);

        restaurantDetailsResponse.address(restaurantDetailsResponseAddress);

    return new ResponseEntity<>(restaurantDetailsResponse,HttpStatus.OK);
}


}