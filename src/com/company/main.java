package com.company;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/FXML_TEST.fxml"));
        stage.setTitle("Home Screen");
        stage.setScene(new Scene(root));
        stage.show();

        System.out.println("Text from client");
    }
}
