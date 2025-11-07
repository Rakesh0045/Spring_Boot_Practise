package LooseCouplingIOC;

public class UserDataBaseImpl implements IUserDataBase {
    @Override
    public String getUserDetails() {
        return "User details fetched from DB";
    }
}
