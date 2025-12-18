package Backend;

public class SystemManager {
    private static UserInfo currentUser;

    public static UserInfo getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(UserInfo user) {
        currentUser = user;
    }

    public static void logout() {
        currentUser = null;
    }
}