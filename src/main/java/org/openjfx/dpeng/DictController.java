package org.openjfx.dpeng;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class DictController implements Initializable {
    @FXML
    private Button addWordButton;

    @FXML
    private Button searchButton;

    @FXML
    private AnchorPane addWordPane;

    @FXML
    void addWord(ActionEvent event) {
        System.out.println("added word!!");
    }

    @FXML
    void showAddWordView(ActionEvent event) {
        addWordPane.setVisible(true);
        addWordPane.toFront();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addWordPane.setVisible(false);
        addWordPane.toBack();
    }
}
