package org.acme.bean;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.acme.controllers.ResumeDetailController;
import org.acme.models.ResumeDetail;

import java.util.List;

@ApplicationScoped
public class ResumeDetailBean {
    @Inject
    ResumeDetailController resumeDetailController;

    public Response create(ResumeDetail resumeDetail){
        try{
            resumeDetailController.create(resumeDetail);
            return Response.status(Response.Status.CREATED).entity(resumeDetail).build();
        }catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to create").build();
        }
    }

    public Response findAll(){
        try{
            List<ResumeDetail> resumeEducations = resumeDetailController.findAll();
            return Response.status(Response.Status.ACCEPTED).entity(resumeEducations).build();
        }catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("No Records Found").build();
        }
    }

    public Response findById(Long id){
        try{
            ResumeDetail resumeDetail = resumeDetailController.findById(id);
            return Response.status(Response.Status.ACCEPTED).entity(resumeDetail).build();
        }catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("No Record Found").build();
        }
    }
}
