<template>
  <div class="topic-index">
    <div class="page-header">
      <div class="header-content">
        <h2>
          <i class="fa fa-comments" style="margin-right: 12px; color: var(--theme-primary-color);"></i>
          话题广场
        </h2>
        <p class="subtitle">汇聚热门游戏话题，分享你的独到见解</p>
      </div>
      <div class="search-box">
        <el-autocomplete
          v-model="searchKeyword"
          :fetch-suggestions="querySearch"
          placeholder="搜索感兴趣的话题..."
          class="search-input"
          @select="handleSelect"
          @keyup.enter="handleSearch"
          clearable
          @clear="handleSearch"
          size="large"
          style="width: 100%"
        >
          <template #prefix>
            <i class="fa fa-search" style="color: #909399"></i>
          </template>
          <template #default="{ item }">
            <div v-if="item.type === 'header'" class="search-suggestion-header" @click.stop>
                <span>{{ item.value }}</span>
            </div>
            <div v-else class="suggestion-item">
              <span v-if="item.type === 'history'" style="color: #909399; margin-right: 8px;">
                <i class="fa fa-history"></i>
              </span>
              <span v-else style="color: #f56c6c; margin-right: 8px;">
                <i class="fa fa-fire"></i>
              </span>
              <span style="flex: 1; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">{{ item.value }}</span>
              <span v-if="item.type === 'history'" class="delete-history" @click.stop="deleteHistoryItem(item)">
                <el-icon><Close /></el-icon>
              </span>
            </div>
          </template>
          <template #append>
            <el-button @click="handleSearch" type="primary" class="search-btn">搜索</el-button>
          </template>
        </el-autocomplete>
      </div>
    </div>
    
    <div class="tag-filter">
      <div class="filter-title">
        <span class="filter-label"><i class="fa fa-tags" style="margin-right: 6px;"></i>标签筛选</span>
        <div class="filter-actions">
          <el-switch v-model="showSubscribedOnly" active-text="只看关注" />
          <el-button v-if="selectedTagIds.length" link type="primary" class="clear-btn" @click="clearTagFilters">清空</el-button>
        </div>
      </div>

      <div class="subscribe-summary" v-if="subscribedTagIds.length">
          已关注 {{ subscribedTagIds.length }} 个标签
          <el-button link type="primary" @click="applySubscribedTags">一键筛选</el-button>
      </div>

      <div v-if="tagLoading" class="tag-loading">
        <el-skeleton :rows="1" animated />
      </div>
      <div v-else class="tag-list">
        <div v-for="tag in tagOptions" :key="tag.id" class="tag-item" :class="{ subscribed: isSubscribed(tag.id) }">
          <el-tag
            class="tag-chip"
            :type="isTagSelected(tag.id) ? 'primary' : 'info'"
            :effect="isTagSelected(tag.id) ? 'dark' : 'plain'"
            @click="toggleTag(tag.id)"
          >
            {{ tag.biaoqianmingcheng }}
          </el-tag>
          <span class="subscribe-btn" :title="isSubscribed(tag.id) ? '取消关注' : '关注标签'" @click.stop="toggleSubscribe(tag.id)">
            <el-icon v-if="isSubscribed(tag.id)"><StarFilled /></el-icon>
            <el-icon v-else><Star /></el-icon>
          </span>
        </div>
        <el-empty v-if="tagOptions.length === 0" description="暂无标签" />
      </div>
    </div>

    <div class="categories-section">
      <div class="section-title-bar">
        <h3 v-if="!isSearching">
          <i class="fa fa-fire" style="color: #f56c6c; margin-right: 8px;"></i>
          热门话题
        </h3>
        <h3 v-else>
          搜索结果: "{{ appliedKeyword }}"
          <el-button link type="primary" @click="clearSearch" style="margin-left: 10px; font-size: 14px;">
            <i class="fa fa-refresh" style="margin-right: 4px"></i>返回热门话题
          </el-button>
        </h3>
        <div class="list-controls">
          <el-select v-model="sortMode" size="small" class="control-item">
            <el-option label="默认排序" value="default" />
            <el-option label="最新发布" value="latest" />
            <el-option label="热度优先" value="hot" />
            <el-option label="讨论最多" value="discuss" />
          </el-select>
          <el-select v-model="timeRange" size="small" class="control-item">
            <el-option label="全部时间" value="all" />
            <el-option label="近7天" value="7" />
            <el-option label="近30天" value="30" />
            <el-option label="近90天" value="90" />
          </el-select>
        </div>
      </div>
      
      <div v-if="loading" class="loading-state">
         <el-skeleton :rows="3" animated count="3" />
      </div>
      <div v-else class="category-list">
        <div 
          v-for="(topic, index) in sortedCategories" 
          :key="topic.id" 
          class="category-tag"
          @click="goToDetail(topic.id)"
        >
            <span class="tag-hash" :class="'text-' + (index % 5)">#</span>
            <span class="tag-text">{{ topic.title }}</span>
            <span class="tag-hot" v-if="index < 3"><i class="fa fa-fire"></i></span>
        </div>
        <div v-if="sortedCategories.length === 0" class="empty-tip">
            <el-empty :description="emptyTip" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import { useRouter } from "vue-router";
