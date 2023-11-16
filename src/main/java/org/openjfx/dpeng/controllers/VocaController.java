package org.openjfx.dpeng.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

public class VocaController implements Initializable {
    private FlashCardController flashCardController;
    private ArrayList<AnchorPane> topics = new ArrayList<>();
    private ArrayList<String> topicNames = new ArrayList<>();
    private static int currTopicSelected = 0;


    @FXML
    private AnchorPane flashCardAnchor;

    @FXML
    private Button learnFlashcardButton;

    @FXML
    private FlowPane flowTopicContainer;

    @FXML
    private ImageView activedTopicImageView;

    @FXML
    private Label activedTopicLabel;

    public void backToVocaView() {
        flashCardAnchor.setVisible(false);
        flashCardAnchor.toBack();
    }

    @FXML
    void switchToFlashcardView(ActionEvent event) {
        flashCardController.updateTopicTitle(topicNames.get(currTopicSelected));
        flashCardController.loadDictAndCard(activedTopicLabel.getText());
        flashCardAnchor.setVisible(true);
        flashCardAnchor.toFront();
    }

    public void updateSelectedTopic() {
        for (int i = 0; i < topics.size(); i++) {
            if (i != currTopicSelected) {
                topics.get(i).getStyleClass().removeAll("active");
            } else {
                topics.get(i).getStyleClass().add("active");
                updateActivedTopic();
            }
        }
    }

    public void updateActivedTopic() {
        Image activeTopicImage = new Image(getClass().getResource(
                "/org/openjfx/dpeng/images/VocabularyImage/topic" + (currTopicSelected + 1) + ".png").toString());
        activedTopicImageView.setImage(activeTopicImage);
        activedTopicLabel.setText(topicNames.get(currTopicSelected));
    }

    public void updateTopics() {
        if (flowTopicContainer == null) {
            System.out.println("Not found Topic!");
            return;
        }

        topics.clear();
        topicNames.clear();
        for (Node element : flowTopicContainer.getChildren()) {
            if (element instanceof AnchorPane) {
                AnchorPane elementPane = (AnchorPane) element;
                topics.add(elementPane);

                for (Node child : elementPane.getChildren()) {
                    if (child instanceof Label) {
                        Label label = (Label) child;
                        topicNames.add(label.getText());
                    }
                }
            }
        }

        listenClickTopic();
        updateSelectedTopic();
        listenClickStar();

    }

    public void listenClickTopic() {
        int setCurr = 0;
        if (topics.size() != 0) {
            for (AnchorPane topic : topics) {
                final int setCurrFinal = setCurr;
                topic.setOnMouseClicked(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        currTopicSelected = setCurrFinal;
                        updateSelectedTopic();
                    }

                });

                setCurr++;
            }
        }
    }

    public void listenClickStar() {
        int indexOfStar = 2;

        for (AnchorPane anchor : topics) {
            if (anchor.getChildren().size() > indexOfStar) {
                ImageView starView = (ImageView) anchor.getChildren().get(indexOfStar);
                if (starView != null) {
                    starView.setOnMouseClicked(event -> {
                        String currPath = starView.getImage().getUrl().toString();
                        if (currPath.contains("starIcon")) {
                            currPath = currPath.replace("starIcon.png", "starActiveIcon.png");
                        } else {
                            currPath = currPath.replace("starActiveIcon.png", "starIcon.png");
                        }
                        starView.setImage(new Image(currPath));
                    });
                }
            }
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            FXMLLoader flashLoader = new FXMLLoader(getClass().getResource("/org/openjfx/dpeng/fxml/flashcard.fxml"));
            AnchorPane flashCardPane = flashLoader.load();
            flashCardAnchor.getChildren().add(flashCardPane);
            flashCardController = flashLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (flashCardAnchor != null) {
            flashCardAnchor.setVisible(false);
            flashCardAnchor.toBack();

            Button backToVocaButton = (Button) flashCardAnchor.lookup("#backToVocaButton");
            backToVocaButton.setOnAction(event -> {
                backToVocaView();
            });
        }

        updateTopics();

    }

}
