package gr.uoa.di.rent.payload.requests;

import gr.uoa.di.rent.models.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;


public class UserUpdateRequest {

    //@NotNull
    private User user;

    //@NotNull
    private Long id;

    //@NotBlank
    private String username;

    //@NotBlank
    private String password;

    //@NotBlank
    private String email;

    //@NotBlank
    private String name;

    //@NotBlank
    private String surname;

    //@NotNull
    private Date birthday;

    private boolean locked;

    //@NotBlank
    private String photo_profile;

    public UserUpdateRequest() {}

    public UserUpdateRequest(User user) {
        this.user = user;
    }

    public User asUser() {

        return new User(
                this.getId(),
                this.getUsername(),
                this.getPassword(),
                this.getEmail(),
                this.getName(),
                this.getSurname(),
                this.getBirthday(),
                this.isLocked(),
                this.getPhoto_profile()  // It may be null since it's optional, no problem.
        );
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getPhoto_profile() {
        return photo_profile;
    }

    public void setPhoto_profile(String photo_profile) {
        this.photo_profile = photo_profile;
    }
}
