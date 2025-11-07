package TightCoupling;

public class TightCouplingExample {
    public static void main(String[] args) {
        UserManager userManager = new UserManager();
        System.out.println(userManager.getUserInfo());
    }
}

/*

    Tight Coupling
    --------------

    * We directly fetch User details in UserManager class by creating obj of UserDataBase

    * So there is a tight coupling between those 2 classes

    * When there is a change in Database provider, we have to refactor UserManager class to create the obj of new DB provider

    * So to accomodate 1 change we have to refactor our codebase which is an example of tight coupling


 */
