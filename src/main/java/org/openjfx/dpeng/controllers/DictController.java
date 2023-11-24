package org.openjfx.dpeng.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.openjfx.dpeng.database.dao.VietAnhDAO;
import org.openjfx.dpeng.database.dao.DictionaryDAO;
import org.openjfx.dpeng.database.model.Word;

import com.voicerss.tts.AudioCodec;
import com.voicerss.tts.AudioFormat;
import com.voicerss.tts.Languages;
import com.voicerss.tts.VoiceParameters;
import com.voicerss.tts.VoiceProvider;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.web.WebView;

public class DictController implements Initializable {
    private static ArrayList<String> historySearch = new ArrayList<String>();
    private static ArrayList<String> historyVASearch = new ArrayList<String>();
    private static ArrayList<String> historyAVSearch = new ArrayList<String>();
    public static Word currWordResult = null;
    private static final String voiceApi = "8aa655c9fcfd4f23b8e11593e22f9131";
    private static final String keyWordVoicePath = "./voices/word.mp3";
    private static MediaPlayer keyWordPlayer;
    private static String currentDict = "en";
    private static String lastKeyWord = "";

    public static void stopAudio() {
        if (keyWordPlayer != null && keyWordPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            keyWordPlayer.stop();
            keyWordPlayer.dispose();
        }
    }

    public static String convertTextToHtml(String text) {
        String htmlResult = "";
        String[] lines = text.split(System.lineSeparator());

        for (String line : lines) {
            if (line.startsWith("Từ: ")) {
                line = line.substring(4).trim();
                line = capFirstLetter(line);

                htmlResult += "<h2 class=\"nameWord\">" + line + "</h2>\n";
            }
            else if (line.startsWith("Phát âm:")) {
                int posOfPron = line.indexOf("/");
                String pronOfWord = posOfPron < 0 ? "" : line.substring(posOfPron).trim();

                htmlResult += "<h3 class=\"pronounWord\">" + pronOfWord + "</h3>\n";
            }
            else if (line.startsWith("Loại:")) {
                line = line.substring(6).trim();
                line = capFirstLetter(line);
    
                htmlResult += "<h4 class = \"typeWord\">" + line + "</h4>\n";
            }
            else if (line.startsWith("Nghĩa:")) {
                line = line.substring(7).trim();
                line = capFirstLetter(line);
    
                htmlResult += "<h5 class=\"meanWord\">" + line + "</h5>\n";
            }
            else if (line.startsWith("Ví dụ:")) {
                line = line.substring(7).trim();
                int posOfTrans = line.indexOf(". Dịch:");
                if (posOfTrans > 0) {
                    String example = line.substring(0, posOfTrans);
                    String tmp = line.substring(posOfTrans);
                    String translate = tmp.substring(8);

                    htmlResult += "<h6 class=\"exampleWord\">\n"
                    + "<p>" + example + ": </p>\n"
                    + "<p>" + translate + "</p>\n"
                    + "</h6>\n";
                } else {
                    String example = line;
                    String translate = "";

                    htmlResult += "<h6 class=\"exampleWord\">\n"
                    + "<p>" + example + ": </p>\n"
                    + "<p>" + translate + "</p>\n"
                    + "</h6>\n";
                }   

            }

        }

        return htmlResult;
    }

    private static final String resultHTMLTemplate = """
        <html>
        <head>
            <style>
                html {
                    background-color: #fff;
                }
                body * {
                    margin: 0;
                }
                .nameWord {
                    font-family: Georgia, 'Times New Roman', Times, serif;
                    color: black;
                    font-size: 34px;
                    margin: 0 0 15 0;
                }
        
                .pronounWord {
                    font-size: 26px;
                    color: #333;
                    font-weight: 500;
                    margin: 0 0 15 0;
                }
        
                .typeWord {
                    font-family: Arial, Helvetica, sans-serif;
                    display: inline-block;
                    background-color: #a6a6a670;
                    color: #7d42f1;
                    padding: 8px;
                    border-radius: 8px;
                    margin: 15 0 15 10;
                    font-size: 18px;
                    font-weight: 800;
                }
        
                .meanWord {
                    position: relative;
                    font-family: Arial, Helvetica, sans-serif;
                    display: block;
                    color: #333;
                    margin: 0 0 15 50;
                    font-size: 18px;
                    font-weight: 700;
                }
        
                .meanWord::before {
                    position: absolute;
                    content: "";
                    left: -16;
                    border-left: solid 8px #2e6df6 ;
                    display: block;
                    height: 20px;
                }
                
                .exampleWord {
                    position: relative;
                    font-size: 18px;
                    font-weight: 700;
                    font-family: Arial, Helvetica, sans-serif;
                    margin: 0 0 15 80;
                }
                
                .exampleWord::before {
                    position: absolute;
                    content: "";
                    top: 6;
                    left: -24;
                    border-left: solid 14px #ff9c07 ;
                    display: block;
                    height: 8px;
                }
                
                .exampleWord p:first-child {
                    color: #1299B7;
                    display: inline;
                    font-style: italic;
                }
        
                .exampleWord p:last-child {
                    color: #333;
                    display: inline;
                    font-weight: 400;
                }
        
            </style>
        </head>
        <body>
        </body>
        </html>
    """;

