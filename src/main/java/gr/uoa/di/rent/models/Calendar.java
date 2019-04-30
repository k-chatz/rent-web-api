package gr.uoa.di.rent.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import gr.uoa.di.rent.models.audit.UserDateAudit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "calendar", schema = "rent")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "start_date",
        "end_date",
        "expire",
        "price"
})
public class Calendar extends UserDateAudit {
    @Id
    @Column(name = "id")
    @JsonProperty("id")
    private Long id;

    @Column(name = "start_date", nullable = false)
    @JsonProperty("start_date")
    private Date start_date;

    @Column(name = "end_date", nullable = false)
    @JsonProperty("end_date")
    private Date end_date;

    @Column(name = "expire", nullable = false)
    @JsonProperty("expire")
    private Boolean expire;

    @Column(name = "price", nullable = false)
    @JsonProperty("price")
    private Integer price;

    public Calendar() {
    }

    public Calendar(Long id, Date start, Date end, Boolean expire, Integer price) {
        this.id = id;
        this.start_date = start_date;
        this.end_date = end_date;
        this.expire = expire;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Boolean getExpire() {
        return expire;
    }

    public void setExpire(Boolean expire) {
        this.expire = expire;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Calendar{" +
                "id=" + id +
                ", start_date=" + start_date +
                ", end_date=" + end_date +
                ", expire=" + expire +
                ", price=" + price +
                '}';
    }
}
