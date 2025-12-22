<template>
    <div class="views-my-profile">
        <e-container>
            <div class="profile-card">
                <div class="profile-header">
                    <div class="avatar">
                        <img v-if="avatarUrl" :src="avatarUrl" alt="头像" />
                        <div v-else class="avatar-placeholder">
                            <i class="fa fa-user"></i>
                        </div>
                    </div>
                    <div class="profile-info">
                        <div class="name">{{ user.mingcheng || user.zhanghao || "用户" }}</div>
                        <div class="account">账号：{{ user.zhanghao || "-" }}</div>
                        <div class="signature" v-if="user.gexingqianming" v-html="user.gexingqianming"></div>
                        <div class="signature" v-else>暂无签名</div>
                    </div>
                    <div class="profile-stats">
                        <div class="stat-item">
                            <div class="value">{{ counts.follow }}</div>
                            <div class="label">关注</div>
                        </div>
                        <div class="stat-item">
                            <div class="value">{{ counts.fans }}</div>
                            <div class="label">粉丝</div>
                        </div>
                        <div class="stat-item">
                            <div class="value">{{ counts.history }}</div>
                            <div class="label">浏览</div>
                        </div>
                    </div>
                </div>

                <div class="profile-summary">
                    <div class="summary-item">
                        <div class="value">{{ counts.biji }}</div>
                        <div class="label">笔记</div>
                    </div>
                    <div class="summary-item">
                        <div class="value">{{ counts.pinglun }}</div>
                        <div class="label">评论</div>
                    </div>
                    <div class="summary-item">
                        <div class="value">{{ counts.shoucang }}</div>
                        <div class="label">收藏</div>
                    </div>
                    <div class="summary-item">
                        <div class="value">{{ counts.dianzan }}</div>
                        <div class="label">点赞</div>
                    </div>
                </div>

                <el-tabs v-model="activeTab">
                    <el-tab-pane label="我的笔记" name="notes">
                        <div class="list-grid">
                            <div v-for="note in noteList" :key="note.id" class="list-card" @click="goToDetail('biji', note.id)">
                                <div class="list-title">{{ note.bijimingcheng }}</div>
                                <div class="list-desc">{{ formatText(note.xiangqing) }}</div>
                                <div class="list-meta">
                                    <span>{{ formatTime(note.addtime) }}</span>
                                </div>
                            </div>
                            <el-empty v-if="noteList.length === 0" description="暂无笔记" />
                        </div>
                    </el-tab-pane>
                    <el-tab-pane label="我的评论" name="comments">
                        <div class="list-grid">
                            <div v-for="comment in commentList" :key="comment.id" class="list-card">
                                <div class="list-title">{{ comment.biaoti || "评论内容" }}</div>
                                <div class="list-desc">{{ comment.pinglunneirong }}</div>
                                <div class="list-meta">
                                    <span>{{ formatTime(comment.addtime) }}</span>
                                    <el-button type="primary" link @click="goToCommentTarget(comment)">查看</el-button>
                                </div>
                            </div>
                            <el-empty v-if="commentList.length === 0" description="暂无评论" />
                        </div>
                    </el-tab-pane>
                </el-tabs>
            </div>
        </e-container>
    </div>
</template>

<script setup>
import { computed, onMounted, ref, reactive } from "vue";
import { useRouter } from "vue-router";
import DB from "@/utils/db";
import config from "@/config";
import { session } from "@/utils/utils";
import { fetchUserStats } from "@/utils/user-stats";

const router = useRouter();
const username = session("username");
const user = ref({});
const noteList = ref([]);
const commentList = ref([]);
const activeTab = ref("notes");

const counts = reactive({
    biji: 0,
    pinglun: 0,
    shoucang: 0,
    dianzan: 0,
    follow: 0,
    fans: 0,
    history: 0,
});

const avatarUrl = computed(() => {
    const url = user.value.touxiang;
    if (!url) return "";
    if (url.startsWith("http")) return url;
    return config.service_url + (url.startsWith("/") ? "" : "/") + url;
});

const formatTime = (value) => {
    if (!value) return "";
    return String(value).substring(0, 19);
};

const formatText = (value) => {
    if (!value) return "";
    return String(value).replace(/<[^>]+>/g, "").substring(0, 80);
};

const goToDetail = (biao, id) => {
    if (!id) return;
    router.push({ path: `/${biao}/detail`, query: { id } });
};

const goToCommentTarget = (comment) => {
    const biao = comment.biao || "biji";
    const id = comment.biaoid || comment.id;
    goToDetail(biao, id);
};

const loadUser = async () => {
    if (!username) return;
    const list = await DB.name("yonghu").where("zhanghao", username).select();
    user.value = list?.[0] || {};
};

const loadLists = async () => {
    if (!username) return;
    noteList.value = await DB.name("biji").where("tianjiaren", username).order("id desc").limit(6).select();
    commentList.value = await DB.name("pinglun").where("pinglunren", username).order("id desc").limit(6).select();
};

const loadCounts = async () => {
    const { counts: nextCounts } = await fetchUserStats(username);
    Object.assign(counts, nextCounts);
};

onMounted(() => {
    loadUser();
    loadLists();
    loadCounts();
});
</script>

<style scoped lang="scss">
.views-my-profile {
    padding: 24px 0 40px;
}

.profile-card {
    background: var(--theme-surface-color);
    border-radius: 12px;
    padding: 24px;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.05);
}

.profile-header {
    display: flex;
    gap: 20px;
    align-items: center;
    flex-wrap: wrap;
}

.avatar {
    width: 90px;
    height: 90px;
    border-radius: 16px;
    overflow: hidden;
    background: #f2f4f8;
    display: flex;
    align-items: center;
    justify-content: center;

    img {
        width: 100%;
        height: 100%;
        object-fit: cover;
    }
}

.avatar-placeholder {
    color: #909399;
    font-size: 28px;
}

.profile-info {
    flex: 1;
    min-width: 200px;
    .name {
        font-size: 22px;
        font-weight: 600;
        color: #303133;
    }
    .account {
        color: #909399;
        font-size: 13px;
        margin-top: 6px;
    }
    .signature {
        margin-top: 8px;
        color: #606266;
        font-size: 13px;
    }
}

.profile-stats {
    display: flex;
    gap: 16px;
    .stat-item {
        text-align: center;
        .value {
            font-size: 18px;
            font-weight: 600;
            color: #303133;
        }
        .label {
            font-size: 12px;
            color: #909399;
            margin-top: 4px;
        }
    }
}

.profile-summary {
    margin-top: 20px;
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
    gap: 12px;
}

.summary-item {
    background: #ffffff;
    border-radius: 10px;
    padding: 12px;
    border: 1px solid var(--theme-border-color);
    text-align: center;
    .value {
        font-size: 18px;
        font-weight: 600;
        color: #303133;
    }
    .label {
        font-size: 12px;
        color: #909399;
        margin-top: 4px;
    }
}

.list-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
    gap: 16px;
    margin-top: 10px;
}

.list-card {
    background: #ffffff;
    border-radius: 12px;
    padding: 16px;
    border: 1px solid var(--theme-border-color);
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
    cursor: pointer;
    transition: transform 0.2s;

    &:hover {
        transform: translateY(-2px);
    }
}

.list-title {
    font-size: 15px;
    font-weight: 600;
    color: #303133;
}

.list-desc {
    font-size: 13px;
    color: #606266;
    margin-top: 8px;
    line-height: 1.6;
}

.list-meta {
    margin-top: 12px;
    font-size: 12px;
    color: #909399;
    display: flex;
    justify-content: space-between;
    align-items: center;
}
</style>
