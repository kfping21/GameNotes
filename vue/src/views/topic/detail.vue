<template>
  <div class="topic-detail">
    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="topic" class="content">
      <div class="nav-bar">
        <div class="back-link" @click="router.push('/topic')">
            <el-icon><ArrowLeft /></el-icon> 返回话题广场
        </div>
        <el-button type="danger" plain size="small" @click="openReportTopic">举报话题</el-button>
      </div>

      <div class="topic-header">
        <h1>{{ topic.title }}</h1>
        <div class="topic-desc">{{ topic.intro }}</div>
      </div>

      <div class="topic-tabs">
        <div 
          class="tab-item" 
          :class="{ active: activeTab === 'discussions' }"
          @click="activeTab = 'discussions'"
        >
          讨论
        </div>
        <div 
          class="tab-item" 
          :class="{ active: activeTab === 'notes' }"
          @click="activeTab = 'notes'"
        >
          笔记集合
        </div>
        <div 
          class="tab-item" 
          :class="{ active: activeTab === 'chat' }"
          @click="activeTab = 'chat'"
        >
          聊天室
        </div>
      </div>

      <div class="tab-content">
        <!-- 讨论列表 -->
        <div v-if="activeTab === 'discussions'" class="discussions-section">
          <div class="create-discussion">
            <el-input
              v-model="newDiscussionContent"
              type="textarea"
              :rows="2"
              placeholder="发表你的看法..."
            />
            <el-button type="primary" @click="submitDiscussion" style="margin-top: 10px;">发布讨论</el-button>
          </div>
          <div class="discussion-list">
            <div v-for="item in discussions" :key="item.id" class="discussion-item">
              <div class="discussion-avatar">
                  <e-img :src="item.creatorAvatar" v-if="item.creatorAvatar" class="avatar-img" />
                  <img src="@/components/comments/asset/default.gif" v-else class="avatar-img" />
              </div>
              <div class="discussion-main">
                  <div class="discussion-meta">
                    <span class="user">{{ item.creatorName || '用户' + item.createdBy }}</span>
                    <span class="time">{{ item.createdAt }}</span>
                  </div>
                  <div class="discussion-content">{{ item.content }}</div>
                  <div class="discussion-actions">
                      <el-button type="danger" link size="small" @click="openReportDiscussion(item)">举报</el-button>
                  </div>
              </div>
            </div>
            <div v-if="discussions.length === 0" class="empty-tip">暂无讨论，快来抢沙发吧~</div>
          </div>
        </div>

        <!-- 笔记列表 -->
        <div v-if="activeTab === 'notes'" class="notes-section">
          <div v-for="note in notes" :key="note.id" class="note-item" @click="goToNote(note.id)">
            <div class="note-image" v-if="note.bijifengmian">
                <e-img :src="note.bijifengmian" style="width: 120px; height: 90px; object-fit: cover; border-radius: 4px;"></e-img>
            </div>
            <div class="note-info">
                <div class="note-title">{{ note.bijimingcheng }}</div>
                <div class="note-content">{{ formatNoteContent(note.xiangqing) }}</div>
            </div>
          </div>
          <div v-if="notes.length === 0" class="empty-tip">暂无笔记</div>
        </div>

        <!-- 聊天室 -->
        <div v-if="activeTab === 'chat'" class="chat-section">
          <div class="room-list" v-if="!currentRoom">
            <div class="room-list-header">
                <h3>选择聊天室</h3>
                <el-button type="primary" size="small" @click="showCreateRoomDialog = true">创建房间</el-button>
            </div>
            
            <div 
              v-for="room in chatRooms" 
              :key="room.id" 
              class="room-item"
              @click="enterRoom(room)"
            >
              {{ room.name }}
            </div>
            <div v-if="chatRooms.length === 0" class="empty-tip">暂无聊天室</div>
          </div>

          <div class="chat-room" v-else>
            <div class="room-header">
              <span @click="leaveRoom" class="back-btn">&lt; 返回</span>
              <span>{{ currentRoom.name }}</span>
            </div>
            <div class="message-list" ref="msgListRef">
              <div v-for="msg in messages" :key="msg.id" class="message-item" :class="{ 'my-msg': userStore.session && msg.senderId == userStore.session.id }">
                <div class="msg-info">
                    <span class="sender">{{ msg.senderName || '用户' + msg.senderId }}</span>
                    <span class="time">{{ msg.createdAt }}</span>
                </div>
                <div class="content">{{ msg.content }}</div>
              </div>
            </div>
            <div class="input-area">
              <input 
                v-model="inputMsg" 
                @keyup.enter="sendMessage"
                placeholder="输入消息..." 
                class="msg-input"
              />
              <button @click="sendMessage" class="send-btn">发送</button>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div v-else class="error">
      话题不存在或加载失败
    </div>

    <el-dialog v-model="showCreateRoomDialog" title="创建聊天室" width="30%">
        <el-input v-model="newRoomName" placeholder="请输入聊天室名称" />
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="showCreateRoomDialog = false">取消</el-button>
                <el-button type="primary" @click="handleCreateRoom">创建</el-button>
            </span>
        </template>
    </el-dialog>

    <report-dialog v-model="reportVisible" :target="reportTarget" />
  </div>
