package gr.uoa.di.rent.payload.responses;


import gr.uoa.di.rent.models.User;


public class UserResponse {

    private User user;

    public UserResponse() {
    }

    public UserResponse(User user) {
       this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "user=" + user +
                '}';
    }
}
