package DAO;

import Model.Customers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.util.Collections;
public class CustomersDAO {
    /**
     * Selects all customers and populates table with SQL parameters.
     * @return
     */
    public static ObservableList<Customers> getAllCustomers() {
        ObservableList<Customers> customerList = FXCollections.observableArrayList();
        try {
            String SQL = "SELECT customers.Customer_ID, customers.Customer_Name, customers.Phone, customers.Address, first_level_divisions.Division, countries.Country, customers.Postal_Code, countries.country_id, customers.Division_ID FROM customers LEFT JOIN first_level_divisions ON customers.Division_ID=first_level_divisions.Division_ID LEFT JOIN countries ON first_level_divisions.Country_ID=countries.Country_ID";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int custID = rs.getInt("Customer_ID");
                String custName = rs.getString("Customer_Name");
                String custPhone = rs.getString("Phone");
                String custAddress = rs.getString("Address");
                String custState = rs.getString("Division");
                String custCountry = rs.getString("Country");
                String custPostal = rs.getString("Postal_Code");
                int custCountryID = rs.getInt("Country_ID");
                int custStateID = rs.getInt("Division_ID");

                Customers customer = new Customers(custID, custName, custPhone, custAddress, custState, custCountry, custPostal, custCountryID, custStateID);
                customerList.add(customer);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        /**
         * sorts customers once populated / modified
         */
        Collections.sort(customerList, (c1, c2) -> c1.getCustID() - c2.getCustID());
        return customerList;
    }

    /**
     * Recieves customer Id and determines its SQL Customer_ID FK
     * @return
     */
    public static Integer assignCustomerID() {
        Integer customer_ID = 1;
        try {
            Statement stmt = DatabaseConnection.getConnection().createStatement();
            String query = "SELECT customer_Id FROM customers ORDER BY customer_Id";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                while (rs.getInt("customer_Id") == customer_ID) {
                    customer_ID++;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return customer_ID;
    }

    /**
     * Ads customer to SQL customers database with user input and selected parameters
     * @param name
     * @param address
     * @param postal
     * @param phone
     * @param stateID
     */
    public static void addCustomer(String name, String address, String postal, String phone, int stateID) {
        Integer customer_ID = assignCustomerID();
        Timestamp utcTime = DateTimeModifiers.getTimestamp();
        try {
            Statement stmt = DatabaseConnection.getConnection().createStatement();
            String q = "INSERT INTO customers SET customer_ID=" + customer_ID + ", customer_Name='" + name + "', address='" + address + "', postal_code='" + postal + "', phone='" + phone + "', Create_date='" + utcTime + "', Created_by='script', Last_update='" + utcTime + "', Last_Updated_By='script', Division_ID=" + stateID;
            stmt.executeUpdate(q);
            System.out.println("Customer was added");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Deletes customer from SQL database customers.
     * @param customer_id
     * @throws SQLException
     */
    public static void delCustomer(int customer_id) throws SQLException {
        Statement stmt = DatabaseConnection.getConnection().createStatement();
        String del = "DELETE FROM customers WHERE customer_id =" + customer_id;
        stmt.executeUpdate(del);
    }

    /**
     * Populates text/combo boxes with table selected values and the allows the user to change information for a customer
     * @param customerID
     * @param name
     * @param address
     * @param postal
     * @param phone
     * @param stateID
     */
    public static void modifyCustomer(int customerID, String name, String address, String postal, String phone, int stateID) {
        Timestamp utcTime = DateTimeModifiers.getTimestamp();
        try {
            Statement stmt = DatabaseConnection.getConnection().createStatement();
            String mod = "UPDATE customers SET Customer_Name = '" + name + "', Address = '" + address + "', Postal_Code = '" + postal + "', Phone = '" + phone + "', Created_by='script', Last_Update = '" + utcTime + "', Division_ID = '" + stateID + "' WHERE Customer_ID =" + customerID;

            stmt.executeUpdate(mod);
            System.out.println("Customer was modified");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Obtains customer ID from customers for FK
     * @return
     */
    public static ObservableList getCustomerID() {
        ObservableList<Integer> customerIDList = FXCollections.observableArrayList();
        try{
            String cust = "SELECT customer_id FROM customers";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(cust);
            ResultSet rs = ps.executeQuery(cust);
            while (rs.next()) {
                int customerID = rs.getInt("Customer_ID");
                customerIDList.add(customerID);
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        customerIDList = customerIDList.sorted();
        return customerIDList;
    }
}
