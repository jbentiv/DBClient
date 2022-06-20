package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class UserDAO {
    public static String currentUser = null;
    public static int userID = 0;
    public static int currentUserID = 0;

    /**
     * takes current users name and determines userID from SQL query
     * @return
     */
    public static ObservableList getCurrentUserID() {
        ObservableList<Integer> currentUserIdList = FXCollections.observableArrayList();
        try {
            String SQL = "SELECT User_ID FROM users WHERE User_Name ='" + currentUser + "'";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                currentUserID = rs.getInt("User_ID");
                currentUserIdList.add(currentUserID);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return currentUserIdList;
    }

    /**
     * obtains all user ID from sql table users
     * @return
     */
    public static ObservableList getUserID() {
        ObservableList<Integer> userIdList = FXCollections.observableArrayList();
        try {
            String SQL = "SELECT * FROM users";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                userID = rs.getInt("User_ID");
                userIdList.add(userID);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return userIdList;
    }

    /**
     * Determins if user inputs for Username and Password are valid and matching pairs in the Users SQL table
     * @param username
     * @param password
     * @return
     */
    public static boolean userLogin(String username, String password) {
        try{
            String sql = "SELECT * FROM users WHERE User_Name = ? AND Password = ?";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);


            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                setCurrentUser(username);
                return true;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * sets currents username in string format for table
     * @param username
     */
    private static void setCurrentUser(String username) {
        currentUser = username;
    }
}
