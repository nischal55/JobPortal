package org.acme.facades;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.acme.models.JobDetail;

@ApplicationScoped
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
