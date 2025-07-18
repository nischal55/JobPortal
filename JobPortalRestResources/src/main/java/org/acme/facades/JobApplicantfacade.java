package org.acme.facades;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.acme.models.JobApplicants;

@ApplicationScoped
public class JobApplicantfacade extends AbstractFacade<JobApplicants>{

    @Inject
    EntityManager em;

    public JobApplicantfacade(){
        super(JobApplicants.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
