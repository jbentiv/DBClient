package Controller;

import DAO.CountriesDAO;
import DAO.CustomersDAO;
import DAO.StateDAO;
import Model.Appointments;
import Model.Countries;
import Model.Customers;
import Model.States;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ResourceBundle;

public class CustomersController implements Initializable {

    public TableView<Customers> customerTable;
    public TableColumn<Customers, Integer> customerTableID;
    public TableColumn<Customers, String> customerTableName;
    public TableColumn<Customers, String> customerTableAddress;
    public TableColumn<Customers, String> customerTablePostalCode;
    public TableColumn<Customers, String> customerTablePhone;
    public TableColumn<Object, Object> customerTableState;
    public TableColumn<Object, Object> customerTableCountry;
    public Button customersNewButton;
    public Button customersModifyButton;
    public Button customersDeleteButton;
    public Button CustomerSaveButton;
    public Button CustomerClearButton;
    public TextField CustomerIDTextBox;
    public TextField CustomerNameTextBox;
    public TextField CustomerPhoneTextBox;
    public TextField CustomerAddressTextBox;
    public ComboBox<Countries> CustomerCountryTextBox;
    public ComboBox<States> CustomerStateProvinceComboBox;
    public TextField CustomerPostalCodeTextBox;
    public Button customersBackButton;
    public Label id;
    public Label name;
    public Label phone;
    public Label address;
    public Label country;
    public Label state;
    public Label post;
    public Label background2;
    public Label background;

    ObservableList<Customers> customersList = FXCollections.observableArrayList();

