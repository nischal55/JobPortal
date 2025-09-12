package org.acme.restServices;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.bean.JobApplicationBean;
import org.acme.models.JobApplicants;

@Path("/jobApplicants")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class jobApplicantResources {

    @Inject
    JobApplicationBean jobApplicationBean;

    @GET
    @Path("/findAll")
    public Response findAll(){
        return jobApplicationBean.findAll();
    }

    @POST
    @Path("/create")
    public Response create(JobApplicants jobApplicants){
        return jobApplicationBean.create(jobApplicants);
    }

    @GET
    @Path("/findById/{applicantId}")
    public Response findById(@PathParam("applicantId") Long applicantId){
        return jobApplicationBean.findById(applicantId);
    }

    @GET
    @Path("/findApplicantsByUserId/{userId}")
    public Response findApplicantsByUserId(@PathParam("userId") Long userId){
        return jobApplicationBean.findApplicantsByUserId(userId);
    }

    @GET
    @Path("/findApplicantsByJobId/{id}")
    public Response findApplicantsByJobId(@PathParam("id") Long jobId){
        return jobApplicationBean.findApplicantsByJobId(jobId);
    }

    @PUT
    @Path("/update")
    public Response udpate(JobApplicants jobApplicants){
        return jobApplicationBean.updateJobApplicant(jobApplicants);
    }

}
