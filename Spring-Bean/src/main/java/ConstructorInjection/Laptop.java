package ConstructorInjection;

public class Laptop {

    private Specification specification;

    public Laptop(Specification specification){
        this.specification = specification;
    }

    public void displayInfo(){
        System.out.println("Laptop details: "+specification.toString());
    }
}
