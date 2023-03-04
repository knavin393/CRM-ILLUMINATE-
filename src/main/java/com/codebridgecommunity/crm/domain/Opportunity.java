package com.codebridgecommunity.crm.domain;

import com.codebridgecommunity.crm.domain.enumeration.OppStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Opportunity.
 */
@Table("opportunity")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Opportunity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("follow_up")
    private String followUp;

    @NotNull(message = "must not be null")
    @Column("status")
    private OppStatus status;

    @Transient
    private Customer customer;

    @Transient
    private Employee employee;

    @Transient
    private Product product;

    @Column("customer_id")
    private Long customerId;

    @Column("employee_id")
    private Long employeeId;

    @Column("product_id")
    private Long productId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Opportunity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFollowUp() {
        return this.followUp;
    }

    public Opportunity followUp(String followUp) {
        this.setFollowUp(followUp);
        return this;
    }

    public void setFollowUp(String followUp) {
        this.followUp = followUp;
    }

    public OppStatus getStatus() {
        return this.status;
    }

    public Opportunity status(OppStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OppStatus status) {
        this.status = status;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        this.customerId = customer != null ? customer.getId() : null;
    }

    public Opportunity customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
        this.employeeId = employee != null ? employee.getId() : null;
    }

    public Opportunity employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
        this.productId = product != null ? product.getId() : null;
    }

    public Opportunity product(Product product) {
        this.setProduct(product);
        return this;
    }

    public Long getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(Long customer) {
        this.customerId = customer;
    }

    public Long getEmployeeId() {
        return this.employeeId;
    }

    public void setEmployeeId(Long employee) {
        this.employeeId = employee;
    }

    public Long getProductId() {
        return this.productId;
    }

    public void setProductId(Long product) {
        this.productId = product;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Opportunity)) {
            return false;
        }
        return id != null && id.equals(((Opportunity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Opportunity{" +
            "id=" + getId() +
            ", followUp='" + getFollowUp() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