    public static String capFirstLetter(String word) {
        if (word == null || word.isEmpty()) {
            return word;
        }

        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    @FXML
    private Button addWordButton;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchWordField;

    @FXML
    private Label suggestAndHistoryLabel;
    
    @FXML
    private VBox suggestAndHistoryBox;

    @FXML
    private AnchorPane addWordPane;

    @FXML
    private ChoiceBox<String> dictChoice;

    @FXML
    private TextArea resultSearchArea;

    @FXML
    private WebView resultWordView;

    public void updateResultView() {
        if (currWordResult == null) {
            resultWordView.getEngine().loadContent("");
            return;
        }

        String fullResult = resultHTMLTemplate.replace("<body>", "<body>" + currWordResult.getHtmlDescription());
        resultWordView.getEngine().loadContent(fullResult);
    }

    // @FXML
    // void addWord(ActionEvent event) {
    //     AppController.showAddWordView();
    // }
    
    /**
     * Display RESULT
     * @param event
     */

    @FXML
    void searchExactWord(ActionEvent event) {
        String exactKWord = searchWordField.getText();

        Word exactWord = null;
        if (currentDict.equals("vi")) {
            exactWord = VietAnhDAO.getInstance().selectByKeyWord(exactKWord);
        } else {
            exactWord = DictionaryDAO.getInstance().selectByKeyWord(exactKWord);
        }

        if (exactWord != null) {
            currWordResult = exactWord;
            updateResultView();
            addWordToHistory(exactKWord);
        } else {
            showNotFoundWordView();
        }
    }


    public void showNotFoundWordView() {
        addWordPane.setVisible(true);
        addWordPane.toFront();
    }

    public void hideNotFoundWordView() {
        addWordPane.setVisible(false);
        addWordPane.toBack();
    }

    public static void addOrEditWord(String keyWord, String desWord) {
        String textDes = "Từ: " + keyWord + "\n" + desWord;
        String htmlDes = convertTextToHtml(textDes);

        Word newWord = new Word(keyWord, textDes, htmlDes);
        if (currentDict.equals("vi")) {
            VietAnhDAO.getInstance().insert(newWord);
        } else {
            DictionaryDAO.getInstance().insert(newWord);
        }

        currWordResult = newWord;
        addWordToHistory(capFirstLetter(currWordResult.getKey()));
        
    }

    public static void addWordToHistory(String word) {
        if (currentDict.equals("vi")) {
            historySearch = historyVASearch;
        } else {
            historySearch = historyAVSearch;
        }

        if (historySearch.contains(word)) {
            historySearch.remove(word);
        }

        while (historySearch.size() >= 70) {
            historySearch.remove(historySearch.size() - 1);
        }

        historySearch.add(0, word);
    }

    public void addWordToResultView(String kword) {
        Word wordResult = null;
        if (currentDict.equals("vi")) {
            wordResult = VietAnhDAO.getInstance().selectByKeyWord(kword);
        } else {
            wordResult = DictionaryDAO.getInstance().selectByKeyWord(kword);
        }
        currWordResult = wordResult;

        String fullResult= resultHTMLTemplate.replace("<body>", "<body>" + wordResult.getHtmlDescription());
        resultWordView.getEngine().loadContent(fullResult);
    }

    public void showWordHistory() {
        hideNotFoundWordView();
        suggestAndHistoryLabel.setText("Gần đây");
        suggestAndHistoryBox.getChildren().clear();
        for (String word : historySearch) {
            Button historyButton = new Button(capFirstLetter(word)); 
            historyButton.getStyleClass().addAll("historyWordButton", "hoverCursor", "pressEffect");
            historyButton.setOnAction(event -> {
                addWordToResultView(historyButton.getText());
            });

            suggestAndHistoryBox.getChildren().add(historyButton);
        }
    }

    public void showWordSuggestions(String keyWord) {
        if (keyWord.isBlank()) {
            showWordHistory();
            return;
        }

        suggestAndHistoryLabel.setText("Kết quả");
        ArrayList<String> suggestWords = new ArrayList<>();
        if (currentDict.equals("vi")) {
            suggestWords = VietAnhDAO.getInstance().suggestByKeyWord(keyWord);
        } else {
            suggestWords = DictionaryDAO.getInstance().suggestByKeyWord(keyWord);
        }

        suggestAndHistoryBox.getChildren().clear();
        
        if (suggestWords.size() == 0) {
            showNotFoundWordView();
        } else {
            hideNotFoundWordView();
        }

        for (String suggestWord : suggestWords) {
            Button suggestButton = new Button(capFirstLetter(suggestWord)); 
            suggestButton.getStyleClass().addAll("historyWordButton", "hoverCursor", "pressEffect");
            suggestButton.setOnAction(event -> {
                addWordToHistory(suggestButton.getText());
                addWordToResultView(suggestButton.getText());
            });

            suggestAndHistoryBox.getChildren().add(suggestButton);
        }
    }
    
    /**
     * PLAY SOUND OF WORD
     */
    @FXML
    void playWordSound(ActionEvent event) throws Exception {
        if (currWordResult == null) {
            return;
        }

        if (keyWordPlayer != null && keyWordPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            keyWordPlayer.stop();
            keyWordPlayer.dispose();
            return;
        }

        if (!(lastKeyWord.equals(currWordResult.getKey()))) {
            //update lastInput
            lastKeyWord = currWordResult.getKey();

            //Write new voice input
            VoiceProvider tts = new VoiceProvider(voiceApi);
            
            VoiceParameters params = new VoiceParameters(lastKeyWord,
                                    currentDict.equals("vi") ? 
                                    Languages.Vietnamese : Languages.English_UnitedStates);

            params.setCodec(AudioCodec.MP3);
            params.setFormat(AudioFormat.Format_44KHZ.AF_44khz_16bit_stereo);
            params.setBase64(false);
            params.setSSML(false);
            params.setRate(0);
            
            byte[] voice = tts.speech(params);
    
            FileOutputStream fos = new FileOutputStream(keyWordVoicePath);
            fos.write(voice, 0, voice.length);
            fos.flush();
            fos.close();

            //update keyWordPlayer
            stopAudio();
            File kWordVoiceFile = new File(keyWordVoicePath);
            String kWordFullPath = kWordVoiceFile.toURI().toString();
            
            Media mediaKWord = new Media(kWordFullPath);
            keyWordPlayer = new MediaPlayer(mediaKWord);
            keyWordPlayer.play();
            keyWordPlayer.setOnEndOfMedia(() -> {
                stopAudio();
            });
            
        } else {
            //Play current voice input
            stopAudio();
            File kWordVoiceFile = new File(keyWordVoicePath);
            String kWordFullPath = kWordVoiceFile.toURI().toString();
            
            Media mediaKWord = new Media(kWordFullPath);
            keyWordPlayer = new MediaPlayer(mediaKWord);
            keyWordPlayer.play();
            keyWordPlayer.setOnEndOfMedia(() -> {
                stopAudio();
            });
        }
    }

    /**
     * DELETE WORD
     */
    @FXML
    private void confirmToDeleteWord(ActionEvent event) {
        if (currWordResult == null) {
            return;
        }
        
        Alert alertDelete = new Alert(AlertType.ERROR);
        alertDelete.setTitle("XOÁ TỪ");
        alertDelete.setHeaderText("BẠN ĐANG XOÁ TỪ??");
        alertDelete.setContentText("Hãy chắc chắn rằng bạn muốn xoá từ này!");

        ButtonType CANCEL = new ButtonType("Thôi không xoá nữa", ButtonData.CANCEL_CLOSE);
        alertDelete.getButtonTypes().add(CANCEL);

        ButtonType OK = new ButtonType("Xoá luônnn", ButtonData.OK_DONE);
        alertDelete.getDialogPane().getStylesheets().add(getClass().getResource("/org/openjfx/dpeng/css/alert.css").toExternalForm());
        alertDelete.getButtonTypes().remove(ButtonType.OK);
        alertDelete.getButtonTypes().add(OK);

        if (alertDelete.showAndWait().get() == OK) {
            historySearch.remove(capFirstLetter(currWordResult.getKey()));
            resultWordView.getEngine().loadContent("");
            if (currentDict.equals("vi")) {
                VietAnhDAO.getInstance().delete(currWordResult);
            } else {
                DictionaryDAO.getInstance().delete(currWordResult);
            }
            showWordSuggestions(searchWordField.getText());
            currWordResult = null;
            lastKeyWord = "";
        }
    }

    /**
     * EDIT WORD
     */

    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addWordPane.setVisible(false);
        addWordPane.toBack();
        
        if (currentDict.equals("vi")) {
            dictChoice.setValue("Việt - Anh");
        } else {
            dictChoice.setValue("Anh - Việt");
        }
        dictChoice.getItems().addAll("Anh - Việt", "Việt - Anh");
        dictChoice.valueProperty().addListener(new ChangeListener<String>(){

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.equals("Việt - Anh")) {
                    if (currentDict.equals("en")) {
                        historyAVSearch = historySearch;
                        currentDict = "vi";
                        historySearch = historyVASearch;
                        currWordResult = null;
                        updateResultView();
                        showWordHistory();
                    }
                } else {
                    if (currentDict.equals("vi")) {
                        historyVASearch = historySearch;
                        currentDict = "en";
                        historySearch = historyAVSearch;
                        currWordResult = null;
                        updateResultView();
                        showWordHistory();
                    }
                }
            }

        });

        searchWordField.textProperty().addListener((observable, oldValue, newValue) -> {
            showWordSuggestions(newValue);
        });

        if (currWordResult != null) {
            String fullResult= resultHTMLTemplate.replace("<body>", "<body>" + currWordResult.getHtmlDescription());
            resultWordView.getEngine().loadContent(fullResult);
        }
        if (historySearch != null) {
            showWordHistory();
        }
    }
}