</template>

<script setup>
import { ref, onMounted, watch, nextTick, onBeforeUnmount } from "vue";
import { useRoute, useRouter } from "vue-router";
import { ArrowLeft } from "@element-plus/icons-vue";
import { 
    getTopicById,
    getTopicDiscussions,
    getTopicNotes,
    getChatRooms, 
    getChatMessages, 
    sendChatMessage, 
    createTopicDiscussion,
    createChatRoom
} from "@/module/topic";
import { useUserStore } from "@/stores";
import { ElMessage } from "element-plus";
import config from "@/config";
import DB from "@/utils/db";
import ReportDialog from "@/components/report/ReportDialog.vue";
import { pushHistory } from "@/utils/history";

const route = useRoute();
const router = useRouter();
const userStore = useUserStore();
const topicId = route.query.id;

const topic = ref(null);
const discussions = ref([]);
const notes = ref([]);
const loading = ref(false);
const activeTab = ref("discussions");

const reportVisible = ref(false);
const reportTarget = ref({});

const formatNoteContent = (content) => {
    if (!content) return "";
    const text = content.replace(/<[^>]+>/g, "");
    return text.length > 100 ? text.slice(0, 100) + "..." : text;
};

const newDiscussionContent = ref("");

const chatRooms = ref([]);
const currentRoom = ref(null);
const messages = ref([]);
const inputMsg = ref("");
const msgListRef = ref(null);
const showCreateRoomDialog = ref(false);
const newRoomName = ref("");
let ws = null;

const openReportTopic = () => {
    reportTarget.value = {
        type: "topic",
        typeLabel: "话题",
        id: topic.value?.id,
        title: topic.value?.title,
        content: topic.value?.intro,
        url: `/topic/detail?id=${topic.value?.id}`,
    };
    reportVisible.value = true;
};

const openReportDiscussion = (discussion) => {
    reportTarget.value = {
        type: "discussion",
        typeLabel: "讨论",
        id: discussion.id,
        title: topic.value?.title,
        content: discussion.content,
        url: `/topic/detail?id=${topic.value?.id}`,
    };
    reportVisible.value = true;
};

const fetchDetail = async () => {
  if (!topicId) return;
  loading.value = true;
  try {
    const [topicRes, discussionRes, noteRes] = await Promise.all([
        getTopicById(topicId),
        getTopicDiscussions({ topicId: topicId, page: 1, pagesize: 10 }),
        getTopicNotes({ topicId: topicId, page: 1, pagesize: 10 })
    ]);

    if (topicRes.code === 0 || topicRes.code === "0") {
      topic.value = topicRes.data;
      pushHistory("topic", {
          id: topic.value.id,
          title: topic.value.title,
          summary: topic.value.intro,
          url: `/topic/detail?id=${topic.value.id}`,
      });
    } else {
      ElMessage.error(topicRes.msg || "获取话题详情失败");
    }

    if (discussionRes.code === 0 || discussionRes.code === "0") {
      let list = discussionRes.data.lists || [];
      // Fetch user info for discussions
      const userIds = [...new Set(list.map(d => d.createdBy))];
      if (userIds.length > 0) {
          try {
              const users = await DB.name("yonghu").where("id", "in", userIds).select();
              const userMap = {};
              users.forEach(u => {
                  userMap[u.id] = u;
              });
              list = list.map(d => ({
                  ...d,
                  creatorName: userMap[d.createdBy]?.mingcheng || userMap[d.createdBy]?.zhanghao || `用户${d.createdBy}`,
                  creatorAvatar: userMap[d.createdBy]?.touxiang || ''
              }));
          } catch (e) {
              console.error("Failed to fetch user info for discussions", e);
          }
      }
      discussions.value = list;
    }

    if (noteRes.code === 0 || noteRes.code === "0") {
      notes.value = noteRes.data.lists || [];
    }
  } catch (error) {
    console.error(error);
    ElMessage.error("网络错误");
  } finally {
    loading.value = false;
  }
};

