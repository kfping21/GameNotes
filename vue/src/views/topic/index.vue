<template>
  <div class="topic-index">
    <div class="page-header">
      <div class="header-content">
        <h2><i class="fa fa-comments" style="margin-right: 12px; color: #409eff;"></i>话题广场</h2>
        <p class="subtitle">汇聚热门游戏话题，分享你的独到见解</p>
      </div>
      <div class="search-box">
        <el-input 
          v-model="searchKeyword" 
          placeholder="搜索感兴趣的话题..." 
          class="search-input"
          @keyup.enter="handleSearch"
          clearable
          @clear="handleSearch"
          size="large"
        >
          <template #prefix>
            <i class="fa fa-search" style="color: #909399"></i>
          </template>
          <template #append>
            <el-button @click="handleSearch" type="primary" class="search-btn">搜索</el-button>
          </template>
        </el-input>
      </div>
    </div>
    
    <div class="categories-section">
      <div class="section-title-bar">
        <h3 v-if="!isSearching"><i class="fa fa-fire" style="color: #f56c6c; margin-right: 8px;"></i>热门话题</h3>
        <h3 v-else>
            搜索结果: "{{ searchKeyword }}" 
            <el-button link type="primary" @click="clearSearch" style="margin-left: 10px; font-size: 14px;">
                <i class="fa fa-refresh" style="margin-right: 4px"></i>返回热门话题
            </el-button>
        </h3>
      </div>
      
      <div v-if="loading" class="loading-state">
         <el-skeleton :rows="3" animated count="3" />
      </div>
      <div v-else class="category-list">
        <div 
          v-for="(topic, index) in categories" 
          :key="topic.id" 
          class="category-item"
          @click="goToDetail(topic.id)"
        >
          <div class="card-body">
              <div class="topic-icon" :class="'bg-' + (index % 5)">
                  {{ topic.title ? topic.title.substring(0, 1) : '#' }}
              </div>
              <div class="topic-info">
                  <h4>{{ topic.title }}</h4>
                  <p class="intro">{{ topic.intro || '暂无简介' }}</p>
              </div>
          </div>
          <div class="card-footer">
              <span class="action-text">点击进入讨论</span>
              <i class="fa fa-arrow-right action-icon"></i>
          </div>
        </div>
        <div v-if="categories.length === 0" class="empty-tip">
            <el-empty :description="isSearching ? '未找到相关话题' : '暂无热门话题'" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { getTopicCategories } from '@/module/topic';
import { ElMessage } from 'element-plus';

const router = useRouter();
const categories = ref([]);
const loading = ref(false);
const searchKeyword = ref('');
const isSearching = ref(false);

const fetchCategories = async () => {
  loading.value = true;
  try {
    // 使用新的 POST 接口，带分页参数和搜索关键字
    const params = { 
        page: 1, 
        pagesize: 20,
        keyword: searchKeyword.value // 传递搜索关键字给后端
    };
    const res = await getTopicCategories(params);
    if (res.code === 0 || res.code === '0') {
      // 后端返回结构: { lists: [...], total, page, pagesize }
      let list = res.data.lists || [];
      
      // 前端辅助过滤：如果后端未实现模糊搜索，这里进行二次过滤确保效果
      if (searchKeyword.value.trim()) {
          const k = searchKeyword.value.trim().toLowerCase();
          list = list.filter(item => 
              (item.title && item.title.toLowerCase().includes(k)) || 
              (item.intro && item.intro.toLowerCase().includes(k))
          );
      }
      
      categories.value = list;
    } else {
      ElMessage.error(res.msg || '获取话题失败');
    }
  } catch (error) {
    console.error(error);
    ElMessage.error('网络错误');
  } finally {
    loading.value = false;
  }
};

const handleSearch = () => {
    if (!searchKeyword.value.trim()) {
        isSearching.value = false;
        fetchCategories();
        return;
    }
    isSearching.value = true;
    fetchCategories();
};

const clearSearch = () => {
    searchKeyword.value = '';
    isSearching.value = false;
    fetchCategories();
};

const goToDetail = (id) => {
  router.push({ path: '/topic/detail', query: { id } });
};

onMounted(() => {
  fetchCategories();
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
    margin-bottom: 40px;
    padding-bottom: 20px;
    border-bottom: 1px solid #ebeef5;
    
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
        width: 350px;
        .search-input {
            :deep(.el-input-group__append) {
                background-color: #409eff;
                color: white;
                border-color: #409eff;
                &:hover {
                    background-color: #66b1ff;
                }
            }
        }
    }
  }

  .categories-section {
    .section-title-bar {
        margin-bottom: 25px;
        h3 {
            margin: 0;
            font-size: 20px;
            color: #303133;
            display: flex;
            align-items: center;
        }
    }
  }

  .category-list {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
    gap: 25px;

    .category-item {
      background: #fff;
      border-radius: 12px;
      overflow: hidden;
      cursor: pointer;
      transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
      border: 1px solid #ebeef5;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      height: 180px;

      &:hover {
        transform: translateY(-5px);
        box-shadow: 0 12px 24px rgba(0, 0, 0, 0.1);
        border-color: #c6e2ff;
        
        .card-footer {
            background-color: #f5f7fa;
            .action-icon {
                transform: translateX(5px);
            }
            .action-text {
                color: #409eff;
            }
        }
      }

      .card-body {
          padding: 20px;
          display: flex;
          align-items: flex-start;
          
          .topic-icon {
              width: 50px;
              height: 50px;
              border-radius: 12px;
              display: flex;
              align-items: center;
              justify-content: center;
              font-size: 24px;
              font-weight: bold;
              color: white;
              margin-right: 15px;
              flex-shrink: 0;
              
              &.bg-0 { background: linear-gradient(135deg, #409eff, #79bbff); }
              &.bg-1 { background: linear-gradient(135deg, #67c23a, #95d475); }
              &.bg-2 { background: linear-gradient(135deg, #e6a23c, #f3d19e); }
              &.bg-3 { background: linear-gradient(135deg, #f56c6c, #fab6b6); }
              &.bg-4 { background: linear-gradient(135deg, #909399, #c8c9cc); }
          }
          
          .topic-info {
              flex: 1;
              overflow: hidden;
              
              h4 {
                margin: 0 0 8px 0;
                font-size: 18px;
                color: #303133;
                white-space: nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
              }

              .intro {
                margin: 0;
                color: #606266;
                font-size: 14px;
                line-height: 1.5;
                display: -webkit-box;
                -webkit-line-clamp: 3;
                -webkit-box-orient: vertical;
                overflow: hidden;
              }
          }
      }
      
      .card-footer {
          padding: 12px 20px;
          border-top: 1px solid #f2f6fc;
          display: flex;
          justify-content: space-between;
          align-items: center;
          transition: background-color 0.3s;
          
          .action-text {
              font-size: 13px;
              color: #909399;
              transition: color 0.3s;
          }
          
          .action-icon {
              color: #c0c4cc;
              transition: transform 0.3s;
          }
      }
    }
    
    .empty-tip {
        grid-column: 1 / -1;
        padding: 60px 0;
    }
  }
  
  .loading-state {
      padding: 20px;
  }
}
</style>
