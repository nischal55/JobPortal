package org.acme.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "job_applicants")
public class JobApplicants extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "job_id")
    private JobDetail job;

    @ManyToOne
    @JoinColumn(name = "seeker_id")
    private JobSeekerDetail seeker;

    @ManyToOne
    @JoinColumn(name = "resume_id")
    private ResumeDetail resume;

    @Column(name = "cover_letter")
    private String coverLetter;

    @Enumerated(EnumType.STRING)
    private Status status = Status.applied;

    @Column(name = "applied_at")
    private LocalDateTime appliedAt = LocalDateTime.now();

    public enum Status {
        applied, reviewing, rejected, accepted
    }

    public JobDetail getJob() {
        return job;
    }

    public void setJob(JobDetail job) {
        this.job = job;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(LocalDateTime appliedAt) {
        this.appliedAt = appliedAt;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    public ResumeDetail getResume() {
        return resume;
    }

    public void setResume(ResumeDetail resume) {
        this.resume = resume;
    }

    public JobSeekerDetail getSeeker() {
        return seeker;
    }

    public void setSeeker(JobSeekerDetail seeker) {
        this.seeker = seeker;
    }
}
