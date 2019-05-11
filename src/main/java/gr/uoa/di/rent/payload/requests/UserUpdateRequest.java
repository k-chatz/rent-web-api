package gr.uoa.di.rent.payload.requests;

import gr.uoa.di.rent.models.Profile;
import gr.uoa.di.rent.models.Role;
import gr.uoa.di.rent.models.User;

import java.util.Date;

public class UserUpdateRequest {

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
    private String photo_url;

    public UserUpdateRequest() {
    }

    public User asUser(Long userId, Role role) {

        // Check the case that the id is only provided in the url=path.
        if (this.id == null)
            this.id = userId;

        User user_temp = new User(
                userId,
                this.username,
                this.password,
                this.email,
                role,
                false,
                false,
                null,
                null
        );

        Profile profile = new Profile(
                user_temp,
                this.getName(),
                this.getSurname(),
                this.getBirthday(),
                this.getPhoto_url()
        );
        user_temp.setProfile(profile);

        return user_temp;
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

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    @Override
    public String toString() {
        return "UserUpdateRequest{" +
                ", id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", birthday=" + birthday +
                ", locked=" + locked +
                ", photo_url='" + photo_url + '\'' +
                '}';
    }
}
