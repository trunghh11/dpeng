package org.openjfx.dpeng.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;

import org.openjfx.dpeng.database.model.TopicDict;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.util.Duration;

public class PlayGameController implements Initializable {
    Map<String, String> currDictGame = new HashMap<String, String>(); // lưu các từ của topic hiện tại ( theo kiểu từ - nghĩa ý )
    ArrayList<String> answerList = new ArrayList<>(); // lưu các câu trả lời nè
    ArrayList<String> questionList = new ArrayList<>(); // lưu các câu hỏi theo từng câu trả lời nè (vì câu trả lời là key nên khi random chỉ cần random câu trả lời)
    String currAnswer = ""; // lưu câu trả lời của câu hỏi hiện tại nè
    ArrayList<String> answerCharacters = new ArrayList<>(); // lưu các letter của câu trả lời nè
    String[] messagesCorrect = {"Đúng rồiiii đcmmmmmmm!!!!", "Ôiii, đỉnh quáaaa!!!",
                                 "Chuẩn cơm mẹ nấu!!!", "Chính xác luôn!!!", 
                                 "Sao biết hay vậy!!!", "Sai vào đâu được!!!", 
                                 "Cho 100 điểm!", "Hay quá cậu ơiiii"};
    String[] messagesWrong = {"Sai mất rồi...", "Đừng lo, bạn vẫn đang học mà, cố lên!", "Tiếc quá!", 
                                "Thử lại vào lần sau nhé!", "Ủaaa", "Có chắc là học bài rồi chưa?", 
                                "Học dzữ chưa?", "Có làm được không thì bảo?"};
    

    int currQuestionGameIndex = 0; // lưu vị trí của câu hỏi hiện tại, để hiện lên thanh tiến trình cũng như lấy câu trả lời đúng
    int totalQuestion; // lưu tổng câu hỏi hiện tại nè
    int countFilledLetters = 0;

    @FXML
    private Button backToGameHomeButton; // cái nút "trở lại" ý
    
    @FXML
    private ImageView heart1; // Trái tim đầu tiên <3

    @FXML
    private ImageView heart2; // Trái tim thứ 2 đồ <3
    
    @FXML
    private ImageView heart3; // Trái tim cuối cùng TˆT
    
    @FXML
    private Button musicButton; // Nút âm nhạc, yayy

    @FXML
    private Button helpButton; // Nút cấp cứu, ựa
    
    @FXML
    private Label progressWordLabel; // Cái này là chỉ câu hỏi hiện tại là câu số mấy
    
    @FXML
    private Label currentScoreLabel; // Cái này là điểm ý
    
    @FXML
    private Label questionLabel; // Cái này để hiện câu hỏi đó
    
    @FXML
    private FlowPane fillLetterFlowPane; // Cái này lưu các letter trống, ảo diệu vl
    
    @FXML
    private FlowPane answerLetterFlowPane; // Cái này để lưu các letter trả lời đồ, cũng ảo diệu vl

    @FXML
    private FlowPane messageFlowPane; // Cái này để hiện các câu chửi mắng, khen ngợi

    @FXML
    private ProgressBar timeProgress; // Cái này để chạy thời gian

    // Hàm này cập nhật lại câu hỏi, gọi khi next câu hỏi (khi mới hiện play game, trả lời sai, trả lời đúng)
    public void updateQuestion() {
        questionLabel.setText(questionList.get(currQuestionGameIndex)); // lấy câu hỏi tại vị trí câu hỏi hiện tại ra rồi nhét vào #questionLabel
        progressWordLabel.setText((currQuestionGameIndex + 1) + "/" + totalQuestion); // cập nhật tiến trình câu hỏi vcl
        currAnswer = answerList.get(currQuestionGameIndex).toLowerCase(); // lấy câu trả lời đúng của câu hỏi này, cho nó vào #currAnswer

        answerCharacters.clear(); // cái này clear hết các letter (nếu có) của câu trả lời trước đó
        // Duyệt qua từng letter của #currAnswer để nhét vào #answerCharacters, mỗi letter là 1 string cho dễ xử lí
        for(int i = 0; i < currAnswer.length(); i++) {
            answerCharacters.add(String.valueOf(currAnswer.charAt(i))); // add từng cái vào #answerCharacters
        }

        updateLetters(); // Cái này để update 2 cái FlowPane của letter trống và letter trả lời, bấm vào để đọc
        listenClickToLetters(); // Này để lắng nghe các sự kiện click vào letter nói chung, bấm vào để đọc

    }

