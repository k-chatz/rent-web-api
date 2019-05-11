package gr.uoa.di.rent.payload.requests;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class ReservationRequest {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) //accept Dates only in YYYY-MM-DD
    private Date startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) //accept Dates only in YYYY-MM-DD
    private Date endDate;

    public ReservationRequest() {
    }

    public ReservationRequest(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
