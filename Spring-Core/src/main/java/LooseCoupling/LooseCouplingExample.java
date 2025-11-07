package LooseCoupling;

public class LooseCouplingExample {
    public static void main(String[] args) {

//        //Instantiate the reference of Interface by the help of its implementation class
//        IUserDataBase iUserDataBase = new UserDataBaseImpl();
//        //Create the obj of UserManager
//        UserManager userManager = new UserManager(iUserDataBase);
//        System.out.println(userManager.getUserInfo());


        //Change DB Provider to New class

        //Instantiate the reference of Interface by the help of its implementation class
        IUserDataBase iUserDataBase1 = new NewUserDataBaseImpl();

        //Create the obj of UserManager
        UserManager userManager1 = new UserManager(iUserDataBase1);

        System.out.println(userManager1.getUserInfo());
    }
}

/*

    Loose Coupling
    --------------

    * We dont fetch user details in UserManager by creating object of implementation class

    * Rather we declare the reference of base interface and instantiate the ref with required provider class obj with in the parameterized constructor of UserManager

    * Then using the ref of interface which now is instantiated with reqd provider class object, we call the required method to fetch user details from DB

    * Instead of calling directly (Through provider class obj) we call indirectly (Through base interface reference instantiated with required provider class object)



    * When we change the DB provider to something else, then we just need to pass that class obj as ref in parameterized constructor of UserManager class during
    its instantiation

    * We dont have to refactor UserManager class again and again to accommodate changes



    Basically the interface acts as a layer of abstraction which is on top of every implementation classes or DB providers. So we use the ref of interface with
    instantiated with object of required provider class to get desired output.




 */
