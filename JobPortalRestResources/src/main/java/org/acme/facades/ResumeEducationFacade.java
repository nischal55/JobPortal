package org.acme.facades;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.acme.models.ResumeEducation;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ResumeEducationFacade extends AbstractFacade<ResumeEducation> {

    @Inject
    EntityManager em;

    public ResumeEducationFacade(){
        super(ResumeEducation.class);
    }

    @Override
    public EntityManager getEntityManager(){
        return em;
    }

    public List<ResumeEducation> findResumeByResumeId(Long resumeId) {
        List<ResumeEducation> result = new ArrayList<>();
        try {
            TypedQuery<ResumeEducation> query = em.createQuery("SELECT c from ResumeEducation c where c.resume.id = :resumeId", ResumeEducation.class);
            query.setParameter("resumeId", resumeId);
            return result = query.getResultList();
        } catch (Exception e) {
            return result;
        }
    }

}
