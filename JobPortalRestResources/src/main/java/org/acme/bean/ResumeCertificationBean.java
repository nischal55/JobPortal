package org.acme.bean;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.acme.controllers.ResumeCertificationController;
import org.acme.models.ResumeCertification;

import java.util.List;

@ApplicationScoped
public class ResumeCertificationBean {
    @Inject
    ResumeCertificationController resumeCertificationController;

    public Response create(ResumeCertification resumeCertification){
        try{
            resumeCertificationController.create(resumeCertification);
            return Response.status(Response.Status.CREATED).entity("Created Successfully").build();
        }catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to create").build();
        }
    }

    public Response findAll(){
        try{
            List<ResumeCertification> resumeCertifications = resumeCertificationController.findAll();
            return Response.status(Response.Status.ACCEPTED).entity(resumeCertifications).build();
        }catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("No Records Found").build();
        }
    }

    public Response findById(Long id){
        try{
            ResumeCertification resumeCertifications = resumeCertificationController.findById(id);
            return Response.status(Response.Status.ACCEPTED).entity(resumeCertifications).build();
        }catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("No Record Found").build();
        }
    }
}
