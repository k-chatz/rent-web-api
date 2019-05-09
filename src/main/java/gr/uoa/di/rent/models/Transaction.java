package gr.uoa.di.rent.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import gr.uoa.di.rent.models.audit.UserDateAudit;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "transactions", schema = "rent")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "user_id",
        "business_id",
        "amount"
})
public class Transaction extends UserDateAudit implements Serializable {

    @Id
    @Column(name = "id")
    @JsonProperty("id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // from
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonProperty("user_id")
    private User user;

    // to
    @ManyToOne
    @JoinColumn(name = "business_id", nullable = false)
    @JsonProperty("business_id")
    private Business business;

    // how much
    @Column(name = "amount")
    @JsonProperty("amount")
    private Integer amount;

    public Transaction() {
    }

    public Transaction(User from, Business to, Integer amount) {
        this.user = from;
        this.business = to;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", user=" + user +
                ", business=" + business +
                ", amount=" + amount +
                '}';
    }
}
