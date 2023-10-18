package org.openjfx.dpeng;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("fxml/login.fxml"));
        Parent rootLogin = loginLoader.load();

        scene = new Scene(rootLogin);
        stage.setScene(scene);

        Image logo = new Image(getClass().getResourceAsStream("images/AppImage/logo.png"));
        stage.getIcons().add(logo);

        stage.setTitle("DPeng demoooooooooooooooo");

        stage.show();
    }

    public static void setRoot(Parent root) throws IOException {
        scene.setRoot(root);
    }

    public static void main(String[] args) {
        launch();
    }
}