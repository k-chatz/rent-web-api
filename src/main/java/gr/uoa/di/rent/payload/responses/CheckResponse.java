package gr.uoa.di.rent.payload.responses;

public class CheckResponse {

    private Boolean exists;

    public CheckResponse() {
    }

    public CheckResponse(Boolean exists) {
        this.exists = exists;
    }

    public Boolean getExists() {
        return exists;
    }

    public void setExists(Boolean exists) {
        this.exists = exists;
    }

    @Override
    public String toString() {
        return "CheckResponse{" +
                "exists=" + exists +
                '}';
    }
}
