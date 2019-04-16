package gr.uoa.di.rent.payload.responses;

public class SignUpResponse {

    private String username;

    private String email;

    public SignUpResponse() {
    }

    public SignUpResponse(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
