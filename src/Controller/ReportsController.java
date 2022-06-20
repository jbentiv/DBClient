package Controller;
/**
 * REPORTS TYPES
 * 1. A schedule for each contact in your organization that includes appointment ID, title, type and description, start date and time, end date and time, and customer ID
 * 2. the total number of customer appointments by type and month
 * 3. The location and type total of Customers and their appointments
 */
import DAO.*;
import Model.Appointments;
import Model.Contacts;
import Model.Reports;
import Model.ReportsCustomer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Collections;
import java.util.ResourceBundle;
public class ReportsController implements Initializable {
    public static String comboBoxValue = "";
    public static int comboBoxValueID = 0;
    public ComboBox<Contacts> reportsContactComboBox;
    public TableView<Appointments> appointmentsAllTable;
    public TableColumn<Appointments, Integer> appointmentIDCol;
    public TableColumn<Appointments, String> titleCol;
    public TableColumn<Appointments, String> typeCol;
    public TableColumn<Appointments, String> descriptionCol;
    public TableColumn<Appointments, Timestamp> startDateTimeCol;
    public TableColumn<Appointments, Timestamp> endDateTimeCol;
    public TableColumn<Appointments, Integer> customerIDCol;

    /**
     * Month reports table
     */
    public TableView<ReportsCustomer> reportsMonthTable;
    public TableColumn<ReportsCustomer, String> reportMonthCol;
    public TableColumn<ReportsCustomer, String> reportTypeCol;
    public TableColumn<ReportsCustomer, String> reportTotalCol;

    /**
     * total customer table
     */
    public TableColumn<Reports, String> reportYearCol;
    public TableColumn<Reports, String> reportTotalColYear;
    public TableColumn<Reports, String> reportTypeColYear;
    public TableView<Reports>  AllTableYear;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        reportsContactComboBox.setItems(ContactsDAO.getAllContacts());
        /**
         * populates month table
         */
        reportsMonthTable.setItems(getMonthReports());
        reportMonthCol.setCellValueFactory(new PropertyValueFactory<>("month"));
        reportTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        reportTotalCol.setCellValueFactory(new PropertyValueFactory<>("totalApp"));
        /**
         * populates location table
         */
        AllTableYear.setItems(getTotalCustReports());
        reportYearCol.setCellValueFactory(new PropertyValueFactory<>("loc"));
        reportTypeColYear.setCellValueFactory(new PropertyValueFactory<>("typ"));
        reportTotalColYear.setCellValueFactory(new PropertyValueFactory<>("total"));
    }

    /**
     * Populates table from users selection of contact combobox, updates with button click.
     * @param actionEvent
     */
    public void onActionButtonView(ActionEvent actionEvent) {
        comboBoxValue = String.valueOf(reportsContactComboBox.getValue());
        getCurrentContactID();
        populateAppointmentList();
        System.out.println(comboBoxValue);
        System.out.println(comboBoxValueID);

    }

    /**
     * Takes user back to HomeForm.fxml
     * @param actionEvent
     * @throws IOException
     */
    public void onActionButtonBack(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/HomeForm.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Takes the current users name and determines through SQL database what their user ID is to reference in Appointments table
     * @return
     */
    public static ObservableList getCurrentContactID() {
        ObservableList<Integer> currentcontactIdList = FXCollections.observableArrayList();
        try {
            String SQL = "SELECT Contact_ID FROM contacts WHERE Contact_Name ='" + comboBoxValue + "'";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                comboBoxValueID = (rs.getInt("Contact_ID"));
                currentcontactIdList.add(comboBoxValueID);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return currentcontactIdList;
    }

    /**
     * A schedule for each contact in your organization that includes appointment ID, title, type and description, start date and time, end date and time, and customer ID
     * @return
     */
    public static ObservableList<Appointments> getAllAppointments() {

        ObservableList<Appointments> appointmentList = FXCollections.observableArrayList();

        try {
            String SQL = "SELECT appointments.Appointment_ID, appointments.Title, appointments.Type, appointments.Description, appointments.Start, appointments.End, appointments.Customer_ID FROM appointments WHERE Contact_ID=" + comboBoxValueID;

            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String type = rs.getString("Type");
                String description = rs.getString("Description");
                String startTime = rs.getString("Start");
                String endTime = rs.getString("End");
                int customerID = rs.getInt("Customer_ID");

                String startTimeLocal = DateTimeModifiers.convertToLocalTime(startTime);
                String endTimeLocal = DateTimeModifiers.convertToLocalTime(endTime);


                Appointments appointment = new Appointments(appointmentId, title, type, description, startTimeLocal, endTimeLocal, customerID);
                appointmentList.add(appointment);
            }
        } catch (SQLException | ParseException ex) {
            ex.printStackTrace();
        }


        Collections.sort(appointmentList, (a1, a2) -> a1.getAppointmentID()-a2.getAppointmentID());
        return appointmentList;
    }

    /**
     * Populates table data that is used for report 1.
     */
    public void populateAppointmentList() {
        appointmentsAllTable.setItems(getAllAppointments());
        appointmentIDCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        startDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTimeLocal"));
        endDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTimeLocal"));
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
    }

    /**
     * The total number of customer appointments by type and month
     * @return
     */
    public static ObservableList<ReportsCustomer> getMonthReports() {
        ObservableList<ReportsCustomer> reportsCustomers = FXCollections.observableArrayList();
        try {
            String SQL = "SELECT monthname(Start) AS month, type, count(*) AS TotalAPP FROM appointments GROUP BY month, type ORDER BY month ASC";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String month = rs.getString("month");
                String type = rs.getString("type");
                String totalApp = rs.getString("TotalAPP");
                ReportsCustomer re = new ReportsCustomer(month, type, totalApp);
                reportsCustomers.add(re);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reportsCustomers;
    }

    /**
     * 3. The location and type total of Customers and their appointments
     * @return
     */
    public static ObservableList<Reports> getTotalCustReports() {
        ObservableList<Reports> reportsnew = FXCollections.observableArrayList();
        try {
            String sq = "SELECT Location AS loc, type, count(*) AS Total FROM appointments GROUP BY loc, type ORDER BY loc ASC";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sq);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String loc = rs.getString("loc");
                String typ = rs.getString("type");
                String total = rs.getString("Total");
                Reports r = new Reports(loc, typ, total);
                reportsnew.add(r);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reportsnew;
    }

    /**
     * Not in use
     * @param actionEvent
     */
    public void onBoxActionContactComboBox(ActionEvent actionEvent) {
    }
}
