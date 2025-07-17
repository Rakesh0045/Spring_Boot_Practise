package AutoWiringByType;

public class WarrantySpec {

    private int warranty_years;
    private String warrantyOn;

    public int getWarranty_years() {
        return warranty_years;
    }

    public void setWarranty_years(int warranty_years) {
        this.warranty_years = warranty_years;
    }

    public String getWarrantyOn() {
        return warrantyOn;
    }

    public void setWarrantyOn(String warrantyOn) {
        this.warrantyOn = warrantyOn;
    }

    @Override
    public String toString() {
        return "WarrantySpec{" +
                "warranty_years=" + warranty_years +
                ", warrantyOn='" + warrantyOn + '\'' +
                '}';
    }
}
