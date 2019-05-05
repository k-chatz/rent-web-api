package gr.uoa.di.rent.payload.requests;

import java.util.List;

public class ApproveApplicationRequest {

    private List<Long> userIDs;

    public ApproveApplicationRequest() {
    }

    public ApproveApplicationRequest(List<Long> userIDs) {
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
        return "LockUnlockRequest{" +
                "userIDs=" + userIDs +
                '}';
    }
}
