package gr.uoa.di.rent.payload.responses;

public class SignInResponse {

    private String accessToken;
    private String tokenType = "Bearer";

    private Long id;
    private String email;
    private String name;
    private String surname;
    private String role;

    public SignInResponse(String accessToken, Long id, String email, String name, String surname, String role) {
        this.accessToken = accessToken;
        this.id = id;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.role = role;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
