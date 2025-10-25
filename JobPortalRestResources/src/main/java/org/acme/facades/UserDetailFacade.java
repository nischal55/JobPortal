package org.acme.facades;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.acme.models.UserDetail;

@ApplicationScoped
public class UserDetailFacade extends AbstractFacade<UserDetail> {

    @Inject
    EntityManager em;

    public UserDetailFacade(){
        super(UserDetail.class);
    }

    @Override
    public EntityManager getEntityManager(){
        return em;
    }

    public UserDetail findByUsername(String username) {
        try {
            TypedQuery<UserDetail> query = em.createQuery(
                    "SELECT u FROM UserDetail u WHERE u.username = :username", UserDetail.class
            );
            return query.setParameter("username", username).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public UserDetail findById(Long id){
        try{
            TypedQuery<UserDetail> query = em.createQuery("SELECT u from UserDetail u where u.id = :id", UserDetail.class);
            return query.setParameter("id", id).getSingleResult();
        }catch (Exception e){
            return null;
        }
    }
}
