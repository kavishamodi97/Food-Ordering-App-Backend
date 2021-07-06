package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.AddressService;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.common.UtilityProvider;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Address Controller to Handle all Address Related Endpoints
 */
@RestController
@RequestMapping("/")
public class AddressController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UtilityProvider utilityProvider;

    @Autowired
    private AddressService addressService;

    /**
     * This endpoint is used to save address of a customer in the 'restaurantdb' database.
     *
     * @param authorization customer login access token in 'Bearer <access-token>' format.
     * @return ResponseEntity<SaveAddressResponse> type object along with HttpStatus as Ok.
     * @throws AuthorizationFailedException if any of the validation on customer access token fails.
     */
    @CrossOrigin
    @RequestMapping(path = "address", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveAddressResponse> saveAddress(@RequestBody(required = false) final SaveAddressRequest saveAddressRequest, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, AddressNotFoundException, SaveAddressException {

        final String accessToken = utilityProvider.getAccessTokenFromAuthorization(authorization);
        CustomerEntity customerEntity = customerService.getCustomer(accessToken);
        AddressEntity addressEntity = new AddressEntity();

        if (saveAddressRequest != null) {
            addressEntity.setUuid(UUID.randomUUID().toString());
            addressEntity.setCity(saveAddressRequest.getCity());
            addressEntity.setFlatBuilNo(saveAddressRequest.getFlatBuildingName());
            addressEntity.setLocality(saveAddressRequest.getLocality());
            addressEntity.setPincode(saveAddressRequest.getPincode());
            addressEntity.setActive(1);
        }

        StateEntity stateEntity = addressService.getStateByUUID(saveAddressRequest.getStateUuid());
        addressEntity.setState(stateEntity);

        final AddressEntity saveAddress = addressService.saveAddress(addressEntity, customerEntity);

        SaveAddressResponse saveAddressResponse =
                new SaveAddressResponse()
                        .id(saveAddress.getUuid())
                        .status("ADDRESS SUCCESSFULLY REGISTERED");

        return new ResponseEntity<SaveAddressResponse>(saveAddressResponse, HttpStatus.CREATED);
    }

    /**
     * This endpoint is used retrieves all the saved addresses of a customer from 'restaurantdb' database.
     *
     * @param authorization customer login access token in 'Bearer <access-token>' format.
     * @return ResponseEntity<AddressListResponse> type object along with HttpStatus as OK.
     */
    @CrossOrigin
    @RequestMapping(path = "address/customer", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AddressListResponse> getAllSavedAddresses(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {
        final AddressListResponse addressListResponse = new AddressListResponse();
        final String accessToken = utilityProvider.getAccessTokenFromAuthorization(authorization);
        CustomerEntity customerEntity = customerService.getCustomer(accessToken);
        final List<AddressEntity> addressEntityList = addressService.getAllAddress(customerEntity);

        if (!addressEntityList.isEmpty()) {
            for (AddressEntity addressEntity : addressEntityList) {
                AddressList addressResponseList = new AddressList().id(UUID.fromString(addressEntity.getUuid()))
                        .flatBuildingName(addressEntity.getFlatBuilNo())
                        .city(addressEntity.getCity()).pincode(addressEntity.getPincode())
                        .locality(addressEntity.getLocality())
                        .state(new AddressListState().id(UUID.fromString(addressEntity.getState().getUuid())).stateName(addressEntity.getState().getStateName()));
                addressListResponse.addAddressesItem(addressResponseList);
            }
        } else {
            List<AddressList> addresses = Collections.emptyList();
            addressListResponse.addresses(addresses);
        }
        return new ResponseEntity<AddressListResponse>(addressListResponse, HttpStatus.OK);
    }

    /**
     * This endpoint is used to delete customer address from 'restaurantdb' database
     * address.
     *
     * @param addressId     Address uuid is used to fetch the correct address.
     * @param authorization customer login access token in 'Bearer <access-token>' format.
     * @return ResponseEntity<DeleteAddResponse> with HttpStatus as OK
     */
    @CrossOrigin
    @RequestMapping(path = "address/{address_id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<DeleteAddressResponse> deleteSavedAddress(@PathVariable("address_id") final String addressId, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, AddressNotFoundException {

        final String accessToken = utilityProvider.getAccessTokenFromAuthorization(authorization);
        CustomerEntity customerEntity = customerService.getCustomer(accessToken);
        final AddressEntity addressEntity = addressService.getAddressByUUID(addressId, customerEntity);
        final AddressEntity deletedAddressEntity = new AddressEntity();
        deletedAddressEntity.setUuid(UUID.randomUUID().toString());
        final AddressEntity deleteAddress = addressService.deleteAddress(addressEntity);
        DeleteAddressResponse deleteAddressResponse = new DeleteAddressResponse().id(UUID.fromString(deleteAddress.getUuid())).status("ADDRESS DELETED SUCCESSFULLY");
        return new ResponseEntity<DeleteAddressResponse>(deleteAddressResponse, HttpStatus.OK);
    }

    /**
     * This endpoint is used retrieve all the states from 'restaurantdb' the database.
     *
     * @return ResponseEntity<StatesListResponse> type object along with HttpStatus as OK.
     */
    @CrossOrigin
    @RequestMapping(path = "states", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<StatesListResponse> getAllStates() {

        final StateEntity stateEntity = new StateEntity();
        final StatesListResponse statesListResponse = new StatesListResponse();
        stateEntity.setUuid(UUID.randomUUID().toString());
        final List<StateEntity> statesLists = addressService.getAllStates();

        for (StateEntity stateEntityList : statesLists) {
            StatesList states = new StatesList().id(UUID.fromString(stateEntityList.getUuid())).stateName(stateEntityList.getStateName());
            statesListResponse.addStatesItem(states);
        }
        return new ResponseEntity<StatesListResponse>(statesListResponse, HttpStatus.OK);
    }
}
