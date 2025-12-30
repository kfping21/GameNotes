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
                        <div class="name-row">
                            <div class="name">{{ user.mingcheng || user.zhanghao || "用户" }}</div>
                            <el-button type="primary" link :icon="Edit" @click="$router.push('/yonghu/updtself')">编辑资料</el-button>
                            <el-button type="primary" link :icon="Key" @click="showPasswordDialog = true">修改密码</el-button>
                            <el-button type="success" link :icon="Plus" @click="$router.push('/biji/add')">发布笔记</el-button>
                        </div>
                        <div class="account">账号：{{ user.zhanghao || "-" }}</div>
                        <div class="signature" v-if="user.gexingqianming" v-html="user.gexingqianming"></div>
                        <div class="signature" v-else>暂无签名</div>
                    </div>
                    <div class="profile-stats">
                        <div class="stat-item" @click="openUserList('follow')" style="cursor: pointer;">
                            <div class="value">{{ counts.follow }}</div>
                            <div class="label">关注</div>
                        </div>
                        <div class="stat-item" @click="openUserList('fans')" style="cursor: pointer;">
                            <div class="value">{{ counts.fans }}</div>
                            <div class="label">粉丝</div>
                        </div>
                        <div class="stat-item" @click="openUserList('history')" style="cursor: pointer;">
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
                    <div class="summary-item">
                        <div class="value">{{ counts.zhongcao }}</div>
                        <div class="label">种草</div>
                    </div>
                </div>

                <el-tabs v-model="activeTab" class="profile-tabs">
                    <el-tab-pane label="我的笔记" name="notes">
                        <div class="list-grid">
                            <div v-for="note in noteList" :key="note.id" class="list-card" @click="goToDetail('biji', note.id)">
                                <div class="card-cover" v-if="note.bijifengmian">
                                    <e-img :src="note.bijifengmian" class="cover-img" />
                                </div>
                                <div class="card-content">
                                    <div class="list-title">{{ note.bijimingcheng }}</div>
                                    <div class="list-desc">{{ formatText(note.xiangqing) }}</div>
                                    <div class="list-meta">
                                        <span>{{ formatTime(note.addtime) }}</span>
                                        <span class="meta-right">
                                            <el-button type="primary" link :icon="Edit" @click.stop="editNote(note.id)">编辑</el-button>
                                            <el-button type="danger" link :icon="Delete" @click.stop="deleteNote(note.id)">删除</el-button>
                                            <span style="margin-left: 10px;"><i class="fa fa-heart-o"></i> {{ note.dianzan || 0 }}</span>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <el-empty v-if="noteList.length === 0" description="暂无笔记" />
                        </div>
                    </el-tab-pane>
                    <el-tab-pane label="我的收藏" name="collects">
                        <div class="list-grid">
                            <div v-for="note in collectList" :key="note.id" class="list-card" @click="goToDetail('biji', note.id)">
                                <div class="card-cover" v-if="note.bijifengmian">
                                    <e-img :src="note.bijifengmian" class="cover-img" />
                                </div>
                                <div class="card-content">
                                    <div class="list-title">{{ note.bijimingcheng }}</div>
                                    <div class="list-desc">{{ formatText(note.xiangqing) }}</div>
                                    <div class="list-meta">
                                        <span>{{ formatTime(note.addtime) }}</span>
                                        <span class="meta-right"><i class="fa fa-star"></i> 已收藏</span>
                                    </div>
                                </div>
                            </div>
                            <el-empty v-if="collectList.length === 0" description="暂无收藏" />
                        </div>
                    </el-tab-pane>
                    <el-tab-pane label="我的点赞" name="likes">
                        <div class="list-grid">
                            <div v-for="note in likeList" :key="note.id" class="list-card" @click="goToDetail('biji', note.id)">
                                <div class="card-cover" v-if="note.bijifengmian">
                                    <e-img :src="note.bijifengmian" class="cover-img" />
                                </div>
                                <div class="card-content">
                                    <div class="list-title">{{ note.bijimingcheng }}</div>
                                    <div class="list-desc">{{ formatText(note.xiangqing) }}</div>
                                    <div class="list-meta">
                                        <span>{{ formatTime(note.addtime) }}</span>
                                        <span class="meta-right"><i class="fa fa-heart" style="color: #f56c6c"></i> 已点赞</span>
                                    </div>
                                </div>
                            </div>
                            <el-empty v-if="likeList.length === 0" description="暂无点赞" />
                        </div>
                    </el-tab-pane>
                    <el-tab-pane label="我的种草" name="zhongcao">
                        <div class="list-grid">
                            <div v-for="note in zhongcaoList" :key="note.id" class="list-card" @click="goToDetail('biji', note.id)">
                                <div class="card-cover" v-if="note.bijifengmian">
                                    <e-img :src="note.bijifengmian" class="cover-img" />
                                </div>
                                <div class="card-content">
                                    <div class="list-title">{{ note.bijimingcheng }}</div>
                                    <div class="list-desc">{{ formatText(note.xiangqing) }}</div>
                                    <div class="list-meta">
                                        <span>{{ formatTime(note.addtime) }}</span>
                                        <span class="meta-right"><i class="fa fa-leaf" style="color: #67c23a"></i> 已种草</span>
                                    </div>
                                </div>
                            </div>
                            <el-empty v-if="zhongcaoList.length === 0" description="暂无种草" />
                        </div>
                    </el-tab-pane>
                    <el-tab-pane label="我的评论" name="comments">
                        <div class="list-grid">
                            <div v-for="comment in commentList" :key="comment.id" class="list-card">
                                <div class="card-content">
                                    <div class="list-title" style="margin-bottom: 8px;">
                                        <span style="font-weight: normal; color: #909399; font-size: 13px; margin-right: 4px;">评论了</span>
                                        {{ comment.biaoti || "评论内容" }}
                                    </div>
                                    <div class="list-desc" style="height: auto; -webkit-line-clamp: 3; background: #f5f7fa; padding: 8px; border-radius: 4px; margin-bottom: 12px;">
                                        {{ comment.pinglunneirong }}
                                    </div>
                                    <div class="list-meta">
                                        <span>{{ formatTime(comment.addtime) }}</span>
                                        <el-button type="primary" link @click="goToCommentTarget(comment)">查看原文</el-button>
                                    </div>
                                </div>
                            </div>
                            <el-empty v-if="commentList.length === 0" description="暂无评论" />
                        </div>
                    </el-tab-pane>
                </el-tabs>
            </div>

            <el-dialog v-model="showUserListDialog" :title="userListTitle" width="500px">
                <div v-loading="userListLoading" class="user-list-container">
                    <div v-if="userList.length > 0" class="user-list">
                        <div v-for="u in userList" :key="u.id" class="user-item" @click="goToUserDetail(u.id)">
                            <el-avatar :size="50" :src="getAvatarUrl(u.touxiang)">
                                <img src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"/>
                            </el-avatar>
                            <div class="user-info">
                                <div class="user-name">{{ u.mingcheng || u.zhanghao }}</div>
                                <div class="user-sign">{{ u.gexingqianming || '暂无签名' }}</div>
                            </div>
                            <el-button type="primary" link>查看</el-button>
                        </div>
                    </div>
                    <el-empty v-else description="暂无数据" />
                </div>
            </el-dialog>

            <el-dialog v-model="showPasswordDialog" title="修改密码" width="400px">
                <el-form ref="passwordFormRef" :model="passwordForm" :rules="passwordRules" label-width="80px">
                    <el-form-item label="原密码" prop="oldPassword">
                        <el-input v-model="passwordForm.oldPassword" type="password" show-password></el-input>
                    </el-form-item>
                    <el-form-item label="新密码" prop="newPassword">
                        <el-input v-model="passwordForm.newPassword" type="password" show-password></el-input>
                    </el-form-item>
                    <el-form-item label="确认密码" prop="confirmPassword">
                        <el-input v-model="passwordForm.confirmPassword" type="password" show-password></el-input>
                    </el-form-item>
                </el-form>
                <template #footer>
                    <span class="dialog-footer">
                        <el-button @click="showPasswordDialog = false">取消</el-button>
                        <el-button type="primary" @click="handlePasswordChange">确认修改</el-button>
                    </span>
                </template>
            </el-dialog>
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
import { Edit, Key, Plus, Delete } from "@element-plus/icons-vue";
import http from "@/utils/ajax/http";
import { useUserStore } from "@/stores";
import { ElMessage, ElMessageBox } from "element-plus";

