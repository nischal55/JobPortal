package org.acme.restServices;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.bean.ResumeCertificationBean;
import org.acme.models.ResumeCertification;

@Path("/certification")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class resumeCertificationResources {

    @Inject
    ResumeCertificationBean resumeCertificationBean;

    @POST
    @Path("/create")
    public Response create(ResumeCertification resumeCertification){
        return resumeCertificationBean.create(resumeCertification);
    }

    @GET
    @Path("/findAll")
    public Response findAll(){
        return resumeCertificationBean.findAll();
    }

    @PUT
    @Path("/findById/{id}")
    public Response findById(@PathParam("id") Long id){
        return resumeCertificationBean.findById(id);
    }
}
