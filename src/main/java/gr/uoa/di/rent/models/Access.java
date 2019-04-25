package gr.uoa.di.rent.models;

public class Access {

    private String token;
    private String type;

    public Access(String token, String type) {
        this.token = token;
        this.type = type;
    }

    public Access() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Access{" +
                "token='" + token + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
