package org.openjfx.dpeng;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class AppController implements Initializable {

    @FXML
    private AnchorPane appAnchor;

    public AnchorPane getAppAnchor() {
        return appAnchor;
    }

    @FXML
    private Button dictButton;

    @FXML
    private Button gameButton;

    @FXML
    private Button translateButton;

    @FXML
    private Button vocaButton;

    @FXML
    private void switchToDictView(ActionEvent event) {
        System.out.println("kkkk");
    }

    @FXML
    private void switchToGameView(ActionEvent event) {
        System.out.println("kkkk");

    }

    @FXML
    private void switchToTranslateView(ActionEvent event) {
        System.out.println("kkkk");
    }

    @FXML
    private void switchToVocaView(ActionEvent event) {
        System.out.println("kkkk");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            FXMLLoader translateLoader = new FXMLLoader(getClass().getResource("fxml/translate.fxml"));
            AnchorPane rootTranslate = translateLoader.load();

            appAnchor.getChildren().add(rootTranslate);
        }
        catch (IOException e) {
            e.printStackTrace();
        }


    }

}
