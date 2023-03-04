package com.codebridgecommunity.crm.domain;

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
 * A Company.
 */
@Table("company")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("industry")
    private String industry;

    @NotNull(message = "must not be null")
    @Column("location")
    private String location;

    @NotNull(message = "must not be null")
    @Column("established")
    private String established;

    @Column("engage")
    private SocMed engage;

    @Transient
    @JsonIgnoreProperties(value = { "orders", "company" }, allowSetters = true)
    private Set<Customer> customers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Company id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIndustry() {
        return this.industry;
    }

    public Company industry(String industry) {
        this.setIndustry(industry);
        return this;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getLocation() {
        return this.location;
    }

    public Company location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEstablished() {
        return this.established;
    }

    public Company established(String established) {
        this.setEstablished(established);
        return this;
    }

    public void setEstablished(String established) {
        this.established = established;
    }

    public SocMed getEngage() {
        return this.engage;
    }

    public Company engage(SocMed engage) {
        this.setEngage(engage);
        return this;
    }

    public void setEngage(SocMed engage) {
        this.engage = engage;
    }

    public Set<Customer> getCustomers() {
        return this.customers;
    }

    public void setCustomers(Set<Customer> customers) {
        if (this.customers != null) {
            this.customers.forEach(i -> i.setCompany(null));
        }
        if (customers != null) {
            customers.forEach(i -> i.setCompany(this));
        }
        this.customers = customers;
    }

    public Company customers(Set<Customer> customers) {
        this.setCustomers(customers);
        return this;
    }

    public Company addCustomer(Customer customer) {
        this.customers.add(customer);
        customer.setCompany(this);
        return this;
    }

    public Company removeCustomer(Customer customer) {
        this.customers.remove(customer);
        customer.setCompany(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Company)) {
            return false;
        }
        return id != null && id.equals(((Company) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Company{" +
            "id=" + getId() +
            ", industry='" + getIndustry() + "'" +
            ", location='" + getLocation() + "'" +
            ", established='" + getEstablished() + "'" +
            ", engage='" + getEngage() + "'" +
            "}";
    }
}
