package org.acme.controllers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.facades.UserDetailFacade;
import org.acme.models.UserDetail;

import java.util.List;

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

    public List<UserDetail> findAll(){
        try{
            return userFacade.findAll();
        }catch(Exception e){
            return null;
        }
    }

    public UserDetail findById(Long userId){
        try{
            return userFacade.find(userId);
        }catch (Exception e){
            return null;
        }
    }

    public UserDetail update(UserDetail userDetail){
        try{
            userFacade.edit(userDetail);
            return userDetail;
        }catch(Exception e){
            return null;
        }
    }

    public boolean deleteUser(UserDetail userDetail){
        try{
            userFacade.remove(userDetail);
            return Boolean.TRUE;
        }catch(Exception e){
            return Boolean.FALSE;
        }
    }
}
