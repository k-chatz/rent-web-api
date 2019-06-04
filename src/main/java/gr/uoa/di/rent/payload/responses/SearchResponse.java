package gr.uoa.di.rent.payload.responses;

import gr.uoa.di.rent.models.Hotel;

public class SearchResponse {
    private int floorPrice;
    private int ceilPrice;
    private AmenitiesCount amenitiesCount;

    private PagedResponse<Hotel> results;

    public SearchResponse() {
    }

    public SearchResponse(int floorPrice, int ceilPrice, AmenitiesCount amenitiesCount, PagedResponse<Hotel> results) {
        this.floorPrice = floorPrice;
        this.ceilPrice = ceilPrice;
        this.amenitiesCount = amenitiesCount;
        this.results = results;
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

    public PagedResponse<Hotel> getResults() {
        return results;
    }

    public void setResults(PagedResponse<Hotel> results) {
        this.results = results;
    }

    public AmenitiesCount getAmenitiesCount() {
        return amenitiesCount;
    }

    public void setAmenitiesCount(AmenitiesCount amenitiesCount) {
        this.amenitiesCount = amenitiesCount;
    }

    @Override
    public String toString() {
        return "SearchResponse{" +
                "floorPrice=" + floorPrice +
                ", ceilPrice=" + ceilPrice +
                ", amenitiesCount=" + amenitiesCount +
                ", results=" + results +
                '}';
    }
}
