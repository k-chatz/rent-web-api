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
        "start",
        "end",
        "expire",
        "price"
})
public class Calendar extends UserDateAudit {
    @Id
    @Column(name = "id")
    @JsonProperty("id")
    private Long id;

    @Column(name = "start", nullable = false)
    @JsonProperty("start")
    private Date start;

    @Column(name = "end", nullable = false)
    @JsonProperty("end")
    private Date end;

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
        this.start = start;
        this.end = end;
        this.expire = expire;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
                ", start=" + start +
                ", end=" + end +
                ", expire=" + expire +
                ", price=" + price +
                '}';
    }
}
