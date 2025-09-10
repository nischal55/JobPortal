package org.acme.restServices;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.bean.ResumeExperienceBean;
import org.acme.models.ResumeExperience;

@Path("/resumeExperiences")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class resumeExperienceResources {

    @Inject
    ResumeExperienceBean resumeExperienceBean;

    @POST
    @Path("/create")
    public Response create(ResumeExperience resumeExperience){
        return resumeExperienceBean.create(resumeExperience);
    }

    @GET
    @Path("/findAll")
    public Response findAll(){
        return resumeExperienceBean.findAll();
    }

    @GET
    @Path("/findById/{id}")
    public Response findById(@PathParam("id") Long id){
        return resumeExperienceBean.findById(id);
    }

    @GET
    @Path("/findByResumeId/{id}")
    public Response findByResumeId(@PathParam("id") Long id){
        return resumeExperienceBean.findByResumeId(id);
    }

    @PUT
    @Path("/update")
    public Response update(ResumeExperience resumeExperience){
        return resumeExperienceBean.updateResumeExperience(resumeExperience);
    }
}
