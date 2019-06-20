package gr.uoa.di.rent.payload.responses;

import gr.uoa.di.rent.models.Hotel;

import java.util.List;

public class SearchResponse {

    private int floorPrice;
    private int ceilPrice;
    private AmenitiesCount amenitiesCount;
    private List<Hotel> allHotels;
    private PagedResponse<Hotel> pagedHotels;

    public SearchResponse() {
    }

    public SearchResponse(int floorPrice, int ceilPrice, AmenitiesCount amenitiesCount,
                          List<Hotel> allHotels,
                          PagedResponse<Hotel> pagedHotels) {
        this.floorPrice = floorPrice;
        this.ceilPrice = ceilPrice;
        this.amenitiesCount = amenitiesCount;
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

    public AmenitiesCount getAmenitiesCount() {
        return amenitiesCount;
    }

    public void setAmenitiesCount(AmenitiesCount amenitiesCount) {
        this.amenitiesCount = amenitiesCount;
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
                ", amenitiesCount=" + amenitiesCount +
                ", allHotels=" + allHotels +
                ", pagedHotels=" + pagedHotels +
                '}';
    }
}
