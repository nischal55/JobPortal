package org.acme.controllers;

import jakarta.inject.Inject;
import org.acme.facades.JobSeekerDetailFacade;
import org.acme.models.JobSeekerDetail;

import java.util.List;

public class JobSeekerController {
    @Inject
    JobSeekerDetail jobSeekerDetail;

    @Inject
    JobSeekerDetailFacade jobSeekerDetailFacade;

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
