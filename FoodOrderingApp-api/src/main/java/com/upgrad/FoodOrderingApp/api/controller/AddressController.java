package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.SaveAddressRequest;
import com.upgrad.FoodOrderingApp.api.model.SaveAddressResponse;
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

import java.util.UUID;

@RestController
@RequestMapping("/")
public class AddressController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UtilityProvider utilityProvider;

    @Autowired
    private AddressService addressService;

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
}