    // Hàm này để update các letter với câu trả lời hiện tại, gọi khi hiện play game, next câu hỏi
    public void updateLetters() {
        if (answerCharacters != null) { // Nếu #answerCharacters không chứa letter nào thì cook
            fillLetterFlowPane.getChildren().clear(); // phải clear những gì đã cũ
            answerLetterFlowPane.getChildren().clear(); // ruồng bỏ những gì đã qua
            for(String letter : answerCharacters) { // lặp qua từng letter của answer hiện tại
                Label letterLabel = new Label(letter); // tạo một cái label chứa letter đang xét
                
                AnchorPane letterFillPane = new AnchorPane(); // tạo cái pane của letter trống
                AnchorPane letterAnswerPane = new AnchorPane(); // tạo cái pane của letter trả lời
                letterFillPane.getStyleClass().add("letterUnfillBox"); // thêm mã css cho letter trống
                letterAnswerPane.getChildren().add(letterLabel); // thêm cái label chứa letter vào letter trả lời
                letterAnswerPane.getStyleClass().add("letterOfWordBox"); // thêm css cho letter trả lời đồ

                fillLetterFlowPane.getChildren().add(letterFillPane); // xong xuôi thì nhét cái letter trống mới vào #fillLetterFlowPane
                answerLetterFlowPane.getChildren().add(letterAnswerPane); // tương tự với letter trả lời
            }
        }
    }

    // Hàm lắng nghe sự kiện click vào letter nói chung, gọi khi hiện play game, khi letter thay đổi, khi next câu hỏi
    public void listenClickToLetters() {
        if (answerLetterFlowPane.getChildren() == null) { // thực ra cần kiểm tra null của 2 cái FLowPane nhưng nó có sự ăn khớp nên kiểm tra 1 là đủ
            return;
        }

        // Đầu tiên là lặp qua các letter trong #answerLetterFlowPane
        for (Node letter : answerLetterFlowPane.getChildren()) {
            if (letter instanceof AnchorPane) {
                AnchorPane letterPane = (AnchorPane) letter;
                Label letterLabel = (Label) letterPane.getChildren().get(0); // lấy ra label của letter này
                letterPane.setOnMouseClicked(e -> {
                    answerLetterFlowPane.getChildren().remove(letter); // khi click vào phải xoá luôn cả cái letter này trong #answerLetterFlowPane
                    fillLetter(letterLabel); // gọi hàm #fillLetter để fill letter được chọn vào letter trống đầu tiên, bấm vào để đọc thêm
                    checkAndHandleAnswer();
                });
            }
        }

        // Lặp qua các letter trong #fillLetterFlowPane
        for (Node letterFill : fillLetterFlowPane.getChildren()) {
            if (letterFill instanceof AnchorPane) {
                AnchorPane letterFillPane = (AnchorPane) letterFill; // lấy cả letter ra dưới dạng AnchorPane
                letterFillPane.setOnMouseClicked(e -> {
                    unFillLetter(letterFillPane); // thực hiện nhấc letter bị unFill để nhét vào #answerLetterFlowPane, bấm vào để đọc chi tiết
                });
            }
        }
    }

    // Cái hàm này để fill vào letter trống, được gọi khi click vào letter trả lời
    public void fillLetter(Label letterLabel) { // Nhận vào là cái Label của letter trả lời được bấm vào
        for (Node letterFill : fillLetterFlowPane.getChildren()) { // Lặp qua tất cả các cái letter trong #fillLetterFlowPane để tìm ra letter chưa fill
            if (letterFill instanceof AnchorPane) { // kiếm tra phải AnchorPane không, nếu phải thì nó là 1 letter
                AnchorPane letterFillPane = (AnchorPane) letterFill; // này tự hiểu chả biết nói sao
                if (letterFillPane.getChildren().size() == 0) { // gặp cái letter trống đầu tiên (không có label)
                    letterFillPane.getChildren().add(letterLabel); // add cái label được đưa vào vào cái letter trống này
                    letterFillPane.getStyleClass().add("filled"); // thêm css filled cho nó
                    countFilledLetters ++;
                    break; // cook khỏi vòng lặp
                }
            }
        }
        listenClickToLetters(); // cái này là do các letter được update nên lại phải lắng nghe lại sự kiện click của chúng nó
    }

