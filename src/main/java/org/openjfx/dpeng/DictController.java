package org.openjfx.dpeng;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class DictController implements Initializable {
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

    @FXML
    private Button addWordButton;

    @FXML
    private Button searchButton;

    @FXML
    private AnchorPane addWordPane;

    @FXML
    private ChoiceBox<String> dictChoice;

    @FXML
    private TextArea resultSearchArea;

    @FXML
    private HTMLEditor resultWordEditor;

    @FXML
    private WebView resultWordView;

    // @FXML
    // void addWord(ActionEvent event) {
    //     AppController.showAddWordView();
    // }

    @FXML
    void showNotFoundWordView(ActionEvent event) {
        addWordPane.setVisible(true);
        addWordPane.toFront();
    }

    public static void addNewWordToDict(String keyWord, String typeWord, String desWord) {
        System.out.println("success to add new word!!");
        System.out.println("new word: " + keyWord + " type: " + typeWord + " des: " + desWord);
    }

    public String capFirstLetter(String word) {
        if (word == null || word.isEmpty()) {
            return word;
        }

        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    private String convertToHtml(String line) {
        line = line.trim();
        if (line.startsWith("@")) {
            line = line.substring(1);
            int posOfPron = line.indexOf("/"); 
            String word = line.substring(0, posOfPron);
            word = capFirstLetter(word);
            String pronOfWord = line.substring(posOfPron);

            return "<h2 class=\"nameWord\">" + word + "</h2>\n"
               + "<h3 class=\"pronounWord\">" + pronOfWord + "</h3>\n";
        }
        else if (line.startsWith("*")) {
            line = line.substring(1).trim();
            line = capFirstLetter(line);

            return "<h4 class = \"typeWord\">" + line + "</h4>\n";
        }
        else if (line.startsWith("-")) {
            line = line.substring(1).trim();
            line = capFirstLetter(line);

            return "<h5 class=\"meanWord\">" + line + "</h5>\n";
        }
        else if (line.startsWith("=")) {
            line = line.substring(1).trim();
            String[] words = line.split("\\+");
            String exSentence = words[0];
            exSentence = capFirstLetter(exSentence);
            String meanOfEx = words[1];
            meanOfEx = capFirstLetter(meanOfEx);

            return "<h6 class=\"exampleWord\">\n"
                + "<p>" + exSentence + ": </p>\n"
                + "<p>" + meanOfEx + "</p>\n"
                + "</h6>\n";
        }
        else {
            return "";
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addWordPane.setVisible(false);
        addWordPane.toBack();

        dictChoice.setValue("Anh - Việt");
        dictChoice.getItems().addAll("Anh - Việt", "Việt - Anh");

        WebEngine engineResult = resultWordView.getEngine();
        String examString = """
            @hard /hɑ:d/
            *  tính từ
            - cứng, rắn
            =hard as steel+ rắn như thép
            - rắn chắc, cứng cáp
            =hard muscles+ bắp thịt rắn chắc
            - cứng (nước)
            =hard water+ nước cứng (có hoà tan nhiều muối vô cơ)
            - thô cứng; gay gắt, khó chịu
            =hard feature+ những nét thô cứng
            =hard to the ear+ nghe khó chịu
            =hard to the eye+ nhìn khó chịu
            - hà khắc, khắc nghiệt, nghiêm khắc, không thương xót, không có tính cứng rắn, cứng cỏi; hắc, keo cú, chi li
            =a hard look+ cái nhìn nghiêm khắc
            =hard discipline+ kỷ luật khắc nghiệt
            =hard winter+ mùa đông khắc nghiệt
            =to be hard on (upon) somebody+ khắc nghiệt với ai
            - nặng, nặng nề
            =a hard blow+ một đòn nặng nề, một đòn trời giáng
            =hard of hearing+ nặng tai
            =a hard drinker+ người nghiện rượu nặng
            - gay go, khó khăn, gian khổ, hắc búa
            =a hard problem+ vấn đề hắc búa
            =hard lines+ số không may; sự khổ cực
            =to be hard to convince+ khó mà thuyết phục
            - không thể chối câi được, không bác bỏ được, rõ rành rành
            =hard facts+ sự việc rõ rành rành không thể chối câi được
            - cao, đứng giá (thị trường giá cả)
            - (ngôn ngữ học) kêu (âm)
            - bằng đồng, bằng kim loại (tiền)
            =hard cash+ tiền đồng, tiền kim loại
            - (từ Mỹ,nghĩa Mỹ) có nồng độ rượu cao
            =hard liquors+ rượu mạnh
            !hard and fast
            - cứng rắn, chặt chẽ (nguyên tắc, luật lệ...)
            !hard as nails
            - (xem) nail
            !a hard nut to crack
            - (xem) nut
            !a hard row to hoe
            - (xem) row
            *  phó từ
            - hết sức cố gắng, tích cực
            =to try hard to succeed+ cố gắng hết sức để thành công
            - chắc, mạnh, nhiều
            =to hold something hard+ nắm chắc cái gì
            =to strike hard+ đánh mạnh
            =to drink hard+ uống tuý luý, uống rượu như hũ chìm
            =it's raining hard+ trời mưa to
            - khắc nghiệt, nghiêm khắc; cứng rắn; hắc
            =don't use him too hard+ đừng khắc nghiệt quá đối với nó
            =to criticize hard+ phê bình nghiêm khắc
            - gay go, khó khăn, chật vật, gian khổ
            =to die hard+ chết một cách khó khăn
            =hard won+ thắng một cách chật vật
            - sát, gần, sát cạnh
            =hard by+ sát cạnh, gần bên
            =to follow hard after+ bám sát theo sau
            !to be hard bit
            - (xem) bit
            !to be hard pressed
            - (xem) press
            !to be hard put to bit
            - bị lâm vào hoàn cảnh khó khăn
            !to be hard up
            - cạn túi, cháy túi, hết tiền
            !to be hard up for
            - bí không bới đâu ra, bế tắc không tìm đâu ra (cái gì...)
            !to be hard up against it
            !to have it hard
            - (từ Mỹ,nghĩa Mỹ),  (thông tục) lâm vào hoàn cảnh khó khăn, phải va chạm với những khó khăn
            !hard upon
            - gần sát, xấp xỉ
            =it is getting hard upon twelve+ đã gần 12 giờ rồi
            !it will go hard with him
            - rất là khó khăn bất lợi cho anh ta
            *  danh từ
            - đường dốc xuống bâi, đường dốc xuống bến
            - (từ lóng) khổ sai
            =to get two year hard+ bị hai năm khổ sai
        """;

        String[] lines = examString.split(System.lineSeparator());
        // for (String line : lines) {
        //     System.out.println(line);
        // }
        String result = "";
        for (String line : lines) {
            result  += convertToHtml(line);
        }

        String fullResult= resultHTMLTemplate.replace("<body>", "<body>" + result);
        engineResult.loadContent(fullResult);

    }
}
