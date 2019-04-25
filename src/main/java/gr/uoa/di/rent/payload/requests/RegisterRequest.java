package gr.uoa.di.rent.payload.requests;

import org.springframework.lang.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

public class RegisterRequest {

    @NotBlank
    @Size(min = 2, max = 45)
    private String username;

    @NotBlank
    @Size(min = 6, max = 60)
    private String password;

    @NotBlank
    @Email(message = "Invalid email pattern, e.g myemail@email.com")
    @Size(min = 6, max = 60, message = "Email min 6 and max 60")
    private String email;

    @NotBlank
    @Size(min = 2, max = 45)
    private String name;

    @NotBlank
    @Size(min = 2, max = 45)
    private String surname;

    @NonNull
    private Date birthday;

    public RegisterRequest() {
    }

    public RegisterRequest(String username, String password, String email, String name, String surname, Date birthday) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "RegisterRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", birthday=" + birthday +
                '}';
    }
}
