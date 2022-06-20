package Controller;

import DAO.AppointmentDAO;
import DAO.UserDAO;
import Model.Appointments;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML public TableView<Appointments> homeTable;
    @FXML public TableColumn<Appointments, Integer> homeTableID;
    @FXML public TableColumn<Appointments, String> homeTableName;
    @FXML public TableColumn<Appointments, Timestamp> homeTableTime;

    ObservableList<Appointments> soonAppointmentList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UserDAO.getCurrentUserID();
        System.out.println("User logged in as user ID# " + UserDAO.currentUser);
        System.out.println(UserDAO.currentUserID);
        populateSoonAppointmentList();
    }

    /**
     * shows user on MainForm.fxml appointments that are within 15 min, this only populates to the userID that is logged in at the time.
     */
    public void populateSoonAppointmentList() {
        homeTable.setItems(AppointmentDAO.getSoonAppointments());
        homeTableID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        homeTableName.setCellValueFactory(new PropertyValueFactory<>("title"));
        homeTableTime.setCellValueFactory(new PropertyValueFactory<>("startTimeLocal"));
    }

    /**
     * Takes user to AppointmentsForm.fxml to view/add/modify appointments
     * @param actionEvent
     * @throws IOException
     */
    public void onButtonActionAppointments(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AppointmentsForm.fxml"));
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
     * Takes user to CustomersForm.fxml to view/add/modify customers
     * @param actionEvent
     * @throws IOException
     */
    public void onButtonActionCustomers(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/CustomersForm.fxml"));
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
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Takes user to ReportsForm.fxml to view reports
     * @param actionEvent
     * @throws IOException
     */
    public void onButtonActionReports(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/ReportsForm.fxml"));
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
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Takes user to LoginForm.fxml and ends connections
     * @param actionEvent
     * @throws IOException
     */
    public void onButtonActionLogOutButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginForm.fxml"));
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
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}
