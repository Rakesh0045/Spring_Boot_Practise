package com.jt.di;

import org.springframework.stereotype.Component;

@Component //Engine class bean will be managed by Spring container
public class Engine {
    public void startEngine(){
        System.out.println("Engine is started");
    }
}
