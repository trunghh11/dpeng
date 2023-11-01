package org.openjfx.dpeng;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ResourceBundle;

import com.google.cloud.translate.*;
import com.voicerss.tts.AudioCodec;
import com.voicerss.tts.AudioFormat;
import com.voicerss.tts.Languages;
import com.voicerss.tts.VoiceParameters;
import com.voicerss.tts.VoiceProvider;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;

public class TranslateController implements Initializable {
    private static String currentInput = "";
    private static String currentTranslate = "";
    private static String currentSourceLang = "en";
    private static String currentTargetLang = "vi";
    private static boolean isTranslateButtonOn = false;
    private static final String apiKey = "AIzaSyCc3HAo5z9i3rD1fCQSSLOmBjJPngFOhVA"; // Thay YOUR_API_KEY bằng khóa API thực tế của bạn.
    private static final String voiceApi = "8aa655c9fcfd4f23b8e11593e22f9131";
    private static final String inputVoicePath = "./voices/input.mp3";
    private static final String translateVoicePath = "./voices/translate.mp3";
    private static MediaPlayer inputPlayer;
    private static MediaPlayer translatePlayer;
    private static String lastInput = "";
    private static String lastTranslate = "";
    private static String lastSourceLang = "en";
    private static String lastTargetLang = "vi";



    @FXML
    private Button copyButton;

    @FXML
    private Button sourceLang;

    @FXML
    private Button targetLang;

    @FXML
    private Button reverseLangButton;

    @FXML
    private TextArea inputArea;

    @FXML
    private TextArea translateArea;

    @FXML
    private Button translateButton;

    @FXML
    private Button inputSound;

    @FXML
    private Button translateSound;

    public static void stopAudio() {
        if (inputPlayer != null && inputPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            inputPlayer.stop();
            inputPlayer.dispose();
        }

        if (translatePlayer != null && translatePlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            translatePlayer.stop();
            translatePlayer.dispose();
        }
    }
    
    @FXML
    void playInputSound(ActionEvent event) throws Exception {
        if (inputPlayer != null && inputPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            inputPlayer.stop();
            inputPlayer.dispose();
            return;
        }

        if (!(lastInput.equals(currentInput) && lastSourceLang.equals(currentSourceLang))) {
            //update lastInput
            lastInput = currentInput;
            lastSourceLang = currentSourceLang;

            //Write new voice input
            VoiceProvider tts = new VoiceProvider(voiceApi);
            
            VoiceParameters params = new VoiceParameters(lastInput,
                                    currentSourceLang.equals("vi") ? 
                                    Languages.Vietnamese : Languages.English_UnitedStates);

            params.setCodec(AudioCodec.MP3);
            params.setFormat(AudioFormat.Format_44KHZ.AF_44khz_16bit_stereo);
            params.setBase64(false);
            params.setSSML(false);
            params.setRate(0);
            
            byte[] voice = tts.speech(params);
    
            FileOutputStream fos = new FileOutputStream(inputVoicePath);
            fos.write(voice, 0, voice.length);
            fos.flush();
            fos.close();

            //update inputPlayer
            stopAudio();
            File inputVoiceFile = new File(inputVoicePath);
            String inputFullPath = inputVoiceFile.toURI().toString();
            
            Media mediaInput = new Media(inputFullPath);
            inputPlayer = new MediaPlayer(mediaInput);
            inputPlayer.play();
            inputPlayer.setOnEndOfMedia(() -> {
                stopAudio();
            });
            
        } else {
            //Play current voice input
            stopAudio();
            File inputVoiceFile = new File(inputVoicePath);
            String inputFullPath = inputVoiceFile.toURI().toString();
            
            Media mediaInput = new Media(inputFullPath);
            inputPlayer = new MediaPlayer(mediaInput);
            inputPlayer.play();
            inputPlayer.setOnEndOfMedia(() -> {
                stopAudio();
            });
        }
    }

