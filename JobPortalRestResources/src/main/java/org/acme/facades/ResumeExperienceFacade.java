package org.acme.facades;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.acme.models.ResumeExperience;

@ApplicationScoped
public class ResumeExperienceFacade extends AbstractFacade<ResumeExperience>{

    @Inject
    EntityManager em;

    public ResumeExperienceFacade(){
        super(ResumeExperience.class);
    }

    @Override
    public EntityManager getEntityManager(){
        return em;
    }
}
