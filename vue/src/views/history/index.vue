<template>
    <div class="views-history">
        <e-container>
            <div class="history-card">
                <div class="history-header">
                    <div class="title">浏览历史</div>
                    <div class="actions">
                        <el-button size="small" @click="clearCurrent" :disabled="!activeList.length">清空当前</el-button>
                        <el-button size="small" type="danger" @click="clearAll" :disabled="!hasAnyHistory">清空全部</el-button>
                    </div>
                </div>

                <el-tabs v-model="activeTab" class="history-tabs">
                    <el-tab-pane label="笔记" name="biji" />
                    <el-tab-pane label="话题" name="topic" />
                    <el-tab-pane label="游戏" name="youxi" />
                    <el-tab-pane label="商品" name="mall" />
                </el-tabs>

                <el-empty v-if="activeList.length === 0" description="暂无浏览记录" />

                <div v-else class="history-list">
                    <div v-for="item in activeList" :key="itemKey(item)" class="history-item">
                        <div class="cover" v-if="item.cover">
                            <e-img :src="item.cover" class="cover-img" />
                        </div>
                        <div class="info">
                            <div class="title">{{ item.title || "未命名" }}</div>
                            <div class="desc" v-if="item.summary">{{ item.summary }}</div>
                            <div class="meta">{{ formatTime(item.visitedAt) }}</div>
                        </div>
                        <div class="ops">
                            <el-button type="primary" link @click="goTo(item)">查看</el-button>
                        </div>
                    </div>
                </div>
            </div>
        </e-container>
    </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref, watch } from "vue";
import { useRouter } from "vue-router";
import { clearHistory, getHistory } from "@/utils/history";

const router = useRouter();
const activeTab = ref("biji");
const historyMap = reactive({
    biji: [],
    topic: [],
    youxi: [],
    mall: [],
});

const loadHistory = (type) => {
    historyMap[type] = getHistory(type, 100);
};

const loadAll = () => {
    Object.keys(historyMap).forEach((key) => loadHistory(key));
};

const activeList = computed(() => historyMap[activeTab.value] || []);
const hasAnyHistory = computed(() => {
    return Object.values(historyMap).some((list) => list.length > 0);
});

const itemKey = (item) => `${item.type}-${item.id}-${item.visitedAt}`;

const formatTime = (value) => {
    if (!value) return "";
    return new Date(value).toLocaleString();
};

const goTo = (item) => {
    if (!item?.url) return;
    router.push(item.url);
};

const clearCurrent = () => {
    clearHistory(activeTab.value);
    loadHistory(activeTab.value);
};

const clearAll = () => {
    clearHistory();
    loadAll();
};

watch(activeTab, (val) => loadHistory(val));

onMounted(() => {
    loadAll();
});
</script>

<style scoped lang="scss">
.views-history {
    padding: 24px 0 40px;
}

.history-card {
    background: var(--theme-surface-color);
    border-radius: 12px;
    padding: 24px;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.05);
}

.history-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;

    .title {
        font-size: 20px;
        font-weight: 600;
        color: #303133;
    }

    .actions {
        display: flex;
        gap: 10px;
    }
}

.history-list {
    display: flex;
    flex-direction: column;
    gap: 12px;
}

.history-item {
    display: flex;
    align-items: center;
    gap: 16px;
    padding: 12px;
    border: 1px solid var(--theme-border-color);
    border-radius: 10px;
    background: var(--theme-surface-muted);
}

.cover {
    width: 88px;
    height: 66px;
    border-radius: 8px;
    overflow: hidden;
    flex-shrink: 0;
    background: #f5f5f5;
}

.cover-img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.info {
    flex: 1;
    min-width: 0;

    .title {
        font-size: 15px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }

    .desc {
        font-size: 13px;
        color: #606266;
        margin-bottom: 6px;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
    }

    .meta {
        font-size: 12px;
        color: #909399;
    }
}

.ops {
    flex-shrink: 0;
}
</style>
