package com.upgrad.FoodOrderingApp.service.common;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ApplicationUtil {
    /**
     * Method to validate the customer 5 being the best
     *
     * @param cutomerRating
     * @return
     */
    public boolean validateCustomerRating(String cutomerRating) {
        if (cutomerRating.equals("5.0")) {
            return true;
        }
        Pattern p = Pattern.compile("[1-4].[0-9]");
        Matcher m = p.matcher(cutomerRating);
        return (m.find() && m.group().equals(cutomerRating));
    }
}
