package org.acme.restServices;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.acme.bean.UserDetailBean;
import org.acme.models.UserDetail;

import java.util.Map;

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
    @Path("/findById/{userId}")
    public Response findById(@PathParam("userId") Long userId){
        return userDetailBean.findById(userId);
    }

    @PUT
    @Path("/update/{userId}")
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

    @POST
    @Path("/refresh-token")
    public Response refreshAccessToken(String refreshToken){
        return userDetailBean.refreshAccessToken(refreshToken);
    }

    @GET
    @Path("/check")
    @Produces(MediaType.APPLICATION_JSON)
    public Response check(@CookieParam("access_token") String accessToken) {
        return userDetailBean.checkAccessToken(accessToken);
    }
}
