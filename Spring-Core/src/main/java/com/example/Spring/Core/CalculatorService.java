package com.example.Spring.Core;

public class CalculatorService {

    public int add(int a, int b){
        return a+b;
    }

    public int diff(int a, int b){
        return a-b;
    }

    public int mul(int a, int b){
        return a*b;
    }

    public int div(int a, int b){
        if(b!=0) {
            return a / b;
        }
        return -1;
    }
}
