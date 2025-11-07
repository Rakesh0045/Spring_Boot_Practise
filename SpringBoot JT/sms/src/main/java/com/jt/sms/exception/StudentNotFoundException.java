package com.jt.sms.exception;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(){
        super(); //If we dont want to pass any msg for exception use non-parameterized constructor
    }

    public StudentNotFoundException(String msg){
        super(msg);
    }
}
