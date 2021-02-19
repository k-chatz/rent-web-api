package gr.uoa.di.rent.payload.requests.filters;

import gr.uoa.di.rent.util.AppConstants;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class PagedHotelsFilter extends PagedResponseFilter {

    /**
     * * * * * * * * * * * * *
     * Hotel Basic Filters   *
     * * * * * * * * * * * * *
     */
    @FutureOrPresent
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate checkin = AppConstants.DEFAULT_CHECKIN_DATE;

    @FutureOrPresent
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate checkout = AppConstants.DEFAULT_CHECKOUT_DATE;

    private int visitors = AppConstants.DEFAULT_VISITORS_NUMBER;

    @NotNull
    private double lat;
    @NotNull
    private double lng;

    private double radius = 5.0;


    /**
     * * * * * * * * * * * * *
     * Amenities Filters    *
     * * * * * * * * * * * * *
     */
    private boolean wifi = false;
    private boolean swimmingPool = false;
    private boolean gym = false;
    private boolean spa = false;
    private boolean bar = false;
    private boolean restaurant = false;
    private boolean petsAllowed = false;
    private boolean parking = false;
    private boolean roomService = false;

    private int minPrice = 0;
    private int maxPrice = 2000;

    public PagedHotelsFilter() {
        super();
    }

    public PagedHotelsFilter(int page, int size, String sort_field, String order, @FutureOrPresent LocalDate checkin,
                             @FutureOrPresent LocalDate checkout, int visitors, double lat, double lng, double radius, boolean wifi,
                             boolean swimmingPool, boolean gym, boolean spa, boolean bar, boolean restaurant,
                             boolean petsAllowed, boolean parking, boolean roomService, int minPrice, int maxPrice) {
        super(page, size, sort_field, order);
        this.checkin = checkin;
        this.checkout = checkout;
        this.visitors = visitors;
        this.lat = lat;
        this.lng = lng;
        this.radius = radius;
        this.wifi = wifi;
        this.swimmingPool = swimmingPool;
        this.gym = gym;
        this.spa = spa;
        this.bar = bar;
        this.restaurant = restaurant;
        this.petsAllowed = petsAllowed;
        this.parking = parking;
        this.roomService = roomService;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public LocalDate getCheckin() {
        return checkin;
    }

    public void setCheckin(LocalDate checkin) {
        this.checkin = checkin;
    }

    public LocalDate getCheckout() {
        return checkout;
    }

    public void setCheckout(LocalDate checkout) {
        this.checkout = checkout;
    }

    public int getVisitors() {
        return visitors;
    }

    public void setVisitors(int visitors) {
        this.visitors = visitors;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public boolean isSwimmingPool() {
        return swimmingPool;
    }

    public void setSwimmingPool(boolean swimmingPool) {
        this.swimmingPool = swimmingPool;
    }

    public boolean isGym() {
        return gym;
    }

    public void setGym(boolean gym) {
        this.gym = gym;
    }

    public boolean isSpa() {
        return spa;
    }

    public void setSpa(boolean spa) {
        this.spa = spa;
    }

    public boolean isBar() {
        return bar;
    }

    public void setBar(boolean bar) {
        this.bar = bar;
    }

    public boolean isRestaurant() {
        return restaurant;
    }

    public void setRestaurant(boolean restaurant) {
        this.restaurant = restaurant;
    }

    public boolean isPetsAllowed() {
        return petsAllowed;
    }

    public void setPetsAllowed(boolean petsAllowed) {
        this.petsAllowed = petsAllowed;
    }

    public boolean isParking() {
        return parking;
    }

    public void setParking(boolean parking) {
        this.parking = parking;
    }

    public boolean isRoomService() {
        return roomService;
    }

    public void setRoomService(boolean roomService) {
        this.roomService = roomService;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }

    @Override
    public String toString() {
        return "PagedHotelsFilter{" +
                "checkin=" + checkin +
                ", checkout=" + checkout +
                ", visitors=" + visitors +
                ", lat=" + lat +
                ", lng=" + lng +
                ", radius=" + radius +
                ", wifi=" + wifi +
                ", swimmingPool=" + swimmingPool +
                ", gym=" + gym +
                ", spa=" + spa +
                ", bar=" + bar +
                ", restaurant=" + restaurant +
                ", petsAllowed=" + petsAllowed +
                ", parking=" + parking +
                ", roomService=" + roomService +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                '}';
    }
}
