package org.openjfx.dpeng.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class GameController implements Initializable {
    PlayGameController playGameController; //CONTROLLER của pane play game
    ArrayList<AnchorPane> topicsGame = new ArrayList<AnchorPane>(); // chứa các TOPIC của game (dạng anchor pane)
    ArrayList<String> topicGameNames = new ArrayList<>();// chứa các tên của TOPIC theo thứ tự của #topicsGame
    int currTopicSelected = 0; // lưu vị trí của topic đang được chọn, dùng để thêm css và lấy ra tên của topic đang được chọn
    String currLabelTopic = ""; // lưu tên của TOPIC đang được chọn
    
    @FXML
    private AnchorPane gamePane; // Pane của phần chọn topic, không bao gồm play game pane
    
    @FXML 
    private AnchorPane playGamePane; //Pane của #playGame.fxml

    @FXML
    private Button playGameButton; // nút "Chơi"

    // trở về màn hình chọn topic cho game (ẩn pane playgame rồi cho nó về sau là xong)
    public void backToGameHome() {
        playGamePane.setVisible(false);
        playGamePane.toBack();
        playGamePane.setDisable(true);
        gamePane.setDisable(false); // Enable mấy cái topic lên để còn bấm được
    }

    // Action của nút "Chơi", hiện pane playgame lên rồi gọi hàm #loadDictAndQuestion của playgame controller (dùng controller để gọi được hàm này)
    @FXML
    void switchToPlayGameView(ActionEvent event) {
        updateSelectedTopic();
        playGameController.loadDictAndQuestion(currLabelTopic); // Bấm vào để xem chi tiết hàm này
        playGamePane.setVisible(true);
        playGamePane.toFront();
        playGamePane.setDisable(false);
        gamePane.setDisable(true); // Disable mấy cái topic đi lỡ bấm nhầm là ăn cook
    }

    // hàm update lại trạng thái của topic được chọn, gọi khi click vào topic bất kì
    public void updateSelectedTopic() {
        for (int i = 0; i < topicsGame.size(); i++) { //duyệt qua từng topic (kiểu AnchorPane)
            if (i != currTopicSelected) {
                topicsGame.get(i).getStyleClass().removeAll("active"); //nếu chưa duyệt tới #currTopicSelected thì xoá class active của topic này (nếu có)
            } else {
                topicsGame.get(i).getStyleClass().add("active"); // nếu duyệt tới #currTopicSelected thì thêm class active cho topic này
                currLabelTopic = topicGameNames.get(i); // cập nhật tên của topic đang chọn
            }
        }
    }

    // hàm lắng nghe sự kiện click vào topic, cập nhật vị trí topic hiện tại và gọi hàm #updateSelectedTopic
    public void listenClickTopic() {
        int setCurr = 0; // biến để cập nhật topic đang chọn, mỗi topic tương ứng với một #setCurr riêng
        if (topicsGame.size() != 0) {
            for (AnchorPane topic : topicsGame) { // duyệt từng topic trong danh sách #topicsGame
                final int setCurrFinal = setCurr; // muốn truyền được vào hàm sự kiện thì phải là final
                topic.setOnMouseClicked(e -> { // hàm này chỉ chạy khi topic này được click vào
                    currTopicSelected = setCurrFinal; // cập nhật vị trí của topic đang được chọn
                    updateSelectedTopic(); // gọi hàm #updateSelectedTopic
                });

                setCurr++; // tăng vị trí của topic lên
            }
        }
    }

    // Hàm khởi tạo, chỉ chạy khi #game.fxml được render lên màn hình (đối tượng GameController được tạo)
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (Node topic : gamePane.getChildren()) { // duyệt từng con của #gamePane
            if (topic instanceof AnchorPane) { // nếu là AnchorPane thì là topic
                AnchorPane topicPane = (AnchorPane) topic; // DownCasting thành AnchorPane
                topicsGame.add(topicPane); // add topic vào #topicsGame (ArrayList chứa topic)

                for (Node child : topicPane.getChildren()) { // duyệt từng con của topic
                    if (child instanceof Label) {
                        Label label = (Label) child; // lấy ra label chứa tên của topic này
                        topicGameNames.add(label.getText().trim()); // add tên vào #topicGameNames sau khi đã xoá dấu cách thừa
                    }
                }
            }
        }

        listenClickTopic(); // Khi đã có list các topic, tiến hành lắng nghe sự kiện click chuột cho từng cái luôn

        try { // load luôn play game khi load game, đặt nó về sau để đỡ phải load sau này
            FXMLLoader playLoader = new FXMLLoader(getClass().getResource("/org/openjfx/dpeng/fxml/playGame.fxml"));
            AnchorPane playPane = playLoader.load();
            playGamePane.getChildren().add(playPane); // nhét AnchorPane lấy được từ load vào play game pane
            playGameController = playLoader.getController(); // lấy ra controller của play game rồi đưa ra ngoài biến global #playGameController để dùng tới đoạn bên trên
            playGameController.setGameController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (playGamePane != null) {
            playGamePane.setVisible(false); // load được rồi thì ẩn nó đi thôi
            playGamePane.toBack();
            playGamePane.setDisable(true); // cook nó về dưới đáy xã hội
            gamePane.setDisable(false); // Enable mấy cái topic lên để còn bấm được

            // Button backToGameHomeButton = (Button) playGamePane.lookup("#backToGameHomeButton"); // tìm kiếm cái nút "trở lại" ở trong play game pane ý
            // backToGameHomeButton.setOnAction(event -> {
            //     backToGameHome(); // khi bấm vào cái nút này thì cho nó gọi hàm #backToGameHome
            // });
        }


    }
}
