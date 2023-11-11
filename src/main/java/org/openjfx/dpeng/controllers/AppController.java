package org.openjfx.dpeng.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
public class AppController implements Initializable {
    // private boolean isTranslate, isVoca, isGame, isDict;
    private static boolean isMenuOn = false;
    private static Timeline menuSlideInAnimation;
    private static Timeline menuSlideOutAnimation;

    @FXML
    private AnchorPane appAnchor;

    public AnchorPane getAppAnchor() {
        return appAnchor;
    }

    private Label menuLabel = new Label("MENU"),
            translateLabel = new Label("DỊCH"),
            vocaLabel = new Label("TỪ VỰNG"),
            gameLabel = new Label("GAME"),
            dictLabel = new Label("TỪ ĐIỂN");
    
    @FXML
    private HBox menuHBox, translateHBox, vocaHBox, gameHBox, dictHBox;

    @FXML
    private AnchorPane appBackground;

    @FXML
    private AnchorPane appWrapper;

    @FXML
    private AnchorPane appMenu;

    @FXML
    private Button menuButton;

    @FXML
    private Button translateButton;

    @FXML
    private Button vocaButton;
    
    @FXML
    private Button gameButton;
    
    @FXML
    private Button dictButton;

    //Handle Add Word View
    @FXML
    private Button addAndEditButton, closeAddWordButton;

    @FXML
    private AnchorPane addWordPane;

    @FXML
    private TextField keyWordAdd;

    @FXML
    private TextArea desWordAdd;

    public void showAddAndEditWordView(String kWord, String dWord) {
        if (kWord.isEmpty()) {
            keyWordAdd.setDisable(false);
        } else {
            keyWordAdd.setDisable(true);
        }

        keyWordAdd.setText(kWord);
        desWordAdd.setText(dWord);

        addWordPane.setDisable(false);
        addWordPane.setVisible(true);
        addWordPane.toFront();
    }

    public void confirmAddAndEditWord(ActionEvent event) {
        DictController.addOrEditWord(keyWordAdd.getText(), desWordAdd.getText());
        keyWordAdd.setText(null);
        desWordAdd.setText(null);
        
        closeAddWordView(event);
    }

    @FXML
    void closeAddWordView(ActionEvent event) {
        addWordPane.setDisable(true);
        addWordPane.setVisible(false);
        addWordPane.toBack();
    }

    // Sliding MENU
    @FXML
    void hideMenuWhenCick(MouseEvent event) {
        if (isMenuOn) {
            slideOutMenu();
        }
    }

    @FXML
    void slideMenu(ActionEvent event) {
        boolean isSlidingMenu = (menuSlideInAnimation.getStatus() == Animation.Status.RUNNING 
                        || menuSlideOutAnimation.getStatus() == Animation.Status.RUNNING);

        if (isSlidingMenu) return;
        
        if (!isMenuOn) {
            slideInMenu();
        } else {
            slideOutMenu();
        }
    }

    private void slideInMenu() {
        isMenuOn = true;
        menuSlideInAnimation.play();
        menuSlideInAnimation.setOnFinished(event -> {
            menuHBox.getChildren().add(menuLabel);
            translateHBox.getChildren().add(translateLabel) ;
            vocaHBox.getChildren().add(vocaLabel) ;
            gameHBox.getChildren().add(gameLabel);
            dictHBox.getChildren().add(dictLabel);
        });

        appBackground.setOpacity(0.2);
        
        appMenu.getStyleClass().removeAll("backGround_0_7");
        appMenu.getStyleClass().add("backGround_0_9");

        appWrapper.setDisable(true);
    }

    private void slideOutMenu() {
        isMenuOn = false;
        menuHBox.getChildren().removeAll(menuLabel);
        translateHBox.getChildren().removeAll(translateLabel) ;
        vocaHBox.getChildren().removeAll(vocaLabel) ;
        gameHBox.getChildren().removeAll(gameLabel);
        dictHBox.getChildren().removeAll(dictLabel);
        menuSlideOutAnimation.play();

        appBackground.setOpacity(1);
        
        appMenu.getStyleClass().removeAll("backGround_0_9");
        appMenu.getStyleClass().add("backGround_0_7");
        
        appWrapper.setDisable(false);
    }

