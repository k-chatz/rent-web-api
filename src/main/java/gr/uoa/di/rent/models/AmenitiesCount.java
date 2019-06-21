package gr.uoa.di.rent.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class AmenitiesCount implements Serializable {

    @Id
    private String amenity;

    private Integer count;

    public AmenitiesCount() {
    }

    public AmenitiesCount(String amenity, Integer count) {
        this.amenity = amenity;
        this.count = count;
    }

    public String getAmenity() {
        return amenity;
    }

    public void setAmenity(String amenity) {
        this.amenity = amenity;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "AmenitiesCount{" +
                "amenity='" + amenity + '\'' +
                ", count=" + count +
                '}';
    }
}
