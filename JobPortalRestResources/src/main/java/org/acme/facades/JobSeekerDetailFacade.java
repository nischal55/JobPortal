package org.acme.facades;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.acme.models.JobSeekerDetail;

import java.util.List;

@ApplicationScoped
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

    public JobSeekerDetail findByUserId(Long userId){
       try{
           TypedQuery<JobSeekerDetail> query = em.createQuery("Select c from JobSeekerDetail c where c.user.id = :userId",JobSeekerDetail.class);
           query.setParameter("userId",userId);
           return query.getSingleResult();
       }catch(Exception e){
           return  null;
       }
    }
}
