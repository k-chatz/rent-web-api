package gr.uoa.di.rent.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import gr.uoa.di.rent.models.audit.UserDateAudit;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "rooms", schema = "rent")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "hotel_id",
        "price",
        "capacity",
})
public class Room extends UserDateAudit  implements Serializable {

    @Id
    @Column(name = "id")
    @JsonProperty("id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hotel", nullable = false)
    @JsonIgnore
    private Hotel hotel;

    @Transient
    @JsonProperty("hotel_id")
    private Long hotel_id;

    @Column(name = "price")
    @JsonProperty("price")
    private Integer price;

    @Column(name = "capacity")
    @JsonProperty("capacity")
    private Integer capacity;

    public Room() {
    }

    public Room(Hotel hotel, Integer price, Integer capacity) {
        this.setHotel(hotel);
        this.price = price;
        this.capacity = capacity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
        this.hotel_id = hotel.getId();
    }

    public Long getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(Long hotel_id) {
        this.hotel_id = hotel_id;
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
                ", hotel=" + hotel +
                ", hotel_id=" + hotel_id +
                ", price=" + price +
                ", capacity=" + capacity +
                '}';
    }
}
