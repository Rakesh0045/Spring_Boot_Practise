package AutoWiringByName;

public class Laptop {

    private Specification specificationBean;

    public void setSpecificationBean(Specification specificationBean) {
        this.specificationBean = specificationBean;
    }

    public void displayInfo() {
        System.out.println("Laptop details: " + specificationBean.toString());
    }
}
