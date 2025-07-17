package org.acme.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "resume_certification")
public class ResumeCertification extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "resume_id")
    private ResumeDetail resume;

    @Column(name="certification_title")
    private String certificationTitle;

    @Column(name = "issuing_org")
    private String issuingOrg;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public String getCertificationTitle() {
        return certificationTitle;
    }

    public void setCertificationTitle(String certificationTitle) {
        this.certificationTitle = certificationTitle;
    }

    public ResumeDetail getResume() {
        return resume;
    }

    public void setResume(ResumeDetail resume) {
        this.resume = resume;
    }

    public String getIssuingOrg() {
        return issuingOrg;
    }

    public void setIssuingOrg(String issuingOrg) {
        this.issuingOrg = issuingOrg;
    }
}