const submitDiscussion = async () => {
    if (!newDiscussionContent.value.trim()) {
        ElMessage.warning("请输入讨论内容");
        return;
    }
    if (!userStore.session || !userStore.session.id) {
        ElMessage.warning("请先登录");
        return;
    }
    try {
        const res = await createTopicDiscussion({
            topicId: topicId,
            content: newDiscussionContent.value,
            createdBy: userStore.session.id,
        });
        if (res.code === 0 || res.code === "0") {
            ElMessage.success("发布成功");
            newDiscussionContent.value = "";
            fetchDetail();
        } else {
            ElMessage.error(res.msg || "发布失败");
        }
    } catch (e) {
        console.error(e);
        ElMessage.error("网络错误");
    }
};

const goToNote = (id) => {
    if (id) {
        router.push({ path: "/biji/detail", query: { id } });
    }
};

const fetchChatRooms = async () => {
  try {
    const res = await getChatRooms();
    if (res.code === 0 || res.code === "0") {
      const allRooms = res.data || [];
      const filtered = allRooms.filter((r) => r.topicMainId == topicId);
      const uniqueRooms = [];
      const seenIds = new Set();
      const seenNames = new Set();
      
      for (const room of filtered) {
        if (!seenIds.has(room.id) && !seenNames.has(room.name)) {
          seenIds.add(room.id);
          seenNames.add(room.name);
          uniqueRooms.push(room);
        }
      }
      chatRooms.value = uniqueRooms;
    }
  } catch (error) {
    console.error(error);
  }
};

const handleCreateRoom = async () => {
    if (!newRoomName.value.trim()) return;
    if (!userStore.session || !userStore.session.id) {
        ElMessage.warning("请先登录");
        return;
    }
    try {
        const res = await createChatRoom({
            topicMainId: topicId,
            ownerId: userStore.session.id,
            name: newRoomName.value,
        });
        if (res.code === 0 || res.code === "0") {
            ElMessage.success("创建成功");
            showCreateRoomDialog.value = false;
            newRoomName.value = "";
            fetchChatRooms();
        } else {
            ElMessage.error(res.msg || "创建失败");
        }
    } catch (e) {
        console.error(e);
    }
};

const initWebSocket = () => {
    if (ws) return;
    if (!userStore.session || !userStore.session.id) return;

    let wsUrl = config.service_url.replace("http", "ws");
    if (wsUrl.startsWith("/")) {
        wsUrl = `ws://${window.location.host}${wsUrl}`;
    }
    if (wsUrl.endsWith("/")) wsUrl = wsUrl.slice(0, -1);
    
    wsUrl = `${wsUrl}/websocket/${userStore.session.id}`;
    ws = new WebSocket(wsUrl);
    
    ws.onopen = () => {
        if (currentRoom.value) {
            joinRoom(currentRoom.value.id);
        }
    };
    
    ws.onmessage = async (event) => {
        try {
            const data = JSON.parse(event.data);
            if (currentRoom.value && data.roomId == currentRoom.value.id) {
                if (!data.senderName && data.senderId) {
                    try {
                        const user = await DB.name("yonghu").find(data.senderId);
                        if (user) {
                            data.senderName = user.mingcheng || user.zhanghao;
                        }
                    } catch (e) {
                        // ignore
                    }
                }
                messages.value.push(data);
                scrollToBottom();
            }
        } catch (e) {
            console.error("WS message parse error", e);
        }
    };
    
    ws.onclose = () => {
        ws = null;
    };
};

