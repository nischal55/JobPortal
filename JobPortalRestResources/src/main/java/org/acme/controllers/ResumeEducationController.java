package org.acme.controllers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.facades.ResumeEducationFacade;
import org.acme.models.ResumeEducation;

import java.util.List;

@ApplicationScoped
public class ResumeEducationController {
    @Inject
    ResumeEducationFacade resumeEducationFacade;

    @Transactional
    public void create(ResumeEducation resumeEducation) {
        try {
            resumeEducationFacade.create(resumeEducation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ResumeEducation> findAll() {
        try {
            return resumeEducationFacade.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResumeEducation findById(Long id) {
        try {
            return resumeEducationFacade.find(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<ResumeEducation> findByResumeId(Long id){
        try{
            return resumeEducationFacade.findResumeByResumeId(id);
        }catch (Exception e){
            return null;
        }
    }

    @Transactional
    public void udpateResumeEducation(ResumeEducation resumeEducation){
        try{
            resumeEducationFacade.edit(resumeEducation);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
