package org.acme.models;

import jakarta.persistence.*;

@Entity
@Table(name = "job_seeker_details")
public class JobSeekerDetail extends BaseEntity{
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private UserDetail user;

    @Column(name="contact_no", nullable = false)
    private String contactNo;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "summary",nullable = false)
    private String summary;

    @Column(name = "skills", nullable = false)
    private String skills;

    @Column(name="linkedIn" ,nullable = true)
    private String linkedIn;

    public String getLinkedIn() {
        return linkedIn;
    }

    public void setLinkedIn(String linkedIn) {
        this.linkedIn = linkedIn;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public UserDetail getUser() {
        return user;
    }

    public void setUser(UserDetail user) {
        this.user = user;
    }
}