const joinRoom = (roomId) => {
    if (ws && ws.readyState === WebSocket.OPEN) {
        ws.send(
            JSON.stringify({
                cmd: "joinRoom",
                data: { roomId: roomId },
            })
        );
    }
};

const enterRoom = async (room) => {
  if (!userStore.session || !userStore.session.id) {
      ElMessage.warning("请先登录后进入聊天室");
      return;
  }
  currentRoom.value = room;
  await fetchMessages(room.id);
  
  if (!ws) {
      initWebSocket();
  } else {
      joinRoom(room.id);
  }
};

const leaveRoom = () => {
    currentRoom.value = null;
};

const fetchMessages = async (roomId) => {
  try {
    const res = await getChatMessages(roomId);
    if (res.code === 0 || res.code === "0") {
      let msgs = res.data || [];
      
      // Fetch user info for these messages
      const senderIds = [...new Set(msgs.map(m => m.senderId))];
      if (senderIds.length > 0) {
          try {
              const users = await DB.name("yonghu").where("id", "in", senderIds).select();
              const userMap = {};
              users.forEach(u => {
                  userMap[u.id] = u.mingcheng || u.zhanghao;
              });
              
              msgs = msgs.map(m => ({
                  ...m,
                  senderName: userMap[m.senderId] || `用户${m.senderId}`
              }));
          } catch (e) {
              console.error("Failed to fetch user info", e);
          }
      }
      
      messages.value = msgs;
      scrollToBottom();
    }
  } catch (error) {
    console.error(error);
  }
};

const sendMessage = async () => {
  if (!inputMsg.value.trim()) return;
  if (!userStore.session || !userStore.session.id) {
      ElMessage.warning("请先登录");
      return;
  }
  
  try {
    const res = await sendChatMessage({
      roomId: currentRoom.value.id,
      senderId: userStore.session.id,
      content: inputMsg.value,
    });
    
    if (res.code === 0 || res.code === "0") {
      messages.value.push({
          id: Date.now(),
          roomId: currentRoom.value.id,
          senderId: userStore.session.id,
          senderName: userStore.session.mingcheng || userStore.session.username,
          content: inputMsg.value,
          createdAt: new Date().toLocaleString(),
      });
      scrollToBottom();
      inputMsg.value = "";
    } else {
      ElMessage.error(res.msg || "发送失败");
    }
  } catch (error) {
    console.error(error);
    ElMessage.error("发送失败");
  }
};

const scrollToBottom = () => {
  nextTick(() => {
    if (msgListRef.value) {
      msgListRef.value.scrollTop = msgListRef.value.scrollHeight;
    }
  });
};

watch(activeTab, (newVal) => {
  if (newVal === "chat") {
    fetchChatRooms();
  }
});

onMounted(() => {
  fetchDetail();
});

onBeforeUnmount(() => {
    if (ws) {
        ws.close();
        ws = null;
    }
});
</script>

