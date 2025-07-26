package org.acme.controllers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.facades.ResumeExperienceFacade;
import org.acme.models.ResumeExperience;

import java.util.List;

@ApplicationScoped
public class ResumeExperienceController {
    @Inject
    ResumeExperienceFacade resumeExperienceFacade;

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
}
