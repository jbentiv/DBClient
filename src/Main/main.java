package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
public class main extends Application {
    /**
     *Starts application on LoginForm.fxml
     * @param stage
     * @throws SQLException
     * @throws IOException
     */

    @Override
    public void start(Stage stage) throws SQLException, IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/LoginForm.fxml"));
        stage.setTitle("C195 - Jacob Bentivolio #001252668");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
    public static void main(String[] args) {
        launch();
    }
}
