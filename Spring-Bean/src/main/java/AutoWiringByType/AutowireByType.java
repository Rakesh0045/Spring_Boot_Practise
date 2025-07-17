package AutoWiringByType;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AutowireByType {
    public static void main(String[] args) {
        // 1. Load Spring config from classpath
        ApplicationContext context = new ClassPathXmlApplicationContext("autowireByTypeConfig.xml");

        Laptop laptop = (Laptop) context.getBean("laptopBean");
        laptop.displayInfo();

        Laptop2 laptop2 = (Laptop2) context.getBean("laptopWarrantyBean");
        laptop2.displayInfo();           // Uses Specification bean
        laptop2.displayWarrantyInfo();   // Uses WarrantySpec bean

        /*

                1. **Spring sees `autowire="byType"`** in the `laptopWarrantyBean` definition.

                2. `Laptop2` has two dependencies:
                                                   * `Specification specification`
                                                   * `WarrantySpec warrantySpec`

                3. Spring **searches for beans of matching types** in the container:

                       * Finds `specificationBean` → injects into `setSpecification(...)`
                       * Finds `warrantyBean` → injects into `setWarrantySpec(...)`

                4. You fetch only `laptopWarrantyBean`, but Spring automatically:
                                                                                   * **Instantiates**
                                                                                   * **Injects**

                5. As a result, you can use both `displayInfo()` and `displayWarrantyInfo()` — no need to manually fetch `specificationBean` or `warrantyBean`.

         */
    }
}

    /*

       🎯 What is Autowiring by Type?

        🔹 Definition:
        When autowiring by type, Spring looks at the type of the property in a bean and automatically injects a matching bean of the same type from the Spring container.

        🔹 Rule:
        Spring scans the container for a single bean of the matching type and injects it via the setter method.

        In this mode, Spring tries to match the data type of the property (not its name) with a bean class defined in the XML.
                ✅ Setter-based injection
                ✅ Works only when exactly one matching type is found in Spring container


        | Feature           | `autowire="byName"`                  | `autowire="byType"`                  |
        | ----------------- | ------------------------------------ | ------------------------------------ |
        | Matching Based On | Property **name**                    | Property **data type**               |
        | Setter Required?  | ✅ Yes                               | ✅ Yes                               |
        | Case-sensitive?   | ✅ Yes                               | ❌ No (not based on name)            |
        | Risk              | Silent failure if name doesn't match | Error if multiple beans of same type |


        ⚠️ Common Error

            ❌ Error if multiple beans of same type:

            org.springframework.beans.factory.NoUniqueBeanDefinitionException:
            No qualifying bean of type 'AutoWiringByType.Specification' available:
            expected single matching bean but found 2: spec1,spec2

            ✅ Solution:

            Use @Primary or @Qualifier annotation in annotation-based configs
            Or avoid using byType in XML if multiple beans of same type exist


         ✅ **Autowire by Type – Summary**

            Autowiring by type tells the Spring container to **inject a dependency based on the data type** (i.e., the class or interface type) of the property.

            🔧 **How it Works**

            1. You declare a bean with `autowire="byType"` in XML:

               ```xml
               <bean id="myBean" class="com.example.MyClass" autowire="byType" />
               ```

            2. Spring looks at the **type of each setter** in `MyClass`.

            3. For each setter, Spring:

               * Searches the container for a bean **matching the required type** (class/interface).
               * If exactly **one bean of that type exists**, Spring injects it.
               * If **multiple beans** of the same type exist → Spring throws an error (ambiguity).
               * If **no bean** of that type is found → no injection is done, and the field remains `null`.

            ### 🧠 **Key Points**

            * Matches **by data type**, not by name.
            * Requires **setter methods** to inject dependencies.
            * Safer than `autowire="byName"` because it doesn’t rely on ID matching.
            * **Can lead to ambiguity** if multiple beans of the same type exist.





     */
