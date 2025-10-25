package org.acme.facades;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.acme.models.Message;
import org.acme.models.UserDetail;

import java.util.List;

@ApplicationScoped
public class MessageFacade extends AbstractFacade<Message> {

    @Inject
    EntityManager em;

    public MessageFacade(){
        super(Message.class);
    }
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }


    public List<Message> findConversation(UserDetail user1, UserDetail user2) {
        TypedQuery<Message> query = em.createQuery("""
            SELECT m FROM Message m
            WHERE (m.sender = :user1 AND m.receiver = :user2)
               OR (m.sender = :user2 AND m.receiver = :user1)
            ORDER BY m.timestamp ASC
        """, Message.class);
        query.setParameter("user1", user1);
        query.setParameter("user2", user2);
        return query.getResultList();
    }

    public List<Message> findUnread(UserDetail receiver, UserDetail sender) {
        TypedQuery<Message> query = em.createQuery("""
            SELECT m FROM Message m
            WHERE m.receiver = :receiver AND m.sender = :sender AND m.seen = false
        """, Message.class);
        query.setParameter("receiver", receiver);
        query.setParameter("sender", sender);
        return query.getResultList();
    }
}
