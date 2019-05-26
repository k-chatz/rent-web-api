package gr.uoa.di.rent.payload.requests;

import gr.uoa.di.rent.models.Hotel;

import java.util.List;

public class HotelRequest {

    private String name;

    private String email;

    private Integer number_of_rooms;

    private double lat;

    private double lng;

    private String description_short;

    private String description_long;

    private double stars;

    private List<String> amenities;

    public HotelRequest() {
    }

    public Hotel asHotel(Long business_id) {
        return new Hotel(
                business_id,
                this.getName(),
                this.getEmail(),
                this.getNumber_of_rooms(),
                this.getLat(),
                this.getLng(),
                this.getDescription_short(),
                this.getDescription_long(),
                this.getStars()
        );
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

    public Integer getNumber_of_rooms() {
        return number_of_rooms;
    }

    public void setNumber_of_rooms(Integer number_of_rooms) {
        this.number_of_rooms = number_of_rooms;
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

    public double getStars() {
        return stars;
    }

    public void setStars(double stars) {
        this.stars = stars;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }
}
