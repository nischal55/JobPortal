package org.acme.bean;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.acme.controllers.UserDetailController;
import org.acme.models.UserDetail;

@ApplicationScoped
public class UserDetailBean {

    @Inject
    UserDetailController userDetailController;

    public Response createUser(UserDetail userDetail){
        if(userDetail.getId() == null){
            Response.status(Response.Status.BAD_REQUEST).entity("Please Fill Records").build();
        }
        try{
            userDetailController.createUser(userDetail);
            if (userDetail.getId() != null) {
                return Response.status(Response.Status.CREATED)
                        .entity(userDetail)
                        .build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("Failed to create user.")
                        .build();
            }
        }catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to create user account").build();
        }
    }
}
