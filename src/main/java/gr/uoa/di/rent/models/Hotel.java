package gr.uoa.di.rent.models;

import com.fasterxml.jackson.annotation.*;
import gr.uoa.di.rent.models.audit.UserDateAudit;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "hotels", schema = "rent")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"provider_id"})
@JsonPropertyOrder({
        "id",
        "name",
        "provider_id",
        "business_id",
        "number_of_rooms",
        "stars",
        "lat",
        "lng",
        "description_short",
        "description_long",
        "rooms"
})
public class Hotel extends UserDateAudit implements Serializable {

    @Id
    @Column(name = "id")
    @JsonProperty("id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @JsonProperty("name")
    private String name;

    @Column(name = "number_of_rooms", nullable = false)
    @JsonProperty("number_of_rooms")
    private Integer number_of_rooms;

    @Column(name = "lat", nullable = false)
    @JsonProperty("lat")
    private String lat;

    @Column(name = "lng", nullable = false)
    @JsonProperty("lng")
    private String lng;

    @Column(name = "description_short", nullable = false)
    @JsonProperty("description_short")
    private String description_short;

    @Column(name = "description_long", nullable = false)
    @JsonProperty("description_long")
    private String description_long;

    @Column(name = "stars")
    @JsonProperty("stars")
    private String stars;

    @OneToMany(mappedBy = "hotel", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Room> rooms;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "business", nullable = false)
    @JsonIgnore
    private Business business;

    @Transient
    @JsonProperty("business_id")
    private Long business_id;

    @OneToMany(mappedBy = "hotel", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<File> hotel_photos;

    public Hotel() {
    }

    public Hotel(Business business, String name, Integer number_of_rooms, String lat, String lng,
                 String description_short, String description_long, String stars) {
        this.name = name;
        this.number_of_rooms = number_of_rooms;
        this.lat = lat;
        this.lng = lng;
        this.description_short = description_short;
        this.description_long = description_long;
        this.stars = stars;
        this.setBusiness(business);
    }

    // Used by the HotelRequest (In which we don't have the business-object, just its id)
    public Hotel(Long business_id, String name, Integer number_of_rooms, String lat, String lng,
                 String description_short, String description_long, String stars) {
        this.name = name;
        this.number_of_rooms = number_of_rooms;
        this.lat = lat;
        this.lng = lng;
        this.description_short = description_short;
        this.description_long = description_long;
        this.stars = stars;
        this.business_id = business_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumber_of_rooms() {
        return number_of_rooms;
    }

    public void setNumber_of_rooms(Integer number_of_rooms) {
        this.number_of_rooms = number_of_rooms;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getDescription_short() {
        return description_short;
    }

    public void setDescription_short(String description_short) {
        this.description_short = description_short;
    }

    public String getDescription_long() {
        return description_long;
    }

    public void setDescription_long(String description_long) {
        this.description_long = description_long;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
        this.setBusiness_id(business.getId());
    }

    public Long getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(Long business_id) {
        this.business_id = business_id;
    }

    public List<File> getHotel_photos() {
        return hotel_photos;
    }

    public void setHotel_photos(List<File> hotel_photos) {
        this.hotel_photos = hotel_photos;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "id=" + id +
                ", name=" + name +
                ", number_of_rooms=" + number_of_rooms +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", description_short='" + description_short + '\'' +
                ", description_long='" + description_long + '\'' +
                ", stars='" + stars + '\'' +
                ", business_id=" + business_id +
                '}';
    }
}
