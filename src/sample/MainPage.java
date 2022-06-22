package sample;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

public class MainPage {
    Password password;
    JSONObject passwordData = new JSONObject();
    JSONParser jsonParser = new JSONParser();
    JSONArray passwordList = new JSONArray();

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

    public MainPage() throws IOException {
    }

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

        readFile();
    }


    public void addRow(){
        password = new Password(textFieldNameRow.getText(), textFieldLoginRow.getText(),
                textFieldPasswordRow.getText(),textFieldURLRow.getText());
        tableID.getItems().add(password);
        writeFile(password);
    }

    private Main m = new Main();

    public void userLogOut() throws IOException {
        m.changeScene("sample.fxml");
    }


    public void writeFile(Password password){
        passwordData.put("passwordName", password.getPasswordName());
        passwordData.put("login", password.getLogin());
        passwordData.put("password", password.getPassword());
        passwordData.put("passwordURL", password.getPasswordURL());

        passwordList.add(passwordData);
        try {
            FileWriter passwordsFile = new FileWriter("passwords.json", false);
            passwordsFile.write(passwordList.toJSONString());
            passwordsFile.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFile() {
        try {
            FileReader passwordsReader = new FileReader("passwords.json");
            Object obj = jsonParser.parse(passwordsReader);
            passwordList = (JSONArray) obj;
            ObjectMapper objectMapper = new ObjectMapper();
            passwordList.forEach(password -> {
                try {
                    Password deserializedPassword = objectMapper.readValue(password.toString(), Password.class);
                    tableID.getItems().add(deserializedPassword);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
