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
        "provider_id",
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

    @ManyToOne
    @JoinColumn(name = "provider_id", nullable = false)
    @JsonProperty("provider_id")
    private User provider;

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
    @JsonProperty("rooms")
    private List<Room> rooms;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "business", nullable = false)
    @JsonProperty("business")
    private Business business;


    public Hotel() {
    }

    public Hotel(User provider, Business business, Integer number_of_rooms, String lat, String lng,
                 String description_short, String description_long, String stars) {
        this.provider = provider;
        this.number_of_rooms = number_of_rooms;
        this.lat = lat;
        this.lng = lng;
        this.description_short = description_short;
        this.description_long = description_long;
        this.stars = stars;
        this.business = business;
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
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "id=" + id +
                ", provider=" + provider +
                ", number_of_rooms=" + number_of_rooms +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", description_short='" + description_short + '\'' +
                ", description_long='" + description_long + '\'' +
                ", stars='" + stars + '\'' +
                ", rooms=" + rooms +
                ", business=" + business +
                '}';
    }
}
