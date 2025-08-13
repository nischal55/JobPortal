package org.acme.controllers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.facades.JobApplicantfacade;
import org.acme.models.JobApplicants;

import java.util.List;

@ApplicationScoped
public class JobApplicantController {

    @Inject
    JobApplicantfacade jobApplicantfacade;

    @Transactional
    public void create(JobApplicants jobApplicants){
        try{
            jobApplicantfacade.create(jobApplicants);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public List<JobApplicants> findAll(){
        try{
            return jobApplicantfacade.findAll();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public JobApplicants findById(Long id){
        try{
            return jobApplicantfacade.find(id);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
