package gr.uoa.di.rent.payload.responses;

import gr.uoa.di.rent.models.Room;


public class RoomResponse {

    private Room room;

    public RoomResponse() {}

    public RoomResponse(Room room) {
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public String toString() {
        return "RoomResponse{" +
                "room=" + room +
                '}';
    }
}