import { getTopicCategories } from "@/module/topic";
import http from "@/utils/ajax/http";
import { ElMessage } from "element-plus";
import { Star, StarFilled, Close } from "@element-plus/icons-vue";
import { getSubscribedTags, toggleSubscribedTag } from "@/utils/tag-subscription";

const router = useRouter();
const categories = ref([]);
const loading = ref(false);
const searchKeyword = ref("");
const appliedKeyword = ref("");
const isSearching = ref(false);
const tagOptions = ref([]);
const tagLoading = ref(false);
const selectedTagIds = ref([]);
const tagMatchMode = ref("any");
const subscribedTagIds = ref(getSubscribedTags());
const showSubscribedOnly = ref(false);
const sortMode = ref("default");
const timeRange = ref("all");

const getRandomHotSearches = () => {
    const pool = [
        "王者荣耀上分攻略集中营", 
        "原神探索与剧情交流", 
        "崩铁与回合制RPG深度研讨", 
        "黑神话：悟空 Boss攻坚", 
        "只狼 弹反练习与架势压制", 
        "艾尔登法环 开荒路线", 
        "黑暗之魂3 新手入坑避坑", 
        "鬼泣 华丽连段与评分提升", 
        "明末：渊虚之羽 剧情考据", 
        "巫师3 支线推荐与炼金法印", 
        "最终幻想14 新人入坑与副本", 
        "生化危机8 解谜流程与资源", 
        "博德之门3 职业搭配与剧情", 
        "战神5 战斗连招与支线收集"
    ];
    return pool.sort(() => 0.5 - Math.random()).slice(0, 3);
};

const getHistory = () => {
    try {
        return JSON.parse(localStorage.getItem("topic_search_history") || "[]");
    } catch (e) {
        return [];
    }
};

const saveHistory = (keyword) => {
    if (!keyword) return;
    try {
        let history = JSON.parse(localStorage.getItem("topic_search_history") || "[]");
        history = history.filter((h) => h !== keyword);
        history.unshift(keyword);
        if (history.length > 10) history = history.slice(0, 10);
        localStorage.setItem("topic_search_history", JSON.stringify(history));
    } catch (e) {
        console.error(e);
    }
};

let lastSearchCallback = null;

    const deleteHistoryItem = (item) => {
        try {
            let history = JSON.parse(localStorage.getItem("topic_search_history") || "[]");
            history = history.filter((h) => h !== item.value);
            localStorage.setItem("topic_search_history", JSON.stringify(history));
            if (lastSearchCallback) {
                querySearch(searchKeyword.value, lastSearchCallback);
            }
    } catch (e) {
        console.error(e);
    }
};

