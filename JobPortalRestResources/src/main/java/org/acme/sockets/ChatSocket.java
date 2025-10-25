package org.acme.sockets;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/chat")
@ApplicationScoped
public class ChatSocket {

    private static final Map<String, Session> userSessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Session opened: " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            JsonObject json = Json.createReader(new StringReader(message)).readObject();
            // Support both senderId and sender.id formats
            String senderId = json.containsKey("senderId") ? json.getString("senderId") :
                    (json.containsKey("sender") ? json.getJsonObject("sender").getString("id") : null);
            String receiverId = json.containsKey("receiverId") ? json.getString("receiverId") :
                    (json.containsKey("receiver") ? json.getJsonObject("receiver").getString("id") : null);

            if (senderId == null || receiverId == null) {
                System.err.println("Invalid message: missing senderId or receiverId - " + message);
                session.getBasicRemote().sendText("{\"error\": \"Missing senderId or receiverId\"}");
                return;
            }

            System.out.println("Message from " + senderId + " to " + receiverId + ": " + message);

            // Register sender session
            userSessions.put(senderId, session);

            // Forward to receiver
            Session receiverSession = userSessions.get(receiverId);
            if (receiverSession != null && receiverSession.isOpen()) {
                receiverSession.getBasicRemote().sendText(message);
                System.out.println("Message sent to receiver: " + receiverId);
            } else {
                System.out.println("Receiver session not found or closed: " + receiverId);
            }

            // Echo back to sender
            if (session.isOpen()) {
                session.getBasicRemote().sendText(message);
                System.out.println("Message echoed to sender: " + senderId);
            }

        } catch (Exception e) {
            System.err.println("Error processing message: " + message);
            e.printStackTrace();
            try {
                session.getBasicRemote().sendText("{\"error\": \"Invalid message format\"}");
            } catch (IOException ignored) {}
        }
    }

    @OnClose
    public void onClose(Session session) {
        userSessions.values().remove(session);
        System.out.println("Session closed: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("WebSocket error for session " + session.getId() + ": " + throwable.getMessage());
    }
}