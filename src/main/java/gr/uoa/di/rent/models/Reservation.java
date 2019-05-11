package gr.uoa.di.rent.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import gr.uoa.di.rent.models.audit.UserDateAudit;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "reservations", schema = "rent")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "room_id",
        "calendar"
})
public class Reservation extends UserDateAudit implements Serializable {

    @Id
    @Column(name = "id")
    @JsonProperty("id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    @JsonProperty("room_id")
    private Room room;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "transaction_id", nullable = false)
    @JsonProperty("transaction_id")
    private Transaction transaction;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "calendar_id", nullable = false)
    @JsonProperty("calendar")
    private Calendar calendar;

    public Reservation() {
    }

    public Reservation(Room room, Transaction transaction, Calendar calendar) {
        this.room = room;
        this.transaction = transaction;
        this.calendar = calendar;
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

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", room=" + room +
                ", transaction=" + transaction +
                ", calendar=" + calendar +
                '}';
    }
}
