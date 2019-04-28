package gr.uoa.di.rent.payload.requests;

import org.hibernate.validator.constraints.Length;
import org.springframework.lang.NonNull;

import javax.validation.constraints.*;
import java.util.Date;

import static gr.uoa.di.rent.config.Constraint.*;
import static gr.uoa.di.rent.config.Constraint.USERNAME_PATTERN_MESSAGE;

public class RegisterRequest {

    @NotBlank
    @Length(min = USERNAME_MIN, max = USERNAME_MAX)
    @Pattern(regexp = USERNAME_PATTERN, message = USERNAME_PATTERN_MESSAGE)
    private String username;

    @NotBlank
    @Length(min = PASSWORD_MIN, max = PASSWORD_MAX)
    @Pattern(regexp = PASSWORD_PATTERN, message = PASSWORD_PATTERN_MESSAGE)
    private String password;

    @NotBlank
    @Email(message = EMAIL_PATTERN_MESSAGE)
    @Length(min = EMAIL_MIN, max = EMAIL_MAX, message = EMAIL_MIN_MAX_MESSAGE)
    private String email;

    @NotBlank
    @Length(min = FIRSTNAME_MIN, max = FIRSTNAME_MAX, message = FIRSTNAME_MIN_MAX_MESSAGE)
    @Pattern(regexp = FIRSTNAME_PATTERN, message = FIRSTNAME_PATTERN_MESSAGE)
    private String name;

    @NotBlank
    @Length(min = LASTNAME_MIN, max = LASTNAME_MAX, message = LASTNAME_MIN_MAX_MESSAGE)
    @Pattern(regexp = LASTNAME_PATTERN, message = LASTNAME_PATTERN_MESSAGE)
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
