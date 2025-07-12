package FirstBean;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        // 1. Load Spring context from XML
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationBeanContext.xml");

        // 2. Retrieve the bean from Spring container
        MyBean myBean = (MyBean) context.getBean("myBean");

        // 3. Use the bean
        myBean.showMessage();

        /*

        | Term                             | Role                                        |
        | -------------------------------- | ------------------------------------------- |
        | `ApplicationContext`             | The Spring container (manages the beans)    |
        | `ClassPathXmlApplicationContext` | Loads the XML file from the `classpath`     |

         */
    }
}

/*

| Component                    | Description                                                |
| ---------------------------- | ---------------------------------------------------------- |
| `MyBean.java`                | A simple POJO (Plain Old Java Object)                      |
| `applicationBeanContext.xml` | XML file where Spring registers beans                      |
| `App.java`                   | The main class that loads Spring context and uses the bean |


➡️ You run App.java
    ➡️ It loads XML file from classpath
        ➡️ Spring finds <bean id="myBean"...>
            ➡️ Spring creates an object of MyBean
            ➡️ Spring calls setMessage("Hello from XML Bean!")
            ➡️ Spring adds it to container with name "myBean"
    ➡️ App.java calls getBean("myBean")
        ➡️ Spring returns that object
            ➡️ showMessage() prints: Hello from XML Bean!

 */
