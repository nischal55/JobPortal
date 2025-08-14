package org.acme.controllers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.facades.ResumeDetailFacade;
import org.acme.models.ResumeDetail;

import java.util.List;

@ApplicationScoped
public class ResumeDetailController {
    @Inject
    ResumeDetailFacade resumeDetailFacade;

    @Transactional
    public void create(ResumeDetail resumeDetail) {
        try {
            resumeDetailFacade.create(resumeDetail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ResumeDetail> findAll() {
        try {
            return resumeDetailFacade.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResumeDetail findById(Long id) {
        try {
            return resumeDetailFacade.find(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResumeDetail findByUserId(Long id){
        try{
            return resumeDetailFacade.findByUserId(id);
        }catch (Exception e){
            return null;
        }
    }
}