<style scoped lang="scss">
.topic-detail {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;

  .nav-bar {
      margin-bottom: 15px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      gap: 12px;

      .back-link {
          display: inline-flex;
          align-items: center;
          cursor: pointer;
          font-size: 14px;
          color: #606266;
          transition: color 0.3s;
          
          .el-icon {
              margin-right: 4px;
          }

          &:hover {
              color: var(--theme-primary-color);
          }
      }
  }

  .topic-header {
    margin-bottom: 20px;
    background: var(--theme-surface-color);
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0,0,0,0.05);
    h1 {
      margin: 0 0 10px 0;
    }
    .topic-desc {
      color: #666;
    }
  }

  .topic-tabs {
    display: flex;
    border-bottom: 1px solid var(--theme-border-color);
    margin-bottom: 20px;
    background: var(--theme-surface-color);
    padding: 0 20px;
    border-radius: 8px 8px 0 0;
    
    .tab-item {
      padding: 15px 20px;
      cursor: pointer;
      font-weight: 500;
      &.active {
        color: var(--theme-primary-color);
        border-bottom: 2px solid var(--theme-primary-color);
      }
      &:hover {
        color: var(--theme-primary-color);
      }
    }
  }

  .tab-content {
    background: var(--theme-surface-color);
    padding: 20px;
    min-height: 400px;
    border-radius: 0 0 8px 8px;
  }

  .note-item,
  .discussion-item {
    padding: 15px;
    border-bottom: 1px solid #f0f0f0;
    &:last-child {
      border-bottom: none;
    }
    .note-title {
      margin: 0 0 5px 0;
      font-size: 16px;
      font-weight: bold;
    }
    .note-content {
      margin: 0;
      color: #666;
      font-size: 14px;
    }
  }

  .note-item {
      display: flex;
      gap: 15px;
      cursor: pointer;
      transition: background-color 0.2s;
      
      &:hover {
          background-color: #fafafa;
      }

      .note-image {
          flex-shrink: 0;
      }
      
      .note-info {
          flex: 1;
          overflow: hidden;
      }
  }

  .discussion-item {
    background: var(--theme-surface-color);
    transition: background-color 0.3s;
    display: flex;
    gap: 15px;
    padding: 20px 0;
    border-bottom: 1px solid #f0f0f0;

    &:hover {
      background-color: #fafafa;
    }

    .discussion-avatar {
        flex-shrink: 0;
        width: 40px;
        height: 40px;
        border-radius: 50%;
        overflow: hidden;
        background: #f0f0f0;
        
        .avatar-img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }
    }

    .discussion-main {
        flex: 1;
    }

    .discussion-meta {
      display: flex;
      justify-content: space-between;
      margin-bottom: 6px;
      font-size: 12px;
      color: #999;
      
      .user {
        color: #333;
        font-weight: 600;
        font-size: 14px;
      }
    }
    .discussion-content {
      font-size: 14px;
      line-height: 1.6;
      color: #333;
      white-space: pre-wrap;
      margin-bottom: 8px;
    }
    .discussion-actions {
      margin-top: 0;
    }
  }

  .room-list {
    .room-item {
      padding: 15px;
      border: 1px solid var(--theme-border-color);
      margin-bottom: 10px;
      cursor: pointer;
      border-radius: 4px;
      &:hover {
        background-color: var(--theme-surface-muted);
      }
    }
  }

  .chat-room {
    display: flex;
    flex-direction: column;
    height: 500px;
    
    .room-header {
      padding-bottom: 10px;
      border-bottom: 1px solid var(--theme-border-color);
      margin-bottom: 10px;
      .back-btn {
        cursor: pointer;
        color: var(--theme-primary-color);
        margin-right: 10px;
      }
    }

    .message-list {
      flex: 1;
      overflow-y: auto;
      padding: 20px;
      background: #f5f5f5;
      border-radius: 4px;
      margin-bottom: 10px;
      
      .message-item {
        margin-bottom: 20px;
        display: flex;
        flex-direction: column;
        align-items: flex-start;

        .msg-info {
            margin-bottom: 6px;
            font-size: 12px;
            color: #999;
            
            .sender {
                color: #666;
                margin-right: 8px;
            }
        }

        .content {
            padding: 10px 15px;
            background: #fff;
            border-radius: 0 12px 12px 12px;
            box-shadow: 0 1px 2px rgba(0,0,0,0.05);
            max-width: 70%;
            word-break: break-word;
            line-height: 1.5;
            position: relative;
        }

        &.my-msg {
            align-items: flex-end;

            .msg-info {
                text-align: right;
                .sender {
                    margin-right: 0;
                    margin-left: 8px;
                }
            }

            .content {
                background: #95ec69;
                border-radius: 12px 0 12px 12px;
            }
        }
      }
    }

    .input-area {
      display: flex;
      gap: 10px;
      .msg-input {
        flex: 1;
        padding: 10px;
        border: 1px solid #ddd;
        border-radius: 4px;
      }
      .send-btn {
        padding: 0 20px;
        background: var(--theme-primary-color);
        color: #fff;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        &:hover {
          background: var(--theme-primary-hover-color);
        }
      }
    }
  }
  
  .empty-tip {
    text-align: center;
    color: #999;
    padding: 20px;
  }
}
</style>
