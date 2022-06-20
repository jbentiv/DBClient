package Controller;

import DAO.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;
public class LoginController implements Initializable {
    public Button loginButton;
    public TextField loginUsernameBox;
    public PasswordField loginPasswordBox;
    public TextField loginLocationBox;
    public TextField loginLanguageBox;
    public Button exitButton;
    public Label passwordText;
    public Label usernameText;
    public Label scheduleLoginText;
    public Label invalidPrompt;
    boolean loginPass = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        languageLocationSet();
        /**
         * Sets date a time formats for initialized tables
         */
        DateFormat dateFormatUTC = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date timestampUTC = null;
        try {
            timestampUTC = dateFormatUTC.parse("2022-01-01 20:32:00");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        String timeLocal = dateFormatLocal.format(timestampUTC);

        System.out.println(timeLocal);
    }

    /**
     * Exits application and logs out user, system exit code 0.
     * @param actionEvent
     * @throws IOException
     */
    public void onButtonActionExitButton(ActionEvent actionEvent) throws IOException {

        System.exit(0);
    }
    public void setLocation() {
        /**
         * Sets timezone in login box
         */
        OffsetDateTime odt = OffsetDateTime.now ();
        ZoneOffset zoneOffset = odt.getOffset ();
        loginLocationBox.setText(String.valueOf(ZoneId.systemDefault() + "  " + zoneOffset + " (UTC)"));
        //System.out.println(ZoneId.systemDefault() + "  " + zoneOffset + "(UTC)");

    }

    /**
     * gets current timestamp in UTC time. See changes in code for SQL format to application preferred format
     * @return
     * @throws ParseException
     */
    private static Date getCurrentUtcTime() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat localDateFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
        return localDateFormat.parse( simpleDateFormat.format(new Date()) );
    }

    /**
     * setting time zone and language preferences for the login screen.
     * Determines the UTC difference and displays English of French text.
     */
    public void languageLocationSet() {

        OffsetDateTime odt = OffsetDateTime.now ();
        ZoneOffset zoneOffset = odt.getOffset ();
        loginLocationBox.setText(String.valueOf(ZoneId.systemDefault() + "  " + zoneOffset + " (UTC)"));
        ZoneId currentZoneID = ZoneId.systemDefault();
        Locale French = new Locale("fr", "FR");
        ResourceBundle rb = ResourceBundle.getBundle("Controller/French", Locale.FRENCH);

        /**
         * Changing from English to French with the use of French.properties through ResourceBundle
         */
        if(Locale.getDefault().getLanguage().equals("fr")) {
            Locale.setDefault(French);
            scheduleLoginText.setText((rb.getString("Schedule,Login")).replaceAll(","," "));
            usernameText.setText((rb.getString("Username")).replaceAll(","," "));
            passwordText.setText((rb.getString("Password")).replaceAll(","," "));
            loginButton.setText((rb.getString("Login")).replaceAll(","," "));
            exitButton.setText((rb.getString("Exit,Application")).replaceAll(","," "));
            loginLanguageBox.setText((rb.getString("English")).replaceAll(","," "));
        }
    }

    /**
     * Receives user input for Username and Password and determines validity in SQL database.
     * Uses if/else logic to determine what error messages to display for missing input.
     * @param actionEvent
     * @throws IOException
     * @throws SQLException
     */
    public void onButtonActionLoginButton(ActionEvent actionEvent) throws IOException, SQLException {
        String username = loginUsernameBox.getText();
        String password = loginPasswordBox.getText();
        invalidPrompt.setText("");

        boolean login = UserDAO.userLogin(username, password);

        Locale French = new Locale("fr", "FR");
        ResourceBundle rb = ResourceBundle.getBundle("Controller/French", Locale.FRENCH);

        if (loginUsernameBox.getText().isBlank() && loginPasswordBox.getText().isBlank()) {
            if(Locale.getDefault().getLanguage().equals("fr")) {
                Locale.setDefault(French);
                invalidPrompt.setText((rb.getString("Username,and,Password,Required")).replaceAll(",", " "));
            }else {
                invalidPrompt.setText("Username and Password Required");
            }
        }else {
            if (loginUsernameBox.getText().isBlank()) {
                if(Locale.getDefault().getLanguage().equals("fr")) {
                    Locale.setDefault(French);
                    invalidPrompt.setText((rb.getString("Did,you,forget,to,enter,your,username")).replaceAll(",", " "));
                }else {
                    invalidPrompt.setText("Did you forget to enter your username?");
                }
            }else {
                if (loginPasswordBox.getText().isBlank()) {
                    if(Locale.getDefault().getLanguage().equals("fr")) {
                        Locale.setDefault(French);
                        invalidPrompt.setText((rb.getString("Please,enter,your,password,too!")).replaceAll(",", " "));
                    }else {
                        invalidPrompt.setText("Please enter your password too!");
                    }
                }else {
                    if (login) {
                        UserDAO.currentUser = loginUsernameBox.getText();
                        loginPass = true;
                        Parent root = FXMLLoader.load(getClass().getResource("/view/HomeForm.fxml"));
                        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                        Scene scene = new Scene(root);
                        stage.setResizable(false);
                        stage.setScene(scene);
                        stage.show();

                    }
                    else {
                        if(Locale.getDefault().getLanguage().equals("fr")) {
                            Locale.setDefault(French);
                            invalidPrompt.setText((rb.getString("Sorry!,Username,and,Password,are,invalid")).replaceAll(",", " "));
                        }else {
                            invalidPrompt.setText("Sorry! Username and Password are invalid.");
                            loginPass = false;
                        }
                    }
                    trackLoginPass();
                }
            }
        }
    }

    /**
     * Tracks the login attempts, wrong or correct logins will be tacked in LoginAttempts.txt file
     * @throws IOException
     */
    public void trackLoginPass() throws IOException {
        Timestamp time = Timestamp.valueOf(LocalDateTime.now());

        FileWriter file = new FileWriter("LoginAttempts.txt", true);
        PrintWriter print = new PrintWriter(file);
        print.print("¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯ \n");
        print.print("LOGIN ATTEMPT: ");
        if (loginPass) {
            print.print("PASSED! \n");
        }
        else {
            print.print("          FAILED! \n");
        }
        print.print("Date/Time: " + time + "\n");
        print.print("Username: " + loginUsernameBox.getText() + "\n");
        print.print(("Password: " + loginPasswordBox.getText() + "\n\n"));
        print.close();
    }
}



