    // Hàm này để undo lại các từ vừa bấm, gọi khi click vào các letter trống đã được fill
    public void unFillLetter(AnchorPane letterFilled) { // truyền vào AnchorPane là một letter
        if (letterFilled.getChildren().size() == 0) { // nếu không có con (không có label) thì cook, không làm gì cả
            return;
        }

        //nếu có con
        Label letterLabel = (Label) letterFilled.getChildren().get(0); // lấy ra cái label luôn vì chắc chắn nó có label (con đầu tiên luôn)
        letterFilled.getChildren().remove(letterLabel); // xoá cái label trong cái letter này đi
        letterFilled.getStyleClass().removeAll("filled"); // xoá cái class "filled" của cái letter này đi

        AnchorPane letterUnfillPane = new AnchorPane(); // tạo một cái letter AnchorPane mới
        letterUnfillPane.getChildren().add(letterLabel); // add thêm cái label của letter vừa xoá vào AnchorPane này
        letterUnfillPane.getStyleClass().add("letterOfWordBox"); // thêm cho nó class #letterOfWordBox
        answerLetterFlowPane.getChildren().add(letterUnfillPane); // thêm cái letter này vào FlowPane của letter trả lời

        countFilledLetters --;
        listenClickToLetters(); // do các letter được cập nhật nên phải lắng nghe lại từng cái
    }

    public void checkAndHandleAnswer() {
        if (countFilledLetters < answerCharacters.size()) {
            return;
        }

        String answerFromPlayer = getAnswerString();
        if (answerFromPlayer.equals(currAnswer)) {
            handleCorrectAnswer();
        } else {
            handleWrongAnswer();
        }
    }

