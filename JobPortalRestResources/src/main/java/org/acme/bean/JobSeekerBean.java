package org.acme.bean;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.acme.controllers.JobSeekerController;
import org.acme.models.JobSeekerDetail;

import java.util.List;

@ApplicationScoped
public class JobSeekerBean {
    @Inject
    JobSeekerController jobSeekerController;

    public Response createJobseeker(JobSeekerDetail jobSeekerDetail){
        try {
            jobSeekerController.createAccount(jobSeekerDetail);
            return Response.status(Response.Status.ACCEPTED).entity("Created Account Successfully").build();
        }catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to Create Account").build();
        }
    }

    public Response findAll(){
        try{
            List<JobSeekerDetail> jobSeekerDetailList = jobSeekerController.findAll();
            return Response.status(Response.Status.ACCEPTED).entity(jobSeekerDetailList).build();
        }catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to Fetch list").build();
        }
    }

    public Response findById(Long id){
        try{
            JobSeekerDetail jobSeekerDetail = jobSeekerController.findById(id);
            return Response.status(Response.Status.ACCEPTED).entity(jobSeekerDetail).build();
        }catch(Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("No Records found").build();
        }
    }
}
