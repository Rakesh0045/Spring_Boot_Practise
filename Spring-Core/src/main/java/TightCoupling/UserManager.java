package TightCoupling;

public class UserManager {
    private NewUserDataBase userDatabase = new NewUserDataBase();

    public String getUserInfo(){
        return userDatabase.getUserDetails();
    }
}
