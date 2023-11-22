package org.openjfx.dpeng;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

import java.io.IOException;


import org.openjfx.dpeng.database.model.SoundHelper;
import org.openjfx.dpeng.database.model.TopicDict;
/**
 * JavaFX App.
 */
public class App extends Application {

    private static Scene scene;
    private static AudioClip click;
    private static EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            click.play();
        }
    };

    public static void registerMouseEvent() {
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler);
    }

    public static void removeMouseEvent() {
        scene.removeEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/org/openjfx/dpeng/fxml/appMenu.fxml"));
        Parent rootMenu = menuLoader.load();

        scene = new Scene(rootMenu);
        stage.setScene(scene);


        Image logo = new Image(getClass().getResourceAsStream("images/AppImage/dpengLogo.png"));
        stage.getIcons().add(logo);
        stage.setTitle("DPeng - Ứng dụng học tiếng Anh siêu hiệu quả!");
        
        TopicDict.loadAllDict();
        SoundHelper.loadAllSound();

        click = SoundHelper.soundClick;
        registerMouseEvent();

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