import { selectGuanzhuren, selectTianjiaren } from "@/module/guanzhu";

const router = useRouter();
const username = session("username");
const user = ref({});
const noteList = ref([]);
const commentList = ref([]);
const collectList = ref([]);
const likeList = ref([]);
const zhongcaoList = ref([]);
const activeTab = ref("notes");

const showUserListDialog = ref(false);
const userListTitle = ref("");
const userList = ref([]);
const userListLoading = ref(false);

const showPasswordDialog = ref(false);
const passwordFormRef = ref(null);
const passwordForm = reactive({
    oldPassword: '',
    newPassword: '',
    confirmPassword: ''
});

const validateConfirmPassword = (rule, value, callback) => {
    if (value !== passwordForm.newPassword) {
        callback(new Error('两次输入密码不一致'));
    } else {
        callback();
    }
};

const passwordRules = {
    oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
    newPassword: [{ required: true, message: '请输入新密码', trigger: 'blur' }],
    confirmPassword: [
        { required: true, message: '请确认新密码', trigger: 'blur' },
        { validator: validateConfirmPassword, trigger: 'blur' }
    ]
};

const counts = reactive({
    biji: 0,
    pinglun: 0,
    shoucang: 0,
    dianzan: 0,
    zhongcao: 0,
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

const getAvatarUrl = (url) => {
    if (!url) return "";
    if (url.startsWith("http")) return url;
    return config.service_url + (url.startsWith("/") ? "" : "/") + url;
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
    noteList.value = await DB.name("biji").where("tianjiaren", username).order("id desc").limit(20).select();
    commentList.value = await DB.name("pinglun").where("pinglunren", username).order("id desc").limit(20).select();

    // 加载收藏 (目前仅加载笔记类型的收藏)
    const collections = await DB.name("shoucang").where("username", username).order("id desc").select();
    // shoucang 表的主键通常是 xwid
    const bijiIds = collections.filter(c => c.biao === 'biji').map(c => c.xwid || c.biaoid);
    if (bijiIds.length > 0) {
        const bijis = await DB.name("biji").where("id", "in", bijiIds).select();
        collectList.value = bijis;
    }

    // 加载点赞 (目前仅加载笔记类型的点赞)
    const likes = await DB.name("dianzan").where("username", username).order("id desc").select();
    const likedBijiIds = likes.filter(c => c.biao === 'biji').map(c => c.biaoid || c.xwid);
    if (likedBijiIds.length > 0) {
        const likedBijis = await DB.name("biji").where("id", "in", likedBijiIds).select();
        likeList.value = likedBijis;
    }

    // 加载种草
    const zhongcaos = await DB.name("zhongcao").where("zhongcaoren", username).order("id desc").select();
    const zhongcaoBijiIds = zhongcaos.map(z => z.bijiid).filter(id => id);
    if (zhongcaoBijiIds.length > 0) {
        const bijis = await DB.name("biji").where("id", "in", zhongcaoBijiIds).select();
        zhongcaoList.value = bijis;
    }
};

const openUserList = async (type) => {
    if (type === 'history') {
        router.push('/history');
        return;
    }
    
    showUserListDialog.value = true;
    userListLoading.value = true;
    userList.value = [];
    
    try {
        let res;
        let targetUsernames = [];
        
        if (type === 'follow') {
            userListTitle.value = "我的关注";
            // 查询我关注的人 (我是关注人)
            res = await selectGuanzhuren({ username, page: 1, size: 50 });
            if (res && res.code === 0 && res.data.lists.records) {
                // 假设返回字段是 beiguanzhuren (被关注人)
                targetUsernames = res.data.lists.records.map(r => r.beiguanzhuren || r.followee);
            }
        } else if (type === 'fans') {
            userListTitle.value = "我的粉丝";
            // 查询关注我的人 (我是被关注人/添加人)
            res = await selectTianjiaren({ username, page: 1, size: 50 });
            if (res && res.code === 0 && res.data.lists.records) {
                // 假设返回字段是 guanzhuren (关注人)
                targetUsernames = res.data.lists.records.map(r => r.guanzhuren || r.follower);
            }
        }
        
        // 去重并过滤空值
        targetUsernames = [...new Set(targetUsernames.filter(u => u))];
        
        if (targetUsernames.length > 0) {
            // 查询用户信息
            const users = await DB.name("yonghu").where("zhanghao", "in", targetUsernames).select();
            userList.value = users;
        }
    } catch (e) {
        console.error(e);
    } finally {
        userListLoading.value = false;
    }
};

const goToUserDetail = (id) => {
    router.push({ path: '/yonghu/detail', query: { id } });
    showUserListDialog.value = false;
};

const handlePasswordChange = () => {
    if (!passwordFormRef.value) return;
    passwordFormRef.value.validate(async (valid) => {
        if (valid) {
            try {
                const res = await http.post(config.user_mod_post, passwordForm);
                if (res.code === 0) {
                    ElMessage.success('密码修改成功，请重新登录');
                    showPasswordDialog.value = false;
                    useUserStore().logout().then(() => {
                        router.push('/login');
                    });
                } else {
                    ElMessage.error(res.msg || '修改失败');
                }
            } catch (e) {
                ElMessage.error(e.message || '修改失败');
            }
        }
    });
};

const editNote = (id) => {
    router.push({ path: '/biji/updt', query: { id } });
};

const deleteNote = (id) => {
    ElMessageBox.confirm('确定要删除这条笔记吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(async () => {
        try {
            const res = await http.post('/api/biji/delete', [id]);
            if (res.code === 0) {
                ElMessage.success('删除成功');
                loadLists();
                loadCounts();
            } else {
                ElMessage.error(res.msg || '删除失败');
            }
        } catch (e) {
            ElMessage.error(e.message || '删除失败');
        }
    }).catch(() => {});
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
    .name-row {
        display: flex;
        align-items: center;
        gap: 10px;
        .name {
            font-size: 22px;
            font-weight: 600;
            color: #303133;
        }
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

.profile-tabs {
    margin-top: 20px;
}

.list-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
    gap: 16px;
    margin-top: 10px;
}

.list-card {
    background: #ffffff;
    border-radius: 12px;
    padding: 0;
    border: 1px solid var(--theme-border-color);
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
    cursor: pointer;
    transition: transform 0.2s;
    overflow: hidden;
    display: flex;
    flex-direction: column;

    &:hover {
        transform: translateY(-2px);
    }

    .card-cover {
        width: 100%;
        height: 140px;
        overflow: hidden;
        .cover-img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }
    }

    .card-content {
        padding: 12px;
        flex: 1;
        display: flex;
        flex-direction: column;
    }
}

.list-title {
    font-size: 15px;
    font-weight: 600;
    color: #303133;
    margin-bottom: 6px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.list-desc {
    font-size: 13px;
    color: #606266;
    line-height: 1.5;
    height: 40px;
    overflow: hidden;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    margin-bottom: 8px;
}

.list-meta {
    margin-top: auto;
    font-size: 12px;
    color: #909399;
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    .meta-right {
        display: flex;
        align-items: center;
        gap: 4px;
    }
}

.user-list-container {
    max-height: 400px;
    overflow-y: auto;
}

.user-list {
    display: flex;
    flex-direction: column;
    gap: 12px;
    padding: 4px;
}

.user-item {
    display: flex;
    align-items: center;
    gap: 16px;
    padding: 16px;
    border-radius: 12px;
    background-color: #fff;
    border: 1px solid #ebeef5;
    cursor: pointer;
    transition: all 0.3s ease;

    &:hover {
        background-color: #f9fafc;
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
        border-color: var(--theme-primary-color, #409eff);
    }

    .user-info {
        flex: 1;
        overflow: hidden;

        .user-name {
            font-size: 16px;
            font-weight: 600;
            color: #303133;
            margin-bottom: 6px;
        }

        .user-sign {
            font-size: 13px;
            color: #909399;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }
    }
}
</style>
