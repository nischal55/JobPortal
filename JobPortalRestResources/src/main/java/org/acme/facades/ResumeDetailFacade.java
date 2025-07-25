package org.acme.facades;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.acme.models.ResumeDetail;

import javax.swing.text.html.parser.Entity;

@ApplicationScoped
public class ResumeDetailFacade extends AbstractFacade<ResumeDetail> {

    @Inject
    EntityManager em;

    public ResumeDetailFacade(){
        super(ResumeDetail.class);
    }

    @Override
    public EntityManager getEntityManager(){
        return em;
    }
}
