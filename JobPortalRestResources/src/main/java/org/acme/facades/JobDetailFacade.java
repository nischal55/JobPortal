package org.acme.facades;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.acme.models.JobDetail;

public class JobDetailFacade extends AbstractFacade<JobDetail> {

    @Inject
    EntityManager em;

    public JobDetailFacade(){
        super(JobDetail.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
