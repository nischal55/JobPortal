package org.acme.facades;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.acme.models.JobSeekerDetail;

public class JobSeekerDetailFacade extends AbstractFacade<JobSeekerDetail>{
    @Inject
    EntityManager em;

    public JobSeekerDetailFacade(){
        super(JobSeekerDetail.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
