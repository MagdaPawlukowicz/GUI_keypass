package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import java.io.*;


public class LogInPage {
    @FXML
    private TextField userNameID;
    @FXML
    private PasswordField passwordID;
    @FXML
    private Circle circleID;
    @FXML
    private Label wrongLogInID;
    @FXML
    private Label pathInformation;
    @FXML
    private Label lastLogInInformation;

    private final Main m = new Main();
    private final MainPage mainPage = new MainPage();
    static File file;
    String fileAbsolutePath;
    private static BufferedReader reader;


    public void initialize() {
        try {
            reader = new BufferedReader(new FileReader("src/data/lastLogInTime.txt"));
            lastLogInInformation.setText(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void userLogIn() throws IOException {
        checkLogIn();
    }

    private void checkLogIn() throws IOException {
        if (mainPage.getMainLogin(file).equals(userNameID.getText())
                && mainPage.getMainPassword(file).equals(passwordID.getText())
                && file != null) {
            wrongLogInID.setText("SUCCESS");
            wrongLogInID.setStyle("-fx-text-fill: #32a852;");
            circleID.setFill(Paint.valueOf("#32a852"));
            m.changeScene("mainPage.fxml");
        } else if (file == null) {
            wrongLogInID.setText("UPLOAD JSON FILE");
            wrongLogInID.setStyle("-fx-text-fill: #ff7700;");
            circleID.setFill(Paint.valueOf("#ff7700"));
        } else if ((userNameID.getText().isEmpty() && passwordID.getText().isEmpty())) {
            wrongLogInID.setText("WRITE DOWN ALL DATA");
            wrongLogInID.setStyle("-fx-text-fill: #ff7700;");
            circleID.setFill(Paint.valueOf("#ff7700"));
        } else {
            wrongLogInID.setText("CHECK YOUR DATA");
            wrongLogInID.setStyle("-fx-text-fill: #ff7700;");
            circleID.setFill(Paint.valueOf("#ff7700"));
        }
        wrongLogInID.setVisible(true);
        circleID.setVisible(true);
    }

    public void chooseSingleJSONFile() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        file = fc.showOpenDialog(null);

        if (file != null) {
            pathInformation.setText("Selected file: " + file.getAbsolutePath());
            pathInformation.setVisible(true);
            fileAbsolutePath = file.getAbsolutePath();
        }
    }

}
