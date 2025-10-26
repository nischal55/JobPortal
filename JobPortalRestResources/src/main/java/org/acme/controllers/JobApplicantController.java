package org.acme.controllers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.facades.JobApplicantfacade;
import org.acme.models.JobApplicants;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class JobApplicantController {

    @Inject
    JobApplicantfacade jobApplicantfacade;

    @Transactional
    public void create(JobApplicants jobApplicants){
        try{
            Long jobid = jobApplicants.getJob().getId();
            Long seekerId = jobApplicants.getSeeker().getId();
            JobApplicants alreadyApplied = jobApplicantfacade.findJobApplicantsByJobIdAndSeekerId(jobid,seekerId);
            if(alreadyApplied != null){
                throw new IllegalStateException("You have already applied to this job");
            }
            jobApplicantfacade.create(jobApplicants);
        }catch(Exception e){
            e.printStackTrace();
            throw e;
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

    public List<JobApplicants> findApplicantsByUserId(Long id){
        try{
            return jobApplicantfacade.findApplicantsByUserId(id);
        }catch (Exception e){
            return null;
        }
    }

    public List<JobApplicants> findApplicantsByJobId(Long id){
        List<JobApplicants> jobApplicants = new ArrayList<>();
        try{
            jobApplicants = jobApplicantfacade.findApplicantsByJobId(id);
            return jobApplicants;
        }catch (Exception e){
            return  jobApplicants;
        }
    }

    @Transactional
    public void udpateApplicant(JobApplicants jobApplicants){
        try{
            jobApplicantfacade.edit(jobApplicants);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Long findJobApplicantCount(){
        try{
            return jobApplicantfacade.findApplicantsCount();
        }catch(Exception e){
            return null;
        }
    }
}
