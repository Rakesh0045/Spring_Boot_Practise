package AutoWiringByType;

public class Laptop {
    private Specification specification;

    public void setSpecification(Specification specification) {
        this.specification = specification;
    }

    public void displayInfo() {
        System.out.println("Laptop details: " + specification);
    }
}

