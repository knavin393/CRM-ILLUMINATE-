package com.codebridgecommunity.crm.domain;

import com.codebridgecommunity.crm.domain.enumeration.Gender;
import com.codebridgecommunity.crm.domain.enumeration.SocMed;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Customer.
 */
@Table("customer")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("cust_name")
    private String custName;

    @NotNull(message = "must not be null")
    @Column("gender")
    private Gender gender;

    @NotNull(message = "must not be null")
    @Column("cust_ic")
    private String custIC;

    @NotNull(message = "must not be null")
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+.com")
    @Column("cust_email")
    private String custEmail;

    @NotNull(message = "must not be null")
    @Column("cust_phone")
    private String custPhone;

    @NotNull(message = "must not be null")
    @Column("company_name")
    private String companyName;

    @NotNull(message = "must not be null")
    @Column("cust_job_title")
    private String custJobTitle;

    @Column("engage")
    private SocMed engage;

    @Transient
    @JsonIgnoreProperties(value = { "product", "customer" }, allowSetters = true)
    private Set<ProductOrder> orders = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "customers" }, allowSetters = true)
    private Company company;

    @Column("company_id")
    private Long companyId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Customer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustName() {
        return this.custName;
    }

    public Customer custName(String custName) {
        this.setCustName(custName);
        return this;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public Gender getGender() {
        return this.gender;
    }

    public Customer gender(Gender gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getCustIC() {
        return this.custIC;
    }

    public Customer custIC(String custIC) {
        this.setCustIC(custIC);
        return this;
    }

    public void setCustIC(String custIC) {
        this.custIC = custIC;
    }

    public String getCustEmail() {
        return this.custEmail;
    }

    public Customer custEmail(String custEmail) {
        this.setCustEmail(custEmail);
        return this;
    }

    public void setCustEmail(String custEmail) {
        this.custEmail = custEmail;
    }

    public String getCustPhone() {
        return this.custPhone;
    }

    public Customer custPhone(String custPhone) {
        this.setCustPhone(custPhone);
        return this;
    }

    public void setCustPhone(String custPhone) {
        this.custPhone = custPhone;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public Customer companyName(String companyName) {
        this.setCompanyName(companyName);
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCustJobTitle() {
        return this.custJobTitle;
    }

    public Customer custJobTitle(String custJobTitle) {
        this.setCustJobTitle(custJobTitle);
        return this;
    }

    public void setCustJobTitle(String custJobTitle) {
        this.custJobTitle = custJobTitle;
    }

    public SocMed getEngage() {
        return this.engage;
    }

    public Customer engage(SocMed engage) {
        this.setEngage(engage);
        return this;
    }

    public void setEngage(SocMed engage) {
        this.engage = engage;
    }

    public Set<ProductOrder> getOrders() {
        return this.orders;
    }

    public void setOrders(Set<ProductOrder> productOrders) {
        if (this.orders != null) {
            this.orders.forEach(i -> i.setCustomer(null));
        }
        if (productOrders != null) {
            productOrders.forEach(i -> i.setCustomer(this));
        }
        this.orders = productOrders;
    }

    public Customer orders(Set<ProductOrder> productOrders) {
        this.setOrders(productOrders);
        return this;
    }

    public Customer addOrder(ProductOrder productOrder) {
        this.orders.add(productOrder);
        productOrder.setCustomer(this);
        return this;
    }

    public Customer removeOrder(ProductOrder productOrder) {
        this.orders.remove(productOrder);
        productOrder.setCustomer(null);
        return this;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
        this.companyId = company != null ? company.getId() : null;
    }

    public Customer company(Company company) {
        this.setCompany(company);
        return this;
    }

    public Long getCompanyId() {
        return this.companyId;
    }

    public void setCompanyId(Long company) {
        this.companyId = company;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customer)) {
            return false;
        }
        return id != null && id.equals(((Customer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Customer{" +
            "id=" + getId() +
            ", custName='" + getCustName() + "'" +
            ", gender='" + getGender() + "'" +
            ", custIC='" + getCustIC() + "'" +
            ", custEmail='" + getCustEmail() + "'" +
            ", custPhone='" + getCustPhone() + "'" +
            ", companyName='" + getCompanyName() + "'" +
            ", custJobTitle='" + getCustJobTitle() + "'" +
            ", engage='" + getEngage() + "'" +
            "}";
    }
}
