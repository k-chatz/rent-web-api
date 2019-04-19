package gr.uoa.di.rent.payload.responses;


public class CheckEmailResponse {

    private Boolean available;

    public CheckEmailResponse() {
    }

    public CheckEmailResponse(Boolean available) {
        this.available = available;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

}




