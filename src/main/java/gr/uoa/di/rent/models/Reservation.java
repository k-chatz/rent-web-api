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
        "start",
        "end",
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

    @Column(name = "start", nullable = false)
    @JsonProperty("start")
    private Date start;

    @Column(name = "end", nullable = false)
    @JsonProperty("end")
    private Date end;

    @OneToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    @JsonProperty("transaction_id")
    private Transaction transaction;

    public Reservation() {
    }

    public Reservation(Long id, Room room, Date start, Date end, Transaction transaction) {
        this.id = id;
        this.room = room;
        this.start = start;
        this.end = end;
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

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
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
                ", start=" + start +
                ", end=" + end +
                ", transaction=" + transaction +
                '}';
    }
}
