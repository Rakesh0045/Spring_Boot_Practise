package LooseCouplingIOC;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class IOCLooseCouplingExample {
    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("applicationIOC.xml");

        UserManager userManagerWithUserDatabaseImpl = (UserManager) context.getBean("userManagerWithUserDatabaseImpl");
        System.out.println( userManagerWithUserDatabaseImpl.getUserInfo());

        UserManager userManagerWithNewUserDatabaseImpl = (UserManager) context.getBean("userManagerWithNewUserDatabaseImpl");
        System.out.println(userManagerWithNewUserDatabaseImpl.getUserInfo());
    }
}