    @FXML
    void playTranslateSound(ActionEvent event) throws Exception {
        if (translatePlayer != null && translatePlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            translatePlayer.stop();
            translatePlayer.dispose();
            return;
        }

        if (!(lastTranslate.equals(currentTranslate) && lastTargetLang.equals(currentTargetLang))) {
            //update lasttranslate
            lastTranslate = currentTranslate;
            lastTargetLang = currentTargetLang;

            //Write new voice translate
            VoiceProvider tts = new VoiceProvider(voiceApi);
            
            VoiceParameters params = new VoiceParameters(lastTranslate,
                                    currentTargetLang.equals("vi") ? 
                                    Languages.Vietnamese : Languages.English_UnitedStates);

            params.setCodec(AudioCodec.MP3);
            params.setFormat(AudioFormat.Format_44KHZ.AF_44khz_16bit_stereo);
            params.setBase64(false);
            params.setSSML(false);
            params.setRate(0);
            
            byte[] voice = tts.speech(params);
    
            FileOutputStream fos = new FileOutputStream(translateVoicePath);
            fos.write(voice, 0, voice.length);
            fos.flush();
            fos.close();

            //update translatePlayer
            File translateVoiceFile = new File(translateVoicePath);
            String translateFullPath = translateVoiceFile.toURI().toString();
            
            stopAudio();
            Media mediaTranslate = new Media(translateFullPath);
            translatePlayer = new MediaPlayer(mediaTranslate);
            translatePlayer.play();
            translatePlayer.setOnEndOfMedia(() -> {
                stopAudio();
            });
            
        } else {
            //Play current voice translate
            stopAudio();
            File translateVoiceFile = new File(translateVoicePath);
            String translateFullPath = translateVoiceFile.toURI().toString();
            
            Media mediaTranslate = new Media(translateFullPath);
            translatePlayer = new MediaPlayer(mediaTranslate);
            translatePlayer.play();
            translatePlayer.setOnEndOfMedia(() -> {
                stopAudio();
            });
        }
    }

    @FXML
    void reverseLanguage(ActionEvent event) {
        currentSourceLang = currentSourceLang.equals("en") ? "vi" : "en";
        currentTargetLang = currentTargetLang.equals("en") ? "vi" : "en";

        sourceLang.setText(currentSourceLang.equals("en") ? "Anh" : "Việt");
        targetLang.setText(currentTargetLang.equals("en") ? "Anh" : "Việt");
    }

    @FXML
    @SuppressWarnings("deprecation")
    void translate(ActionEvent event) {
        Translate translate = TranslateOptions.newBuilder()
                .setApiKey(apiKey)
                .build()
                .getService();
        
        Translation translation = translate.translate(
            inputArea.getText(),
            Translate.TranslateOption.sourceLanguage(currentSourceLang),
            Translate.TranslateOption.targetLanguage(currentTargetLang),
            Translate.TranslateOption.format("text")
        );
        translateArea.setText(translation.getTranslatedText());

        currentInput = inputArea.getText();
        currentTranslate = translateArea.getText();
        translateSound.setDisable(false);
        copyButton.setDisable(false);
    }

    @FXML
    void whenInputTranslate(KeyEvent event) {
        stopAudio();
        String input = inputArea.getText();
        currentInput = input;
        if (!input.isBlank()) {
            translateButton.setDisable(false);
            translateButton.setOpacity(1);
            isTranslateButtonOn = true;

            inputSound.setDisable(false);

            translateArea.setDisable(false);
        } else {
            translateButton.setDisable(true);
            translateButton.setOpacity(0.45);
            translateArea.setText("");
            currentTranslate = translateArea.getText();
            isTranslateButtonOn = false;

            inputSound.setDisable(true);
            translateSound.setDisable(true);
            copyButton.setDisable(true);

            translateArea.setDisable(true);
            translateArea.setOpacity(1);
        }
    }

    @FXML
    void copyTranslateToClipbroad(ActionEvent event) {
        // Khởi tạo Clipboard
        Clipboard clipboard = Clipboard.getSystemClipboard();

        // Tạo đối tượng ClipboardContent
        ClipboardContent content = new ClipboardContent();

        // Đặt nội dung văn bản vào ClipboardContent
        content.putString(translateArea.getText());

        // Đặt ClipboardContent vào Clipboard
        clipboard.setContent(content);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (!isTranslateButtonOn) {
            translateButton.setDisable(true);
            translateButton.setOpacity(0.45);

            inputSound.setDisable(true);
        } else {
            translateButton.setDisable(false);
            translateButton.setOpacity(1);

            inputSound.setDisable(false);
        }

        if (currentTranslate.isBlank()) {
            translateArea.setDisable(true);
            translateArea.setOpacity(1);

            translateSound.setDisable(true);
            copyButton.setDisable(true);
        } else {
            translateArea.setDisable(false);

            translateSound.setDisable(false);
            copyButton.setDisable(false);
        }

        inputArea.setText(currentInput);
        translateArea.setText(currentTranslate);

        sourceLang.setText(currentSourceLang.equals("en") ? "Anh" : "Việt");
        targetLang.setText(currentTargetLang.equals("en") ? "Anh" : "Việt");

    }
}
