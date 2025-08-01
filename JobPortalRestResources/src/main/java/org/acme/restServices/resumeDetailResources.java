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
}
