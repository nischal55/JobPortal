package org.acme.facades;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.acme.models.ResumeCertification;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ResumeCertificationFacade extends AbstractFacade<ResumeCertification>{

    @Inject
    EntityManager em;

    public ResumeCertificationFacade(){
        super(ResumeCertification.class);
    }

    @Override
    public EntityManager getEntityManager(){
        return em;
    }

    public List<ResumeCertification> findResumeByResumeId(Long resumeId) {
        List<ResumeCertification> result = new ArrayList<>();
        try {
            TypedQuery<ResumeCertification> query = em.createQuery("SELECT c from ResumeExperience c where c.resume.id = :resumeId", ResumeCertification.class);
            query.setParameter("resumeId", resumeId);
            return result = query.getResultList();
        } catch (Exception e) {
            return result;
        }
    }
}
