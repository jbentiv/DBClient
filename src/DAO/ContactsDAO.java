package DAO;

import Model.Contacts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class ContactsDAO {
    /**
     * populates combobox contact with all contact names
     * @return
     */
    public static ObservableList<Contacts> getAllContacts() {
        ObservableList<Contacts> contactsList = FXCollections.observableArrayList();
        try {
            String SQL = "SELECT * FROM contacts";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int contactID = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                Contacts c = new Contacts(contactID, contactName);
                contactsList.add(c);
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        return contactsList;
    }
}
