<template>
    <div class="views-my-growth">
        <e-container>
            <div class="growth-card">
                <div class="header">
                    <div>
                        <div class="title">成长中心</div>
                        <div class="subtitle">签到、任务与成就都在这里</div>
                    </div>
                    <el-button type="primary" @click="handleSignIn">每日签到</el-button>
                </div>

                <div class="level-panel">
                    <div class="level-left">
                        <div class="level-value">Lv. {{ levelInfo.level }}</div>
                        <div class="level-desc">成长值 {{ state.points }}</div>
                    </div>
                    <div class="level-right">
                        <el-progress :percentage="levelPercent" :stroke-width="12" />
                        <div class="level-tip" v-if="!levelInfo.isMax">
                            距离下一级还差 {{ levelInfo.max - state.points }} 成长值
                        </div>
                        <div class="level-tip" v-else>已达到最高等级</div>
                    </div>
                </div>

                <div class="section">
                    <div class="section-title">成长任务</div>
                    <div class="task-grid">
                        <div v-for="task in taskList" :key="task.id" class="task-item">
                            <div class="task-info">
                                <div class="task-title">{{ task.title }}</div>
                                <div class="task-desc">{{ task.desc }}</div>
                                <div class="task-reward">奖励 {{ task.reward }} 成长值</div>
                            </div>
                            <div class="task-action">
                                <el-button
                                    size="small"
                                    :type="task.isClaimed ? 'success' : 'primary'"
                                    :disabled="!task.isDone || task.isClaimed"
                                    @click="handleClaim(task)"
                                >
                                    {{ task.isClaimed ? "已领取" : task.isDone ? "领取" : "未完成" }}
                                </el-button>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="section">
                    <div class="section-title">成就进度</div>
                    <div class="achievement-list">
                        <div v-for="item in achievements" :key="item.id" class="achievement-item">
                            <div class="achievement-head">
                                <div class="achievement-title">{{ item.title }}</div>
                                <div class="achievement-value">{{ item.current }}/{{ item.target }}{{ item.unit }}</div>
                            </div>
                            <el-progress :percentage="item.percent" :stroke-width="10" />
                        </div>
                    </div>
                </div>

                <div class="section">
                    <div class="section-title">成长记录</div>
                    <div class="history-list">
                        <div v-if="state.history.length === 0" class="empty">暂无记录</div>
                        <div v-for="(row, index) in state.history" :key="index" class="history-item">
                            <div class="history-type">{{ row.type }}</div>
                            <div class="history-meta">
                                <span class="history-points">+{{ row.points }}</span>
                                <span class="history-time">{{ formatTime(row.time) }}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </e-container>
    </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from "vue";
import { ElMessage } from "element-plus";
import { session } from "@/utils/utils";
import { fetchUserStats } from "@/utils/user-stats";
import { claimReward, getGrowthState, getLevelInfo, signIn } from "@/utils/growth";

const username = session("username");
const state = reactive(getGrowthState(username));

const counts = reactive({
    biji: 0,
    pinglun: 0,
    shoucang: 0,
    dianzan: 0,
    follow: 0,
    fans: 0,
    history: 0,
});

const refreshState = () => {
    const next = getGrowthState(username);
    Object.assign(state, next);
};

const loadCounts = async () => {
    const { counts: nextCounts } = await fetchUserStats(username);
    Object.assign(counts, nextCounts);
};

const levelInfo = computed(() => getLevelInfo(state.points));
const levelPercent = computed(() => Math.round(levelInfo.value.progress * 100));

const taskList = computed(() => {
    const claimed = state.claimed || {};
    const build = (id, title, desc, reward, isDone) => ({
        id,
        title,
        desc,
        reward,
        isDone,
        isClaimed: !!claimed[id],
    });
    return [
        build("task_comment", "首次评论", "发布 1 条评论", 10, counts.pinglun >= 1),
        build("task_biji", "发布笔记", "发布 1 篇笔记", 20, counts.biji >= 1),
        build("task_like", "首次点赞", "完成 1 次点赞", 10, counts.dianzan >= 1),
        build("task_favorite", "首次收藏", "完成 1 次收藏", 10, counts.shoucang >= 1),
        build("task_follow", "关注达人", "关注 3 位用户", 20, counts.follow >= 3),
        build("task_fans", "人气新星", "拥有 3 位粉丝", 30, counts.fans >= 3),
    ];
});

