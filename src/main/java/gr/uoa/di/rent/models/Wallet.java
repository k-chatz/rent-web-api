package gr.uoa.di.rent.models;

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
        "user_id",
        "card_number",
        "balance"
})
public class Wallet extends UserDateAudit implements Serializable {

    @Id
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonProperty("user_id")
    private User user_id;

    @Column(name = "card_number", unique = true, nullable = false)
    @JsonProperty("card_number")
    private String card_number;

    @Column(name = "balance", nullable = false)
    @JsonProperty("balance")
    private Double balance;

    public Wallet() {
    }

    public Wallet(User user_id, String card_number, Double balance) {
        this.user_id = user_id;
        this.card_number = card_number;
        this.balance = balance;
    }

    public User getUser_id() {
        return user_id;
    }

    public void setUser_id(User user_id) {
        this.user_id = user_id;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "user_id=" + user_id +
                ", card_number='" + card_number + '\'' +
                ", balance=" + balance +
                '}';
    }
}
