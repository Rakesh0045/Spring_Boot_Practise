package SetterInjection;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SetterInjection {
    public static void main(String[] args) {
        // 1. Load Spring config from classpath
        ApplicationContext context = new ClassPathXmlApplicationContext("setterInjection.xml");

        // 2. Fetch the bean from container
        Laptop laptop = (Laptop) context.getBean("laptopBean");

        // 3. Use the injected bean
        laptop.displayInfo();
    }

    /*

            | Class/File Name        | Purpose                                             |
            | ---------------------- | --------------------------------------------------- |
            | `Specification.java`   | POJO holding processor & modelName                  |
            | `Laptop.java`          | Depends on `Specification`, injected via setter     |
            | `setterInjection.xml`  | Spring config XML with beans and property injection |
            | `SetterInjection.java` | Main class that loads context and runs the app      |


            ➡️ Spring loads XML
                ➡️ Finds <bean id="specificationBean">
                    ➡️ Creates Specification
                    ➡️ Calls setProcessor(...)
                    ➡️ Calls setModelName(...)
                ➡️ Finds <bean id="laptopBean">
                    ➡️ Creates Laptop
                    ➡️ Calls setSpecification(...) with reference to spec bean
            ➡️ You call displayInfo()
                ➡️ Outputs spec details



     */
}
