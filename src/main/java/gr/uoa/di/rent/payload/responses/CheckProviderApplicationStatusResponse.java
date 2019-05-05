package gr.uoa.di.rent.payload.responses;

public class CheckProviderApplicationStatusResponse {

    private Boolean pending;

    public CheckProviderApplicationStatusResponse() {
    }

    public CheckProviderApplicationStatusResponse(Boolean pending) {
        this.pending = pending;
    }

    public Boolean getPending() {
        return pending;
    }

    public void setPending(Boolean pending) {
        this.pending = pending;
    }

    @Override
    public String toString() {
        return "CheckProviderApplicationStatusResponse{" +
                "pending=" + pending +
                '}';
    }
}
