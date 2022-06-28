package sample;

import java.util.List;

public class Category {
    String name;
    List<Password> passwords;

    public Category(String name, List<Password> passwords) {
        this.name = name;
        this.passwords = passwords;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Password> getPasswords() {
        return passwords;
    }

    public void setPasswords(List<Password> passwords) {
        this.passwords = passwords;
    }
}