const querySearch = (queryString, cb) => {
    lastSearchCallback = cb;
    let results = [];
    const history = getHistory();

    if (!queryString) {
        // 历史记录
        const historyItems = history.slice(0, 3).map(item => ({ value: item, type: 'history' }));
        if (historyItems.length > 0) {
            results.push({ value: '历史搜索', type: 'header' });
            results.push(...historyItems);
        }

        // 热门推荐
        const hotItems = getRandomHotSearches().map(item => ({ value: item, type: 'hot' }));
        if (hotItems.length > 0) {
            results.push({ value: '热门推荐', type: 'header' });
            results.push(...hotItems);
        }
    } else {
        const historyItems = history.filter(item => item.toLowerCase().includes(queryString.toLowerCase()))
            .slice(0, 10)
            .map(item => ({ value: item, type: 'history' }));
        results = historyItems;
    }
    cb(results);
};

const handleSelect = (item) => {
    if (item.type === 'header') return;
    searchKeyword.value = item.value;
    handleSearch();
};

const normalizeTagId = (id) => String(id);

const parseTagIds = (raw) => {
    if (!raw) return [];
    if (Array.isArray(raw)) {
        return raw.map((item) => normalizeTagId(item)).filter(Boolean);
    }
    return String(raw)
        .replace(/，/g, ",")
        .split(/[,;|\s]+/)
        .map((item) => item.trim())
        .filter(Boolean)
        .map((item) => normalizeTagId(item));
};

const getTopicTagIds = (topic) => {
    if (!topic) return [];
    return parseTagIds(topic.tagIds || topic.tag_ids);
};

const isTagSelected = (id) => selectedTagIds.value.includes(normalizeTagId(id));
const isSubscribed = (id) => subscribedTagIds.value.includes(normalizeTagId(id));

const toggleTag = (id) => {
    const normalized = normalizeTagId(id);
    const idx = selectedTagIds.value.indexOf(normalized);
    if (idx >= 0) {
        selectedTagIds.value.splice(idx, 1);
    } else {
        selectedTagIds.value.push(normalized);
    }
};

const toggleSubscribe = (id) => {
    subscribedTagIds.value = toggleSubscribedTag(id);
};

const applySubscribedTags = () => {
    selectedTagIds.value = [...subscribedTagIds.value];
};

const clearTagFilters = () => {
    selectedTagIds.value = [];
};

const fetchCategories = async () => {
  loading.value = true;
  try {
    const params = { 
        page: 1, 
        pagesize: 200,
        keyword: appliedKeyword.value,
    };
    const res = await getTopicCategories(params);
    if (res.code === 0 || res.code === "0") {
      categories.value = res.data.lists || [];
    } else {
      ElMessage.error(res.msg || "获取话题失败");
    }
  } catch (error) {
    console.error(error);
    ElMessage.error("网络错误");
  } finally {
    loading.value = false;
  }
};

const fetchTags = async () => {
    tagLoading.value = true;
    try {
        const res = await http.get("/api/biaoqian/selectAll");
        if (res.code === 0 || res.code === "0") {
            tagOptions.value = res.data || [];
        } else {
            ElMessage.error(res.msg || "获取标签失败");
        }
    } catch (error) {
        console.error(error);
        ElMessage.error("网络错误");
    } finally {
        tagLoading.value = false;
    }
};

const getTopicTime = (item) => {
    const raw = item.addtime || item.createdAt || item.created_at || item.add_time || item.create_time || item.time;
    if (!raw) return 0;
    const stamp = new Date(raw).getTime();
    return Number.isNaN(stamp) ? 0 : stamp;
};

const getTopicHeat = (item) => {
    return Number(item.hot || item.heat || item.views || item.view_count || item.clicknum || item.discuss_count || item.discussCount || 0);
};

const getTopicDiscuss = (item) => {
    return Number(item.discuss_count || item.discussCount || item.comment_count || item.commentCount || 0);
};

