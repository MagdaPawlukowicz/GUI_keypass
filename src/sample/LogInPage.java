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
    private TextField userNameTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Circle circle;
    @FXML
    private Label wrongLogInLabel;
    @FXML
    private Label pathInformationLabel;
    @FXML
    private Label lastLogInInformationLabel;

    private final Main m = new Main();
    private final MainPage mainPage = new MainPage();
    static File file;
    String fileAbsolutePath;
    private static BufferedReader reader;


    public void initialize() {
        try {
            reader = new BufferedReader(new FileReader("src/data/lastLogInTime.txt"));
            lastLogInInformationLabel.setText(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void userLogIn() throws IOException {
        checkLogIn();
    }

    private void checkLogIn() throws IOException {
        if (mainPage.getMainLogin(file).equals(userNameTextField.getText())
                && mainPage.getMainPassword(file).equals(passwordField.getText())
                && file != null) {
            wrongLogInLabel.setText("SUCCESS");
            wrongLogInLabel.setStyle("-fx-text-fill: #32a852;");
            circle.setFill(Paint.valueOf("#32a852"));
            m.changeScene("mainPage.fxml");
        } else if (file == null) {
            wrongLogInLabel.setText("UPLOAD JSON FILE");
            wrongLogInLabel.setStyle("-fx-text-fill: #ff7700;");
            circle.setFill(Paint.valueOf("#ff7700"));
        } else if ((userNameTextField.getText().isEmpty() && passwordField.getText().isEmpty())) {
            wrongLogInLabel.setText("WRITE DOWN ALL DATA");
            wrongLogInLabel.setStyle("-fx-text-fill: #ff7700;");
            circle.setFill(Paint.valueOf("#ff7700"));
        } else {
            wrongLogInLabel.setText("CHECK YOUR DATA");
            wrongLogInLabel.setStyle("-fx-text-fill: #ff7700;");
            circle.setFill(Paint.valueOf("#ff7700"));
        }
        wrongLogInLabel.setVisible(true);
        circle.setVisible(true);
    }

    public void chooseSingleJSONFile() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json"));
        file = fc.showOpenDialog(null);

        if (file != null) {
            pathInformationLabel.setText("Selected file: " + file.getAbsolutePath());
            pathInformationLabel.setVisible(true);
            fileAbsolutePath = file.getAbsolutePath();
        }
    }

}
