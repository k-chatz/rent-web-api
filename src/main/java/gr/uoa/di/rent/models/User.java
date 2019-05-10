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
import java.util.List;

@Entity
@Table(name = "users", schema = "rent")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "username",
        "email",
        "role",
        "locked",
        "pending_provider",
})
public class User extends DateAudit implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(User.class);

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "username", unique = true, nullable = false, length = 45)
    @JsonProperty("username")
    private String username;

    @NotNull
    @JsonIgnore
    @Column(name = "password", unique = true, nullable = false, length = 60)
    private String password;

    @NotNull
    @Column(name = "email", unique = true, nullable = false, length = 60)
    @JsonProperty("email")
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    @JsonProperty("role")
    private Role role;

    @JsonIgnore
    @Column(name = "locked", nullable = false)
    private Boolean locked;

    @JsonProperty("pending_provider")
    @Column(name = "pending_provider", nullable = false)
    private Boolean pending_provider;

    @OneToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "owner")
    @JsonIgnore
    private Profile profile;

    @OneToOne(mappedBy = "provider", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Business business;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "wallet")
    @JsonIgnore
    private Wallet wallet;

    @OneToMany(mappedBy = "uploader", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<File> files;


    public User() {
    }

    public User(@NotNull String username, @NotNull String password, @NotNull String email, Role role, Boolean locked, Boolean pending_provider, Profile profile) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.locked = locked;
        this.pending_provider = pending_provider;
        this.profile = profile;
    }

    public User(Long id, @NotNull String username, @NotNull String password, @NotNull String email, Role role, Boolean locked, Boolean pending_provider, Profile profile) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.locked = locked;
        this.pending_provider = pending_provider;
        this.profile = profile;
    }

    // Used for updating user-data (don't use the role)
    public User(Long id, @NotNull String username, @NotNull String password, @NotNull String email, Boolean locked, Boolean pending_provider, Profile profile) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.locked = locked;
        this.pending_provider = pending_provider;
        this.profile = profile;
    }

    public User(@NotNull String username, @NotNull String password, @NotNull String email, Role role, Boolean locked,
                Boolean pending_provider, Profile profile, Wallet wallet) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.locked = locked;
        this.pending_provider = pending_provider;
        this.profile = profile;
        this.wallet = wallet;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", locked=" + locked +
                ", pending_provider=" + pending_provider +
                ", profile=" + profile +
                ", business=" + business +
                ", wallet=" + wallet +
                ", files=" + files +
                '}';
    }
}
