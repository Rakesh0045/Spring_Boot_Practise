package BookBean;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class LibraryApp {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("libraryConfig.xml");
        Library library = (Library) context.getBean("libraryBean");
        library.showLibraryDetails();
    }
}
