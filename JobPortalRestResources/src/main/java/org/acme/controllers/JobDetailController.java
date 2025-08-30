package org.acme.controllers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.facades.JobDetailFacade;
import org.acme.models.JobDetail;

import java.util.List;

@ApplicationScoped
public class JobDetailController {
    @Inject
    JobDetailFacade jobDetailFacade;

    @Transactional
    public void createJob(JobDetail jobDetail) {
        try {
            jobDetailFacade.create(jobDetail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<JobDetail> jobDetailList() {
        try {
            return jobDetailFacade.findAll();
        } catch (Exception e) {
            return null;
        }
    }

    public JobDetail findById(Long id) {
        try {
            return jobDetailFacade.find(id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<JobDetail> recommendJobsByResume(Long resumeId) {
        try {
            return jobDetailFacade.recommendJobsByResume(resumeId);
        } catch (Exception e) {
            return null;
        }
    }

    public List<JobDetail> findByProvider(Long id) {
        try {
            return jobDetailFacade.findByProviderId(id);
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    public void updateJob(JobDetail jobDetail) {
        try {
            jobDetailFacade.edit(jobDetail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void deleteJob(JobDetail jobDetail){
        try{
            jobDetailFacade.remove(jobDetail);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
