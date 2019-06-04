package gr.uoa.di.rent.payload.responses;

public class AmenitiesCount {
    private int wifi;
    private int swimmingPool;
    private int petsAllowed;
    private int gym;
    private int spa;
    private int bar;
    private int restaurant;
    private int roomService;
    private int parking;

    public AmenitiesCount() {
    }

    public AmenitiesCount(int wifi, int swimmingPool, int petsAllowed, int gym, int spa, int bar, int restaurant, int roomService, int parking) {
        this.wifi = wifi;
        this.swimmingPool = swimmingPool;
        this.petsAllowed = petsAllowed;
        this.gym = gym;
        this.spa = spa;
        this.bar = bar;
        this.restaurant = restaurant;
        this.roomService = roomService;
        this.parking = parking;
    }

    public int getWifi() {
        return wifi;
    }

    public void setWifi(int wifi) {
        this.wifi = wifi;
    }

    public int getSwimmingPool() {
        return swimmingPool;
    }

    public void setSwimmingPool(int swimmingPool) {
        this.swimmingPool = swimmingPool;
    }

    public int getPetsAllowed() {
        return petsAllowed;
    }

    public void setPetsAllowed(int petsAllowed) {
        this.petsAllowed = petsAllowed;
    }

    public int getGym() {
        return gym;
    }

    public void setGym(int gym) {
        this.gym = gym;
    }

    public int getSpa() {
        return spa;
    }

    public void setSpa(int spa) {
        this.spa = spa;
    }

    public int getBar() {
        return bar;
    }

    public void setBar(int bar) {
        this.bar = bar;
    }

    public int getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(int restaurant) {
        this.restaurant = restaurant;
    }

    public int getRoomService() {
        return roomService;
    }

    public void setRoomService(int roomService) {
        this.roomService = roomService;
    }

    public int getParking() {
        return parking;
    }

    public void setParking(int parking) {
        this.parking = parking;
    }

    @Override
    public String toString() {
        return "AmenitiesCount{" +
                "wifi=" + wifi +
                ", swimmingPool=" + swimmingPool +
                ", petsAllowed=" + petsAllowed +
                ", gym=" + gym +
                ", spa=" + spa +
                ", bar=" + bar +
                ", restaurant=" + restaurant +
                ", roomService=" + roomService +
                ", parking=" + parking +
                '}';
    }
}
