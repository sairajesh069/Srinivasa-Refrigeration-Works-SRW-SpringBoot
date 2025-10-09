package com.srinivasa.refrigeration.works.srw_springboot.exceptions;

public class UserValidationException extends RuntimeException{

    public UserValidationException(String message) {
        super(message);
    }
}