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
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "profiles", schema = "rent")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "owner_id",
        "name",
        "surname",
        "birthday",
        "photo_url"
})
public class Profile extends DateAudit implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(Profile.class);


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner", nullable = false)
    @JsonIgnore
    private User owner;

    @Transient
    @JsonProperty("owner_id")
    private Long owner_id;

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
    private String photo_url;   // It may be null.


    public Profile() {
    }

    public Profile(@NotNull String name, @NotNull String surname, Date birthday, String photo_url) {
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
        this.photo_url = photo_url;
    }

    public static Logger getLogger() {
        return logger;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
        this.setOwner_id(owner.getId());
    }

    public Long getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(Long owner_id) {
        this.owner_id = owner_id;
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

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }
}
