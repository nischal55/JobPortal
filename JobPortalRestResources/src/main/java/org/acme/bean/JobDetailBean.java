package org.acme.bean;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.acme.controllers.JobDetailController;
import org.acme.models.JobDetail;

import java.util.List;

@ApplicationScoped
public class JobDetailBean {
    @Inject
    JobDetailController jobDetailController;

    public Response findAllJob(){
        try{
            List<JobDetail> jobDetailList =  jobDetailController.jobDetailList();
            return Response.status(Response.Status.ACCEPTED).entity(jobDetailList).build();
        }catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to fetch JobList").build();
        }
    }

    public Response createJob(JobDetail jobDetail){
        try{
            jobDetailController.createJob(jobDetail);
            return Response.status(Response.Status.CREATED).entity(jobDetail).build();
        }catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to create").build();
        }
    }

    public Response findJobById(Long id){
        try{
            JobDetail jobDetail = jobDetailController.findById(id);
            return Response.status(Response.Status.ACCEPTED).entity(jobDetail).build();
        }catch(Exception e){
            return Response.status(Response.Status.BAD_REQUEST).entity("No Records found").build();
        }
    }

    public Response recommendJobClient(Long id){
        try{
            List<JobDetail> jobDetailList = jobDetailController.recommendJobsByResume(id);
            return Response.status(Response.Status.CREATED).entity(jobDetailList).build();
        }catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).entity("No Records found").build();
        }

    }

    public Response findByProvider(Long id){
        try{
            List<JobDetail> jobDetailList = jobDetailController.findByProvider(id);
            return Response.status(Response.Status.CREATED).entity(jobDetailList).build();
        }catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).entity("No Records found").build();
        }
    }

    public Response updateJob(JobDetail jobDetail){
        try{
            jobDetailController.updateJob(jobDetail);
            return Response.status(Response.Status.ACCEPTED).entity(jobDetail).build();
        }catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to update").build();
        }
    }

    public Response deleteJob(JobDetail jobDetail){
        try{
            jobDetailController.deleteJob(jobDetail);
            return Response.status(Response.Status.ACCEPTED).entity(jobDetail).build();
        }catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to delete").build();
        }
    }
}
