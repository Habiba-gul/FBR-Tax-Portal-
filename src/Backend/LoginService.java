
package Backend;

public class LoginService {

   
    private final String correctUsername = "user";
    private final String correctPassword = "pass";

    public boolean validateLogin(String username, String password) {
        return username.equals(correctUsername) && password.equals(correctPassword);
    }
}
