package gr.uoa.di.rent.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import gr.uoa.di.rent.models.audit.DateAudit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "profiles", schema = "rent")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "user_id",
        "name",
        "surname",
        "birthday",
        "photo_profile"
})
public class Profile extends DateAudit {

    private static final Logger logger = LoggerFactory.getLogger(Profile.class);

    @Id
    @OneToOne
    @JoinColumn(name = "owner", nullable = false)
    @JsonProperty("owner")
    private User owner;

    @NotNull
    @Column(name = "name", nullable = false, length = 45)
    @JsonProperty("name")
    private String name;

    @NotNull
    @Column(name = "surname", nullable = false, length = 45)
    @JsonProperty("surname")
    private String surname;


    @Column(name = "birthday", nullable = false)
    @JsonProperty("birthday")
    private Date birthday;

    @Column(name = "photo_url")
    @JsonProperty("photo_url")
    private String photo_profile;   // It may be null.

    public Profile() {
    }

    public Profile(User owner, @NotNull String name, @NotNull String surname, Date birthday, String photo_profile) {
        this.owner = owner;
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
        this.photo_profile = photo_profile;
    }

    public static Logger getLogger() {
        return logger;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
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

    public String getPhoto_profile() {
        return photo_profile;
    }

    public void setPhoto_profile(String photo_profile) {
        this.photo_profile = photo_profile;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "owner=" + owner +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", birthday=" + birthday +
                ", photo_profile='" + photo_profile + '\'' +
                '}';
    }
}
