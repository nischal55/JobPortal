package org.acme.restServices;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.bean.ResumeEducationBean;
import org.acme.models.ResumeEducation;
import org.acme.models.ResumeExperience;

@Path("/education")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class resumeEducationResources {
    @Inject
    ResumeEducationBean resumeEducationBean;

    @POST
    @Path("/create")
    public Response create(ResumeEducation resumeEducation){
        return resumeEducationBean.create(resumeEducation);
    }

    @GET
    @Path("/findAll")
    public Response findAll(){
        return resumeEducationBean.findAll();
    }

    @GET
    @Path("/findById/{id}")
    public Response findById(@PathParam("id") Long id){
        return resumeEducationBean.findById(id);
    }

    @GET
    @Path("/findByResumeId/{id}")
    public Response findByResumeId(@PathParam("id") Long id){
        return resumeEducationBean.findByResumeId(id);
    }

    @PUT
    @Path("/update")
    public Response udpate(ResumeEducation resumeEducation){
        return resumeEducationBean.updateResumeEducation(resumeEducation);
    }

}
