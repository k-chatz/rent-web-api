package gr.uoa.di.rent.payload.responses;

public class SignInResponse {

    public class Token {
        private String accessToken;
        private String tokenType;

        public Token(String accessToken, String tokenType) {
            this.accessToken = accessToken;
            this.tokenType = tokenType;
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
    }
    private Token token;
    private Long id;
    private String email;
    private String name;
    private String surname;
    private String role;

    public SignInResponse(String accessToken, String tokenType, Long id, String email, String name, String surname, String role) {
        this.token = new Token(accessToken, tokenType);
        this.id = id;
        this.email = email;
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
