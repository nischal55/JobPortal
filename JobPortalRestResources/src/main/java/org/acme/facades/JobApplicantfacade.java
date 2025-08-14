package org.acme.facades;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.acme.models.JobApplicants;

import java.util.ArrayList;
import java.util.List;

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

    public List<JobApplicants> findApplicantsByUserId(Long id) {
        List<JobApplicants> jobApplicants = new ArrayList<>();
        try{
            TypedQuery<JobApplicants> query = em.createQuery("Select c from JobApplicants  c where c.seeker.user.id = :userId",JobApplicants.class);
            query.setParameter("userId", id);
            jobApplicants = query.getResultList();
            return jobApplicants;
        }catch(Exception e){
            return jobApplicants;
        }
    }
}
