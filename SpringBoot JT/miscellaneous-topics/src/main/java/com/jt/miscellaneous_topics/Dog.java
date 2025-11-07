package com.jt.miscellaneous_topics;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class Dog implements Animal {

    @Override
    public void eat(){
        System.out.println("Dog eat");
    }
}

/*

SOLUTION --> Remove @Component from 1 class

*/
