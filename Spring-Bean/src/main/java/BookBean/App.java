package BookBean;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("BookConfig.xml");
        Book book = (Book) context.getBean("bookBean");
        book.display();
    }
}
