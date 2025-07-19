package org.acme.restServices;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.acme.bean.UserDetailBean;
import org.acme.models.UserDetail;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class userResources {
    @Inject
    UserDetailBean userDetailBean;

    @POST
    @Path("/create")
    public Response createUser(UserDetail userDetail){
        return userDetailBean.createUser(userDetail);
    }

    @GET
    @Path("/findAll")
    public Response findAll(UserDetail userDetail){
        return userDetailBean.findAll();
    }

    @GET
    @Path("/findById")
    public Response findById(Long userId){
        return userDetailBean.findById(userId);
    }

    @PUT
    @Path("/update")
    public Response update(UserDetail userDetail){
        return userDetailBean.update(userDetail);
    }

    @DELETE
    @Path("/delete")
    public Response delete(UserDetail userDetail){
        return userDetailBean.delete(userDetail);
    }

    @POST
    @Path("/login")
    public Response login(UserDetail userDetail,@Context UriInfo uriInfo){
        return userDetailBean.login(userDetail,uriInfo);
    }
}
