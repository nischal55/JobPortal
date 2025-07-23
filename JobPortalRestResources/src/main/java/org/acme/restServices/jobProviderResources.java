package org.acme.restServices;

import io.quarkus.runtime.Application;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.bean.JobProviderBean;
import org.acme.models.JobProviderDetail;

@Path("/jobproviders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class jobProviderResources {
    @Inject
    JobProviderBean jobProviderBean;

    @POST
    @Path("/create")
    public Response createUser(JobProviderDetail jobProviderDetail){
        return jobProviderBean.create(jobProviderDetail);
    }

    @GET
    @Path("findAll")
    public Response findAll(){
        return jobProviderBean.findAll();
    }
}
