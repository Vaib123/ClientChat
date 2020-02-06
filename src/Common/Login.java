package Common;

import java.io.Serializable;


public class Login implements Serializable {  //implements serializable for object passing through streams
    String username;
    String code;
    String pass;
    public Login(String username, String pass,String code) { //creating login information
        this.username = username;
        this.code = code;
        this.pass=pass;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
