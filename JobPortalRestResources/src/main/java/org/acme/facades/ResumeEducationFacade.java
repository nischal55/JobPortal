package org.acme.facades;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.acme.models.ResumeEducation;

public class ResumeEducationFacade extends AbstractFacade<ResumeEducation> {

    @Inject
    EntityManager em;

    public ResumeEducationFacade(){
        super(ResumeEducation.class);
    }

    @Override
    public EntityManager getEntityManager(){
        return em;
    }
}
