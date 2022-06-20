package Controller;

import DAO.*;
import Model.Appointments;
import Model.Contacts;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Pair;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ResourceBundle;
public class AppointmentsController implements Initializable {

    /**
     * All appointments view
     */
    @FXML
    public TableView<Appointments> appointmentsAllTable;
    @FXML
    public TableColumn<Appointments, Integer> appointmentIDCol;
    @FXML
    public TableColumn<Appointments, String> titleCol;
    @FXML
    public TableColumn<Appointments, String> descritpionCol;
    @FXML
    public TableColumn<Appointments, String> locationCol;
    @FXML
    public TableColumn<Appointments, Integer> contactCol;
    @FXML
    public TableColumn<Appointments, String> typeCol;
    @FXML
    public TableColumn<Appointments, Timestamp> startDateTimeCol;
    @FXML
    public TableColumn<Appointments, Timestamp> endDateTimeCol;
    @FXML
    public TableColumn<Appointments, Integer> customerIDCol;
    @FXML
    public TableColumn<Appointments, Integer> userIDCol;
    @FXML
    public TableColumn<Appointments, Integer> contactIDCol;
    public Button appointmentsBackButton;
    public ComboBox addAppointmentEndTimeComboBox;
    public DatePicker addAppointmentEndDate;
    public ComboBox addAppointmentStartTimeComboBox;
    public DatePicker addAppointmentStartDate;
    public ComboBox<Integer> addAppointmentUserComboBox;
    public ComboBox<Integer> addAppointmentCustomerComboBox;
    public ComboBox<Contacts> addAppointmentContactComboBox;
    public TextField addAppointmentLocationTextBox;
    public TextField addAppointmentTitleTextBox;
    public TextField addAppointmentIDTextBox;
    public Label id;
    public Label title;
    public Label description;
    public Label location;
    public Label contact;
    public Label customer;
    public Label user;
    public Label startdate;
    public Label starttime;
    public Label enddate;
    public Label endtime;
    public Button appointmentsAddButton;
    public Button appointmentsModifyButton;
    public Button appointmentsDeleteButton;
    public Button addAppointmentSaveButton;
    public Button addAppointmentCancelButton;
    public TextField addAppointmentDescriptionBox;
    public TextField addAppointmentTypeBox;
    public Label type;
    public RadioButton appointmentsWeeklyRadio;
    public RadioButton appointmentsMonthlyRadio;
    public RadioButton apointmentsAllRadio;



    public AppointmentsController() throws SQLException, ParseException {
    }

    /**
     * Comboboxes and textboxes that need to be populated with data, toggle user input to false, can populate list for table
     * @param url
     * @param resourceBundle
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addAppointmentCustomerComboBox.setItems(CustomersDAO.getCustomerID());
        addAppointmentStartTimeComboBox.setItems(AppointmentDAO.availableTimes());
        addAppointmentEndTimeComboBox.setItems(AppointmentDAO.availableTimes());
        addAppointmentUserComboBox.setItems(UserDAO.getUserID());
        addAppointmentContactComboBox.setItems(ContactsDAO.getAllContacts());
        populateAppointmentList();
        toggleUserInput(true);
        /**
         * disables SAT and Sunday from calendar selector
         */

