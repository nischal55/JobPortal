package org.acme.bean;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.acme.controllers.UserDetailController;
import org.acme.models.UserDetail;

import java.util.List;

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
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to create user account").build();
        }
    }

    public Response findAll(){
        try{
            List<UserDetail> userDetailList= userDetailController.findAll();
            return Response.status(Response.Status.ACCEPTED).entity(userDetailList).build();
        }catch(Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to find user list").build();
        }
    }

    public Response findById(Long userId){
        try{
            UserDetail userDetail = userDetailController.findById(userId);
            return Response.status(Response.Status.ACCEPTED).entity(userDetail).build();
        }catch (Exception e){
            return  Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("No record with the given Id").build();
        }
    }

    public Response update(UserDetail userDetail){
        try{
            UserDetail updatedUserDetail = userDetailController.update(userDetail);
            return Response.status(Response.Status.ACCEPTED).entity(updatedUserDetail).build();
        }catch(Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to update the records").build();
        }
    }

    public Response delete(UserDetail userDetail){
        try{
            if(userDetailController.deleteUser(userDetail)){
                return Response.status(Response.Status.ACCEPTED).entity("Deleted Successfully").build();
            }else{
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to delete the record").build();
            }
        }catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to delete the record").build();
        }
    }
}
