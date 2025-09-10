package org.acme.restServices;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.bean.JobSeekerBean;
import org.acme.models.JobSeekerDetail;

@Path("/jobseeker")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class jobSeekerResources {

    @Inject
    JobSeekerBean jobSeekerBean;

    @POST
    @Path("/create")
    public Response createJobSeekerAcc(JobSeekerDetail jobSeekerDetail){
        return  jobSeekerBean.createJobseeker(jobSeekerDetail);
    }

    @GET
    @Path("/findAll")
    public Response findAll(){
        return jobSeekerBean.findAll();
    }

    @GET
    @Path("/findById/{id}")
    public Response findById(@PathParam("id") Long id){
        return jobSeekerBean.findById(id);
    }

    @GET
    @Path("/findByUserId/{id}")
    public Response findByUserId(@PathParam("id") Long id){
        return jobSeekerBean.findByUserId(id);
    }

    @PUT
    @Path("/update")
    public Response update(JobSeekerDetail jobSeekerDetail){
        return jobSeekerBean.updateJobSeeker(jobSeekerDetail);
    }
}
