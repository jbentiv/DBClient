package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Model.States;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class StateDAO {
    /**
     * Adds all state objects to a list
     *
     * @param countryID is used to filter states by country
     * @return stateList
     */
    public static ObservableList<States> getAllStates(int countryID) {
        ObservableList<States> stateList = FXCollections.observableArrayList();
        try {
            String SQL = "SELECT * FROM first_level_divisions WHERE Country_ID = ?";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(SQL);
            ps.setInt(1, countryID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int stateID = rs.getInt("Division_ID");
                String stateName = rs.getString("Division");
                States s = new States(stateID, stateName);
                stateList.add(s);
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        return stateList;
    }
}
