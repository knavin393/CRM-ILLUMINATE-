package com.codebridgecommunity.crm.domain;

import com.codebridgecommunity.crm.domain.enumeration.POStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A PotentialOpportunity.
 */
@Table("potential_opportunity")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PotentialOpportunity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("follow_up")
    private String followUp;

    @NotNull(message = "must not be null")
    @Column("status")
    private POStatus status;

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

    public PotentialOpportunity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFollowUp() {
        return this.followUp;
    }

    public PotentialOpportunity followUp(String followUp) {
        this.setFollowUp(followUp);
        return this;
    }

    public void setFollowUp(String followUp) {
        this.followUp = followUp;
    }

    public POStatus getStatus() {
        return this.status;
    }

    public PotentialOpportunity status(POStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(POStatus status) {
        this.status = status;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        this.customerId = customer != null ? customer.getId() : null;
    }

    public PotentialOpportunity customer(Customer customer) {
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

    public PotentialOpportunity employee(Employee employee) {
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

    public PotentialOpportunity product(Product product) {
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
        if (!(o instanceof PotentialOpportunity)) {
            return false;
        }
        return id != null && id.equals(((PotentialOpportunity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PotentialOpportunity{" +
            "id=" + getId() +
            ", followUp='" + getFollowUp() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
