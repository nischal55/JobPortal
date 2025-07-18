package org.acme.facades;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
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
}
