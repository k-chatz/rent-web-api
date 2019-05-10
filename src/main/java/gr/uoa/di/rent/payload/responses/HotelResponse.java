package gr.uoa.di.rent.payload.responses;

import gr.uoa.di.rent.models.Hotel;


public class HotelResponse {

    private Hotel hotel;

    public HotelResponse() {
    }

    public HotelResponse(Hotel hotel) {
        this.hotel = hotel;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    @Override
    public String toString() {
        return "HotelResponse{" +
                "hotel=" + hotel +
                '}';
    }
}
