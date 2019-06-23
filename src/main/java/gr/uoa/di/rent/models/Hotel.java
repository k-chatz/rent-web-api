package gr.uoa.di.rent.models;

import com.fasterxml.jackson.annotation.*;
import gr.uoa.di.rent.models.audit.UserDateAudit;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "hotels", schema = "rent")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"provider_id"})
@JsonPropertyOrder({
        "id",
        "name",
        "email",
        "provider_id",
        "business_id",
        "number_of_rooms",
        "stars",
        "lat",
        "lng",
        "description_short",
        "description_long",
        "rooms",
        "amenities"
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

    @Nullable
    @Column(name = "email", unique = true, length = 60)
    @JsonProperty("email")
    private String email;

    @Column(name = "number_of_rooms", nullable = false)
    @JsonProperty("number_of_rooms")
    private int numberOfRooms;

    @Column(name = "lat", nullable = false)
    @JsonProperty("lat")
    private double lat;

    @Column(name = "lng", nullable = false)
    @JsonProperty("lng")
    private double lng;

    @Column(name = "description_short", nullable = false, length = 100)
    @JsonProperty("description_short")
    private String descriptionShort;

    @Column(name = "description_long", nullable = false, length = 2500)
    @JsonProperty("description_long")
    private String descriptionLong;

    @Column(name = "stars")
    @JsonProperty("stars")
    private double stars;

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
    //@JsonIgnore
    private List<File> hotel_photos;


      /*                                                                           */
     /*                 Σχέση Πολλά-Προς-Πολλά ((Hotel))-((Amenities))            */
    /*                                                                           */
    @ManyToMany
    @JoinTable(
            name = "hotel_amenities",
            joinColumns = {@JoinColumn(name = "hotel_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "amenity_id", referencedColumnName = "id")}
    )
    @JsonProperty("amenities")
    private Collection<Amenity> amenities;

    public Collection<Amenity> getAmenities() {
        return amenities;
    }

    public void setAmenities(Collection<Amenity> amenities) {
        this.amenities = amenities;
    }

    public Hotel() {
    }

    public Hotel(Business business, String name, String email, Integer numberOfRooms, double lat, double lng,
                 String descriptionShort, String descriptionLong, double stars) {
        this.name = name;

        if ( email == null )
            this.email = "info@" + StringUtils.replace(name, " ", "_").toLowerCase() + ".com";
        else
            this.email = email;

        this.numberOfRooms = numberOfRooms;
        this.lat = lat;
        this.lng = lng;
        this.descriptionShort = descriptionShort;
        this.descriptionLong = descriptionLong;
        this.stars = stars;
        this.setBusiness(business);
    }

    // Used by the HotelRequest (In which we don't have the business-object, just its id)
    public Hotel(Long business_id, String name, String email, Integer numberOfRooms, double lat, double lng,
                 String descriptionShort, String descriptionLong, double stars) {
        this.name = name;

        if ( email == null )
            this.email = "info@" + StringUtils.replace(name, " ", "_").toLowerCase() + ".com";
        else
            this.email = email;

        this.numberOfRooms = numberOfRooms;
        this.lat = lat;
        this.lng = lng;
        this.descriptionShort = descriptionShort;
        this.descriptionLong = descriptionLong;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getDescriptionShort() {
        return descriptionShort;
    }

    public void setDescriptionShort(String descriptionShort) {
        this.descriptionShort = descriptionShort;
    }

    public String getDescriptionLong() {
        return descriptionLong;
    }

    public void setDescriptionLong(String descriptionLong) {
        this.descriptionLong = descriptionLong;
    }

    public double getStars() {
        return stars;
    }

    public void setStars(double stars) {
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
                ", email=" + email +
                ", numberOfRooms=" + numberOfRooms +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", descriptionShort='" + descriptionShort + '\'' +
                ", descriptionLong='" + descriptionLong + '\'' +
                ", stars='" + stars + '\'' +
                ", business_id=" + business_id +
                '}';
    }
}
