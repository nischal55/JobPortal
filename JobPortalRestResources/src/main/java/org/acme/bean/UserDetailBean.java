package org.acme.bean;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.acme.controllers.UserDetailController;
import org.acme.models.UserDetail;
import org.acme.utils.JwtUtil;

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

    public Response login(UserDetail loginRequest, @Context UriInfo uriInfo) {
        UserDetail user = userDetailController.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\":\"Invalid credentials\"}").build();
        }

        String accessToken = JwtUtil.generateAccessToken(user.getUsername(), user.getRole().name());
        String refreshToken = JwtUtil.generateRefreshToken(user.getUsername(), user.getRole().name());

        NewCookie accessCookie = new NewCookie("access_token", accessToken, "/", null, "access", 900, false, true);
        NewCookie refreshCookie = new NewCookie("refresh_token", refreshToken, "/", null, "refresh", 604800, false, true);

        return Response.ok()
                .cookie(accessCookie, refreshCookie)
                .entity("{\"message\":\"Login successful\"}")
                .build();
    }
}
