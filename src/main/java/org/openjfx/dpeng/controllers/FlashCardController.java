package org.openjfx.dpeng.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.openjfx.dpeng.database.model.TopicDict;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class FlashCardController implements Initializable {

    @FXML
    private Button backToVocaButton;

    @FXML
    private Label topicTitleLabel;

    @FXML
    private Button prefCardButton;

    @FXML
    private Button nextCardButton;

    @FXML
    private Label currLabelCard;

    @FXML
    private AnchorPane currentCard;

    @FXML
    private AnchorPane nextCard;

    @FXML
    private Label nextLabelCard;

    @FXML
    private AnchorPane prefCard;

    @FXML
    private Label prefLabelCard;

    @FXML
    private Label progressCardLabel;

    private ParallelTransition slideNextAnimation;
    private ParallelTransition slidePrefAnimation;
    private TranslateTransition cardSlideFromRight, cardSlideToLeft, cardSlideFromLeft, cardSlideToRight;;
    private int currIndexText = 0;
    private int totalOfWords = 0;
    private boolean isWordLabel = true;
    private Map<String, String> currDict = new HashMap<String, String>();
    private ArrayList<String> cardLabelList = new ArrayList<>();

    @FXML
    void shuffleWords(ActionEvent event) {
        Collections.shuffle(cardLabelList);

        isWordLabel = true;
        currLabelCard.setText(cardLabelList.get(currIndexText));
        nextLabelCard.setText(cardLabelList.get(Math.min(currIndexText + 1, cardLabelList.size() - 1)));
        prefLabelCard.setText(cardLabelList.get(Math.max(currIndexText - 1, 0)));
    }

    public void updateTopicTitle(String topicTitle) {
        topicTitleLabel.setText("Topic: " + topicTitle);
    }

    public void loadDictAndCard(String topicTitle) {
        if (currDict.size() > 0 || cardLabelList.size() > 0) {
            cardLabelList.clear();
        }

        currIndexText = 0;
        isWordLabel = true;
        switch (topicTitle) {
            case "Animal":
                currDict = TopicDict.animalDict;
                break;
            case "Health":
                currDict = TopicDict.healthDict;
                break;
            case "Home & Garden":
                currDict = TopicDict.homeDict;
                break;
            case "Art & Carft":
                currDict = TopicDict.artDict;
                break;
            case "Appearance":
                currDict = TopicDict.appearanceDict;
                break;
            case "Science":
                currDict = TopicDict.scienceDict;
                break;
            case "Color & Shape":
                currDict = TopicDict.colorshapeDict;
                break;
            case "Food & Drinks":
                currDict = TopicDict.fooddrinksDict;
                break;
            case "Hobby":
                currDict = TopicDict.hobbyDict;
                break;
            default:
                break;
        }

        currDict.forEach((word, meaning) -> {
            cardLabelList.add(word);
        });
        totalOfWords = cardLabelList.size();

        if (cardLabelList.size() > 0) {
            progressCardLabel.setText((currIndexText + 1) + "/" + totalOfWords);
            updateButton();
            currLabelCard.setText(cardLabelList.get(Math.max(currIndexText - 1, 0)));
            currLabelCard.setText(cardLabelList.get(currIndexText));
            nextLabelCard.setText(cardLabelList.get(Math.min(currIndexText + 1, cardLabelList.size() - 1)));
        }

    }

    public void updateButton() {
        if (currIndexText == cardLabelList.size() - 1) {
            nextCardButton.setDisable(true);
        } else {
            nextCardButton.setDisable(false);
        }

        if (currIndexText == 0) {
            prefCardButton.setDisable(true);
        } else {
            prefCardButton.setDisable(false);
        }
    }

    @FXML
    void goToNextCard(ActionEvent event) {
        if (slideNextAnimation != null && slideNextAnimation.getStatus() == Animation.Status.STOPPED) {
            isWordLabel = true;
            slideNextAnimation.play();
            currIndexText = Math.min(currIndexText + 1, cardLabelList.size() - 1);
            progressCardLabel.setText((currIndexText + 1) + "/" + totalOfWords);
            updateButton();

            slideNextAnimation.setOnFinished(e -> {
                currentCard.setTranslateX(0);
                nextCard.setTranslateX(900);

                currLabelCard.setText(cardLabelList.get(currIndexText));
                nextLabelCard.setText(cardLabelList.get(Math.min(currIndexText + 1, cardLabelList.size() - 1)));
                prefLabelCard.setText(cardLabelList.get(Math.max(currIndexText - 1, 0)));
            });
        }

    }

    @FXML
    void goToPrefCard(ActionEvent event) {
        if (slidePrefAnimation != null && slidePrefAnimation.getStatus() == Animation.Status.STOPPED) {
            isWordLabel = true;
            slidePrefAnimation.play();
            currIndexText = Math.max(currIndexText - 1, 0);
            progressCardLabel.setText((currIndexText + 1) + "/" + totalOfWords);
            updateButton();

            slidePrefAnimation.setOnFinished(e -> {
                currentCard.setTranslateX(0);
                prefCard.setTranslateX(-900);

                currLabelCard.setText(cardLabelList.get(currIndexText));
                prefLabelCard.setText(cardLabelList.get(Math.max(currIndexText - 1, 0)));
                nextLabelCard.setText(cardLabelList.get(Math.min(currIndexText + 1, cardLabelList.size() - 1)));
            });
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        RotateTransition rotateOut = new RotateTransition(Duration.seconds(0.25), currentCard);
        rotateOut.setFromAngle(0);
        rotateOut.setToAngle(-90);
        rotateOut.setAxis(Rotate.X_AXIS);
        rotateOut.setInterpolator(Interpolator.LINEAR);

        RotateTransition rotateIn = new RotateTransition(Duration.seconds(0.25), currentCard);
        rotateIn.setFromAngle(90);
        rotateIn.setToAngle(0);
        rotateIn.setAxis(Rotate.X_AXIS);
        rotateIn.setInterpolator(Interpolator.LINEAR);

        SequentialTransition flipCardAnimation = new SequentialTransition(rotateOut, rotateIn);

        currentCard.setOnMouseClicked(event -> {
            if (flipCardAnimation != null && flipCardAnimation.getStatus() == Animation.Status.STOPPED) {
                flipCardAnimation.play();
                currLabelCard.setText("");
                flipCardAnimation.setOnFinished(e -> {
                    if (isWordLabel) {
                        currLabelCard.setText(currDict.get(cardLabelList.get(currIndexText)));
                        isWordLabel = false;
                    } else {
                        isWordLabel = true;
                        currLabelCard.setText(cardLabelList.get(currIndexText));
                    }
                });
            }
        });

        // next card animation
        cardSlideToLeft = new TranslateTransition(Duration.seconds(0.5), currentCard);
        cardSlideToLeft.setFromX(0);
        cardSlideToLeft.setToX(-900);

        // Xoay thẻ tiếp theo vào màn hình (trượt vào)
        cardSlideFromRight = new TranslateTransition(Duration.seconds(0.5), nextCard);
        cardSlideFromRight.setFromX(900);
        cardSlideFromRight.setToX(0);

        // Pref Card Animation
        cardSlideToRight = new TranslateTransition(Duration.seconds(0.5), currentCard);
        cardSlideToRight.setFromX(0);
        cardSlideToRight.setToX(900);

        // Xoay thẻ tiếp theo vào màn hình (trượt vào)
        cardSlideFromLeft = new TranslateTransition(Duration.seconds(0.5), prefCard);
        cardSlideFromLeft.setFromX(-900);
        cardSlideFromLeft.setToX(0);

        slideNextAnimation = new ParallelTransition(cardSlideToLeft, cardSlideFromRight);
        slidePrefAnimation = new ParallelTransition(cardSlideToRight, cardSlideFromLeft);

    }

}
