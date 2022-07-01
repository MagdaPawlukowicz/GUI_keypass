package sample;

import com.fasterxml.jackson.databind.ObjectMapper;;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class MainPage {
    private String categoryNameApp = "-";
    private String defaultCategoryName = "default";
    private BufferedWriter writer;
    private Main m = new Main();
    private List<String> categories = new LinkedList<>();
    private List<Password> passwordList = new LinkedList<>();
    private BufferedReader reader;
    private String categoriesFilePath = "src/data/categories.txt";

    @FXML
    private TableView tableView;
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
    @FXML
    private ChoiceBox<String> addCategoriesChoiceBox;


    public void initialize() throws IOException {
        TableColumn<Password, String> names = new TableColumn<>("Name:");
        names.setCellValueFactory(new PropertyValueFactory<>("passwordName"));
        tableView.getColumns().add(names);

        TableColumn<Password, String> logins = new TableColumn<>("Login:");
        logins.setCellValueFactory(new PropertyValueFactory<>("login"));
        tableView.getColumns().add(logins);

        TableColumn<Password, String> passwords = new TableColumn<>("Password:");
        passwords.setCellValueFactory(new PropertyValueFactory<>("password"));
        tableView.getColumns().add(passwords);

        TableColumn<Password, String> URLs = new TableColumn<>("URL:");
        URLs.setCellValueFactory(new PropertyValueFactory<>("passwordURL"));
        tableView.getColumns().add(URLs);

        TableColumn<Password, String> categories = new TableColumn<>("Category:");
        categories.setCellValueFactory(new PropertyValueFactory<>("category"));
        tableView.getColumns().add(categories);

        tableView.setEditable(true);
        makeColumnsEditable(tableView, names, logins, passwords, URLs);
        addDefaultCategory(defaultCategoryName);
        getCategories();
        readFile(LogInPage.file);
        passwordList = new LinkedList<>(tableView.getItems());
        showActualTimeLabel();
    }

    public void addRow() {
        Password password = new Password(UUID.randomUUID().toString(), textFieldNameRow.getText(), textFieldLoginRow.getText(),
                textFieldPasswordRow.getText(), textFieldURLRow.getText(), addCategoriesChoiceBox.getValue(),
                PasswordType.BASIC);
        passwordList.add(password);
        tableView.getItems().add(password);
        List<Password> codedPasswordList = codePasswordList(passwordList);
        writeFile(codedPasswordList, LogInPage.file);
    }

    public void deleteRow() {
        Password selectedItem = (Password) tableView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) return;
        List<Password> passwords = new LinkedList<>(tableView.getItems());
        if (!PasswordType.MAIN.equals(selectedItem.getPasswordType())) {
            tableView.getItems().remove(selectedItem);
            passwordList.remove(selectedItem);
            List<Password> codedPasswordList = codePasswordList(passwordList);
            writeFile(codedPasswordList, LogInPage.file);
        }
    }

    public void editRow() {
        Password selectedItem = (Password) tableView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) return;
        passwordList.remove(selectedItem);
        passwordList.add(selectedItem);
        List<Password> codedPasswordList = codePasswordList(passwordList);
        writeFile(codedPasswordList, LogInPage.file);
        savedInformationLabel.setText("SAVED DATA");
        savedInformationLabel.setVisible(true);
    }

    private void getCategories() {
        try {
            reader = new BufferedReader(new FileReader(categoriesFilePath));
            while (reader.ready()) {
                String value = reader.readLine();
                categoriesChoiceBox.getItems().add(value);
                addCategoriesChoiceBox.getItems().add(value);
                categories.add(value);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteCategory() {
        String selectedCategory = categoriesChoiceBox.getSelectionModel().getSelectedItem();
        if (selectedCategory == null) return;
        if (!selectedCategory.equals(defaultCategoryName)) {
            categoriesChoiceBox.getItems().remove(selectedCategory);
            addCategoriesChoiceBox.getItems().remove(selectedCategory);
            categories.remove(selectedCategory);
            try {
                writer = new BufferedWriter(new FileWriter(categoriesFilePath));
                for (String category : categories) {
                    writer.write(category + "\n");
                }
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            deleteObjectsFromCategory(selectedCategory);
        }
    }

    public void deleteObjectsFromCategory(String selectedCategory) {
        List<Password> passwords = new LinkedList<>(tableView.getItems());
        for (Password password : passwords) {
            if (password.getCategory().equals(selectedCategory)) {
                tableView.getItems().remove(password);
            }
        }
        passwordList = passwordList.stream()
                .filter(p -> !selectedCategory.equals(p.getCategory()))
                .collect(Collectors.toList());

        passwords = codePasswordList(passwordList);
        writeFile(passwords, LogInPage.file);

    }

    public void addCategory() {
        String categoryTextField = addCategoryTextField.getText();
        if (!isCategoryExisting(categoryTextField) && !categoryTextField.equals(categoryNameApp)
                && !categoryTextField.equals(defaultCategoryName)) {
            addCategory(categoryTextField);
        }
    }

    private void addCategory(String categoryName) {
        categoriesChoiceBox.getItems().add(categoryName);
        addCategoriesChoiceBox.getItems().add(categoryName);
        try {
            writer = new BufferedWriter(new FileWriter(categoriesFilePath, true));
            writer.write(categoryName + "\n");
            writer.close();
            categories.add(categoryName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void userLogOut() throws IOException {
        m.changeScene("loginPage.fxml");
    }

    public void writeFile(List<Password> passwordList, File file) {
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
                LinkedList<Password> codedPasswordList = new LinkedList<>(Arrays.asList(passwords));
                codedPasswordList.forEach(password -> {
                    Password decodedPassword = decodePassword(password);
                    passwordList.add(password);
                    tableView.getItems().add(decodedPassword);
                });
                if (codedPasswordList.size() == 0) {
                    addDefaultPasswordToFile();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (file.exists() && file.length() == 0) {
            addDefaultPasswordToFile();
        }
    }

    private void addDefaultPasswordToFile() {
        Password password = new Password(UUID.randomUUID().toString(), "KeyPass",
                "user",
                "123",
                "KeyPass",
                categoryNameApp, PasswordType.MAIN);

        tableView.getItems().add(password);
        passwordList.add(password);
        List<Password> codePasswordList = codePasswordList(passwordList);
        writeFile(codePasswordList, LogInPage.file);
    }

    private List<Password> codePasswordList(List<Password> decodedPasswordList) {
        return decodedPasswordList.stream()
                .map(this::codePassword)
                .collect(Collectors.toList());
    }

    private Password codePassword(Password password) {
        Password codedPassword = new Password();
        codedPassword.setId(password.getId());
        codedPassword.setPasswordType(password.getPasswordType());
        codedPassword.setCategory(password.getCategory());
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
        decodedPassword.setPasswordType(password.getPasswordType());
        decodedPassword.setCategory(password.getCategory());
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

    public String codeStringValue(String passwordAsString) {
        char[] password = passwordAsString.toCharArray();
        for (int i = 0; i < password.length; i++) {
            char codedLetter = (char) (password[i] + 3);
            password[i] = codedLetter;
        }
        return new String(password);
    }

    public String decodeStringValue(String passwordAsString) {
        char[] password = passwordAsString.toCharArray();
        for (int i = 0; i < password.length; i++) {
            char decodedLetter = (char) (password[i] - 3);
            password[i] = decodedLetter;
        }
        return new String(password);
    }

    public void showActualTimeLabel() throws IOException {
        timeStamp.setText("LOG IN TIME: " + LocalDate.now() + " " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        writer = new BufferedWriter(new FileWriter("src/data/lastLogInTime.txt"));
        writer.write("LAST LOG IN TIME: " + LocalDate.now() + " " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        writer.close();
        timeStamp.setVisible(true);
    }

    private boolean isCategoryExisting(String category) {
        boolean isExisting = false;
        try {
            reader = new BufferedReader(new FileReader(categoriesFilePath));
            while (reader.ready()) {
                String value = reader.readLine();
                if (value.equals(category)) {
                    isExisting = true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isExisting;
    }

    public String getMainPassword(File file) {
        String mainPassword = null;
        if (file.exists() && file.length() > 0) {
            try {
                FileReader passwordsReader = new FileReader(file);
                ObjectMapper objectMapper = new ObjectMapper();
                Password[] passwords = objectMapper.readValue(passwordsReader, Password[].class);
                List<Password> passwordList = new LinkedList<>(Arrays.asList(passwords));
                for (Password password : passwordList) {
                    if (password.getPasswordType().equals(PasswordType.MAIN)) {
                        mainPassword = decodeStringValue(password.getPassword());
                    }
                }
                return mainPassword;

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "123";
    }

    public String getMainLogin(File file) {
        String mainLogin = null;
        if (file != null && file.exists() && file.length() > 0) {
            try {
                FileReader passwordsReader = new FileReader(file);
                ObjectMapper objectMapper = new ObjectMapper();
                Password[] passwords = objectMapper.readValue(passwordsReader, Password[].class);
                List<Password> passwordList = new LinkedList<>(Arrays.asList(passwords));
                for (Password password : passwordList) {
                    if (password.getPasswordType().equals(PasswordType.MAIN)) {
                        mainLogin = decodeStringValue(password.getLogin());
                    }
                }
                return mainLogin;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "user";
    }

    private void makeColumnsEditable(TableView tableID, TableColumn<Password, String> names,
                                     TableColumn<Password, String> logins,
                                     TableColumn<Password, String> passwords,
                                     TableColumn<Password, String> URLs) {

        names.setCellFactory(TextFieldTableCell.forTableColumn());
        names.setOnEditCommit(
                (TableColumn.CellEditEvent<Password, String> t) -> {
                    (t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setPasswordName(t.getNewValue());
                });

        logins.setCellFactory(TextFieldTableCell.forTableColumn());
        logins.setOnEditCommit(
                (TableColumn.CellEditEvent<Password, String> t) -> {
                    (t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setLogin(t.getNewValue());
                });

        passwords.setCellFactory(TextFieldTableCell.forTableColumn());
        passwords.setOnEditCommit(
                (TableColumn.CellEditEvent<Password, String> t) -> {
                    (t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setPassword(t.getNewValue());
                });

        URLs.setCellFactory(TextFieldTableCell.forTableColumn());
        URLs.setOnEditCommit(
                (TableColumn.CellEditEvent<Password, String> t) -> {
                    (t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setPasswordURL(t.getNewValue());
                });
    }

    public void showCategory() {
        String selectedCategory = categoriesChoiceBox.getValue();
        if (selectedCategory == null) return;
        List<Password> passwordsToDisplay;
        if (defaultCategoryName.equals(selectedCategory)) {
            passwordsToDisplay = passwordList;
        } else {
            passwordsToDisplay = passwordList.stream()
                    .filter(p -> selectedCategory.equals(p.getCategory()))
                    .collect(Collectors.toList());
        }
        tableView.getItems().clear();
        tableView.getItems().addAll(passwordsToDisplay);
    }

    private void addDefaultCategory(String defaultCategory) {
        if (!isCategoryExisting(defaultCategory)) {
            categoriesChoiceBox.getItems().add(defaultCategory);
        }
    }
}