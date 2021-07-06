package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class AddressController {

    @Autowired
    private CustomerService customerService;
}
