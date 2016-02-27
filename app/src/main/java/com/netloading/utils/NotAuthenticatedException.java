package com.netloading.utils;

/**
 * Created by Dandoh on 2/27/16.
 */
public class NotAuthenticatedException extends Exception {

    @Override
    public String getMessage() {
        return "Not authenticated or session expired";
    }
}
