package gr.uoa.di.rent.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "businesses", schema = "rent")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "business_name",
        "address",
        "tax_number",
        "tax_office",
        "name",
        "surname",
        "patronym",
        "id_card_number",
        "id_card_date_of_issue",
        "residence_address"
})
public class Business {

    @Id
    @Column(name = "id")
    @JsonProperty("id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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

    @Column(name = "name", nullable = false)
    @JsonProperty("name")
    private String name;

    @Column(name = "surname", nullable = false)
    @JsonProperty("surname")
    private String surname;

    @Column(name = "patronym", nullable = false)
    @JsonProperty("patronym")
    private String patronym;

    @Column(name = "id_card_number", nullable = false)
    @JsonProperty("id_card_number")
    private String id_card_number;

    @Column(name = "id_card_date_of_issue", nullable = false)
    @JsonProperty("id_card_date_of_issue")
    private Date id_card_date_of_issue;

    @Column(name = "residence_address", nullable = false)
    @JsonProperty("residence_address")
    private String residence_address;

    @ManyToOne
    @JoinColumn(name = "provider_id", nullable = false)
    @JsonProperty("provider_id")
    private User provider;

    public Business() {

    }

    public Business(String address, String tax_number, String tax_office, String name, String surname,
                    String patronym, String id_card_number, Date id_card_date_of_issue,
                    String residence_address, User provider) {
        this.address = address;
        this.tax_number = tax_number;
        this.tax_office = tax_office;
        this.name = name;
        this.surname = surname;
        this.patronym = patronym;
        this.id_card_number = id_card_number;
        this.id_card_date_of_issue = id_card_date_of_issue;
        this.residence_address = residence_address;
        this.provider = provider;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronym() {
        return patronym;
    }

    public void setPatronym(String patronym) {
        this.patronym = patronym;
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

    @Override
    public String toString() {
        return "Business{" +
                "business_name='" + business_name + '\'' +
                ", address='" + address + '\'' +
                ", tax_number='" + tax_number + '\'' +
                ", tax_office='" + tax_office + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", patronym='" + patronym + '\'' +
                ", id_card_number='" + id_card_number + '\'' +
                ", id_card_date_of_issue=" + id_card_date_of_issue +
                ", residence_address='" + residence_address + '\'' +
                ", provider=" + provider +
                '}';
    }
}
