package gr.uoa.di.rent.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import gr.uoa.di.rent.models.audit.UserDateAudit;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "businesses", schema = "rent")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "business_name",
        "address",
        "tax_number",
        "tax_office",
        "owner_name",
        "owner_surname",
        "owner_patronym",
        "id_card_number",
        "id_card_date_of_issue",
        "residence_address",
        "provider"
})
public class Business extends UserDateAudit {

    @Id
    @Column(name = "id")
    @JsonProperty("id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "business_name", unique = true, nullable = false, length = 100)
    @JsonProperty("business_name")
    private String business_name;

    @Column(name = "address", nullable = false)
    @JsonProperty("address")
    private String address;

    @Column(name = "tax_number", unique = true, nullable = false)
    @JsonProperty("tax_number")
    private String tax_number;

    @Column(name = "tax_office", nullable = false)
    @JsonProperty("tax_office")
    private String tax_office;

    @Column(name = "owner_name", nullable = false)
    @JsonProperty("name")
    private String owner_name;

    @Column(name = "owner_surname", nullable = false)
    @JsonProperty("owner_surname")
    private String owner_surname;

    @Column(name = "owner_patronym", nullable = false)
    @JsonProperty("owner_patronym")
    private String owner_patronym;

    @Column(name = "id_card_number", nullable = false)
    @JsonProperty("id_card_number")
    private String id_card_number;

    @Column(name = "id_card_date_of_issue", nullable = false)
    @JsonProperty("id_card_date_of_issue")
    private Date id_card_date_of_issue;

    @Column(name = "residence_address", nullable = false)
    @JsonProperty("residence_address")
    private String residence_address;

    @OneToOne
    @JoinColumn(name = "provider_id", nullable = false)
    @JsonProperty("provider")
    private User provider;

    @OneToMany(mappedBy = "business", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty("hotels")
    private List<Hotel> hotels;


    public Business() {
    }

    public Business(String business_name, String address, String tax_number, String tax_office, String owner_name, String owner_surname, String owner_patronym, String id_card_number, Date id_card_date_of_issue, String residence_address, User provider) {
        this.business_name = business_name;
        this.address = address;
        this.tax_number = tax_number;
        this.tax_office = tax_office;
        this.owner_name = owner_name;
        this.owner_surname = owner_surname;
        this.owner_patronym = owner_patronym;
        this.id_card_number = id_card_number;
        this.id_card_date_of_issue = id_card_date_of_issue;
        this.residence_address = residence_address;
        this.provider = provider;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTax_number() {
        return tax_number;
    }

    public void setTax_number(String tax_number) {
        this.tax_number = tax_number;
    }

    public String getTax_office() {
        return tax_office;
    }

    public void setTax_office(String tax_office) {
        this.tax_office = tax_office;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getOwner_surname() {
        return owner_surname;
    }

    public void setOwner_surname(String owner_surname) {
        this.owner_surname = owner_surname;
    }

    public String getOwner_patronym() {
        return owner_patronym;
    }

    public void setOwner_patronym(String owner_patronym) {
        this.owner_patronym = owner_patronym;
    }

    public String getId_card_number() {
        return id_card_number;
    }

    public void setId_card_number(String id_card_number) {
        this.id_card_number = id_card_number;
    }

    public Date getId_card_date_of_issue() {
        return id_card_date_of_issue;
    }

    public void setId_card_date_of_issue(Date id_card_date_of_issue) {
        this.id_card_date_of_issue = id_card_date_of_issue;
    }

    public String getResidence_address() {
        return residence_address;
    }

    public void setResidence_address(String residence_address) {
        this.residence_address = residence_address;
    }

    public User getProvider() {
        return provider;
    }

    public void setProvider(User provider) {
        this.provider = provider;
    }

    public List<Hotel> getHotels() {
        return hotels;
    }

    public void setHotels(List<Hotel> hotels) {
        this.hotels = hotels;
    }

    @Override
    public String toString() {
        return "Business{" +
                "id=" + id +
                ", business_name='" + business_name + '\'' +
                ", address='" + address + '\'' +
                ", tax_number='" + tax_number + '\'' +
                ", tax_office='" + tax_office + '\'' +
                ", owner_name='" + owner_name + '\'' +
                ", owner_surname='" + owner_surname + '\'' +
                ", owner_patronym='" + owner_patronym + '\'' +
                ", id_card_number='" + id_card_number + '\'' +
                ", id_card_date_of_issue=" + id_card_date_of_issue +
                ", residence_address='" + residence_address + '\'' +
                ", provider=" + provider +
                ", hotels=" + hotels +
                '}';
    }
}
