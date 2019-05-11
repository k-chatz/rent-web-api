package gr.uoa.di.rent.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import gr.uoa.di.rent.models.audit.UserDateAudit;
import gr.uoa.di.rent.validators.Rating;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "reviews", schema = "rent")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "reservation_id",
        "hotel_id",
        "description",
        "rating"
})
public class Review extends UserDateAudit implements Serializable {

    @Id
    @Column(name = "id")
    @JsonProperty("id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    @JsonProperty("reservation_id")
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    @JsonProperty("hotel_id")
    private Hotel hotel;

    @Column(name = "description", nullable = false, length = 255)
    @JsonProperty("description")
    private String description;

    //@Rating
    @Column(name = "rating", nullable = false)
    @JsonProperty("rating")
    private Integer rating;

    public Review() {
    }

    public Review(Long id, Reservation reservation, Hotel hotel, String description, Integer rating) {
        this.id = id;
        this.reservation = reservation;
        this.hotel = hotel;
        this.description = description;
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", reservation=" + reservation +
                ", hotel=" + hotel +
                ", description='" + description + '\'' +
                ", rating=" + rating +
                '}';
    }
}
