package gr.uoa.di.rent.payload.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import gr.uoa.di.rent.models.Role;

import java.util.Date;

public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private String name;
    private String surname;
    private Date birthday;
    private Role Role;
    private Boolean locked;
    private String photo_profile;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;

    public UserResponse() {
    }

    public UserResponse(Long id, String username, String email, String name, String surname, Date birthday,
                        Role role, Boolean locked, String photo_profile) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
        Role = role;
        this.locked = locked;
        this.photo_profile = photo_profile;
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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public gr.uoa.di.rent.models.Role getRole() {
        return Role;
    }

    public void setRole(gr.uoa.di.rent.models.Role role) {
        Role = role;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public String getPhoto_profile() {
        return photo_profile;
    }

    public void setPhoto_profile(String photo_profile) {
        this.photo_profile = photo_profile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
