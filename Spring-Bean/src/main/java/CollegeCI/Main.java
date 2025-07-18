package CollegeCI;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("collegeConfig.xml");
        College college = (College) context.getBean("collegeBean");
        college.showStudentDetails();
    }
}
