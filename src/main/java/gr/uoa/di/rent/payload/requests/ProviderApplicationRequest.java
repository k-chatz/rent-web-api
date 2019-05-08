package gr.uoa.di.rent.payload.requests;

import org.hibernate.validator.constraints.Length;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

import static gr.uoa.di.rent.config.Constraint.*;

public class ProviderApplicationRequest {

    @NotBlank
    @Length(min = COMPANY_NAME_MIN, max = COMPANY_NAME_MAX, message = COMPANY_NAME_MIN_MAX_MESSAGE)
    @Pattern(regexp = COMPANY_NAME_PATTERN, message = COMPANY_NAME_PATTERN_MESSAGE)
    private String company_name;

    @NotBlank
    @Length(min = COMPANY_ADDRESS_MIN, max = COMPANY_ADDRESS_MAX, message = COMPANY_ADDRESS_MIN_MAX_MESSAGE)
    @Pattern(regexp = COMPANY_ADDRESS_PATTERN, message = COMPANY_ADDRESS_PATTERN_MESSAGE)
    private String company_address;

    @NotBlank
    @Length(min = TAX_NUMBER_MIN, max = TAX_NUMBER_MAX, message = TAX_NUMBER_MIN_MAX_MESSAGE)
    @Pattern(regexp = TAX_NUMBER_PATTERN, message = TAX_NUMBER_PATTERN_MESSAGE)
    private String tax_number;

    @NotBlank
    @Length(min = TAX_OFFICE_MIN, max = TAX_OFFICE_MAX, message = TAX_OFFICE_MIN_MAX_MESSAGE)
    @Pattern(regexp = TAX_OFFICE_PATTERN, message = TAX_OFFICE_PATTERN_MESSAGE)
    private String tax_office;

    @NotBlank
    @Length(min = FIRSTNAME_MIN, max = FIRSTNAME_MAX, message = FIRSTNAME_MIN_MAX_MESSAGE)
    @Pattern(regexp = FIRSTNAME_PATTERN, message = FIRSTNAME_PATTERN_MESSAGE)
    private String owner_name;

    @NotBlank
    @Length(min = LASTNAME_MIN, max = LASTNAME_MAX, message = LASTNAME_MIN_MAX_MESSAGE)
    @Pattern(regexp = LASTNAME_PATTERN, message = LASTNAME_PATTERN_MESSAGE)
    private String owner_surname;

    @NotBlank
    @Length(min = PATRONYM_MIN, max = PATRONYM_MAX, message = PATRONYM_MIN_MAX_MESSAGE)
    @Pattern(regexp = PATRONYM_PATTERN, message = PATRONYM_PATTERN_MESSAGE)
    private String owner_patronym;

    @NotBlank
    @Length(min = ID_CARD_NUMBER_MIN, max = ID_CARD_NUMBER_MAX, message = ID_CARD_NUMBER_MIN_MAX_MESSAGE)
    @Pattern(regexp = ID_CARD_NUMBER_PATTERN, message = ID_CARD_NUMBER_PATTERN_MESSAGE)
    private String id_card_number;

    @NonNull
    private Date id_card_date_of_issue;

    @NotBlank
    @Length(min = RESIDENCE_ADDRESS_MIN, max = RESIDENCE_ADDRESS_MAX, message = RESIDENCE_ADDRESS_MIN_MAX_MESSAGE)
    @Pattern(regexp = RESIDENCE_ADDRESS_PATTERN, message = RESIDENCE_ADDRESS_PATTERN_MESSAGE)
    private String residence_address;

    public ProviderApplicationRequest() {
    }

    public ProviderApplicationRequest(@NotBlank String company_name, @NotBlank String company_address, @NotBlank String tax_number, @NotBlank String tax_office, @NotBlank @Size(min = 2, max = 45) String owner_name, @NotBlank @Size(min = 2, max = 45) String owner_surname, @NotBlank String owner_patronym, @NotBlank String id_card_number, Date id_card_date_of_issue, @NotBlank String residence_address) {
        this.company_name = company_name;
        this.company_address = company_address;
        this.tax_number = tax_number;
        this.tax_office = tax_office;
        this.owner_name = owner_name;
        this.owner_surname = owner_surname;
        this.owner_patronym = owner_patronym;
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

    @Override
    public String toString() {
        return "ProviderApplicationRequest{" +
                "company_name='" + company_name + '\'' +
                ", company_address='" + company_address + '\'' +
                ", tax_number='" + tax_number + '\'' +
                ", tax_office='" + tax_office + '\'' +
                ", owner_name='" + owner_name + '\'' +
                ", owner_surname='" + owner_surname + '\'' +
                ", owner_patronym='" + owner_patronym + '\'' +
                ", id_card_number='" + id_card_number + '\'' +
                ", id_card_date_of_issue=" + id_card_date_of_issue +
                ", residence_address='" + residence_address + '\'' +
                '}';
    }
}
