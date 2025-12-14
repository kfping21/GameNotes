import http from "@/utils/ajax/http";

/**
 * 获取话题分类 (大话题)
 * @param {object} data { page, pagesize, orderby, sort }
 */
export const getTopicCategories = (data = { page: 1, pagesize: 10 }) => {
    return http.post("/api/topic/categories", data);
};

/**
 * 获取单个话题详情 (基础信息)
 * @param {string|number} id 话题ID
 */
export const getTopicById = (id) => {
    return http.get(`/api/topic/findById?id=${id}`);
};

/**
 * 获取话题详情 (复合接口：包含主话题、讨论分页、笔记分页)
 * @param {object} data { id, page, pagesize }
 */
export const getTopicCompositeDetail = (data) => {
    return http.post("/api/topic/detail", data);
};

/**
 * 获取话题下的笔记
 * @param {object} data { topicId, page, pagesize }
 */
export const getTopicNotes = (data) => {
    return http.post("/api/topic/notes", data);
};

/**
 * 获取话题讨论列表
 * @param {object} data { topicId, page, pagesize }
 */
export const getTopicDiscussions = (data) => {
    return http.post("/api/topic/discussion/list", data);
};

/**
 * 创建话题讨论
 * @param {object} data { topicId, content, createdBy }
 */
export const createTopicDiscussion = (data) => {
    return http.post("/api/topic/discussion/create", data);
};

/**
 * 获取聊天室列表
 */
export const getChatRooms = () => {
    return http.get("/api/topic/chat/rooms");
};

/**
 * 创建聊天室
 * @param {object} data { topicMainId, ownerId, name }
 */
export const createChatRoom = (data) => {
    return http.post("/api/topic/chat/rooms/create", data);
};

/**
 * 获取聊天室消息 (分页)
 * @param {object} data { roomId, page, pagesize }
 */
export const getChatMessagesPaged = (data) => {
    return http.post("/api/topic/chat/messages/paged", data);
};

/**
 * 获取聊天室消息 (不分页)
 * @param {string|number} roomId 
 */
export const getChatMessages = (roomId) => {
    return http.get(`/api/topic/chat/messages?roomId=${roomId}`);
};

/**
 * 发送聊天消息
 * @param {object} data { roomId, senderId, content }
 */
export const sendChatMessage = (data) => {
    return http.post("/api/topic/chat/send", data);
};
