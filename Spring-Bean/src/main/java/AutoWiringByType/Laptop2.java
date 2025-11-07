package AutoWiringByType;

public class Laptop2 {
    private WarrantySpec warrantySpec;

    public void setWarrantySpec(WarrantySpec warrantySpec) {
        this.warrantySpec = warrantySpec;
    }

    private Specification specification;

    public void setSpecification(Specification specification) {
        this.specification = specification;
    }

    public void displayInfo() {
        System.out.println("Laptop details: " + specification);
    }

    public void displayWarrantyInfo() {
        System.out.println("Laptop Warranty details: " + warrantySpec);
    }
}

