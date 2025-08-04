package org.acme.controllers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.facades.JobProviderDetailFacade;
import org.acme.models.JobProviderDetail;

import java.util.List;

@ApplicationScoped
public class JobProviderController {
    @Inject
    JobProviderDetailFacade jobProviderDetailFacade;

    @Transactional
    public void createJobProvider(JobProviderDetail jobProviderDetail){
        try{
            jobProviderDetailFacade.create(jobProviderDetail);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public List<JobProviderDetail> findAll(){
        try{
            return jobProviderDetailFacade.findAll();
        }catch (Exception e){
            return null;
        }
    }

    public JobProviderDetail findById(Long id){
        try{
            return jobProviderDetailFacade.find(id);
        }catch (Exception e){
            return null;
        }
    }
}
