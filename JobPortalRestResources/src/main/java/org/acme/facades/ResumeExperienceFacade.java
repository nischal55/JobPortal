package org.acme.facades;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.acme.models.ResumeExperience;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ResumeExperienceFacade extends AbstractFacade<ResumeExperience> {

    @Inject
    EntityManager em;

    public ResumeExperienceFacade() {
        super(ResumeExperience.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<ResumeExperience> findResumeByResumeId(Long resumeId) {
        List<ResumeExperience> resumeExperienceList = new ArrayList<>();
        try {
            TypedQuery<ResumeExperience> query = em.createQuery("SELECT c from ResumeExperience c where c.resume.id = :resumeId", ResumeExperience.class);
            query.setParameter("resumeId", resumeId);
            return resumeExperienceList = query.getResultList();
        } catch (Exception e) {
            return resumeExperienceList;
        }
    }
}
