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
        "provider_id",
        "card_number",
        "price"
})
public class Transaction extends UserDateAudit implements Serializable {

    @Id
    @Column(name = "id")
    @JsonProperty("id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "provider_id", nullable = false)
    @JsonProperty("provider")
    private User provider;

    @Column(name = "card_number")
    @JsonProperty("card_number")
    private String card_number;

    @Column(name = "price")
    @JsonProperty("price")
    private Integer price;

    public Transaction() {
    }

    public Transaction(Long id, User provider, String card_number, Integer price) {
        this.id = id;
        this.provider = provider;
        this.card_number = card_number;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getProvider() {
        return provider;
    }

    public void setProvider(User provider) {
        this.provider = provider;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", provider=" + provider +
                ", card_number='" + card_number + '\'' +
                ", price=" + price +
                '}';
    }
}
