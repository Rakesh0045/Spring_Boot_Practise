package LooseCouplingIOC;

public class UserManager {

    public IUserDataBase iUserDataBase;

    public UserManager(IUserDataBase iUserDataBase){
        this.iUserDataBase = iUserDataBase;
    }

    public String getUserInfo(){
        return iUserDataBase.getUserDetails();
    }

}
