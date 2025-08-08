package org.acme.restServices;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.bean.JobDetailBean;
import org.acme.models.JobDetail;

@Path("/jobDetail")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class jobDetailResources {
    @Inject
    JobDetailBean jobDetailBean;

    @POST
    @Path("/create")
    public Response create(JobDetail jobDetail){
        return jobDetailBean.createJob(jobDetail);
    }

    @GET
    @Path("/findAll")
    public Response findAll(){
        return jobDetailBean.findAllJob();
    }

    @GET
     @Path("/findById/{id}")
    public Response findById(@PathParam("id") Long id){
    return jobDetailBean.findJobById(id);
    }

}
