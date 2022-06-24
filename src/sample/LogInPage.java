package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;

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
    static File file;
    String fileAbsolutePath;
    static String passwordApp = "123";
    static String loginApp = "user";
    private static BufferedReader reader;


    public void initialize() throws IOException {
        try {
            reader = new BufferedReader(new FileReader("src/data/lastLogInTime.txt"));
            lastLogInInformation.setText(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void userLogIn () throws IOException {
        checkLogIn();
    }

    private void checkLogIn() throws IOException {
        if (loginApp.equals(userNameID.getText()) && passwordApp.equals(passwordID.getText()) && fileAbsolutePath != null) {
            wrongLogInID.setText("SUCCESS");
            wrongLogInID.setStyle("-fx-text-fill: #32a852;");
            circleID.setFill(Paint.valueOf("#32a852"));
            m.changeScene("mainPage.fxml");
        } else if (userNameID.getText().isEmpty() && passwordID.getText().isEmpty()){
            wrongLogInID.setText("NO DATA");
            wrongLogInID.setStyle("-fx-text-fill: #ff0000;");
            circleID.setFill(Paint.valueOf("#ff0000"));
        } else {
            wrongLogInID.setText("WRONG USERNAME OR PASSWORD");
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

        if (file != null ) {
            pathInformation.setText("Selected file: " + file.getAbsolutePath());
            pathInformation.setVisible(true);
            fileAbsolutePath = file.getAbsolutePath();
        }
    }



    public TextField getUserNameID() {
        return userNameID;
    }

    public void setUserNameID(TextField userNameID) {
        this.userNameID = userNameID;
    }

    public PasswordField getPasswordID() {
        return passwordID;
    }

    public void setPasswordID(PasswordField passwordID) {
        this.passwordID = passwordID;
    }

    public String getPasswordApp() {
        return passwordApp;
    }

    public void setPasswordApp(String passwordApp) {
        this.passwordApp = passwordApp;
    }

    public String getLoginApp() {
        return loginApp;
    }

    public void setLoginApp(String loginApp) {
        this.loginApp = loginApp;
    }
}
