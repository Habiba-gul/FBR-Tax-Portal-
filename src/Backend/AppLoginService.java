package Backend;

public class AppLoginService {
    // Hardcoded credentials for our different roles 🔑
    private final String ADMIN_CNIC = "00000-0000000-0";
    private final String ADMIN_PASS = "admin123";

    private final String USER_CNIC = "user";
    private final String USER_PASS = "pass";

    public String validateLogin(String username, String password) {
        if (username.equals(ADMIN_CNIC) && password.equals(ADMIN_PASS)) {
            return "ADMIN";
        } else if (username.equals(USER_CNIC) && password.equals(USER_PASS)) {
            return "USER";
        } else {
            return "INVALID";
        }
    }
}