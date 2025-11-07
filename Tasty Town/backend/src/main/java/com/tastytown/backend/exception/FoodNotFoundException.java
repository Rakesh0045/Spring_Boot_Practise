package com.tastytown.backend.exception;


public class FoodNotFoundException extends RuntimeException {
    
    public FoodNotFoundException(){
        super();
    }

    public FoodNotFoundException(String msg){
        super(msg);
    }
}
