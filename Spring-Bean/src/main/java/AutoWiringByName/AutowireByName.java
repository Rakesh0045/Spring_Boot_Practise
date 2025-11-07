package AutoWiringByName;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AutowireByName {
    public static void main(String[] args) {
        // 1. Load Spring config from classpath
        ApplicationContext context = new ClassPathXmlApplicationContext("autowireByNameConfig.xml");

        // 2. Fetch the bean from container
        Laptop laptop = (Laptop) context.getBean("laptopBean");

        // 3. Use the injected bean
        laptop.displayInfo();
    }

    /*

       🔷 1. What is Autowiring?

            Autowiring is a feature in Spring that allows the Spring container to automatically inject dependent beans (i.e., objects) into a bean without
            needing to explicitly define <property> tags in XML.

            Instead of manually wiring dependencies, Spring does it automatically — based on:
                                                                                                Property name
                                                                                                Property type
                                                                                                Constructor
                                                                                                Using annotations (e.g., @Autowired)


      🔶 2. What is Autowire by Name?

            📌 autowire="byName" injects the dependency by matching: the property name in the Java class with the bean ID in the XML file.

            If a bean’s setter method is setSpecificationBean(...), then Spring looks for a bean with ID = specificationBean in the XML.

            If the setter is setSpecificationBean(...), but your bean in XML is named specBean, Spring will not inject it.

            Result: specificationBean in Laptop will be null, and you'll get NullPointerException.


        🔍 Internal Flow

                Spring reads the XML and creates the object (Laptop).
                It checks for setter methods (setXYZ() → property name = xyz).
                For each setter, Spring checks:
                    👉 Is there a bean with ID = xyz?
                If yes, it injects that bean using the setter.
                If no bean matches the name → property remains null.


        | Point                  | Explanation                                                |
        | ---------------------- | ---------------------------------------------------------- |
        | Matching is **strict** | The bean ID must **exactly match** the Java property name. |
        | Case-sensitive         | `specificationBean` ≠ `SpecificationBean`                  |
        | Only setter injection  | Works only if there's a **setter method** for the field.   |
        | Avoid name mismatch    | If name mismatch, injection fails silently.                |



     */
}
