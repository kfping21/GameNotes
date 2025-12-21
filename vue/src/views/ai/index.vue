<template>
    <div class="ai-page e-container">
        <el-card class="ai-card">
            <div class="form-row">
                <el-input
                    v-model="query"
                    type="textarea"
                    :rows="3"
                    placeholder="描述你的需求，例如：想找一篇关于XX的笔记 / 有哪些XX相关的话题 / 推荐哪个游戏的攻略"
                />
            </div>
            <div class="form-row actions">
                <div class="filters">
                    <el-select v-model="category" placeholder="选择推荐范围" class="category-select">
                        <el-option label="所有" value="all" />
                        <el-option label="笔记" value="note" />
                        <el-option label="游戏" value="game" />
                        <el-option label="周边" value="product" />
                        <el-option label="大话题" value="topic_main" />
                        <el-option label="小话题" value="topic_sub" />
                    </el-select>
                    <div class="limit">
                        <span>返回数量：</span>
                        <el-input-number v-model="limit" :min="1" :max="20" />
                    </div>
                </div>
                <el-button type="primary" :loading="loading" @click="doRecommend">AI 推荐</el-button>
            </div>
        </el-card>

        <el-card class="ai-card" v-if="results.length">
            <template #header>
                <div class="card-header">推荐结果（{{ results.length }} 条）</div>
            </template>
            <el-timeline>
                <el-timeline-item v-for="item in results" :key="item.type + '-' + item.id" color="#1cb19c">
                    <div class="item-head">
                        <el-tag size="small" effect="dark">{{ typeLabel(item.type) }}</el-tag>
                        <span class="title" v-html="item.title || '未命名'"></span>
                    </div>
                    <div class="summary" v-html="item.summary || '暂无简介'"></div>
                    <div class="actions">
                        <el-button size="small" type="primary" @click="open(item.link)">打开</el-button>
                    </div>
                </el-timeline-item>
            </el-timeline>
        </el-card>

        <el-empty v-else :description="emptyReason || '暂无推荐，请先描述你的需求试试吧'" />
    </div>
</template>

<script setup>
import { ref } from "vue";
import http from "@/utils/ajax/http";
import { ElMessage } from "element-plus";

const query = ref("");
const category = ref("all");
const limit = ref(5);
const loading = ref(false);
const results = ref([]);
const emptyReason = ref("");

const typeLabel = (type) => {
    const map = {
        note: "笔记",
        topic_main: "大话题",
        topic_sub: "小话题",
        game: "游戏",
        product: "周边",
    };
    return map[type] || type || "未知";
};

const doRecommend = async () => {
    if (!query.value.trim()) {
        ElMessage.warning("请先描述你的需求");
        return;
    }
    loading.value = true;
    try {
        const res = await http.post("/api/ai/recommend", {
            query: query.value,
            category: category.value,
            limit: limit.value,
        });
        if (res.code === 0) {
            results.value = (res.data && res.data.items) || [];
            emptyReason.value = (res.data && res.data.emptyReason) || "";
            if (!results.value.length && emptyReason.value) {
                ElMessage.info(emptyReason.value);
            }
        } else {
            ElMessage.error(res.msg || "AI 推荐失败");
            emptyReason.value = "";
        }
    } finally {
        loading.value = false;
    }
};

const open = (link) => {
    if (!link) return;
    window.open(link, "_blank");
};
</script>

<style scoped lang="scss">
.ai-page {
    padding: 20px 0 40px;

    .ai-card {
        margin-bottom: 16px;
    }

    .form-row {
        margin-bottom: 12px;
    }

    .actions {
        display: flex;
        align-items: center;
        gap: 12px;
        justify-content: space-between;

        .filters {
            display: flex;
            align-items: center;
            gap: 12px;

            .category-select {
                width: 200px;
            }

            .limit {
                display: flex;
                align-items: center;
                gap: 6px;
            }
        }
    }

    .item-head {
        display: flex;
        align-items: center;
        gap: 10px;
        margin-bottom: 6px;

        .title {
            font-weight: 600;
            color: #303133;
        }
    }

    .summary {
        color: #666;
        margin-bottom: 8px;
    }
}
</style>
