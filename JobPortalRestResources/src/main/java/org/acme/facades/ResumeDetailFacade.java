package org.acme.facades;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.acme.models.ResumeDetail;

@ApplicationScoped
public class ResumeDetailFacade extends AbstractFacade<ResumeDetail> {

    @Inject
    EntityManager em;

    public ResumeDetailFacade() {
        super(ResumeDetail.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public ResumeDetail findByUserId(Long userId) {
        try {
            TypedQuery<ResumeDetail> query = em.createQuery("SELECT c from ResumeDetail c where c.user.id = :userId", ResumeDetail.class);
            query.setParameter("userId", userId);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }

    }
}
