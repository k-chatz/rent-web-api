package gr.uoa.di.rent.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import gr.uoa.di.rent.models.audit.UserDateAudit;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "wallets", schema = "rent")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "balance"
})
public class Wallet extends UserDateAudit implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    @JsonProperty("id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "balance", nullable = false)
    @JsonProperty("balance")
    private Double balance;

    @OneToOne(mappedBy = "wallet", fetch = FetchType.LAZY, orphanRemoval = true, optional = false)
    @JsonIgnore
    private User user_owner;

    @OneToOne(mappedBy = "wallet", fetch = FetchType.LAZY, orphanRemoval = true, optional = false)
    @JsonIgnore
    private Business business_owner;

    public Wallet() {
    }

    public Wallet(User user_owner, Double balance) {
        this.user_owner = user_owner;
        this.balance = balance;
    }

    public Wallet(Business business_owner, Double balance) {
        this.business_owner = business_owner;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public User getUser_owner() {
        return user_owner;
    }

    public void setUser_owner(User user_owner) {
        this.user_owner = user_owner;
    }

    public Business getBusiness_owner() {
        return business_owner;
    }

    public void setBusiness_owner(Business business_owner) {
        this.business_owner = business_owner;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "id=" + id +
                ", balance=" + balance +
                ", user_owner=" + user_owner +
                ", business_owner=" + business_owner +
                '}';
    }
}
