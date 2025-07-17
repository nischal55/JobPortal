package org.acme.models;

import jakarta.persistence.*;

@Entity
@Table(name = "resume_education")
public class ResumeEducation extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "resume_id")
    private ResumeDetail resume;

    @Column(name = "degree",nullable = false)
    private String degree;

    @Column(name = "institution", nullable = false)
    private String institution;

    @Column(name = "start_year")
    private Integer startYear;

    @Column(name = "end_year")
    private Integer endYear;

    public Integer getEndYear() {
        return endYear;
    }

    public void setEndYear(Integer endYear) {
        this.endYear = endYear;
    }

    public Integer getStartYear() {
        return startYear;
    }

    public void setStartYear(Integer startYear) {
        this.startYear = startYear;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public ResumeDetail getResume() {
        return resume;
    }

    public void setResume(ResumeDetail resume) {
        this.resume = resume;
    }
}
