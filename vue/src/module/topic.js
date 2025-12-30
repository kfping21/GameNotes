import http from "@/utils/ajax/http";

// 简易演示用 Mock 数据（可随时切换真实接口）
const USE_MOCK = true;

const mockTopics = [
    {
        id: 1,
        title: "王者荣耀上分攻略集中营",
        intro: "分享各位置英雄技巧、出装思路与团队配合。",
        tag_ids: "1,4",
        status: "active",
        created_at: "2025-01-08 09:20:00",
        heat: 1280,
        discuss_count: 12,
    },
    {
        id: 2,
        title: "原神探索与剧情交流",
        intro: "讨论地图探索、剧情解析与角色培养心得。",
        tag_ids: "2,3",
        status: "active",
        created_at: "2025-01-08 15:00:00",
        heat: 980,
        discuss_count: 9,
    },
    {
        id: 3,
        title: "崩铁与回合制RPG深度研讨",
        intro: "专注崩坏星穹铁道及其他回合制RPG的策略讨论。",
        tag_ids: "1,2",
        status: "active",
        created_at: "2025-01-09 10:30:00",
        heat: 760,
        discuss_count: 6,
    },
];

const mockNotes = [
    { id: 1, bijimingcheng: "韩信打野节奏全攻略", bijifengmian: "/upload/biji1.jpg", xiangqing: "从开局反野到中期带线..." },
    { id: 2, bijimingcheng: "原神须弥全宝箱地图", bijifengmian: "/upload/biji2.jpg", xiangqing: "标注须弥所有宝箱..." },
    { id: 3, bijimingcheng: "王者五排车队组建指南", bijifengmian: "/upload/biji3.jpg", xiangqing: "如何组建高效车队..." },
    { id: 5, bijimingcheng: "崩铁全成就达成路线", bijifengmian: "/upload/biji5.jpg", xiangqing: "全成就获取路线..." },
    { id: 9, bijimingcheng: "崩铁模拟宇宙速通攻略", bijifengmian: "/upload/biji9.jpg", xiangqing: "模拟宇宙速通路线..." },
];

const mockDiscussions = [
    { id: 1, topicId: 1, content: "韩信打野节奏怎么跑？", createdBy: 1, creatorName: "king001", createdAt: "2025-01-08 12:30:00" },
    { id: 2, topicId: 1, content: "辅助装调整后怎么玩？", createdBy: 3, creatorName: "social003", createdAt: "2025-01-08 12:45:00" },
    { id: 3, topicId: 2, content: "枫丹水下探索推荐带谁？", createdBy: 2, creatorName: "genshin002", createdAt: "2025-01-08 17:00:00" },
    { id: 4, topicId: 3, content: "模拟宇宙新祝福怎么选？", createdBy: 5, creatorName: "explore005", createdAt: "2025-01-09 11:05:00" },
];

const mockRooms = [
    { id: 1, topic_main_id: 1, topic_sub_id: 0, name: "王者五排车队招募区", owner_id: 1 },
    { id: 2, topic_main_id: 2, topic_sub_id: 0, name: "原神联机探索组", owner_id: 2 },
];

const mockMessages = [
    { id: 1, room_id: 1, senderId: 1, senderName: "king001", content: "今晚五排缺辅助吗？", createdAt: "2025-01-08 20:05:00" },
    { id: 2, room_id: 1, senderId: 3, senderName: "social003", content: "我可以打辅助。", createdAt: "2025-01-08 20:06:00" },
];

const ok = (data) => Promise.resolve({ code: 0, data });
const paged = (items, page = 1, pagesize = 10) => {
    const start = (page - 1) * pagesize;
    const slice = items.slice(start, start + pagesize);
    return { lists: slice, total: items.length, page, pagesize };
};

export const getTopicCategories = async (data = { page: 1, pagesize: 10, keyword: "" }) => {
    if (!USE_MOCK) return http.post("/api/topic/categories", data);
    const keyword = (data.keyword || "").toLowerCase();
    let items = [...mockTopics];
    if (keyword) {
        items = items.filter((t) => t.title.toLowerCase().includes(keyword) || t.intro.toLowerCase().includes(keyword));
    }
    const result = paged(items, data.page, data.pagesize);
    return ok(result);
};

export const getTopicById = async (id) => {
    if (!USE_MOCK) return http.get(`/api/topic/findById?id=${id}`);
    const item = mockTopics.find((t) => String(t.id) === String(id));
    return ok(item || null);
};

