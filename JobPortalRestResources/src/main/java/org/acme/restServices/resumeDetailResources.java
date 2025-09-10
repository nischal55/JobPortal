package org.acme.restServices;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.bean.ResumeDetailBean;
import org.acme.models.ResumeDetail;

@Path("/resumeDetail")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class resumeDetailResources {
    @Inject
    ResumeDetailBean resumeDetailBean;

    @POST
    @Path("/create")
    public Response create(ResumeDetail resumeDetail){
        return resumeDetailBean.create(resumeDetail);
    }

    @GET
    @Path("/findAll")
    public Response findAll(){
        return resumeDetailBean.findAll();
    }

    @PUT
    @Path("/findById/{id}")
    public Response findById(@PathParam("id") Long id){
        return resumeDetailBean.findById(id);
    }

    @GET
    @Path("/findByUserId/{id}")
    public Response findByUserId(@PathParam("id") Long id){
        return resumeDetailBean.findByUserId(id);
    }

    @PUT
    @Path("/update")
    public Response updateResumeDetail(ResumeDetail resumeDetail){
        return resumeDetailBean.udpateResumeDetail(resumeDetail);
    }
}
