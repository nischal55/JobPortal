package org.acme.controllers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.facades.ResumeCertificationFacade;
import org.acme.facades.ResumeDetailFacade;
import org.acme.models.ResumeCertification;
import org.acme.models.ResumeDetail;

import java.util.List;

@ApplicationScoped
public class ResumeCertificationController {
    @Inject
    ResumeCertificationFacade resumeCertificationFacade;

    public void create(ResumeCertification resumeCertification) {
        try {
            resumeCertificationFacade.create(resumeCertification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ResumeCertification> findAll() {
        try {
            return resumeCertificationFacade.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResumeCertification findById(Long id) {
        try {
            return resumeCertificationFacade.find(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
