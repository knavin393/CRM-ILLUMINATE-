package com.codebridgecommunity.crm.domain;

import com.codebridgecommunity.crm.domain.enumeration.Gender;
import com.codebridgecommunity.crm.domain.enumeration.SocMed;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Employee.
 */
@Table("employee")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("emp_name")
    private String empName;

    @NotNull(message = "must not be null")
    @Min(value = 20)
    @Max(value = 45)
    @Column("emp_age")
    private Integer empAge;

    @NotNull(message = "must not be null")
    @Column("gender")
    private Gender gender;

    @NotNull(message = "must not be null")
    @Column("emp_job_title")
    private String empJobTitle;

    @NotNull(message = "must not be null")
    @Column("emp_phone")
    private String empPhone;

    @NotNull(message = "must not be null")
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+.com")
    @Column("emp_email")
    private String empEmail;

    @Column("engage")
    private SocMed engage;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Employee id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmpName() {
        return this.empName;
    }

    public Employee empName(String empName) {
        this.setEmpName(empName);
        return this;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public Integer getEmpAge() {
        return this.empAge;
    }

    public Employee empAge(Integer empAge) {
        this.setEmpAge(empAge);
        return this;
    }

    public void setEmpAge(Integer empAge) {
        this.empAge = empAge;
    }

    public Gender getGender() {
        return this.gender;
    }

    public Employee gender(Gender gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getEmpJobTitle() {
        return this.empJobTitle;
    }

    public Employee empJobTitle(String empJobTitle) {
        this.setEmpJobTitle(empJobTitle);
        return this;
    }

    public void setEmpJobTitle(String empJobTitle) {
        this.empJobTitle = empJobTitle;
    }

    public String getEmpPhone() {
        return this.empPhone;
    }

    public Employee empPhone(String empPhone) {
        this.setEmpPhone(empPhone);
        return this;
    }

    public void setEmpPhone(String empPhone) {
        this.empPhone = empPhone;
    }

    public String getEmpEmail() {
        return this.empEmail;
    }

    public Employee empEmail(String empEmail) {
        this.setEmpEmail(empEmail);
        return this;
    }

    public void setEmpEmail(String empEmail) {
        this.empEmail = empEmail;
    }

    public SocMed getEngage() {
        return this.engage;
    }

    public Employee engage(SocMed engage) {
        this.setEngage(engage);
        return this;
    }

    public void setEngage(SocMed engage) {
        this.engage = engage;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employee)) {
            return false;
        }
        return id != null && id.equals(((Employee) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Employee{" +
            "id=" + getId() +
            ", empName='" + getEmpName() + "'" +
            ", empAge=" + getEmpAge() +
            ", gender='" + getGender() + "'" +
            ", empJobTitle='" + getEmpJobTitle() + "'" +
            ", empPhone='" + getEmpPhone() + "'" +
            ", empEmail='" + getEmpEmail() + "'" +
            ", engage='" + getEngage() + "'" +
            "}";
    }
}
