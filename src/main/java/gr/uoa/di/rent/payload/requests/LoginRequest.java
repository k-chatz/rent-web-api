package gr.uoa.di.rent.payload.requests;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class LoginRequest {

    @NotBlank
    @Email(message = "Invalid email pattern, e.g myemail@email.com")
    @Size(min = 6, max = 60, message = "Email min 6 and max 60")
    private String email;

    @NotBlank
    @Size(min = 6, max = 60)
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(@NotBlank String email, @NotBlank String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
