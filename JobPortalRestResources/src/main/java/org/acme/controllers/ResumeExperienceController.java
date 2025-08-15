package org.acme.controllers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.facades.ResumeExperienceFacade;
import org.acme.models.ResumeExperience;

import java.util.List;

@ApplicationScoped
public class ResumeExperienceController {
    @Inject
    ResumeExperienceFacade resumeExperienceFacade;

    @Transactional
    public void create(ResumeExperience resumeExperience){
        try{
            resumeExperienceFacade.create(resumeExperience);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<ResumeExperience> findAll(){
        try{
            return resumeExperienceFacade.findAll();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public ResumeExperience findById(Long id){
        try{
            return resumeExperienceFacade.find(id);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public List<ResumeExperience> findByResumeId(Long id){
        try{
            return resumeExperienceFacade.findResumeByResumeId(id);
        }catch (Exception e){
            return null;
        }
    }
}
