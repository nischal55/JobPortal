package org.acme.controllers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.facades.UserDetailFacade;
import org.acme.models.UserDetail;
import org.acme.utils.hashPasswordUtil;

import java.util.List;

@ApplicationScoped
public class UserDetailController {
    @Inject
    UserDetailFacade userFacade;

    @Transactional
    public UserDetail createUser(UserDetail userDetail){
        if(userDetail == null){
            return null;
        }

        try{
            String hashedPassword = hashPasswordUtil.hash(userDetail.getPassword());
            userDetail.setPassword(hashedPassword);
            userFacade.create(userDetail);
            return userDetail;
        }catch(Exception e){
            e.printStackTrace();
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

    @Transactional
    public UserDetail update(UserDetail userDetail){
        try{
            userFacade.edit(userDetail);
            return userDetail;
        }catch(Exception e){
            return null;
        }
    }

    @Transactional
    public boolean deleteUser(UserDetail userDetail){
        try{
            userFacade.remove(userDetail);
            return Boolean.TRUE;
        }catch(Exception e){
            return Boolean.FALSE;
        }
    }
}
