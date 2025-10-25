package org.acme.restServices;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.controllers.MessageController;
import org.acme.models.Message;
import org.acme.models.MessageRequest;

import java.util.List;

@Path("/messages")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MessageResources {
    @Inject
    MessageController messageService;

    @POST
    @Path("/send")
    public Response sendMessage(@QueryParam("senderId") Long senderId,
                                @QueryParam("receiverId") Long receiverId,
                                MessageRequest messageRequest) {
        Message msg = messageService.sendMessage(senderId, receiverId, messageRequest.content);
        return Response.ok(msg).build();
    }

    @GET
    @Path("/conversation")
    public Response getConversation(@QueryParam("user1Id") Long user1Id,
                                    @QueryParam("user2Id") Long user2Id) {
        List<Message> messages = messageService.getConversation(user1Id, user2Id);
        return Response.ok(messages).build();
    }

    @PUT
    @Path("/seen")
    public Response markAsSeen(@QueryParam("receiverId") Long receiverId,
                               @QueryParam("senderId") Long senderId) {
        messageService.markAsSeen(receiverId, senderId);
        return Response.ok().build();
    }
}
