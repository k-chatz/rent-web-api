package gr.uoa.di.rent.payload.requests;

import java.util.List;

public class LockRequest {

    private List<Long> userIDs;

    public LockRequest() {
    }

    public LockRequest(List<Long> userIDs) {
        this.userIDs = userIDs;
    }

    public List<Long> getUserIDs() {
        return userIDs;
    }

    public void setUserIDs(List<Long> userIDs) {
        this.userIDs = userIDs;
    }

    @Override
    public String toString() {
        return "LockRequest{" +
                "userIDs=" + userIDs +
                '}';
    }
}
