package TightCoupling;

public class UserManager {
    private UserDataBase userDatabase = new UserDataBase();

    public String getUserInfo(){
        return userDatabase.getUserDetails();
    }
}
