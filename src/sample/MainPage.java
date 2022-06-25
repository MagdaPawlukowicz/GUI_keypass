package sample;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class MainPage {
    private Password password;
    private List<Password> passwordList = new LinkedList();
    private BufferedWriter writer;
    private Main m = new Main();

    @FXML
    private TableView tableID;
    @FXML
    private TextField textFieldNameRow;
    @FXML
    private TextField textFieldLoginRow;
    @FXML
    private TextField textFieldPasswordRow;
    @FXML
    private TextField textFieldURLRow;
    @FXML
    private Label timeStamp;
    @FXML
    private Button deleteButton;

    public void initialize() throws IOException {
        TableColumn<Password, String> names = new TableColumn<>("Name:");
        names.setCellValueFactory(new PropertyValueFactory<>("passwordName"));
        tableID.getColumns().add(names);

        TableColumn<Password, String> logins = new TableColumn<>("Login:");
        logins.setCellValueFactory(new PropertyValueFactory<>("login"));
        tableID.getColumns().add(logins);

        TableColumn<Password, String> passwords = new TableColumn<>("Password:");
        passwords.setCellValueFactory(new PropertyValueFactory<>("password"));
        tableID.getColumns().add(passwords);

        TableColumn<Password, String> URLs = new TableColumn<>("URL:");
        URLs.setCellValueFactory(new PropertyValueFactory<>("passwordURL"));
        tableID.getColumns().add(URLs);

        readFile(LogInPage.file);
        showActualTimeLabel();
    }


    public void addRow() {
        password = new Password(UUID.randomUUID().toString(), textFieldNameRow.getText(), textFieldLoginRow.getText(),
                textFieldPasswordRow.getText(), textFieldURLRow.getText());
        tableID.getItems().add(password);
        writeFile(password, LogInPage.file);
    }

    public void userLogOut() throws IOException {
        m.changeScene("loginPage.fxml");
    }

    public void writeFile(Password password, File file) {
        password = encryptPassword(password);
        passwordList.add(password);
        Password[] passwords = passwordList.toArray(Password[]::new);
        try {
            FileWriter passwordsFile = new FileWriter(file, false);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(passwordsFile, passwords);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Password encryptPassword(Password password) {
        Password encryptedPassword = new Password();
        encryptedPassword.setId(password.getId());
        if (password.getPassword() != null) {
            encryptedPassword.setPassword(codeJSONStringValue(password.getPassword()));
        }
        if (password.getLogin() != null) {
            encryptedPassword.setLogin(codeJSONStringValue(password.getLogin()));
        }
        if (password.getPasswordName() != null) {
            encryptedPassword.setPasswordName(codeJSONStringValue(password.getPasswordName()));
        }
        if (password.getPasswordURL() != null) {
            encryptedPassword.setPasswordURL(codeJSONStringValue(password.getPasswordURL()));
        }
        return encryptedPassword;
    }

    public void readFile(File file) {
        if (file.exists() && file.length() > 0) {
            try {
                FileReader passwordsReader = new FileReader(file);
                ObjectMapper objectMapper = new ObjectMapper();
                Password[] passwords = objectMapper.readValue(passwordsReader, Password[].class);
                passwordList = Arrays.asList(passwords);
                passwordList.forEach(password -> {
                        Password decryptedPassword = decryptPassword(password);
                        tableID.getItems().add(decryptedPassword);
                });

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (file.exists() && file.length() == 0) {
            password = new Password(UUID.randomUUID().toString(), "KeyPass",
                    LogInPage.loginApp,
                    LogInPage.passwordApp,
                    "KeyPass");
            tableID.getItems().add(password);
            writeFile(password, LogInPage.file);
        }
    }

    private Password decryptPassword(Password password) {
        if (password.getPassword() != null) {
            password.setPassword(decodeJSONStringValue(password.getPassword()));
        }
        if (password.getLogin() != null) {
            password.setLogin(decodeJSONStringValue(password.getLogin()));
        }
        if (password.getPasswordName() != null) {
            password.setPasswordName(decodeJSONStringValue(password.getPasswordName()));
        }
        if (password.getPasswordURL() != null) {
            password.setPasswordURL(decodeJSONStringValue(password.getPasswordURL()));
        }
        return password;
    }

    public String codeJSONStringValue (String hasloJakoString){
        char[] haslo = hasloJakoString.toCharArray();
        for(int i=0; i< haslo.length; i++){
            char podmianka = (char) (haslo[i]+3);
            haslo[i] = podmianka;
        }
        return new String(haslo);
    }

    public String decodeJSONStringValue (String hasloJakoString) {
        char[] haslo = hasloJakoString.toCharArray();
        for(int i=0; i <  haslo.length; i++){
            char podmianka = (char) (haslo[i]-3);
            haslo[i] = podmianka;
        }
        return new String(haslo);
    }

    public void showActualTimeLabel() throws IOException {
        timeStamp.setText("Last log in time: " + LocalDate.now() + " " + LocalTime.now());
        writer = new BufferedWriter(new FileWriter("src/data/lastLogInTime.txt"));
        writer.write("Last log in time: " + LocalDate.now() + " " + LocalTime.now());
        writer.close();
        timeStamp.setVisible(true);
    }

    public void deleteRow(){
            Password selectedItem = (Password) tableID.getSelectionModel().getSelectedItem();
            tableID.getItems().remove(selectedItem);
    }

    public static void setText(File file, String text) throws Exception {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
        bw.write(text, 0, text.length());
        bw.flush();
        bw.close();
    }
}
