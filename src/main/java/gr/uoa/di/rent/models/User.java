package gr.uoa.di.rent.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

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
        "photo_profile"
})
public class User {

    private static final Logger logger = LoggerFactory.getLogger(User.class);

    @Id
    @Column(name = "id")
    @JsonProperty("id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username", unique=true, nullable = false)
    @JsonProperty("username")
    private String username;

    @JsonIgnore
    @Column(name = "password", unique=true, nullable = false)
    //@JsonProperty("password")
    private String password;

    @Column(name = "email", unique=true, nullable = false)
    @JsonProperty("email")
    private String email;

    @Column(name = "name", nullable = false)
    @JsonProperty("name")
    private String name;

    @Column(name = "surname", nullable = false)
    @JsonProperty("surname")
    private String surname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="role_id", nullable=false)
    @JsonProperty("role")
    private Role role;

    @Column(name = "birthday", nullable = false)
    @JsonProperty("birthday")
    private Date birthday;

    @Column(name = "locked", nullable = false)
    @JsonProperty("locked")
    private Boolean locked;

    @Column(name = "photo_profile")
    @JsonProperty("photo_profile")
    private String photo_profile;   // It may be null.

    @OneToMany(mappedBy = "provider")
    private List<Hotel> hotels;   // It may be null.

    public User() {
    }

    public User(String username, String password, String email, String name, String surname,
                Date birthday, Boolean locked, String photo_profile) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
                ", photo_profile='" + photo_profile + '\'' +
                ", hotels=" + hotels +
                '}';
    }
}
