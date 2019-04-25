package gr.uoa.di.rent.payload.requests;

import org.springframework.lang.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

public class ProviderApplicationRequest {

    @NotBlank
    private String company_name;

    @NotBlank
    private String company_address;

    @NotBlank
    private String tax_number;

    @NotBlank
    private String tax_office;

    @NotBlank
    @Size(min = 2, max = 45)
    private String name;

    @NotBlank
    @Size(min = 2, max = 45)
    private String surname;

    @NotBlank
    private String patronym;

    @NotBlank
    private String id_card_number;

    @NonNull
    private Date id_card_date_of_issue;

    @NotBlank
    private String residence_address;

    public ProviderApplicationRequest() {
    }

    public ProviderApplicationRequest(@NotBlank String company_name, @NotBlank String company_address, @NotBlank String tax_number, @NotBlank String tax_office, @NotBlank @Size(min = 2, max = 45) String name, @NotBlank @Size(min = 2, max = 45) String surname, @NotBlank String patronym, @NotBlank String id_card_number, Date id_card_date_of_issue, @NotBlank String residence_address) {
        this.company_name = company_name;
        this.company_address = company_address;
        this.tax_number = tax_number;
        this.tax_office = tax_office;
        this.name = name;
        this.surname = surname;
        this.patronym = patronym;
        this.id_card_number = id_card_number;
        this.id_card_date_of_issue = id_card_date_of_issue;
        this.residence_address = residence_address;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_address() {
        return company_address;
    }

    public void setCompany_address(String company_address) {
        this.company_address = company_address;
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

    @Override
    public String toString() {
        return "ProviderApplicationRequest{" +
                "company_name='" + company_name + '\'' +
                ", company_address='" + company_address + '\'' +
                ", tax_number='" + tax_number + '\'' +
                ", tax_office='" + tax_office + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", patronym='" + patronym + '\'' +
                ", id_card_number='" + id_card_number + '\'' +
                ", id_card_date_of_issue=" + id_card_date_of_issue +
                ", residence_address='" + residence_address + '\'' +
                '}';
    }
}
