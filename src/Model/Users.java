package Model;

public class Users {
    private static int userID;
    private static String username;
    private static String password;

    /**
     * Constructor for User object
     * @param userID userID
     * @param username username
     */
    public Users(int userID, String username) {
        this.userID = userID;
        this.username = username;

    }
    /**
     * getter and setter for username
     * @return
     */
    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        Users.username = username;
    }

    /**
     * getter for user ID
     * @return
     */
    public static int getUserID() {
        return userID;
    }
}
