package edu.example;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by louis on 1/10/2017.
 */
public class PlayerForm {
    @NotNull
    @Size(min=2, max=10)
    private String username;

    @NotNull
    @Size(min=5, max=20)
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
