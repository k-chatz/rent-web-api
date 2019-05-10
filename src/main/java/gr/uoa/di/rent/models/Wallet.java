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

    @Transient
    @JsonProperty("business_owner_id")
    private Long user_owner_id;

    @OneToOne(mappedBy = "wallet", fetch = FetchType.LAZY, orphanRemoval = true, optional = false)
    @JsonIgnore
    private Business business_owner;

    @Transient
    @JsonProperty("business_owner_id")
    private Long business_owner_id;

    public Wallet() {
    }

    public Wallet(User user_owner, Double balance) {
        this.setUser_owner(user_owner);
        this.balance = balance;
    }

    public Wallet(Business business_owner, Double balance) {
        this.setBusiness_owner(business_owner);
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
        this.setUser_owner_id(user_owner.getId());
    }

    public Long getUser_owner_id() {
        return user_owner_id;
    }

    public void setUser_owner_id(Long user_owner_id) {
        this.user_owner_id = user_owner_id;
    }

    public Business getBusiness_owner() {
        return business_owner;
    }

    public void setBusiness_owner(Business business_owner) {
        this.business_owner = business_owner;
        this.setBusiness_owner_id(business_owner.getId());
    }

    public Long getBusiness_owner_id() {
        return business_owner_id;
    }

    public void setBusiness_owner_id(Long business_owner_id) {
        this.business_owner_id = business_owner_id;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "id=" + id +
                ", balance=" + balance +
                ", user_owner_id=" + user_owner_id +
                ", business_owner_id=" + business_owner_id +
                '}';
    }
}
