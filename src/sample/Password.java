package sample;

public class Password {
    private String id;
    private String passwordName = null;
    private String login = null;
    private String password = null;
    private String passwordURL = null;

    public Password() {
    }
    public Password(String id, String passwordName, String login, String password, String passwordURL) {
        this.id = id;
        this.passwordName = passwordName;
        this.login = login;
        this.password = password;
        this.passwordURL = passwordURL;
    }

    public String getPasswordName() {
        return passwordName;
    }

    public void setPasswordName(String passwordName) {
        this.passwordName = passwordName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordURL() {
        return passwordURL;
    }

    public void setPasswordURL(String passwordURL) {
        this.passwordURL = passwordURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