const filteredCategories = computed(() => {
    let list = categories.value || [];
    const keyword = appliedKeyword.value.trim().toLowerCase();
    if (keyword) {
        list = list.filter((item) =>
            (item.title && item.title.toLowerCase().includes(keyword)) ||
            (item.intro && item.intro.toLowerCase().includes(keyword))
        );
    }
    if (showSubscribedOnly.value && subscribedTagIds.value.length) {
        list = list.filter((item) => {
            const ids = getTopicTagIds(item);
            return subscribedTagIds.value.some((id) => ids.includes(id));
        });
    }
    if (selectedTagIds.value.length > 0) {
        list = list.filter((item) => {
            const ids = getTopicTagIds(item);
            if (ids.length === 0) return false;
            const idSet = new Set(ids);
            const matchAll = tagMatchMode.value === "all";
            return matchAll
                ? selectedTagIds.value.every((id) => idSet.has(id))
                : selectedTagIds.value.some((id) => idSet.has(id));
        });
    }
    if (timeRange.value !== "all") {
        const days = Number(timeRange.value);
        const cutoff = Date.now() - days * 24 * 60 * 60 * 1000;
        list = list.filter((item) => {
            const time = getTopicTime(item);
            return time === 0 || time >= cutoff;
        });
    }
    return list;
});

const sortedCategories = computed(() => {
    const list = [...filteredCategories.value];
    if (sortMode.value === "latest") {
        return list.sort((a, b) => getTopicTime(b) - getTopicTime(a));
    }
    if (sortMode.value === "hot") {
        return list.sort((a, b) => getTopicHeat(b) - getTopicHeat(a));
    }
    if (sortMode.value === "discuss") {
        return list.sort((a, b) => getTopicDiscuss(b) - getTopicDiscuss(a));
    }
    return list;
});

const emptyTip = computed(() => {
    if (isSearching.value) {
        return "未找到相关话题";
    }
    if (showSubscribedOnly.value && subscribedTagIds.value.length === 0) {
        return "还没有关注标签";
    }
    return "暂无热门话题";
});

const handleSearch = () => {
    const keyword = searchKeyword.value.trim();
    if (!keyword) {
        appliedKeyword.value = "";
        isSearching.value = false;
        fetchCategories();
        return;
    }
    appliedKeyword.value = keyword;
    saveHistory(keyword);
    isSearching.value = true;
    fetchCategories();
};

const clearSearch = () => {
    searchKeyword.value = "";
    appliedKeyword.value = "";
    isSearching.value = false;
    fetchCategories();
};

const goToDetail = (id) => {
  router.push({ path: "/topic/detail", query: { id } });
};

onMounted(() => {
  fetchCategories();
  fetchTags();
});
</script>

