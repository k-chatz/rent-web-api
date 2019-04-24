package gr.uoa.di.rent.payload.responses;


public class LockUnlockResponse {

    private int numOfUsers;

    private String message;

    public LockUnlockResponse() {
    }

    public LockUnlockResponse(int numOfUsers, String message) {
        this.numOfUsers = numOfUsers;
        this.message = message;
    }

    public int getNumOfUsers() {
        return numOfUsers;
    }

    public void setNumOfUsers(int numOfUsers) {
        this.numOfUsers = numOfUsers;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "LockUnlockResponse{" +
                "numOfUsers=" + numOfUsers +
                ", message='" + message + '\'' +
                '}';
    }
}
