package org.acme.controllers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.facades.MessageFacade;
import org.acme.facades.UserDetailFacade;
import org.acme.models.Message;
import org.acme.models.UserDetail;

import java.util.List;

@ApplicationScoped
public class MessageController {
    @Inject
    MessageFacade messageFacade;

    @Inject
    UserDetailFacade userDetailFacade;

    @Transactional
    public Message sendMessage(Long senderId, Long receiverId, String content) {
        UserDetail sender = userDetailFacade.findById(senderId);
        UserDetail receiver = userDetailFacade.findById(receiverId);

        if (sender == null || receiver == null) {
            throw new IllegalArgumentException("Invalid sender or receiver");
        }

        Message msg = new Message();
        msg.setSender(sender);
        msg.setReceiver(receiver);
        msg.setContent(content);
        messageFacade.create(msg);

        return msg;
    }

    public List<Message> getConversation(Long user1Id, Long user2Id) {
        UserDetail user1 = userDetailFacade.findById(user1Id);
        UserDetail user2 = userDetailFacade.findById(user2Id);
        return messageFacade.findConversation(user1, user2);
    }

    @Transactional
    public void markAsSeen(Long receiverId, Long senderId) {
        UserDetail receiver = userDetailFacade.findById(receiverId);
        UserDetail sender = userDetailFacade.findById(senderId);

        List<Message> unread = messageFacade.findUnread(receiver, sender);
        unread.forEach(m -> m.setSeen(true));
    }
}
