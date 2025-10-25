<template>
  <Navbar />
  <div class="flex flex-col h-[80vh] max-w-3xl mx-auto mt-5 rounded-lg shadow p-4 bg-white">
    <h2 class="text-2xl font-semibold text-center text-emerald-600 mb-3">Support</h2>

    <!-- Messages Section -->
    <div
      ref="chatContainer"
      class="flex-1 overflow-y-auto border rounded-lg p-3 mb-3 bg-gray-50 space-y-2"
    >
      <div
        v-for="(msg, index) in messages"
        :key="index"
        :class="getSenderId(msg) === userId ? 'text-right' : 'text-left'"
      >
        <div
          :class="[
            'inline-block px-3 py-2 rounded-lg',
            getSenderId(msg) === userId ? 'bg-emerald-500 text-white' : 'bg-gray-200 text-gray-800',
          ]"
        >
          {{ parseContent(msg.content) }}
          <p class="text-[11px] text-gray-400 mt-1">{{ formatDate(msg.timestamp || msg.createdAt) }}</p>
        </div>
      </div>
    </div>

    <!-- Message Input -->
    <div class="flex items-center border-t pt-3">
      <input
        v-model="newMessage"
        @keyup.enter="sendMessage"
        type="text"
        placeholder="Type a message..."
        class="flex-1 border rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-emerald-500"
      />
      <Button @click="sendMessage" class="ml-3 bg-emerald-500 text-white">Send</Button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick, onUnmounted } from "vue";
import { Button } from "primevue";
import Navbar from "@/components/navbar.vue";
import ApiService from "@/services/ApiService";

const ADMIN_ID = 51;
const userId = parseInt(localStorage.getItem("user_id"));
const newMessage = ref("");
const messages = ref([]);
const chatContainer = ref(null);
let socket = null;

const getSenderId = (msg) => {
  return msg.senderId || msg.sender?.id || null;
};

const getReceiverId = (msg) => {
  return msg.receiverId || msg.receiver?.id || null;
};

const parseContent = (raw) => {
  if (!raw) return "";
  try {
    const parsed = JSON.parse(raw);
    return parsed.content || parsed.text || raw;
  } catch {
    return raw.replace(/^"|"$/g, "");
  }
};

const formatDate = (timestamp) => {
  if (!timestamp) return "";
  const date = new Date(timestamp);
  return date.toLocaleString(undefined, { dateStyle: "short", timeStyle: "short" });
};

const scrollToBottom = () => {
  nextTick(() => {
    if (chatContainer.value) {
      chatContainer.value.scrollTop = chatContainer.value.scrollHeight;
    }
  });
};

const loadMessages = async () => {
  try {
    const res = await ApiService.get(
      `/messages/conversation?user1Id=${userId}&user2Id=${ADMIN_ID}`
    );
    messages.value = Array.isArray(res.data) ? res.data : res.data.data || [];
    console.log("Loaded messages:", messages.value);
    scrollToBottom();
  } catch (err) {
    console.error("Error loading messages:", err);
  }
};

const sendMessage = async () => {
  if (!newMessage.value.trim()) return;
  const payload = {
    senderId: userId.toString(), // Ensure string for backend consistency
    receiverId: ADMIN_ID.toString(),
    content: newMessage.value.trim(),
  };

  try {
    const res = await ApiService.post(
      `/messages/send?senderId=${userId}&receiverId=${ADMIN_ID}`,
      payload
    );
    messages.value.push({
      senderId: userId,
      receiverId: ADMIN_ID,
      content: newMessage.value.trim(),
      timestamp: new Date().toISOString(),
    });
    if (socket?.readyState === WebSocket.OPEN) {
      socket.send(JSON.stringify(payload));
      console.log("Sent WebSocket message:", payload);
    }
    newMessage.value = "";
    scrollToBottom();
  } catch (err) {
    console.error("Send error:", err);
  }
};

const initWebSocket = () => {
  const hostname = window.location.hostname;
  socket = new WebSocket(`ws://${hostname}:8888/chat`);

  socket.onopen = () => console.log("Connected to WebSocket");
  socket.onmessage = (event) => {
    try {
      const msg = JSON.parse(event.data);
      console.log("Received WebSocket message:", msg);
      const senderId = getSenderId(msg);
      const receiverId = getReceiverId(msg);

      if (
        (senderId === userId.toString() && receiverId === ADMIN_ID.toString()) ||
        (senderId === ADMIN_ID.toString() && receiverId === userId.toString())
      ) {
        messages.value.push({
          senderId,
          receiverId,
          content: parseContent(msg.content),
          timestamp: msg.timestamp || new Date().toISOString(),
        });
        scrollToBottom();
      }
    } catch (err) {
      console.error("WebSocket message parse error:", err, event.data);
    }
  };
  socket.onclose = () => console.log("WebSocket closed");
  socket.onerror = (err) => console.error("WebSocket error:", err);
};

onMounted(() => {
  loadMessages();
  initWebSocket();
});

onUnmounted(() => {
  socket?.close();
});
</script>