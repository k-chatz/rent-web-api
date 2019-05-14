package gr.uoa.di.rent.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import gr.uoa.di.rent.models.audit.UserDateAudit;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "rooms", schema = "rent")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "room_number",
        "hotel_id",
        "capacity",
})
public class Room extends UserDateAudit implements Serializable {

    @Id
    @Column(name = "id")
    @JsonProperty("id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_number")
    @JsonProperty("room_number")
    private Integer room_number;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hotel", nullable = false)
    @JsonIgnore
    private Hotel hotel;

    @Transient
    @JsonProperty("hotel_id")
    private Long hotel_id;

    @Column(name = "capacity")
    @JsonProperty("capacity")
    private Integer capacity;

    @Column(name = "price", nullable = false)
    @JsonProperty("price")
    private Integer price;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Calendar> calendars;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<File> room_photos;

    public Room() {
    }

    public Room(Integer room_number, Hotel hotel, Integer capacity, Integer price) {
        this.room_number = room_number;
        this.setHotel(hotel);
        this.capacity = capacity;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRoom_number() {
        return room_number;
    }

    public void setRoom_number(Integer room_number) {
        this.room_number = room_number;
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

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public List<Calendar> getCalendars() {
        return calendars;
    }

    public void setCalendars(List<Calendar> calendars) {
        this.calendars = calendars;
    }

    public List<File> getRoom_photos() {
        return room_photos;
    }

    public void setRoom_photos(List<File> room_photos) {
        this.room_photos = room_photos;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", room_number=" + room_number +
                ", hotel_id=" + hotel_id +
                ", capacity=" + capacity +
                ", calendars=" + calendars +
                '}';
    }
}
