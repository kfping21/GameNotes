<template>
    <!-- AI 悬浮按钮 -->
    <div class="ai-float-btn" @click="visible = true">
        <div class="ai-icon">
            <i class="fa fa-magic"></i>
        </div>
        <div class="ai-text">AI助手</div>
    </div>

    <!-- AI 侧边栏 -->
    <el-drawer
        v-model="visible"
        title="AI 智能助手"
        direction="rtl"
        size="400px"
        :append-to-body="true"
    >
        <div class="ai-container">
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
                    <el-select v-model="category" placeholder="选择推荐范围" class="category-select" style="width: 120px; margin-right: 10px;">
                        <el-option label="所有" value="all" />
                        <el-option label="笔记" value="note" />
                        <el-option label="游戏" value="game" />
                        <el-option label="周边" value="product" />
                        <el-option label="大话题" value="topic_main" />
                        <el-option label="小话题" value="topic_sub" />
                    </el-select>
                    <div class="limit">
                        <el-input-number v-model="limit" :min="1" :max="20" style="width: 100px;" controls-position="right" />
                    </div>
                </div>
                <el-button type="primary" :loading="loading" @click="doRecommend" style="margin-top: 10px; width: 100%;">AI 推荐</el-button>
            </div>

            <div class="results-area" v-if="results.length">
                <div class="divider">推荐结果（{{ results.length }} 条）</div>
                <div class="result-list">
                    <div class="result-item" v-for="item in results" :key="item.type + '-' + item.id">
                        <div class="item-head">
                            <el-tag size="small" effect="dark">{{ typeLabel(item.type) }}</el-tag>
                            <span class="title" v-html="item.title || '未命名'"></span>
                        </div>
                        <div class="summary" v-html="item.summary || '暂无简介'"></div>
                        <div class="item-actions">
                            <el-button size="small" type="primary" link @click="open(item.link)">查看详情</el-button>
                        </div>
                    </div>
                </div>
            </div>
            <el-empty v-else-if="!loading && hasSearched" :description="emptyReason || '暂无推荐，请换个说法试试'" />
            <div v-else-if="!hasSearched" class="welcome-tip">
                <p>👋 你好！我是你的 AI 助手。</p>
                <p>我可以帮你找到感兴趣的笔记、话题、游戏或周边。</p>
            </div>
        </div>
    </el-drawer>
</template>

<script setup>
import { ref } from "vue";
import http from "@/utils/ajax/http";
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";

const router = useRouter();
const visible = ref(false);
const query = ref("");
const category = ref("all");
const limit = ref(5);
const loading = ref(false);
const results = ref([]);
const emptyReason = ref("");
const hasSearched = ref(false);

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
    hasSearched.value = true;
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
    } catch (e) {
        ElMessage.error("请求出错");
    } finally {
        loading.value = false;
    }
};

const open = (link) => {
    if (!link) return;
    if (link.startsWith('http')) {
        window.open(link, '_blank');
    } else {
        router.push(link);
        visible.value = false; // Close drawer on navigation
    }
};
</script>

<style scoped lang="scss">
.ai-float-btn {
    position: fixed;
    bottom: 100px;
    right: 30px;
    width: 60px;
    height: 60px;
    background: linear-gradient(135deg, #409eff, #36cfc9);
    border-radius: 50%;
    box-shadow: 0 4px 15px rgba(64, 158, 255, 0.4);
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    cursor: pointer;
    z-index: 9999; /* High z-index to be on top */
    transition: all 0.3s;
    color: #fff;
    
    &:hover {
        transform: translateY(-5px);
        box-shadow: 0 6px 20px rgba(64, 158, 255, 0.6);
    }
    
    .ai-icon {
        font-size: 24px;
        margin-bottom: 2px;
    }
    
    .ai-text {
        font-size: 10px;
    }
}

.ai-container {
    padding: 10px;
    
    .form-row {
        margin-bottom: 15px;
    }
    
    .actions {
        .filters {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 10px;
        }
    }
    
    .divider {
        font-size: 14px;
        font-weight: bold;
        color: #333;
        margin: 20px 0 10px;
        padding-bottom: 5px;
        border-bottom: 1px solid #eee;
    }
    
    .result-list {
        .result-item {
            background: #f9f9f9;
            border-radius: 8px;
            padding: 12px;
            margin-bottom: 12px;
            border: 1px solid #eee;
            
            .item-head {
                display: flex;
                align-items: center;
                margin-bottom: 8px;
                
                .title {
                    font-weight: bold;
                    margin-left: 8px;
                    font-size: 14px;
                    color: #333;
                }
            }
            
            .summary {
                font-size: 13px;
                color: #666;
                line-height: 1.5;
                margin-bottom: 8px;
            }
            
            .item-actions {
                text-align: right;
            }
        }
    }
    
    .welcome-tip {
        text-align: center;
        color: #999;
        margin-top: 50px;
        line-height: 1.8;
    }
}
</style>
