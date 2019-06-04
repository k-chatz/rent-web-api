package gr.uoa.di.rent.payload.requests.filters;

import gr.uoa.di.rent.util.AppConstants;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class PagedHotelsFilter extends PagedResponseFilter {

    /** * * * * * * * * * * * * *
     *    Hotel Basic Filters   *
     *  * * * * * * * * * * * * */
    @FutureOrPresent
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate start_date = AppConstants.DEFAULT_START_DATE;

    @FutureOrPresent
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate end_date = AppConstants.DEFAULT_END_DATE;

    private int visitors = AppConstants.DEFAULT_VISITORS_NUMBER;

    @NotNull
    private double lat;
    @NotNull
    private double lng;

    /** * * * * * * * * * * * * *
     *     Amenities Filters    *
     *  * * * * * * * * * * * * */
    private boolean wifi = false;
    private boolean swimmingPool = false;
    private boolean gym = false;
    private boolean spa = false;
    private boolean bar = false;
    private boolean restaurant = false;
    private boolean petsAllowed = false;
    private boolean parking = false;
    private boolean roomService = false;

    public PagedHotelsFilter() {
        super();
    }

    public PagedHotelsFilter(int page, int size, String sort_field, String order, @FutureOrPresent LocalDate start_date,
                             @FutureOrPresent LocalDate end_date, int visitors, double lat, double lon, boolean wifi,
                             boolean swimmingPool, boolean gym, boolean spa, boolean bar, boolean restaurant,
                             boolean petsAllowed, boolean parking, boolean roomService) {
        super(page, size, sort_field, order);
        this.start_date = start_date;
        this.end_date = end_date;
        this.visitors = visitors;
        this.lat = lat;
        this.lng = lon;
        this.wifi = wifi;
        this.swimmingPool = swimmingPool;
        this.gym = gym;
        this.spa = spa;
        this.bar = bar;
        this.restaurant = restaurant;
        this.petsAllowed = petsAllowed;
        this.parking = parking;
        this.roomService = roomService;
    }

    public LocalDate getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDate start_date) {
        this.start_date = start_date;
    }

    public LocalDate getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDate end_date) {
        this.end_date = end_date;
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

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public boolean isParking() {
        return parking;
    }

    public void setParking(boolean parking) {
        this.parking = parking;
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

    public boolean isRoomService() {
        return roomService;
    }

    public void setRoomService(boolean roomService) {
        this.roomService = roomService;
    }

    public boolean isSwimmingPool() {
        return swimmingPool;
    }

    public void setSwimmingPool(boolean swimmingPool) {
        this.swimmingPool = swimmingPool;
    }

    @Override
    public String toString() {
        return "PagedHotelsFilter{" +
                "start_date=" + start_date +
                ", end_date=" + end_date +
                ", visitors=" + visitors +
                ", lat=" + lat +
                ", lng=" + lng +
                ", gym=" + gym +
                ", spa=" + spa +
                ", bar=" + bar +
                ", wifi=" + wifi +
                ", parking=" + parking +
                ", restaurant=" + restaurant +
                ", petsAllowed=" + petsAllowed +
                ", roomService=" + roomService +
                ", swimmingPool=" + swimmingPool +
                '}';
    }
}
