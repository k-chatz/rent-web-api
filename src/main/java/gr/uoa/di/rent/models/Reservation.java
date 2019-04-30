package gr.uoa.di.rent.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import gr.uoa.di.rent.models.audit.UserDateAudit;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "reservations", schema = "rent")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "room_id",
        "start_date",
        "end_date",
        "transaction_id"
})
public class Reservation extends UserDateAudit {

    @Id
    @Column(name = "id")
    @JsonProperty("id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    @JsonProperty("room_id")
    private Room room;

    @Column(name = "start_date", nullable = false)
    @JsonProperty("start_date")
    private Date start_date;

    @Column(name = "end_date", nullable = false)
    @JsonProperty("end_date")
    private Date end_date;

    @OneToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    @JsonProperty("transaction_id")
    private Transaction transaction;

    public Reservation() {
    }

    public Reservation(Long id, Room room, Date start_date, Date end_date, Transaction transaction) {
        this.id = id;
        this.room = room;
        this.start_date = start_date;
        this.end_date = end_date;
        this.transaction = transaction;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", room=" + room +
                ", start_date=" + start_date +
                ", end_date=" + end_date +
                ", transaction=" + transaction +
                '}';
    }
}