        addAppointmentEndDate.setDayCellFactory(param -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY);
            }
        });

        /**
         * disables SAT and Sunday from calendar selector
         */
        addAppointmentStartDate.setDayCellFactory(param -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY);
            }
        });
    }

    /**
     * Takes user back to Mainform.fxml
     * @param actionEvent
     * @throws IOException
     */
    public void onButtonActionAppointmentsBackButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/HomeForm.fxml"));
        /**
         * get the stage from an event's source widget
         */
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        /**
         * create new scene
         */
        Scene scene = new Scene(root);
        /**
         * RUNTIME ERROR - java: ';' expected
         * stage.setTitle("Add Parts"); used for testing
         * set the scene on the stage
         */
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Toggles user input allowed for text boxes (greys them out)
     * @param actionEvent
     */
    public void onButtonActionAppointmentsAddButton(ActionEvent actionEvent) {
        toggleUserInput(false);
    }

    /**
     * Populates user click to table selection, then populates into textboxes and combo boxes from SQL database
     * @param actionEvent
     */
    public void onButtonActionAppointmentsModifyButton(ActionEvent actionEvent) {
        if (appointmentsAllTable.getSelectionModel().getSelectedItem() == null) {
        }else {
            toggleUserInput(false);
            toggleModifyInput(true);
            Appointments appointmentSelected = (Appointments) appointmentsAllTable.getSelectionModel().getSelectedItem();
            Pair<LocalDate, String> start = DateTimeModifiers.timestampToDateAndTime(appointmentSelected.getStartTimeLocal());
            Pair<LocalDate, String> end = DateTimeModifiers.timestampToDateAndTime(appointmentSelected.getEndTimeLocal());

            addAppointmentIDTextBox.setText(Integer.toString(appointmentSelected.getAppointmentID()));
            addAppointmentTitleTextBox.setText(appointmentSelected.getTitle());
            addAppointmentDescriptionBox.setText(appointmentSelected.getDescription());
            addAppointmentLocationTextBox.setText(appointmentSelected.getLocation());
            addAppointmentContactComboBox.setValue(appointmentSelected.getContactObject());
            addAppointmentTypeBox.setText(appointmentSelected.getType());
            addAppointmentStartDate.setValue(start.getKey());
            addAppointmentStartTimeComboBox.setValue(start.getValue());
            addAppointmentEndDate.setValue(end.getKey());
            addAppointmentEndTimeComboBox.setValue(end.getValue());
            addAppointmentCustomerComboBox.setValue(appointmentSelected.getCustomerID());
            addAppointmentUserComboBox.setValue(appointmentSelected.getUserID());

            int appointment_id = appointmentSelected.getAppointmentID();
            AppointmentDAO.delPartAppointment(appointment_id, appointmentSelected.getType());

        }

    }

    /**
     * LAMBDA Expressions are used here to give the user a confirmation alert for when the user is deleting an appointment
     * to ensure that they truly want to delete that appointment with the prompted Appointment ID:, Type:, and Name.
     * Justification: Simplifies Confirmation code and receives user input to confirm deletion.
     */
    public void onActionButtonAppointmentsDeleteButton(ActionEvent actionEvent) throws SQLException {
        if (appointmentsAllTable.getSelectionModel().getSelectedItem() == null) {
        }else {
            Appointments appointmentSelectec = (Appointments) appointmentsAllTable.getSelectionModel().getSelectedItem();
            int appointment_id = appointmentSelectec.getAppointmentID();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("ATTENTION!");
            alert.setHeaderText("Are you sure you want to delete Appointment ID: " + appointmentSelectec.getAppointmentID() + ", Type: " + appointmentSelectec.getType() + ", with " + appointmentSelectec.getContactName() + "?");
            /** LAMBDA Expression here to promt user for feedback on if they want to confirm deletion of a selected appointment */
            alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
                        AppointmentDAO.delAppointment(appointment_id, appointmentSelectec.getType());
                    });
            appointmentsAllTable.setItems(AppointmentDAO.getAllAppointments());
                populateAppointmentList();
                resetUserInput();

        }
    }

    /**
     * Resets textboxes and combo bxes to blank values through resetUserInput();
     * @param actionEvent
     */
    public void onActionButtonAddAppointmentResetButton(ActionEvent actionEvent) {
        resetUserInput();
    }

    public void onButtonActionAddAppointmentSaveButton(ActionEvent actionEvent) throws ParseException, SQLException {
        if(userMissingInput()) {
            String title = addAppointmentTitleTextBox.getText();
            String description = addAppointmentDescriptionBox.getText();
            String location = addAppointmentLocationTextBox.getText();
            int contactID = addAppointmentContactComboBox.getValue().getContactID();
            String type = addAppointmentTypeBox.getText();
            String startDate = addAppointmentStartDate.getValue().toString();
            String startTime = addAppointmentStartTimeComboBox.getValue().toString();
            String endDate = addAppointmentEndDate.getValue().toString();
            String endTime = addAppointmentEndTimeComboBox.getValue().toString();
            int customerID = addAppointmentCustomerComboBox.getValue();
            int userID = addAppointmentUserComboBox.getValue();

            String localStartTime = DateTimeModifiers.convertUTC(startDate, startTime);
            String localEndTime = DateTimeModifiers.convertUTC(endDate, endTime);

            if (AppointmentDAO.appointmentTimeValidityCheck(localStartTime, localEndTime)) {
                if (AppointmentDAO.customerOverlapCheck(customerID, localStartTime, localEndTime)) {
                    if (addAppointmentIDTextBox.getText().isEmpty()) {
                        AppointmentDAO.addAppointment(title, description, location, type, localStartTime, localEndTime, customerID, userID, contactID);
                        appointmentsAllTable.setItems(AppointmentDAO.getAllAppointments());
                        populateAppointmentList();
                        resetUserInput();
                        toggleUserInput(true);
                        toggleModifyInput(false);
                    }else {
                        int appointmentID = Integer.parseInt(addAppointmentIDTextBox.getText());
                        AppointmentDAO.editAppointment(appointmentID, title, description, location, type, localStartTime, localEndTime, customerID, userID, contactID);
                        appointmentsAllTable.setItems(AppointmentDAO.getAllAppointments());
                        populateAppointmentList();
                        resetUserInput();
                        toggleUserInput(true);
                        toggleModifyInput(false);
                    }
                }
            }
        }
    }

    /**
     * Toggles input between input available and not available on textbox input and combobox input. Can be turned on or off with True/False Bool.
     * @param toggle
     */
    public void toggleUserInput(boolean toggle) {
        id.setDisable(toggle);
        title.setDisable(toggle);
        description.setDisable(toggle);
        location.setDisable(toggle);
        contact.setDisable(toggle);
        customer.setDisable(toggle);
        user.setDisable(toggle);
        startdate.setDisable(toggle);
        starttime.setDisable(toggle);
        enddate.setDisable(toggle);
        endtime.setDisable(toggle);
        addAppointmentSaveButton.setDisable(toggle);
        addAppointmentCancelButton.setDisable(toggle);
        addAppointmentEndTimeComboBox.setDisable(toggle);
        addAppointmentEndDate.setDisable(toggle);
        addAppointmentStartTimeComboBox.setDisable(toggle);
        addAppointmentStartDate.setDisable(toggle);
        addAppointmentUserComboBox.setDisable(toggle);
        addAppointmentCustomerComboBox.setDisable(toggle);
        addAppointmentContactComboBox.setDisable(toggle);
        addAppointmentLocationTextBox.setDisable(toggle);
        addAppointmentTitleTextBox.setDisable(toggle);
        addAppointmentIDTextBox.setDisable(toggle);
        addAppointmentDescriptionBox.setDisable(toggle);
        addAppointmentTypeBox.setDisable(toggle);
        type.setDisable(toggle);
    }


    /**
     * Disables user selection on buttons as when modifiy button is pressed it temporarily deletes appointment start/end time to correctly re-submit to the database. Not saving can cause SQL error.
     * @param toggle
     */
    public void toggleModifyInput(boolean toggle) {
        appointmentsAddButton.setDisable(toggle);
        appointmentsDeleteButton.setDisable(toggle);
        appointmentsModifyButton.setDisable(toggle);
        appointmentsBackButton.setDisable(toggle);
        addAppointmentCancelButton.setDisable(toggle);
    }

    /**
     * Resets textboxes and combo bxes to blank values through resetUserInput();
     */
    public void resetUserInput() {
        addAppointmentIDTextBox.setPromptText("Auto Generated");
        addAppointmentIDTextBox.setText("");
        addAppointmentTitleTextBox.setText("");
        addAppointmentDescriptionBox.setText("");
        addAppointmentTypeBox.setText("");
        addAppointmentLocationTextBox.setText("");
        addAppointmentContactComboBox.setValue(null);
        addAppointmentCustomerComboBox.setValue(null);
        addAppointmentUserComboBox.setValue(null);
        addAppointmentStartDate.setValue(null);
        addAppointmentStartTimeComboBox.setValue(null);
        addAppointmentEndDate.setValue(null);
        addAppointmentEndTimeComboBox.setValue(null);
    }

    /**
     * populates all appointments in the table with the ALL radio selection
     * @param actionEvent
     */
    public void onRadioActionApointmentsAllRadio(ActionEvent actionEvent) {
        populateAppointmentList();

    }

    /**
     * Populates values to appointment list from SQL database
     */
    public void populateAppointmentList() {
        appointmentsAllTable.setItems(AppointmentDAO.getAllAppointments());
        appointmentIDCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descritpionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTimeLocal"));
        endDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTimeLocal"));
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        userIDCol.setCellValueFactory(new PropertyValueFactory<>("userID"));
    }

    /**
     * populates all appointments in the table pertaining to the Weekly radio selection parameter
     * @param actionEvent
     * @throws ParseException
     */
    public void onRadioActionAppointmentsWeeklyRadio(ActionEvent actionEvent) throws ParseException{
        appointmentsAllTable.setItems(AppointmentDAO.getAppointmentsThisWeek());
        appointmentIDCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descritpionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTimeLocal"));
        endDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTimeLocal"));
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        userIDCol.setCellValueFactory(new PropertyValueFactory<>("userID"));
    }

    /**
     * populates all appointments in the table pertaining to the monthly radio selection parameter
     * @param actionEvent
     * @throws ParseException
     */
    public void onRadioActionAppointmentsMonthlyRadio(ActionEvent actionEvent) throws ParseException {
        appointmentsAllTable.setItems(AppointmentDAO.getAppointmentsThisMonth());
        appointmentIDCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descritpionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTimeLocal"));
        endDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTimeLocal"));
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        userIDCol.setCellValueFactory(new PropertyValueFactory<>("userID"));
    }

    /**
     * Quick check to see if the user has missed any input for textbox/combo box values. Missed values will throw SQL error.
     * @return
     */
    public boolean userMissingInput() {
        boolean userMissingInput = false;
        if (addAppointmentTitleTextBox.getText().isEmpty() || addAppointmentDescriptionBox.getText().isEmpty() || addAppointmentTypeBox.getText().isEmpty() ||
                addAppointmentLocationTextBox.getText().isEmpty() || addAppointmentContactComboBox.getValue() == null || addAppointmentCustomerComboBox.getValue() == null ||
                addAppointmentUserComboBox.getValue() == null || addAppointmentStartDate.getValue() == null || addAppointmentStartTimeComboBox.getValue() == null ||
                addAppointmentEndDate.getValue() == null || addAppointmentEndTimeComboBox.getValue() == null) {

            userMissingInput = false;
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error!");
            alert.setHeaderText("Missing input! Please try again.");
            alert.showAndWait();
        }else {
            userMissingInput = true;
        }
        return userMissingInput;
    }
    public void onBoxActionAddAppointmentContact(ActionEvent actionEvent) {
    }

    public void onBoxActionAddAppointmentCustomer(ActionEvent actionEvent) {
    }

    public void onBoxActionAddAppointmentUser(ActionEvent actionEvent) {
    }

    public void onActionAddAppointmentStartDate(ActionEvent actionEvent) {
    }

    public void onBoxActionAddAppointmentStartTime(ActionEvent actionEvent) {
    }

    public void onActionAddAppointmentEndDate(ActionEvent actionEvent) {
    }

    public void onBoxActionAddAppointmentEndTime(ActionEvent actionEvent) {
    }
}
