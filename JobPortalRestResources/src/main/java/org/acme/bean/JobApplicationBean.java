package org.acme.bean;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.acme.controllers.JobApplicantController;
import org.acme.models.JobApplicants;

import java.util.List;

@ApplicationScoped
public class JobApplicationBean {
    @Inject
    JobApplicantController jobApplicantController;

    public Response create(JobApplicants jobApplicants){
        try{
            jobApplicantController.create(jobApplicants);
            return Response.status(Response.Status.ACCEPTED).entity("Created Successfully").build();
        }catch(Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to create").build();
        }
    }

    public Response findAll(){
        try{
            List<JobApplicants> jobApplicants = jobApplicantController.findAll();
            return Response.status(Response.Status.ACCEPTED).entity(jobApplicants).build();
        }catch(Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("No recordds Found").build();
        }
    }

    public Response findById(Long id){
        try{
            JobApplicants jobApplicants = jobApplicantController.findById(id);
            return Response.status(Response.Status.ACCEPTED).entity(jobApplicants).build();
        }catch(Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("No Record Found").build();
        }
    }
}
