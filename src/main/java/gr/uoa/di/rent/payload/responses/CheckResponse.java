package gr.uoa.di.rent.payload.responses;

public class CheckResponse {

    private Boolean available;

    public CheckResponse() {
    }

    public CheckResponse(Boolean available) {
        this.available = available;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

}
