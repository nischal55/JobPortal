package org.acme.facades;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.acme.models.ResumeCertification;

public class ResumeCertificationFacade extends AbstractFacade<ResumeCertification>{

    @Inject
    EntityManager em;

    public ResumeCertificationFacade(){
        super(ResumeCertification.class);
    }

    @Override
    public EntityManager getEntityManager(){
        return em;
    }
}
