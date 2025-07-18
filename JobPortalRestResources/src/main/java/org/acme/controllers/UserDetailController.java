package org.acme.controllers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.facades.UserDetailFacade;
import org.acme.models.UserDetail;

@ApplicationScoped
public class UserDetailController {
    @Inject
    UserDetailFacade userFacade;

    public UserDetail createUser(UserDetail userDetail){
        if(userDetail == null){
            return null;
        }

        try{
            userFacade.create(userDetail);
            return userDetail;
        }catch(Exception e){
            return null;
        }
    }
}
