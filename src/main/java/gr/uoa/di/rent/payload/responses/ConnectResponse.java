package gr.uoa.di.rent.payload.responses;

import gr.uoa.di.rent.models.Profile;
import gr.uoa.di.rent.models.User;

public class ConnectResponse {

    private Access access;
    private User user;
    private Profile profile;

    public ConnectResponse(String accessToken, String tokenType, User user) {
        this.access = new Access(accessToken, tokenType);
        this.user = user;
        this.profile = user.getProfile();
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

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    @Override
    public String toString() {
        return "ConnectResponse{" +
                "access=" + access +
                ", user=" + user +
                ", profile=" + profile +
                '}';
    }
}
