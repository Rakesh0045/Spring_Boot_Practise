package CollegeCI;

public class Department {
    private String name;
    private int buildingNo;

    private Library library;

    public Department(Library library) {
        this.library = library;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBuildingNo() {
        return buildingNo;
    }

    public void setBuildingNo(int buildingNo) {
        this.buildingNo = buildingNo;
    }

    public Library getLibrary(){
        return library;
    }

    @Override
    public String toString() {
        return "Department{" +
                "name='" + name + '\'' +
                ", buildingNo=" + buildingNo +
                '}';
    }


}
