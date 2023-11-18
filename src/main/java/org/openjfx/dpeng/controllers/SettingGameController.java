package org.openjfx.dpeng.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import org.openjfx.dpeng.database.model.SoundHelper;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;

public class SettingGameController implements Initializable {
    PlayGameController playGameControllerOfSetting;

    public void setPlayGameController(PlayGameController playGameController) {
        this.playGameControllerOfSetting = playGameController;
    }

    @FXML
    private Button exitGameButton;

    @FXML
    private ImageView musicSettingImage;

    @FXML
    private Slider musicVolumeSlider;

    @FXML
    private Button resumeGameButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        musicVolumeSlider.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                SoundHelper.setVolume(playGameControllerOfSetting.inGameSound, (double) newValue);  
            }
            
        });
    }

}
