package org.openjfx.dpeng;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

import org.openjfx.dpeng.database.model.SoundHelper;
import org.openjfx.dpeng.database.model.TopicDict;
/**
 * JavaFX App.
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/org/openjfx/dpeng/fxml/appMenu.fxml"));
        Parent rootMenu = menuLoader.load();

        scene = new Scene(rootMenu);
        stage.setScene(scene);

        Image logo = new Image(getClass().getResourceAsStream("images/AppImage/logo.png"));
        stage.getIcons().add(logo);
        stage.setTitle("DPeng demoooooooooooooooo");
        
        TopicDict.loadAllDict();
        SoundHelper.loadAllSound();
        stage.resizableProperty().set(false);
        stage.show();
    }

    public static void setRoot(Parent root) throws IOException {
        scene.setRoot(root);
    }

    public static void main(String[] args) {
        launch();
    }
}