package com.lcwd.user.service.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException()
    {
        super("Resource not found");
    }

    public ResourceNotFoundException(String message){
        super("Resource not found" + message);
    }


}