const achievements = computed(() => {
    const build = (id, title, current, target, unit) => ({
        id,
        title,
        current,
        target,
        unit,
        percent: Math.min(100, Math.round((current / target) * 100)),
    });
    return [
        build("ach_biji", "笔记达人", counts.biji, 10, "篇"),
        build("ach_pinglun", "评论达人", counts.pinglun, 20, "条"),
        build("ach_like", "点赞达人", counts.dianzan, 30, "次"),
        build("ach_favorite", "收藏达人", counts.shoucang, 20, "次"),
        build("ach_follow", "社交达人", counts.follow, 10, "人"),
    ];
});

const handleSignIn = () => {
    const res = signIn(username, 5);
    if (res.ok) {
        ElMessage.success(res.message);
    } else {
        ElMessage.warning(res.message);
    }
    refreshState();
};

const handleClaim = (task) => {
    if (!task.isDone || task.isClaimed) return;
    const res = claimReward(username, task.id, task.reward);
    if (res.ok) {
        ElMessage.success(res.message);
    } else {
        ElMessage.warning(res.message);
    }
    refreshState();
};

const formatTime = (value) => {
    if (!value) return "";
    return String(value).substring(0, 19).replace("T", " ");
};

onMounted(() => {
    loadCounts();
});
</script>

<style scoped lang="scss">
.views-my-growth {
    padding: 24px 0 40px;
}

.growth-card {
    background: var(--theme-surface-color);
    border-radius: 12px;
    padding: 24px;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.05);
}

.header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    .title {
        font-size: 20px;
        font-weight: 600;
        color: #303133;
    }

    .subtitle {
        color: #909399;
        font-size: 13px;
        margin-top: 4px;
    }
}

.level-panel {
    display: flex;
    align-items: center;
    gap: 24px;
    padding: 18px;
    border-radius: 12px;
    background: #ffffff;
    border: 1px solid var(--theme-border-color);
    margin-bottom: 24px;
}

.level-left {
    min-width: 160px;
    .level-value {
        font-size: 26px;
        font-weight: 700;
        color: var(--theme-primary-color);
    }
    .level-desc {
        margin-top: 6px;
        color: #909399;
        font-size: 13px;
    }
}

.level-right {
    flex: 1;
    .level-tip {
        margin-top: 6px;
        color: #909399;
        font-size: 12px;
    }
}

.section {
    margin-top: 24px;
}

.section-title {
    font-size: 16px;
    font-weight: 600;
    margin-bottom: 12px;
    color: #303133;
}

.task-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
    gap: 16px;
}

.task-item {
    background: #ffffff;
    border-radius: 12px;
    padding: 16px;
    border: 1px solid var(--theme-border-color);
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 12px;
}

.task-info {
    .task-title {
        font-weight: 600;
        color: #303133;
    }
    .task-desc {
        color: #909399;
        font-size: 12px;
        margin-top: 6px;
    }
    .task-reward {
        margin-top: 6px;
        color: var(--theme-primary-color);
        font-size: 12px;
    }
}

.achievement-list {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
    gap: 16px;
}

.achievement-item {
    background: #ffffff;
    border-radius: 12px;
    padding: 16px;
    border: 1px solid var(--theme-border-color);
}

.achievement-head {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 10px;
}

.achievement-title {
    font-weight: 600;
    color: #303133;
}

.achievement-value {
    font-size: 12px;
    color: #909399;
}

.history-list {
    background: #ffffff;
    border-radius: 12px;
    padding: 16px;
    border: 1px solid var(--theme-border-color);
}

.history-item {
    display: flex;
    justify-content: space-between;
    padding: 8px 0;
    border-bottom: 1px dashed #ebeef5;

    &:last-child {
        border-bottom: none;
    }
}

.history-type {
    font-weight: 500;
    color: #303133;
}

.history-meta {
    display: flex;
    gap: 12px;
    color: #909399;
    font-size: 12px;
}

.history-points {
    color: var(--theme-primary-color);
}

.empty {
    text-align: center;
    color: #909399;
    padding: 12px 0;
}
</style>