    public void handleCorrectAnswer() {
        
        for (Node letter : fillLetterFlowPane.getChildren()) {
            RotateTransition correctAnimation = new RotateTransition(Duration.seconds(0.2));
            correctAnimation.setFromAngle(-15);
            correctAnimation.setToAngle(15);
            correctAnimation.setAutoReverse(true);
            correctAnimation.setCycleCount(3);

            letter.getStyleClass().add("correct");
            correctAnimation.setNode(letter);
            correctAnimation.play();

            correctAnimation.setOnFinished(e -> {
                Timeline timeline = new Timeline();
                KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.1), new KeyValue(letter.rotateProperty(), 0));
                timeline.getKeyFrames().add(keyFrame);
                timeline.play();
            });
        }
        
        Random random = new Random();
        int indexOfMessage = random.nextInt(8) ;
        Label messageCorrect = new Label(messagesCorrect[indexOfMessage]);
        messageCorrect.getStyleClass().add("messageCorrectLabel");
        timeProgress.setVisible(false);
        messageFlowPane.getChildren().add(messageCorrect);
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2));

        fadeTransition.setFromValue(1.0); // Giá trị hiển thị ban đầu
        fadeTransition.setToValue(0.0); // Giá trị ẩn đi
        fadeTransition.setCycleCount(1); // Chỉ chạy một lần
        fadeTransition.setAutoReverse(false); // Không lặp lại

        fadeTransition.setNode(messageCorrect);

        // Bắt đầu FadeTransition
        fadeTransition.play();
        fadeTransition.setOnFinished(e -> {
            messageFlowPane.getChildren().clear();
            timeProgress.setVisible(true);
        });



    }
    
    public void handleWrongAnswer() { 
        for (Node letter : fillLetterFlowPane.getChildren()) {
            RotateTransition wrongAnimation = new RotateTransition(Duration.seconds(0.15));
            wrongAnimation.setFromAngle(-10);
            wrongAnimation.setToAngle(10);
            wrongAnimation.setAutoReverse(true);
            wrongAnimation.setCycleCount(5);

            letter.getStyleClass().add("incorrect");
            wrongAnimation.setNode(letter);
            wrongAnimation.play();

            wrongAnimation.setOnFinished(e -> {
                Timeline timeline = new Timeline();
                KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.05), new KeyValue(letter.rotateProperty(), 0));
                timeline.getKeyFrames().add(keyFrame);
                timeline.play();
            });
        }

        Random random = new Random();
        int indexOfMessage = random.nextInt(8);

        Label messageWrong = new Label(messagesWrong[indexOfMessage]);
        messageWrong.getStyleClass().add("messageWrongLabel");
        timeProgress.setVisible(false);
        messageFlowPane.getChildren().add(messageWrong);
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2));

        fadeTransition.setFromValue(1.0); // Giá trị hiển thị ban đầu
        fadeTransition.setToValue(0.0); // Giá trị ẩn đi
        fadeTransition.setCycleCount(1); // Chỉ chạy một lần
        fadeTransition.setAutoReverse(false); // Không lặp lại

        fadeTransition.setNode(messageWrong);

        // Bắt đầu FadeTransition
        fadeTransition.play();
        fadeTransition.setOnFinished(e -> {
            messageFlowPane.getChildren().clear();
            timeProgress.setVisible(true);
        });
        
    }


    public String getAnswerString() {
        if (countFilledLetters == 0) {
            return "";
        }

        String result = "";

        for (Node letterFilledNode : fillLetterFlowPane.getChildren()) {
            if (letterFilledNode instanceof AnchorPane) {
                AnchorPane letterFilled = (AnchorPane) letterFilledNode;
                Label answerLabel = (Label) letterFilled.getChildren().get(0);
                result += answerLabel.getText();
            }
        }
        return result;
    }

    // Hàm này được gọi khi bấm nút "Chơi", nhận vào là tên của topic được chọn
    public void loadDictAndQuestion(String topicTitle) {
        if (currDictGame.size() > 0 || questionList.size() > 0) { // Đầu tiên phải xoá những gì đã cũ đi đó
            questionList.clear(); // xoá danh sách câu hỏi cũ nè
            answerList.clear(); // tự hỉu nka
        }

        countFilledLetters = 0;
        currQuestionGameIndex = 0; // cho cái index question bằng 0 để chạy từ đầu list
        switch (topicTitle) { // này hỉu được nèk
            case "Animal":
                currDictGame = TopicDict.animalDict;
                break;
            case "Health":
                currDictGame = TopicDict.healthDict;
                break;
            case "Home & Garden":
                currDictGame = TopicDict.homeDict;
                break;
            case "Art & Craft":
                currDictGame = TopicDict.artDict;
                break;
            case "Appearance":
                currDictGame = TopicDict.appearanceDict;
                break;
            case "Science":
                currDictGame = TopicDict.scienceDict;
                break;
            case "Color & Shape":
                currDictGame = TopicDict.colorshapeDict;
                break;
            case "Food & Drinks":
                currDictGame = TopicDict.fooddrinksDict;
                break;
            case "Hobby":
                currDictGame = TopicDict.hobbyDict;
                break;
            default:
                break;
        }

        // Hiểu đơn giản là lặp qua cái map Dict, gán key vào biến word và gán value vào biến meaning
        ArrayList<String> tmpDict = new ArrayList<>();
        currDictGame.forEach((word, meaning) -> {
            tmpDict.add(word); // answer là key
        });

        Collections.shuffle(tmpDict);
        for (int i = 0; i < 20; i++) {
            String word = tmpDict.get(i);
            answerList.add(word);
            questionList.add(currDictGame.get(word));
        }
        
        totalQuestion = questionList.size(); // lấy tổng câu hỏi ra, bao nhiêu câu hỏi thì sau này tính sau
        updateQuestion(); // thực hiện gọi hàm update question

    }

    // Do lúc mới khởi tạo thì nó cũng bị ẩn đi nên hàm init cũng chưa nói lên được nhiều điều
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

}