export const getTopicCompositeDetail = async (data) => {
    if (!USE_MOCK) return http.post("/api/topic/detail", data);
    const topicRes = await getTopicById(data.id);
    const notesRes = await getTopicNotes({ topicId: data.id, page: 1, pagesize: 10 });
    const discussRes = await getTopicDiscussions({ topicId: data.id, page: 1, pagesize: 10 });
    return ok({ topic: topicRes.data, notes: notesRes.data, discussions: discussRes.data });
};

export const getTopicNotes = async (data) => {
    if (!USE_MOCK) return http.post("/api/topic/notes", data);
    // 简单关联：按 topicId 取部分示例
    let filtered = mockNotes;
    if (data.topicId) {
        const tid = Number(data.topicId);
        if (tid === 1) filtered = mockNotes.filter((n) => [1,3].includes(n.id));
        else if (tid === 2) filtered = mockNotes.filter((n) => [2,8,12].includes(n.id));
        else if (tid === 3) filtered = mockNotes.filter((n) => [5,9].includes(n.id));
    }
    const result = paged(filtered, data.page || 1, data.pagesize || 10);
    return ok(result.lists);
};

export const getTopicDiscussions = async (data) => {
    if (!USE_MOCK) return http.post("/api/topic/discussion/list", data);
    const tid = String(data.topicId || "");
    const list = mockDiscussions.filter((d) => String(d.topicId) === tid);
    const result = paged(list, data.page || 1, data.pagesize || 10);
    return ok(result.lists);
};

export const createTopicDiscussion = async (data) => {
    if (!USE_MOCK) return http.post("/api/topic/discussion/create", data);
    const id = mockDiscussions.length ? Math.max(...mockDiscussions.map((d) => d.id)) + 1 : 1;
    const item = {
        id,
        topicId: data.topicId,
        content: data.content,
        createdBy: data.createdBy || 0,
        creatorName: data.creatorName || "游客",
        createdAt: new Date().toISOString().slice(0, 19).replace("T", " "),
    };
    mockDiscussions.unshift(item);
    return ok(item);
};

export const getChatRooms = async () => {
    if (!USE_MOCK) return http.get("/api/topic/chat/rooms");
    return ok(mockRooms);
};

export const createChatRoom = async (data) => {
    if (!USE_MOCK) return http.post("/api/topic/chat/rooms/create", data);
    const id = mockRooms.length ? Math.max(...mockRooms.map((r) => r.id)) + 1 : 1;
    const room = { id, name: data.name, topic_main_id: data.topicMainId || 0, topic_sub_id: 0, owner_id: data.ownerId || 0 };
    mockRooms.push(room);
    return ok(room);
};

export const getChatMessagesPaged = async (data) => {
    if (!USE_MOCK) return http.post("/api/topic/chat/messages/paged", data);
    const list = mockMessages.filter((m) => String(m.room_id) === String(data.roomId));
    const result = paged(list, data.page || 1, data.pagesize || 50);
    return ok(result);
};

export const getChatMessages = async (roomId) => {
    if (!USE_MOCK) return http.get(`/api/topic/chat/messages?roomId=${roomId}`);
    const list = mockMessages.filter((m) => String(m.room_id) === String(roomId));
    return ok(list);
};

export const sendChatMessage = async (data) => {
    if (!USE_MOCK) return http.post("/api/topic/chat/send", data);
    const id = mockMessages.length ? Math.max(...mockMessages.map((m) => m.id)) + 1 : 1;
    const msg = {
        id,
        room_id: data.roomId,
        senderId: data.senderId || 0,
        senderName: data.senderName || "游客",
        content: data.content,
        createdAt: new Date().toISOString().slice(0, 19).replace("T", " "),
    };
    mockMessages.push(msg);
    return ok(msg);
};

// 后台演示用：列表、保存、删除
export const adminTopicList = async (params) => {
    return getTopicCategories(params);
};

export const adminTopicSave = async (payload) => {
    if (!USE_MOCK) {
        const api = payload.id ? "/api/topic_main/update" : "/api/topic_main/insert";
        return http.post(api, payload);
    }
    if (payload.id) {
        const idx = mockTopics.findIndex((t) => t.id === payload.id);
        if (idx >= 0) mockTopics[idx] = { ...mockTopics[idx], ...payload };
        return ok(mockTopics[idx]);
    }
    const id = mockTopics.length ? Math.max(...mockTopics.map((t) => t.id)) + 1 : 1;
    const item = { ...payload, id, status: payload.status || "active", created_at: new Date().toISOString().slice(0, 19).replace("T", " ") };
    mockTopics.push(item);
    return ok(item);
};

export const adminTopicDelete = async (id) => {
    if (!USE_MOCK) return http.post("/api/topic_main/delete", [id]);
    const idx = mockTopics.findIndex((t) => String(t.id) === String(id));
    if (idx >= 0) mockTopics.splice(idx, 1);
    return ok(true);
};
