package gr.uoa.di.rent.payload.responses;

import gr.uoa.di.rent.models.Token;

public class SignInResponse {

    private Token token;
    private Long id;
    private String email;
    private String username;
    private String name;
    private String surname;
    private String role;

    public SignInResponse(String accessToken, String tokenType, Long id, String email, String username, String name, String surname, String role) {

        this.token = new Token(accessToken, tokenType);
        this.id = id;
        this.email = email;
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.role = role;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