<style scoped lang="scss">
.topic-index {
  padding: 30px 20px;
  max-width: 1200px;
  margin: 0 auto;
  min-height: 80vh;

  .page-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 32px;
    padding-bottom: 20px;
    border-bottom: 1px solid var(--theme-border-color);
    
    .header-content {
        h2 {
            margin: 0 0 8px 0;
            font-size: 28px;
            color: #303133;
            font-weight: 600;
            display: flex;
            align-items: center;
        }
        .subtitle {
            margin: 0;
            color: #909399;
            font-size: 14px;
        }
    }

    .search-box {
        width: 360px;
        .search-input {
            :deep(.el-input-group__append) {
                background-color: var(--theme-primary-color);
                color: white;
                border-color: var(--theme-primary-color);
                &:hover {
                    background-color: var(--theme-primary-hover-color);
                }
            }
        }
    }
  }

  .categories-section {
    .section-title-bar {
        margin-bottom: 20px;
        display: flex;
        align-items: center;
        justify-content: space-between;
        flex-wrap: wrap;
        gap: 12px;

        h3 {
            margin: 0;
            font-size: 20px;
            color: #303133;
            display: flex;
            align-items: center;
        }

        .list-controls {
            display: flex;
            gap: 10px;
            .control-item {
                width: 140px;
            }
        }
    }
  }

  .tag-filter {
    margin-bottom: 24px;
    padding: 16px 18px;
    background: var(--theme-surface-color);
    border: 1px solid var(--theme-border-color);
    border-radius: 12px;
    box-shadow: 0 2px 6px rgba(0, 0, 0, 0.04);

    .filter-title {
      display: flex;
      align-items: center;
      justify-content: space-between;
      margin-bottom: 12px;
      color: #606266;
      font-size: 14px;
      font-weight: 600;

      .filter-actions {
        display: flex;
        align-items: center;
        gap: 10px;
        flex-wrap: wrap;
      }

      .match-toggle {
        :deep(.el-radio-button__inner) {
          padding: 6px 12px;
        }
      }

      .clear-btn {
        font-size: 13px;
      }
    }

    .subscribe-summary {
        font-size: 13px;
        color: #909399;
        margin-bottom: 12px;
        display: flex;
        align-items: center;
        gap: 8px;
    }

    .tag-loading {
      padding: 6px 0;
    }

    .tag-list {
      display: flex;
      flex-wrap: wrap;
      gap: 10px;
    }

    .tag-item {
        display: inline-flex;
        align-items: center;
        gap: 6px;
        padding: 4px 8px;
        border-radius: 16px;
        background: var(--theme-surface-muted);
        border: 1px dashed transparent;

        &.subscribed {
            border-color: var(--theme-primary-border-color);
        }
    }

    .tag-chip {
      cursor: pointer;
      transition: transform 0.2s, box-shadow 0.2s;
      border-radius: 16px;
      padding: 4px 12px;
      user-select: none;

      &:hover {
        transform: translateY(-1px);
        box-shadow: 0 4px 10px rgba(64, 158, 255, 0.15);
      }
    }

    .subscribe-btn {
        display: inline-flex;
        align-items: center;
        justify-content: center;
        width: 24px;
        height: 24px;
        border-radius: 50%;
        cursor: pointer;
        color: var(--theme-primary-color);
        background: var(--theme-primary-soft-color);
        transition: transform 0.2s;

        &:hover {
            transform: scale(1.05);
        }
    }
  }

  .category-list {
    display: flex;
    flex-wrap: wrap;
    gap: 15px;

    .category-tag {
      display: inline-flex;
      align-items: center;
      padding: 10px 20px;
      background: var(--theme-surface-color);
      border: 1px solid var(--theme-border-color);
      border-radius: 30px;
      cursor: pointer;
      transition: all 0.3s;
      font-size: 15px;
      color: #606266;
      box-shadow: 0 2px 6px rgba(0, 0, 0, 0.04);

      &:hover {
        color: var(--theme-primary-color);
        border-color: var(--theme-primary-border-color);
        background-color: var(--theme-primary-soft-color);
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(64, 158, 255, 0.15);
      }

      .tag-hash {
          margin-right: 6px;
          font-weight: bold;
          font-size: 16px;
          
          &.text-0 { color: var(--theme-primary-color); }
          &.text-1 { color: #67c23a; }
          &.text-2 { color: #e6a23c; }
          &.text-3 { color: #f56c6c; }
          &.text-4 { color: #909399; }
      }
      
      .tag-text {
          font-weight: 500;
      }

      .tag-hot {
          margin-left: 8px;
          color: #f56c6c;
          font-size: 12px;
          animation: pulse 2s infinite;
      }
    }
    
    .empty-tip {
        width: 100%;
        padding: 60px 0;
    }
  }

  @keyframes pulse {
      0% { opacity: 1; transform: scale(1); }
      50% { opacity: 0.7; transform: scale(1.1); }
      100% { opacity: 1; transform: scale(1); }
  }
  
  .loading-state {
      padding: 20px;
  }
}
</style>

<style lang="scss">
.search-suggestion-header {
    font-size: 12px;
    color: #999;
    padding: 5px 10px;
    background-color: #f5f7fa;
    border-bottom: 1px solid #eee;
    font-weight: bold;
    pointer-events: none;
    cursor: default;
}

.suggestion-item {
    display: flex;
    align-items: center;
    width: 100%;
    
    .delete-history {
        color: #999;
        cursor: pointer;
        font-size: 12px;
        margin-left: 10px;
        &:hover {
            color: #f56c6c;
        }
    }
}
</style>
