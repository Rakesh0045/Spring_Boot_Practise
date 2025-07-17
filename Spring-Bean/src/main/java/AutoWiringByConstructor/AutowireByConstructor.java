package AutoWiringByConstructor;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AutowireByConstructor {
    public static void main(String[] args) {
        // 1. Load Spring config from classpath
        ApplicationContext context = new ClassPathXmlApplicationContext("autowireByConstructor.xml");

        // 2. Fetch the bean from container
        Laptop laptop = (Laptop) context.getBean("laptopBean");

        // 3. Use the injected bean
        laptop.displayInfo();
    }

    /*

        | Component                    | Role                                                        |
        | ---------------------------- | ----------------------------------------------------------- |
        | `Specification`              | Bean class representing laptop details                      |
        | `Laptop`                     | Depends on `Specification`, receives it via constructor     |
        | `autowireByConstructor.xml`  | Spring config with `autowire="constructor"`                 |
        | `AutowireByConstructor.java` | Main method to run and trigger the bean lifecycle in Spring |

        ✅ You don’t need <constructor-arg> — Spring automatically injects the correct bean by matching parameter type.



        🧠 Behind the Scenes (Spring Lifecycle)

            Spring reads autowireByConstructor.xml.
            It creates a Specification object and sets values via setters.
            Then it looks at the constructor of Laptop, sees it needs a Specification object.
            Spring finds a matching bean (by type) → injects it.
            The final Laptop bean is ready to use with its dependency.



     */
}
