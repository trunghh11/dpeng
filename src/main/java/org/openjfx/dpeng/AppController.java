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
    // private boolean isTranslate, isVoca, isGame, isDict;

    @FXML
    private AnchorPane appAnchor;

    public AnchorPane getAppAnchor() {
        return appAnchor;
    }

    @FXML
    private Button translateButton;

    @FXML
    private Button vocaButton;
    
    @FXML
    private Button gameButton;
    
    @FXML
    private Button dictButton;


    @FXML
    private void switchToTranslateView(ActionEvent event) {
        try {
            FXMLLoader translateLoader = new FXMLLoader(getClass().getResource("fxml/translate.fxml"));
            AnchorPane rootTranslate = translateLoader.load();

            appAnchor.getChildren().clear();
            appAnchor.getChildren().add(rootTranslate);

            translateButton.getStyleClass().add("active");
            vocaButton.getStyleClass().removeAll("active");
            gameButton.getStyleClass().removeAll("active");
            dictButton.getStyleClass().removeAll("active");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void switchToVocaView(ActionEvent event) {
        try {
            FXMLLoader vocaLoader = new FXMLLoader(getClass().getResource("fxml/vocabulary.fxml"));
            AnchorPane rootVoca = vocaLoader.load();

            appAnchor.getChildren().clear();
            appAnchor.getChildren().add(rootVoca);

            translateButton.getStyleClass().removeAll("active");
            vocaButton.getStyleClass().add("active");
            gameButton.getStyleClass().removeAll("active");
            dictButton.getStyleClass().removeAll("active");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void switchToGameView(ActionEvent event) {
        try {
            FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("fxml/game.fxml"));
            AnchorPane rootGame = gameLoader.load();

            appAnchor.getChildren().clear();
            appAnchor.getChildren().add(rootGame);

            translateButton.getStyleClass().removeAll("active");
            vocaButton.getStyleClass().removeAll("active");
            gameButton.getStyleClass().add("active");
            dictButton.getStyleClass().removeAll("active");
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void switchToDictView(ActionEvent event) {
        try {
            FXMLLoader dictLoader = new FXMLLoader(getClass().getResource("fxml/dictionary.fxml"));
            AnchorPane rootDict = dictLoader.load();

            appAnchor.getChildren().clear();
            appAnchor.getChildren().add(rootDict);

            translateButton.getStyleClass().removeAll("active");
            vocaButton.getStyleClass().removeAll("active");
            gameButton.getStyleClass().removeAll("active");
            dictButton.getStyleClass().add("active");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
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
