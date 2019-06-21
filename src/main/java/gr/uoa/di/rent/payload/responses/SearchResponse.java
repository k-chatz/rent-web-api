package gr.uoa.di.rent.payload.responses;

import gr.uoa.di.rent.models.AmenitiesCount;
import gr.uoa.di.rent.models.Hotel;

import java.util.List;

public class SearchResponse {

    private int floorPrice;
    private int ceilPrice;
    private List<AmenitiesCount> amenities;
    private List<Hotel> allHotels;
    private PagedResponse<Hotel> pagedHotels;

    public SearchResponse() {
    }

    public SearchResponse(int floorPrice, int ceilPrice, List<AmenitiesCount> amenities,
                          List<Hotel> allHotels,
                          PagedResponse<Hotel> pagedHotels) {
        this.floorPrice = floorPrice;
        this.ceilPrice = ceilPrice;
        this.amenities = amenities;
        this.allHotels = allHotels;
        this.pagedHotels = pagedHotels;
    }

    public int getFloorPrice() {
        return floorPrice;
    }

    public void setFloorPrice(int floorPrice) {
        this.floorPrice = floorPrice;
    }

    public int getCeilPrice() {
        return ceilPrice;
    }

    public void setCeilPrice(int ceilPrice) {
        this.ceilPrice = ceilPrice;
    }

    public List<AmenitiesCount> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<AmenitiesCount> amenities) {
        this.amenities = amenities;
    }

    public List<Hotel> getAllHotels() {
        return allHotels;
    }

    public void setAllHotels(List<Hotel> allHotels) {
        this.allHotels = allHotels;
    }

    public PagedResponse<Hotel> getPagedHotels() {
        return pagedHotels;
    }

    public void setPagedHotels(PagedResponse<Hotel> pagedHotels) {
        this.pagedHotels = pagedHotels;
    }

    @Override
    public String toString() {
        return "SearchResponse{" +
                "floorPrice=" + floorPrice +
                ", ceilPrice=" + ceilPrice +
                ", amenities=" + amenities +
                ", allHotels=" + allHotels +
                ", pagedHotels=" + pagedHotels +
                '}';
    }
}
