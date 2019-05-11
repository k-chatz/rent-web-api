package gr.uoa.di.rent.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import gr.uoa.di.rent.models.audit.DateAudit;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
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
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    /* Otan ena kentriko kleidi mias ontothtas yparxei ws 3eno kai kentriko kleidi se allo table,
     * tote tote prepei na diagrafei prwta h eggrafh poy yparxei ekeino to kleidi, ki epeita h eggrafh poy to exei ws kentriko.
     * Stin periptwsh mas, prepei prwta na diagrafei to profile poy exei ws kentriko kai 3eno kleidi to user_id
     * ki epeita na diagrafei o user. Me to parakatw omws annotation ayto den einai aparaithto! */
    @OnDelete(action = OnDeleteAction.CASCADE)
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

    public Profile(User owner, @NotNull String name, @NotNull String surname, Date birthday, String photo_url) {
        this.owner = owner;
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
        this.photo_url = photo_url;
    }

    public static Logger getLogger() {
        return logger;
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
