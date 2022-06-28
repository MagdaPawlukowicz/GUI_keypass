package sample;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class MainPage {
    private Password password;
//    private List<Password> passwordList = new LinkedList();
    private BufferedWriter writer;
    private Main m = new Main();
    private List <String> categories = new LinkedList<>();

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
    private Label savedInformationLabel;
    @FXML
    private ChoiceBox<String> categoriesChoiceBox;
    @FXML
    private TextField addCategoryTextField;



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

        tableID.setEditable(true);
        makeColumnsEditable(tableID, names, logins, passwords, URLs);

        readFile(LogInPage.file);
        showActualTimeLabel();
    }

    public void addRow() {
        password = new Password(UUID.randomUUID().toString(), textFieldNameRow.getText(), textFieldLoginRow.getText(),
                    textFieldPasswordRow.getText(), textFieldURLRow.getText());
        tableID.getItems().add(password);
        List<Password> codedPasswordList = codePasswordList(tableID);
        writeFile(codedPasswordList, LogInPage.file);
    }

    public void deleteRow() {
        Password selectedItem = (Password) tableID.getSelectionModel().getSelectedItem();
        tableID.getItems().remove(selectedItem);
        List<Password> codedPasswordList = codePasswordList(tableID);
        writeFile(codedPasswordList, LogInPage.file);
    }

    private List<Password> codePasswordList(TableView tableID) {
        List<Password> codedPasswordList = tableID.getItems();
        codedPasswordList = codedPasswordList.stream()
                .map(this::codePassword)
                .collect(Collectors.toList());
        return codedPasswordList;
    }

    public void editRow() {
        List<Password> codedPasswordList = codePasswordList(tableID);
        writeFile(codedPasswordList, LogInPage.file);
        savedInformationLabel.setText("SAVED DATA");
        savedInformationLabel.setVisible(true);

        }

    public void deleteCategory(){

    }
    public void addCategory(){
        categories.add(addCategoryTextField.getText());
        categoriesChoiceBox.getItems().add(addCategoryTextField.getText());
    }

    public void userLogOut() throws IOException {
        m.changeScene("loginPage.fxml");
    }

    public void writeFile(List <Password> passwordList, File file) {
        Password[] passwordsArray = passwordList.toArray(Password[]::new);
        try {
            FileWriter passwordsFile = new FileWriter(file, false);
            ObjectMapper objectMapper = new ObjectMapper(); //obiekt ktory pozwala na zapisywanie obiektow w formie JSON
            objectMapper.writeValue(passwordsFile, passwordsArray);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void readFile(File file) {
        if (file.exists() && file.length() > 0) {
            try {
                FileReader passwordsReader = new FileReader(file);
                ObjectMapper objectMapper = new ObjectMapper();
                Password[] passwords = objectMapper.readValue(passwordsReader, Password[].class); // to co zostanie przeczytanie w pliku to tablica passwordow
                List<Password> passwordList = new LinkedList<>(Arrays.asList(passwords));
                passwordList.forEach(password -> {
                        Password decodedPassword = decodePassword(password);
                        tableID.getItems().add(decodedPassword);
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
            List<Password> codePasswordList = codePasswordList(tableID);
            writeFile(codePasswordList, LogInPage.file);
        }
    }

    private Password codePassword(Password password) {
        Password codedPassword = new Password();
        codedPassword.setId(password.getId());
        if (password.getPassword() != null) {
            codedPassword.setPassword(codeStringValue(password.getPassword()));
        }
        if (password.getLogin() != null) {
            codedPassword.setLogin(codeStringValue(password.getLogin()));
        }
        if (password.getPasswordName() != null) {
            codedPassword.setPasswordName(codeStringValue(password.getPasswordName()));
        }
        if (password.getPasswordURL() != null) {
            codedPassword.setPasswordURL(codeStringValue(password.getPasswordURL()));
        }
        return codedPassword;
    }

    private Password decodePassword(Password password) {
        Password decodedPassword = new Password();
        decodedPassword.setId(password.getId());
        if (password.getPassword() != null) {
            decodedPassword.setPassword(decodeStringValue(password.getPassword()));
        }
        if (password.getLogin() != null) {
            decodedPassword.setLogin(decodeStringValue(password.getLogin()));
        }
        if (password.getPasswordName() != null) {
            decodedPassword.setPasswordName(decodeStringValue(password.getPasswordName()));
        }
        if (password.getPasswordURL() != null) {
            decodedPassword.setPasswordURL(decodeStringValue(password.getPasswordURL()));
        }
        return decodedPassword;
    }

    public String codeStringValue(String hasloJakoString){
        char[] haslo = hasloJakoString.toCharArray();
        for(int i=0; i< haslo.length; i++){
            char podmianka = (char) (haslo[i]+3);
            haslo[i] = podmianka;
        }
        return new String(haslo);
    }

    public String decodeStringValue(String hasloJakoString) {
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

    private void makeColumnsEditable(TableView tableID, TableColumn<Password, String> names,
                                     TableColumn<Password, String> logins,
                                     TableColumn<Password, String> passwords,
                                     TableColumn<Password, String> URLs) {

        names.setCellFactory(TextFieldTableCell.forTableColumn());
        names.setOnEditCommit(
                (TableColumn.CellEditEvent<Password, String> t) -> {
                    ( t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setPasswordName(t.getNewValue());
                });

        logins.setCellFactory(TextFieldTableCell.forTableColumn());
        logins.setOnEditCommit(
                (TableColumn.CellEditEvent<Password, String> t) -> {
                    ( t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setLogin(t.getNewValue());
                });

        passwords.setCellFactory(TextFieldTableCell.forTableColumn());
        passwords.setOnEditCommit(
                (TableColumn.CellEditEvent<Password, String> t) -> {
                    ( t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setPassword(t.getNewValue());
                });

        URLs.setCellFactory(TextFieldTableCell.forTableColumn());
        URLs.setOnEditCommit(
                (TableColumn.CellEditEvent<Password, String> t) -> {
                    ( t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setPasswordURL(t.getNewValue());
                });
    }
}