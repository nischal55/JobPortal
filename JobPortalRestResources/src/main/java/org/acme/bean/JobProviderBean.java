package org.acme.bean;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.acme.controllers.JobProviderController;
import org.acme.controllers.JobSeekerController;
import org.acme.models.JobProviderDetail;

import java.util.List;

@ApplicationScoped
public class JobProviderBean {
    @Inject
    JobProviderController jobProviderController;

    public Response create(JobProviderDetail jobProviderDetail){
        try{
            jobProviderController.createJobProvider(jobProviderDetail);
            return Response.status(Response.Status.ACCEPTED).entity(jobProviderDetail).build();
        }catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to create account").build();
        }
    }

    public Response findAll(){
        try{
            List<JobProviderDetail> jobProviderDetailList = jobProviderController.findAll();
            return Response.status(Response.Status.ACCEPTED).entity(jobProviderDetailList).build();
        }catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to fetch job providers").build();
        }
    }
}
