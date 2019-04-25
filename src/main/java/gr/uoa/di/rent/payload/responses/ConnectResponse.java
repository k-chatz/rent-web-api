package gr.uoa.di.rent.payload.responses;

import gr.uoa.di.rent.models.Access;
import gr.uoa.di.rent.models.User;

public class ConnectResponse {

    private Access access;
    private User user;

    public ConnectResponse(String accessToken, String tokenType, User user) {
        this.access = new Access(accessToken, tokenType);
        this.user = user;
    }

    public Access getAccess() {
        return access;
    }

    public void setAccess(Access access) {
        this.access = access;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "ConnectResponse{" +
                "access=" + access +
                ", user=" + user +
                '}';
    }
}
