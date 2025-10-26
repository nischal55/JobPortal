<template>
    <div class="flex h-screen bg-gray-100">
        <!-- Sidebar: User list -->
        <div class="w-1/4 bg-white border-r p-4 overflow-y-auto">
            <h2 class="text-xl font-semibold mb-4 text-emerald-600">Users</h2>
            <ul>
                <li v-for="user in users" :key="user.id" @click="selectUser(user)"
                    class="cursor-pointer px-4 py-2 rounded hover:bg-gray-200 transition"
                    :class="{ 'bg-emerald-100': selectedUser?.id === user.id }">
                    {{ user.username || user.name || ('User ' + user.id) }}
                </li>
            </ul>
            <p v-if="!loading && users.length === 0" class="text-gray-500 text-sm mt-4">
                No users found.
            </p>
            <p v-if="loading" class="text-gray-400 text-sm">Loading users...</p>
        </div>

        <!-- Chat Window -->
        <div class="flex-1 flex flex-col">
            <!-- Header -->
            <div class="border-b p-4 bg-white">
                <h2 class="text-lg font-semibold text-emerald-600">
                    {{ selectedUser ? selectedUser.username || selectedUser.name : "Select a user" }}
                </h2>
            </div>

            <!-- Messages -->
            <div ref="chatContainer" class="flex-1 overflow-y-auto p-4 space-y-3 bg-gray-50">
                <div v-for="msg in messages" :key="msg.id || msg.timestamp" class="flex" :class="{
                    'justify-end': getSenderId(msg) === adminId,
                    'justify-start': getSenderId(msg) !== adminId
                }">
                    <div :class="[
                        'p-3 rounded-xl max-w-[70%]',
                        getSenderId(msg) === adminId
                            ? 'bg-emerald-500 text-white'
                            : 'bg-gray-200 text-gray-800'
                    ]">
                        <p class="text-sm whitespace-pre-wrap">
                            {{ parseContent(msg.content) }}
                        </p>
                        <p class="text-[11px] text-gray-300 mt-1 text-right">
                            {{ formatDate(msg.timestamp || msg.createdAt) }}
                        </p>
                    </div>
                </div>
                <div v-if="messages.length === 0 && selectedUser" class="text-gray-500 text-center mt-10">
                    No messages yet.
                </div>
            </div>

            <!-- Input -->
            <div v-if="selectedUser" class="flex items-center border-t bg-white p-3">
                <input v-model="newMessage" @keyup.enter="sendMessage" type="text" placeholder="Type a message..."
                    class="flex-1 rounded-l-lg p-2 outline-none border border-transparent focus:border-emerald-500" />
                <button @click="sendMessage"
                    class="bg-emerald-600 text-white px-4 py-2 rounded-r-lg hover:bg-emerald-700">
                    Send
                </button>
            </div>
        </div>
    </div>
</template>

<script>
import ApiService from "@/services/ApiService";

export default {
    name: "AdminSupport",
    data() {
        return {
            adminId: 51,
            users: [],
            selectedUser: null,
            messages: [],
            newMessage: "",
            socket: null,
            loading: false,
        };
    },
    methods: {
        async fetchUsers() {
            this.loading = true;
            try {
                const res = await ApiService.get("/users/findAll");
                this.users = (Array.isArray(res.data) ? res.data : res.data.data || []).filter(
                    (user) => user.id !== 51
                );
                console.log("✅ Users loaded:", this.users);
            } catch (err) {
                console.error("❌ Error fetching users:", err);
            } finally {
                this.loading = false;
            }
        },
        async selectUser(user) {
            this.selectedUser = user;
            await this.fetchConversation();
        },
        async fetchConversation() {
            if (!this.selectedUser) return;
            try {
                const res = await ApiService.get(`/messages/conversation`, {
                    params: { user1Id: this.adminId, user2Id: this.selectedUser.id },
                });
                this.messages = Array.isArray(res.data) ? res.data : res.data.data || [];
                console.log("💬 Loaded messages:", this.messages);
                this.$nextTick(() => this.scrollToBottom());
            } catch (err) {
                console.error("❌ Error fetching conversation:", err);
            }
        },
        async sendMessage() {
            if (!this.newMessage.trim() || !this.selectedUser) return;
            const payload = {
                senderId: this.adminId.toString(), // Ensure string
                receiverId: this.selectedUser.id.toString(),
                content: this.newMessage.trim(),
                timestamp: new Date().toISOString(),
            };
            try {
                const res = await ApiService.post(
                    `/messages/send?senderId=${this.adminId}&receiverId=${this.selectedUser.id}`,
                    payload,
                    { headers: { "Content-Type": "application/json" } }
                );
                this.messages.push({
                    senderId: this.adminId,
                    receiverId: this.selectedUser.id,
                    content: this.newMessage.trim(),
                    timestamp: payload.timestamp,
                });
                if (this.socket && this.socket.readyState === WebSocket.OPEN) {
                    this.socket.send(JSON.stringify(payload));
                    console.log("Sent WebSocket message:", payload);
                }
                this.newMessage = "";
                this.$nextTick(() => this.scrollToBottom());
            } catch (err) {
                console.error("❌ Error sending message:", err);
            }
        },
        getSenderId(msg) {
            return msg.senderId || msg.sender?.id || null;
        },
        getReceiverId(msg) {
            return msg.receiverId || msg.receiver?.id || null;
        },
        parseContent(raw) {
            if (!raw) return "";
            try {
                const parsed = JSON.parse(raw);
                return parsed.content || parsed.text || raw;
            } catch {
                return raw.replace(/^"|"$/g, "");
            }
        },
        formatDate(timestamp) {
            if (!timestamp) return "";
            const date = new Date(timestamp);
            return date.toLocaleString(undefined, {
                dateStyle: "short",
                timeStyle: "short",
            });
        },
        scrollToBottom() {
            const container = this.$refs.chatContainer;
            if (container) container.scrollTop = container.scrollHeight;
        },
        initSocket() {
            const wsUrl = `ws://${window.location.hostname}:8888/chat`;
            this.socket = new WebSocket(wsUrl);
            this.socket.onopen = () => console.log("✅ Admin WebSocket connected");
            this.socket.onclose = () => console.log("❌ Admin WebSocket closed");
            this.socket.onerror = (err) => console.error("⚠️ WebSocket error:", err);
            this.socket.onmessage = async (event) => {
                try {
                    const msg = JSON.parse(event.data);
                    console.log("Received WebSocket message:", msg);
                    const senderId = this.getSenderId(msg);
                    const receiverId = this.getReceiverId(msg);

                    if (
                        this.selectedUser &&
                        ((senderId === this.selectedUser.id.toString() && receiverId === this.adminId.toString()) ||
                            (senderId === this.adminId.toString() && receiverId === this.selectedUser.id.toString()))
                    ) {
                        this.messages.push({
                            senderId,
                            receiverId,
                            content: this.parseContent(msg.content),
                            timestamp: msg.timestamp || new Date().toISOString(),
                        });
                        this.$nextTick(() => this.scrollToBottom());
                    }
                } catch (err) {
                    console.error("Error parsing WebSocket message:", err, event.data);
                }
            };
        },
    },
    async mounted() {
        await this.fetchUsers();
        this.initSocket();
    },
    beforeUnmount() {
        if (this.socket) this.socket.close();
    },
};
</script>

<style scoped>
::-webkit-scrollbar {
    width: 6px;
}

::-webkit-scrollbar-thumb {
    background: #ccc;
    border-radius: 4px;
}
</style>