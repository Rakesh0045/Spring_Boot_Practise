package FirstBean;

//✅ This is a normal Java class. Spring will create and manage its object using XML.
public class MyBean {

    private String message;

    public void setMessage(String message){
        this.message = message;
    }

    public void showMessage(){
        System.out.println("Message: "+message);
    }

    @Override
    public String toString() {
        return "MyBean{" +
                "message='" + message + '\'' +
                '}';
    }
}
