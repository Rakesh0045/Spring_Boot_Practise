package CollegeCI;

public class College {
    private Student student;
    private Department department;

    public College(Student student, Department department) {
        this.student = student;
        this.department = department;
    }

    public void showStudentDetails(){
        System.out.println("Student Info: "+student.toString());
        System.out.println("Department Info: "+department.toString());
        System.out.println("Library Info: "+department.getLibrary().toString());
    }
}
