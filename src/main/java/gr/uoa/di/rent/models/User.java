package gr.uoa.di.rent.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import gr.uoa.di.rent.models.audit.DateAudit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;

import static gr.uoa.di.rent.config.Constraint.*;

@Entity
@Table(name = "users", schema = "rent")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "username",
        "name",
        "surname",
        "role",
        "birthday",
        "locked",
        "pending_provider",
        "photo_profile"
})
public class User extends DateAudit {

    private static final Logger logger = LoggerFactory.getLogger(User.class);

    @Id
    @Column(name = "id")
    @JsonProperty("id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Length(min = USERNAME_MIN, max = USERNAME_MAX)
    //@Pattern(regexp = USERNAME_PATTERN, message = USERNAME_PATTERN_MESSAGE)
    @Column(name = "username", unique = true, nullable = false, length = 45)
    @JsonProperty("username")
    private String username;

    @JsonIgnore
    @Column(name = "password", unique = true, nullable = false, length = 60)
    //@JsonProperty("password")
    private String password;

    @Column(name = "email", unique = true, nullable = false, length = 60)
    @JsonProperty("email")
    private String email;

    @Column(name = "name", nullable = false, length = 45)
    @JsonProperty("name")
    private String name;

    @Column(name = "surname", nullable = false, length = 45)
    @JsonProperty("surname")
    private String surname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    @JsonProperty("role")
    private Role role;

    @Column(name = "birthday", nullable = false)
    @JsonProperty("birthday")
    private Date birthday;

    @JsonIgnore
    @Column(name = "locked", nullable = false)
    private Boolean locked;

    @JsonProperty("pending_provider")
    @Column(name = "pending_provider", nullable = false)
    private Boolean pending_provider = false;

    @Column(name = "photo_profile")
    @JsonProperty("photo_profile")
    private String photo_profile;   // It may be null.

    @OneToMany(mappedBy = "provider")
    private List<Hotel> hotels;   // It may be null.

    public User() {
    }

    public User(String username, String password, String email, String name, String surname,
                Date birthday, Role role, Boolean locked, String photo_profile) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
        this.role = role;
        this.locked = locked;
        this.photo_profile = photo_profile;
    }

    // Constructor used for updating a user in the database.
    public User(Long id, String username, String password, String email, String name, String surname,
                Date birthday, Boolean locked, String photo_profile) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
        this.locked = locked;
        this.photo_profile = photo_profile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Boolean getPending_provider() {
        return pending_provider;
    }

    public void setPending_provider(Boolean pending_provider) {
        this.pending_provider = pending_provider;
    }

    public String getPhoto_profile() {
        return photo_profile;
    }

    public void setPhoto_profile(String photo_profile) {
        this.photo_profile = photo_profile;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", role=" + role +
                ", birthday=" + birthday +
                ", locked=" + locked +
                ", pending_provider=" + pending_provider +
                ", photo_profile='" + photo_profile + '\'' +
                ", hotels=" + hotels +
                '}';
    }
}
