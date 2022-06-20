package DAO;

import Model.Appointments;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.WeekFields;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
public class AppointmentDAO {
    /**
     * Takes the time values of user input and determines if there are any over lapping times when scheduling time/dates
     * EXAMPLE - Start time is after End time.
     * @param localStartTime
     * @param localEndTime
     * @return
     * @throws ParseException
     */
    public static boolean appointmentTimeValidityCheck(String localStartTime, String localEndTime) throws ParseException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date startDateTime = simpleDateFormat.parse(localStartTime);
        java.util.Date endDateTime = simpleDateFormat.parse(localEndTime);
        boolean validityCheck;

        if (startDateTime.after(endDateTime) || startDateTime.equals(endDateTime)) {
            validityCheck = false;
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Attention!");
            alert.setHeaderText("Appointment cannot overlap with other appointment times AND start times must begin before end times.");
            alert.showAndWait();
        } else {
            validityCheck = true;
        }
        return validityCheck;
    }

    /**
     * Takes the time values of user input and determines if there are any over lapping times.
     * NOTE: When appointment modify is clicked times are temporarily "Null" for simplicity of sql UPDATE
     * @param customerID
     * @param localStartTime
     * @param localEndTime
     * @return
     * @throws ParseException
     */
    public static boolean customerOverlapCheck(int customerID, String localStartTime, String localEndTime) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date newAppointmentStartDateTime = simpleDateFormat.parse(localStartTime);
        java.util.Date newAppointmentEndDateTime = simpleDateFormat.parse(localEndTime);
        boolean overlapTest = false;
        Boolean customerHasAppointmetn = false;
        try {
            String sql = "SELECT Appointment_id, Start, End FROM appointments WHERE Customer_ID= " + customerID;
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int appointmentID = rs.getInt(1);
                String startTime = rs.getString(2);
                String endTime = rs.getString(3);

                customerHasAppointmetn = true;

                java.util.Date startDatetime = simpleDateFormat.parse(startTime);
                java.util.Date endDatetime = simpleDateFormat.parse(endTime);

                if((newAppointmentStartDateTime.before(endDatetime) && newAppointmentEndDateTime.after(startDatetime)) || (newAppointmentEndDateTime.before(endDatetime) && newAppointmentEndDateTime.after(startDatetime)) || (newAppointmentStartDateTime.equals(startDatetime) || newAppointmentEndDateTime.equals(endDatetime))) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("");
                    alert.setHeaderText("Customer has overlapping appointment");
                    alert.setContentText("Appointment ID: " + appointmentID);
                    alert.showAndWait();
                    overlapTest = false;
                    break;
                }else {
                    overlapTest = true;
                }
            }if (!customerHasAppointmetn) {
                overlapTest = true;
            }

        }catch (SQLException | ParseException ex) {
            ex.printStackTrace();
        }return overlapTest;
    }

    /**
     * Database connection for adding appointments to the SQL database
     * @param title
     * @param description
     * @param location
     * @param type
     * @param localStartTime
     * @param localEndTime
     * @param customerID
     * @param userID
     * @param contactID
     * @throws SQLException
     */
    public static void addAppointment(String title, String description, String location, String type, String localStartTime, String localEndTime, int customerID, int userID, int contactID) throws SQLException {
        Integer appointment_ID = assignAppointmentID();
        Timestamp utcTime = DateTimeModifiers.getTimestamp();

        try {
            Statement statement = DatabaseConnection.getConnection().createStatement();
            String add = "INSERT INTO appointments SET appointment_ID=" + appointment_ID + ", Title='" + title + "', Description='" + description + "', Location='" + location + "', Type='" + type + "', Start='" + localStartTime + "', End='" + localEndTime + "', Create_date='" + utcTime + "', Created_by='script', Last_update='" + utcTime + "', Last_Updated_By='script', Customer_ID=" + customerID + ", User_ID=" + userID + ", Contact_ID=" + contactID;
            statement.executeUpdate(add);
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Determines the correct appointment ID from selected ID. Able to user FK for table lookup in appointment SQL table
     * @return
     */
    private static Integer assignAppointmentID() {
        Integer appointment_ID = 1;
        try {
            Statement statement = DatabaseConnection.getConnection().createStatement();
            String assign = "SELECT appointment_ID FROM appointments ORDER BY Appointment_ID";
            ResultSet rs = statement.executeQuery(assign);

            while (rs.next()) {
                while (rs.getInt("appointment_id") == appointment_ID) {
                    appointment_ID++;
                }
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return appointment_ID;
    }

    /**
     * Database connection that sends appointments associated with current user ID that will be within 15 min.
     * Populates the HomeForm.fxml table.
     * @return
     */
    public static ObservableList<Appointments> getSoonAppointments() {
        ObservableList<Appointments> soonAppointmentsList = FXCollections.observableArrayList();
        try {
            String SQL = "SELECT Appointment_ID, Title, Start FROM client_schedule.appointments WHERE Start BETWEEN UTC_TIMESTAMP AND TIMESTAMPADD(MINUTE,15,UTC_TIMESTAMP) AND User_ID =" + UserDAO.currentUserID;
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String startTime = rs.getString("Start");

                String startTimeLocal = DateTimeModifiers.convertToLocalTime(startTime);
                Appointments soonAppointments = new Appointments(appointmentId, title, startTimeLocal);
                soonAppointmentsList.add(soonAppointments);
            }
        } catch (SQLException | ParseException e) {
            throw new RuntimeException(e);
        }
        return soonAppointmentsList;
    }

    /**
     * Populates all appointments to table and selects certain parameters needed for appointment table.
     * Located on AppointmentsForm.fxml
     * @return
     */
    public static ObservableList<Appointments> getAllAppointments() {

        ObservableList<Appointments> appointmentList = FXCollections.observableArrayList();

        try {
            String SQL = "SELECT appointments.Appointment_ID, appointments.Title, appointments.Description, appointments.Location, appointments.Type, appointments.Start, appointments.End, appointments.Customer_ID, appointments.User_ID, appointments.Contact_ID, contacts.contact_Name FROM appointments JOIN contacts ON appointments.contact_ID = contacts.contact_ID";

            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                String startTime = rs.getString("Start");
                String endTime = rs.getString("End");
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int contactID = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");

                String startTimeLocal = DateTimeModifiers.convertToLocalTime(startTime);
                String endTimeLocal = DateTimeModifiers.convertToLocalTime(endTime);


                Appointments appointment = new Appointments(appointmentId, title, description, location, type, startTimeLocal, endTimeLocal, customerID, userID, contactID, contactName);
                appointmentList.add(appointment);
            }
        } catch (SQLException | ParseException ex) {
            ex.printStackTrace();
        }


        Collections.sort(appointmentList, (a1, a2) -> a1.getAppointmentID()-a2.getAppointmentID());
        return appointmentList;
    }

    /**
     * Populates text/combo boxes with SQL data from appointments
     * @param appointmentID
     * @param title
     * @param description
     * @param location
     * @param type
     * @param localStartTime
     * @param localEndTime
     * @param customerID
     * @param userID
     * @param contactID
     */
    public static void editAppointment(int appointmentID, String title, String description, String location, String type, String localStartTime, String localEndTime, int customerID, int userID, int contactID) {
        Timestamp utcTime = DateTimeModifiers.getTimestamp();

        try {
            Statement stmt = DatabaseConnection.getConnection().createStatement();
            String edit = "UPDATE appointments SET Title='" + title + "', Description='" + description + "', Location='" + location + "', Type='" + type + "', Start='" + localStartTime + "', End='" + localEndTime + "', Last_update='" + utcTime + "', Last_Updated_By='script', Customer_ID=" + customerID + ", User_ID=" + userID + ", Contact_ID=" + contactID + " WHERE Appointment_ID=" + appointmentID;
            stmt.executeUpdate(edit);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Sets time parameters for Eastern headquarter parameters.
     * @return
     */
    public static ObservableList availableTimes() {
        ObservableList<String> availableTimeList = FXCollections.observableArrayList();

        String[] timeList = {"13:00", "13:15", "13:30", "13:45", "14:00", "14:15", "14:30", "14:45", "15:00", "15:15", "15:30", "15:45", "16:00", "16:15", "16:30", "16:45", "17:00", "17:15", "17:30", "17:45", "18:00", "18:15", "18:30", "18:45", "19:00", "19:15", "19:30", "19:45", "20:00", "20:15", "20:30", "20:45", "21:00", "21:15", "21:30", "21:45", "22:00", "22:15", "22:30", "22:45", "23:00", "23:15", "23:30", "23:45", "00:00", "00:15", "00:30", "00:45", "01:00", "01:15", "01:30", "01:45", "02:00", "02:15", "02:30", "02:45", "03:00"};


        Arrays.stream(timeList).forEach(i -> {
            try {
                availableTimeList.add(DateTimeModifiers.timeslotConverter(i));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        return availableTimeList;
    }

    /**
     * Radio button connection that populates table with only appointments this month.
     * @return
     * @throws ParseException
     */
    public static ObservableList getAppointmentsThisMonth() throws ParseException{
        String currentDate = DateTimeModifiers.getDate().toString();
        String yearMonth = currentDate.substring(0, 7);
        ObservableList<Appointments> appointmentsThisMonth = FXCollections.observableArrayList();
        try {
            String SQL = "SELECT appointments.Appointment_ID, appointments.Title, appointments.Description, appointments.Location, appointments.Type, appointments.Start, appointments.End, appointments.Customer_ID, appointments.User_ID, appointments.Contact_ID, contacts.contact_Name FROM appointments JOIN contacts ON appointments.contact_ID = contacts.contact_ID";

            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                String startTime = rs.getString("Start");
                String endTime = rs.getString("End");
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int contactID = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");

                String startTimeLocal = DateTimeModifiers.convertToLocalTime(startTime);
                String endTimeLocal = DateTimeModifiers.convertToLocalTime(endTime);

                if (startTime.substring(0, 7).equals(yearMonth)) {
                    Appointments appointment = new Appointments(appointmentId, title, description, location, type, startTimeLocal, endTimeLocal, customerID, userID, contactID, contactName);
                    appointmentsThisMonth.add(appointment);
                }

            }
        } catch (SQLException | ParseException ex) {
            ex.printStackTrace();
        }
        return appointmentsThisMonth;
    }

    /**
     * Radio button connection that populates table with only appointments this week.
     * @return
     */
    public static ObservableList getAppointmentsThisWeek() {
        LocalDate today = LocalDate.now(ZoneId.systemDefault());
        int weekOfYear = today.get(WeekFields.of(Locale.getDefault()).weekOfYear());


        ObservableList<Appointments> appointmentThisWeek = FXCollections.observableArrayList();

        try {
            String SQL = "SELECT appointments.Appointment_ID, appointments.Title, appointments.Description, appointments.Location, appointments.Type, appointments.Start, appointments.End, appointments.Customer_ID, appointments.User_ID, appointments.Contact_ID, contacts.contact_Name FROM appointments JOIN contacts ON appointments.contact_ID = contacts.contact_ID";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                String startTime = rs.getString("Start");
                String endTime = rs.getString("End");
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int contactID = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");

                String startTimeLocal = DateTimeModifiers.convertToLocalTime(startTime);
                String endTimeLocal = DateTimeModifiers.convertToLocalTime(endTime);
                LocalDate appointDay = LocalDate.parse(startTimeLocal.substring(0, 10));
                int appointWeekOfYear = appointDay.get(WeekFields.of(Locale.getDefault()).weekOfYear());

                if (appointWeekOfYear == weekOfYear) {
                    Appointments appointment = new Appointments(appointmentId, title, description, location, type, startTimeLocal, endTimeLocal, customerID, userID, contactID, contactName);
                    appointmentThisWeek.add(appointment);
                }
            }
        } catch (SQLException | ParseException ex) {
            ex.printStackTrace();
        }
        return appointmentThisWeek;
    }

    /**
     * deletes appointments from table
     * @param appointment_id
     * @param type
     */
    public static void delAppointment(int appointment_id, String type) {
        try{
            Statement statement = DatabaseConnection.getConnection().createStatement();
            String del = "DELETE FROM appointments WHERE Appointment_ID =" + appointment_id;
            statement.executeUpdate(del);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * When modify button is pressed, dates are temporarily changed for updates simplicity and Error with updates logic. Still runs time parameters check properly.
     * Avoid throwing error by competing with the table's own time/date selection.
     * @param appointment_id
     * @param type
     */
    public static void delPartAppointment(int appointment_id, String type) {
        try{
            Statement statement = DatabaseConnection.getConnection().createStatement();
            String updateStart = "UPDATE appointments SET Start = '2022-01-01 01:01:01' WHERE Appointment_ID=" + appointment_id;
            String  updateEnd= "UPDATE appointments SET End = '2022-01-01 01:01:02' WHERE Appointment_ID=" + appointment_id;
            statement.executeUpdate(updateEnd);
            statement.executeUpdate(updateStart);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

