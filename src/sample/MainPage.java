package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class MainPage {
    Password password;
    List<Password> passwordList = new LinkedList<>();

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

    public void initialize(){
        TableColumn<Password, SimpleStringProperty> names = new TableColumn<>("Name:");
        names.setCellValueFactory(new PropertyValueFactory<>("passwordName"));
        tableID.getColumns().add(names);

        TableColumn<Password,String> logins = new TableColumn<>("Login:");
        logins.setCellValueFactory(new PropertyValueFactory<>("login"));
        tableID.getColumns().add(logins);

        TableColumn<Password,String> passwords = new TableColumn<>("Password:");
        passwords.setCellValueFactory(new PropertyValueFactory<>("password"));
        tableID.getColumns().add(passwords);

        TableColumn<Password,String> URLs = new TableColumn<>("URL:");
        URLs.setCellValueFactory(new PropertyValueFactory<>("passwordURL"));
        tableID.getColumns().add(URLs);
    }


    public void addRow(){
        password = new Password(textFieldNameRow.getText(), textFieldLoginRow.getText(),
                textFieldPasswordRow.getText(),textFieldURLRow.getText());
        passwordList.add(password);
        tableID.getItems().add(password);
    }

    private Main m = new Main();

    public void userLogOut() throws IOException {
        m.changeScene("sample.fxml");
    }
}
