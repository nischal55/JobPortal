package org.acme.restServices;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.bean.UserDetailBean;
import org.acme.models.UserDetail;

@Path("/user")
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

}
