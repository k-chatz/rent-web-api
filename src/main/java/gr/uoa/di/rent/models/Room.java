package gr.uoa.di.rent.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import gr.uoa.di.rent.models.audit.UserDateAudit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rooms", schema = "rent")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "price",
        "capacity",
        "geopoint"
})
public class Room extends UserDateAudit {

    @Id
    @Column(name = "id")
    @JsonProperty("id")
    private Long id;


    @Column(name = "price")
    @JsonProperty("price")
    private Integer price;

    @Column(name = "capacity")
    @JsonProperty("capacity")
    private Integer capacity;

    public Room() {
    }

    public Room(Long id, Integer price, Integer capacity/*, Geopoint geopoint*/) {
        this.id = id;
        this.price = price;
        this.capacity = capacity;
        //this.geopoint = geopoint;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", price=" + price +
                ", capacity=" + capacity +
                '}';
    }
}
