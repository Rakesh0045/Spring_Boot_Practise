package ConstructorInjection;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ConstructorInjection {
    public static void main(String[] args) {
        // 1. Load Spring config from classpath
        ApplicationContext context = new ClassPathXmlApplicationContext("laptopConfig.xml");

        // 2. Fetch the bean from container
        Laptop laptop = (Laptop) context.getBean("laptopBean");

        // 3. Use the injected bean
        laptop.displayInfo();
    }

    /*


            | Component                   | Description                                                      |
            | --------------------------- | ---------------------------------------------------------------- |
            | `Specification.java`        | Dependant POJO Class with fields like processor and model name   |
            | `Laptop.java`               | Need dependancy of Specification, receives it via constructor    |
            | `laptopConfig.xml`          | XML config that injects the dependency via constructor injection |
            | `ConstructorInjection.java` | Main class that loads Spring context and runs the app            |


            WORKFLOW
            --------

            ➡️ You run ConstructorInjection.java
                ➡️ Spring loads laptopConfig.xml
                    ➡️ Finds <bean id="specificationBean"...>
                        ➡️ Creates Specification object
                        ➡️ Sets processor and modelName via setter
                    ➡️ Finds <bean id="laptopBean"...>
                        ➡️ Passes Specification to Laptop's constructor
                        ➡️ Creates Laptop object
                ➡️ getBean("laptopBean") returns the fully wired object
                    ➡️ displayInfo() prints the laptop details





     */
}
