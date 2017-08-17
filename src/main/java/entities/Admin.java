package entities;

/**
 * Created by sergeybp on 20.07.17.
 */
public class Admin {

    public String userName;

    public String password;

    public String token;


    public Admin() {
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