    @FXML
    private void switchToTranslateView(ActionEvent event) {
        try {
            if (isMenuOn) {
                slideOutMenu();
            }
            FXMLLoader translateLoader = new FXMLLoader(getClass().getResource("/org/openjfx/dpeng/fxml/translate.fxml"));
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
            if (isMenuOn) {
                slideOutMenu();
            }
            FXMLLoader vocaLoader = new FXMLLoader(getClass().getResource("/org/openjfx/dpeng/fxml/vocabulary.fxml"));
            AnchorPane rootVoca = vocaLoader.load();

            appAnchor.getChildren().clear();
            appAnchor.getChildren().add(rootVoca);

            translateButton.getStyleClass().removeAll("active");
            vocaButton.getStyleClass().add("active");
            gameButton.getStyleClass().removeAll("active");
            dictButton.getStyleClass().removeAll("active");

            TranslateController.stopAudio();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void switchToGameView(ActionEvent event) {
        try {
            if (isMenuOn) {
                slideOutMenu();
            }
            FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("/org/openjfx/dpeng/fxml/game.fxml"));
            AnchorPane rootGame = gameLoader.load();

            appAnchor.getChildren().clear();
            appAnchor.getChildren().add(rootGame);

            translateButton.getStyleClass().removeAll("active");
            vocaButton.getStyleClass().removeAll("active");
            gameButton.getStyleClass().add("active");
            dictButton.getStyleClass().removeAll("active");

            TranslateController.stopAudio();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void switchToDictView(ActionEvent event) {
        try {
            if (isMenuOn) {
                slideOutMenu();
            }
            FXMLLoader dictLoader = new FXMLLoader(getClass().getResource("/org/openjfx/dpeng/fxml/dictionary.fxml"));
            AnchorPane rootDict = dictLoader.load();

            appAnchor.getChildren().clear();
            appAnchor.getChildren().add(rootDict);
            Button showAddWordButton = (Button) rootDict.lookup("#addWordButton");
            Button showEditWordButton = (Button) rootDict.lookup("#editWordButton");

            showAddWordButton.setOnAction( e -> {
                showAddAndEditWordView("", "");
            });

            showEditWordButton.setOnAction( e -> {
                if (DictController.currWordResult == null) {
                    return;
                }
                String text = DictController.currWordResult.getTextDescription();
                String[] texts = text.split(System.lineSeparator(), 2);

                String kWord = "", dWord = "";
                kWord = texts[0].substring(4);
                if (texts.length == 2) {
                    dWord = texts[1];
                } 
                
                showAddAndEditWordView(kWord, dWord);
            });

            addAndEditButton.setOnAction(e -> {
                confirmAddAndEditWord(event);

                DictController controller = dictLoader.getController();
                controller.updateResultView();
            });

            translateButton.getStyleClass().removeAll("active");
            vocaButton.getStyleClass().removeAll("active");
            gameButton.getStyleClass().removeAll("active");
            dictButton.getStyleClass().add("active");

            TranslateController.stopAudio();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            FXMLLoader translateLoader = new FXMLLoader(getClass().getResource("/org/openjfx/dpeng/fxml/translate.fxml"));
            AnchorPane rootTranslate = translateLoader.load();

            appAnchor.getChildren().add(rootTranslate);

        }
        catch (IOException e) {
            e.printStackTrace();
        }

        FadeTransition fadeBackground = new FadeTransition(Duration.millis(350), appBackground);
        appBackground.opacityProperty().addListener((observable, oldValue, newValue) -> {
            double oldOpacity = (double) oldValue;
            double newOpacity = (double) newValue;
        
            // Thực hiện các animation tương ứng với thay đổi opacity
            fadeBackground.setFromValue(oldOpacity);
            fadeBackground.setToValue(newOpacity);
            fadeBackground.setInterpolator(Interpolator.LINEAR);
            fadeBackground.play();
        });

        // SlideIn animation
        menuSlideInAnimation = new Timeline();
        Duration duration = Duration.seconds(0.25); // Thời gian animation (2 giây)

        // Tạo KeyValue cho bước chuyển đổi chiều rộng
        KeyValue keyValue = new KeyValue(appMenu.prefWidthProperty(), 265);

        // Tạo KeyFrame cho bước chuyển đổi chiều rộng
        KeyFrame keyFrame = new KeyFrame(duration, keyValue);

        // Thêm KeyFrame vào Timeline
        menuSlideInAnimation.getKeyFrames().add(keyFrame);

        //SlideOut Animation
        menuSlideOutAnimation = new Timeline();
        Duration duration2 = Duration.seconds(0.25); // Thời gian animation (2 giây)

        // Tạo KeyValue cho bước chuyển đổi chiều rộng
        KeyValue keyValue2 = new KeyValue(appMenu.prefWidthProperty(), 110);

        // Tạo KeyFrame cho bước chuyển đổi chiều rộng
        KeyFrame keyFrame2 = new KeyFrame(duration2, keyValue2);

        // Thêm KeyFrame vào Timeline
        menuSlideOutAnimation.getKeyFrames().add(keyFrame2);

        //Hide add word view
        addWordPane.setDisable(true);
        addWordPane.setVisible(false);
        addWordPane.toBack();

    }

}
