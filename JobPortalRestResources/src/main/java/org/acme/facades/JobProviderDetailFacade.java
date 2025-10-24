package org.acme.facades;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.acme.models.JobProviderDetail;

import java.math.BigInteger;

@ApplicationScoped
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

    public Long findJobProviderCount() {
        String jpql = "SELECT COUNT(j.id) FROM JobSeekerDetail j";
        return em.createQuery(jpql, Long.class).getSingleResult();
    }

}
