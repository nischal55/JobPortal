package org.acme.controllers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.facades.JobSeekerDetailFacade;
import org.acme.models.JobSeekerDetail;

import java.util.List;

@ApplicationScoped
public class JobSeekerController {
    @Inject
    JobSeekerDetailFacade jobSeekerDetailFacade;

    @Transactional
    public void createAccount(JobSeekerDetail jobSeekerDetail){
        try{
            jobSeekerDetailFacade.create(jobSeekerDetail);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public List<JobSeekerDetail> findAll(){
        List<JobSeekerDetail> jobSeekerDetails = null;
        try{
            jobSeekerDetails = jobSeekerDetailFacade.findAll();
            return jobSeekerDetails;
        }catch(Exception e){
            return jobSeekerDetails;
        }
    }

    public JobSeekerDetail findById(Long id){
        try{
            return jobSeekerDetailFacade.find(id);
        }catch(Exception e){
            return null;
        }
    }
}
