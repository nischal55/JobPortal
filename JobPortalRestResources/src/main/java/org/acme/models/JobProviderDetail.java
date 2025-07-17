package org.acme.models;

import jakarta.persistence.*;

@Entity
@Table(name = "job_provider_detail")
public class JobProviderDetail extends BaseEntity{
    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private UserDetail user;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "website", nullable = true)
    private String website;

    @Column(name = "contact_no",nullable = false)
    private String contactNo;

    @Column(name = "contact_email",nullable = false)
    private String contactEmail;

    @Column(name = "description", nullable = false)
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public UserDetail getUser() {
        return user;
    }

    public void setUser(UserDetail user) {
        this.user = user;
    }
}
