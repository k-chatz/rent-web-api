package gr.uoa.di.rent.payload.requests;

import gr.uoa.di.rent.models.Hotel;

public class HotelRequest {

    private String name;

    private Integer number_of_rooms;

    private String lat;

    private String lng;

    private String description_short;

    private String description_long;

    private String stars;

    public HotelRequest() {
    }

    public Hotel asHotel(Long business_id) {
        return new Hotel(
                business_id,
                this.getName(),
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

}
