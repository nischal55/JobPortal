package org.acme.facades;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.acme.models.JobProviderDetail;

public class JobProviderDetailFacade extends AbstractFacade<JobProviderDetail> {
    @Inject
    EntityManager em;

    public JobProviderDetailFacade(){
        super(JobProviderDetail.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