    /**
     * first action items CustomerController.java initiates, gets customer list and populates comboboxs for states/ countries
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateCustomerList();
        CustomerCountryTextBox.setItems(CountriesDAO.getAllCountries());
        toggleUserInput(true);
    }

    /**
     * Takes user back to the home screen. Nothing is changed/saved.
     * @param actionEvent
     * @throws IOException
     */
    public void onButtonActionCustomersBackButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/HomeForm.fxml"));
        /**
         * get the stage from an event's source widget
         */
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        /**
         * create new scene
         */
        Scene scene = new Scene(root);
        /**
         * RUNTIME ERROR - java: ';' expected
         * stage.setTitle("Add Parts"); used for testing
         * set the scene on the stage
         */
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * modifys customers from the customer table
     * @param actionEvent
     * @throws IOException
     */
    public void onButtonActionCustomersModifyButton(ActionEvent actionEvent) throws IOException{
        if (customerTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Attention!");
            alert.setHeaderText("Please select a customer first!");
            alert.showAndWait();

        }else {
            toggleUserInput(false);
            Customers customerSelected = (Customers) customerTable.getSelectionModel().getSelectedItem();
            CustomerIDTextBox.setText(Integer.toString(customerSelected.getCustID()));
            CustomerNameTextBox.setText((customerSelected.getCustName()));
            CustomerPhoneTextBox.setText(customerSelected.getCustPhone());
            CustomerAddressTextBox.setText(customerSelected.getCustAddress());
            CustomerCountryTextBox.setValue(customerSelected.getCustCountryBox());
            CustomerStateProvinceComboBox.setValue(customerSelected.getCustStateBox());
            CustomerPostalCodeTextBox.setText(customerSelected.getCustPostal());

        }

        }
    /**
     * LAMBDA Expressions are used here to give the user a confirmation alert for when the user is deleting a customer
     * to ensure that they truly want to delete that customer with the prompted customer name.
     * Justification: Simplifies Confirmation code and receives user input to confirm deletion.
     */
    public void onActionButtonCustomerDeleteButton(ActionEvent actionEvent) throws SQLException {

        if(customerTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Attention!");
            alert.setHeaderText("Please select a customer first!");
            alert.showAndWait();
        }else {
            Customers selectedCustomer = (Customers) customerTable.getSelectionModel().getSelectedItem();
            int customer_id = selectedCustomer.getCustID();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("ATTENTION!");
            alert.setHeaderText("All APPOINTMENTS associated with " + selectedCustomer.getCustName() + " will ALSO be deleted!");

            /** LAMBDA Used to relay information to the user that they will delete appointment data along with their customer of choice.*/
            alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
                try {
                    CustomersDAO.delCustomer(customer_id);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });

                populateCustomerList();
                resetUserInput();
                toggleUserInput(true);
        }
    }

    /**
     * updates the customer table whenever called upon
     */
    public void populateCustomerList() {

        customerTable.setItems(CustomersDAO.getAllCustomers());
        customerTableID.setCellValueFactory(new PropertyValueFactory<>("custID"));
        customerTableName.setCellValueFactory(new PropertyValueFactory<>("custName"));
        customerTablePhone.setCellValueFactory(new PropertyValueFactory<>("custPhone"));
        customerTableAddress.setCellValueFactory(new PropertyValueFactory<>("custAddress"));
        customerTableState.setCellValueFactory(new PropertyValueFactory<>("custState"));
        customerTableCountry.setCellValueFactory(new PropertyValueFactory<>("custCountry"));
        customerTablePostalCode.setCellValueFactory(new PropertyValueFactory<>("custPostal"));
    }

    /**
     * saves user imput into text boxes and updates customer table through populateCustomerList()
     * @param actionEvent
     */
    public void onButtonActionSaveCustomerButton(ActionEvent actionEvent) {
        if (userMissingInput()) {
            String name = CustomerNameTextBox.getText();
            String phone = CustomerPhoneTextBox.getText();
            String address = CustomerAddressTextBox.getText();
            int countryID = CustomerCountryTextBox.getValue().getCountryID();
            int stateID = CustomerStateProvinceComboBox.getValue().getStateID();
            String postal = CustomerPostalCodeTextBox.getText();
            if (CustomerIDTextBox.getText().isEmpty()) {
                CustomersDAO.addCustomer(name, address, postal, phone, stateID);
                resetUserInput();
                populateCustomerList();
                toggleUserInput(true);
            } else {
                int customerID = Integer.parseInt(CustomerIDTextBox.getText());
                CustomersDAO.modifyCustomer(customerID, name, address, postal, phone, stateID);
                resetUserInput();
                populateCustomerList();
                toggleUserInput(true);
            }
        }
    }


    /**
     * populates choices for country combobox
     * @param actionEvent
     */
    public void onBoxActionAddCustomerCountry(ActionEvent actionEvent) {
        Countries c = (Countries) CustomerCountryTextBox.getValue();
        CustomerStateProvinceComboBox.setItems(StateDAO.getAllStates(c.getCountryID()));
    }

    public void onBoxActionAddCustomerStateProvince(ActionEvent actionEvent) {
    }

    /**
     * resets user input in the given text and comboboxes
     * @param actionEvent
     */
    public void onActionButtonCustomerClearButton(ActionEvent actionEvent) {
        resetUserInput();
    }

    /**
     * disables user input temporarily for a cleaner looke when navigating the table for selection values
     * @param toggle
     */
    public void toggleUserInput(boolean toggle) {
        CustomerNameTextBox.setDisable(toggle);
        CustomerPhoneTextBox.setDisable(toggle);
        CustomerAddressTextBox.setDisable(toggle);
        CustomerCountryTextBox.setDisable(toggle);
        CustomerStateProvinceComboBox.setDisable(toggle);
        CustomerPostalCodeTextBox.setDisable(toggle);
        CustomerSaveButton.setDisable(toggle);
        CustomerClearButton.setDisable(toggle);
        id.setDisable(toggle);
        name.setDisable(toggle);
        phone.setDisable(toggle);
        address.setDisable(toggle);
        country.setDisable(toggle);
        state.setDisable(toggle);
        post.setDisable(toggle);
        background2.setDisable(toggle);
    }

    /**
     * Useful button to reset all input values that the user has typed and selected, sets values to null or ""
     */
    public void resetUserInput() {
        CustomerIDTextBox.setText("");
        CustomerNameTextBox.setText("");
        CustomerPhoneTextBox.setText("");
        CustomerAddressTextBox.setText("");
        CustomerCountryTextBox.setValue(null);
        CustomerStateProvinceComboBox.setValue(null);
        CustomerPostalCodeTextBox.setText("");

    }

    /**
     * quick check to see if the user is missing any input for the textboxes/comboboxes
     * @return
     */
    public boolean userMissingInput() {
        boolean userMissingInput = false;
        if (CustomerNameTextBox.getText().isEmpty() || CustomerPhoneTextBox.getText().isEmpty() || CustomerAddressTextBox.getText().isEmpty() || CustomerCountryTextBox.getValue() == null || CustomerStateProvinceComboBox.getValue() == null || CustomerPostalCodeTextBox.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error!");
            alert.setHeaderText("Missing input! Please try again.");
            alert.showAndWait();
        }else {
            userMissingInput = true;
        }return userMissingInput;
    }

    /**
     * Opens user inputer and enables textboxes/comboboxes for the user to create customer
     * @param actionEvent
     */
    public void onButtonActionCustomersNewButton(ActionEvent actionEvent) {
        toggleUserInput(false);
    }

}
































