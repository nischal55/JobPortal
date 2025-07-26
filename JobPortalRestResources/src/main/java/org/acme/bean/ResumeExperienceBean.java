package org.acme.bean;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.acme.controllers.ResumeExperienceController;
import org.acme.models.ResumeExperience;

import java.util.List;

@ApplicationScoped
public class ResumeExperienceBean {
    @Inject
    ResumeExperienceController resumeExperienceController;

    public Response create(ResumeExperience resumeExperience){
        try{
            resumeExperienceController.create(resumeExperience);
            return Response.status(Response.Status.CREATED).entity("Created Successfully").build();
        }catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to create").build();
        }
    }

    public Response findAll(){
        try{
            List<ResumeExperience> resumeExperiences = resumeExperienceController.findAll();
            return Response.status(Response.Status.ACCEPTED).entity(resumeExperiences).build();
        }catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("No Records Found").build();
        }
    }

    public Response findById(Long id){
        try{
            ResumeExperience resumeExperience = resumeExperienceController.findById(id);
            return Response.status(Response.Status.ACCEPTED).entity(resumeExperience).build();
        }catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("No Record Found").build();
        }
    }
}
