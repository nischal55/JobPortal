package org.acme.bean;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.acme.controllers.ResumeEducationController;
import org.acme.models.ResumeEducation;

import java.util.List;

@ApplicationScoped
public class ResumeEducationBean {
    @Inject
    ResumeEducationController resumeEducationController;

    public Response create(ResumeEducation resumeEducation) {
        try {
            resumeEducationController.create(resumeEducation);
            return Response.status(Response.Status.CREATED).entity("Created Successfully").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to create").build();
        }
    }

    public Response findAll() {
        try {
            List<ResumeEducation> resumeEducations = resumeEducationController.findAll();
            return Response.status(Response.Status.ACCEPTED).entity(resumeEducations).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("No Records Found").build();
        }
    }

    public Response findById(Long id) {
        try {
            ResumeEducation resumeEducation = resumeEducationController.findById(id);
            return Response.status(Response.Status.ACCEPTED).entity(resumeEducation).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("No Record Found").build();
        }
    }

    public Response findByResumeId(Long id) {
        try {
            List<ResumeEducation> resumeEducations = resumeEducationController.findByResumeId(id);
            return Response.status(Response.Status.ACCEPTED).entity(resumeEducations).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("No Records Found").build();
        }
    }

    public Response updateResumeEducation(ResumeEducation resumeEducation){
        try{
            resumeEducationController.udpateResumeEducation(resumeEducation);
            return Response.status(Response.Status.ACCEPTED).entity(resumeEducation).build();
        }catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Update Failed").build();
        }
    }
}
