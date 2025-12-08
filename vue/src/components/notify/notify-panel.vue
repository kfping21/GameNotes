<template>
  <div class="notify-panel">
    <div class="notify-toggle" @click.stop="toggleDrawer" role="button" tabindex="0">
      <i class="fa fa-bell" aria-hidden="true"></i>
      <span v-if="totalCount" class="badge">{{ totalCount }}</span>
    </div>

    <el-dialog class="notify-dialog" v-model="visibleLocal" width="78%" center>
      <template #title>
        <div style="display:flex; justify-content:space-between; align-items:center; width:100%;">
          <div style="font-weight:600">通知</div>
          <div>
            <el-button type="text" size="small" @click="markAllRead">全部标为已读</el-button>
            <el-button type="text" size="small" @click="visibleLocal = false">关闭</el-button>
          </div>
        </div>
      </template>

      <div class="notify-dialog-body" style="display:flex; height:60vh;">
        <div class="notify-list-column" style="width:320px; border-right:1px solid #eee; overflow:auto; padding:10px;">
          <el-empty v-if="!lists.length" description="暂无通知"></el-empty>
          <div v-else>
            <div v-for="item in lists" :key="item.id" class="notify-item" @click="openItem(item)" :style="{background: currentItem && currentItem.id===item.id ? '#f5f7fa' : '#fff'}">
              <div style="display:flex; justify-content:space-between; align-items:center">
                <div class="left-type">{{ typeLabel(item.type) }}</div>
                <div class="time">{{ item.addtime ? item.addtime.substring(0,19) : '' }}</div>
              </div>
              <div class="content" v-html="item.tongzhineirong"></div>
            </div>
          </div>
        </div>

        <div class="notify-detail-column" style="flex:1; padding:18px; overflow:auto;">
          <template v-if="view === 'detail' && currentItem">
            <div style="font-weight:700; margin-bottom:8px">{{ typeLabel(currentItem.type) }}</div>
            <div style="color:#999; margin-bottom:10px">{{ currentItem.addtime ? currentItem.addtime.substring(0,19) : '' }}</div>
            <div v-html="currentItem.tongzhineirong"></div>
          </template>
          <template v-else>
            <div style="color:#666">请选择左侧通知以查看详情</div>
          </template>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import http from '@/utils/ajax/http'
import { session } from '@/utils/utils'
import { ElMessage } from 'element-plus'
import { useTixingStore } from '@/stores/tixing'
import { useRouter } from 'vue-router'

const store = useTixingStore()
const router = useRouter()


// local visible ref synchronized with store.showDialog (safer across Element Plus versions)
const visibleLocal = ref(!!store.showDialog)
// sync store -> local
watch(
  () => store.showDialog,
  (v) => {
    visibleLocal.value = !!v
  }
)
// sync local -> store
watch(
  visibleLocal,
  (v) => {
    store.showDialog = !!v
  }
)

// 当对话框打开时，确保拉取最新通知
async function loadNotifications(){
  try{
    // 如果 store 已有数据则不重复拉取
    if(store.lists && store.lists.length>0) return;
    const username = session('username')
    if(!username) return;
    const search = { yonghu: username, page:1, pagesize: 50 }
    const res = await http.post('/api/tongzhi/selectYonghu', search)
    if(res && res.code==0){
      const data = res.data
      const records = (data.lists && data.lists.records) || []
      // newest first
      const ordered = records.slice().reverse()
      store.lists = ordered
    }else{
      console.warn('notify-panel: fetch notifications failed', res)
    }
  }catch(err){
    console.error('notify-panel: loadNotifications error', err)
    ElMessage.error('加载通知失败')
  }
}

watch(visibleLocal, (v)=>{ if(v) loadNotifications() })

const lists = computed(() => store.lists)
// badge shows number of unread notifications
const totalCount = computed(() => (store.lists || []).filter(i => i.issh !== '是').length)

// debug watch: log visible changes
watch(
  () => store.showDialog,
  (v) => {
    console.log('notify-panel: store.showDialog changed ->', v)
  }
)

// 当前在抽屉中显示的视图： 'list' | 'detail'
const view = ref('list')
const currentItem = ref(null)

function toggleDrawer(){
  // toggle shared visible state
  visibleLocal.value = !visibleLocal.value
  if(visibleLocal.value){
    view.value = 'list'
    currentItem.value = null
  }
}

function openItem(item){
  // 在抽屉内显示详情，不跳转路由
  currentItem.value = item
  view.value = 'detail'
  // 标记该条为已读并同步到后端（如果尚未已读）
  if(item && item.issh !== '是'){
    item.issh = '是'
    // 更新 store 中对应项（item 是引用，已生效）
    try{ canMarkRead(item) }catch(e){ console.error(e) }
  }
}

function goToList(){
  view.value = 'list'
  currentItem.value = null
  visibleLocal.value = true
}

function markAllRead(){
  // 将 store 中的通知标为已读并调用后端接口
  const listsCopy = [...store.lists]
  listsCopy.forEach(ci => {
    if(ci.issh !== '是'){
      ci.issh = '是'
      // 后端更新，忽略返回
      canMarkRead(ci)
    }
  })
  // 清空前端列表或保留已读逻辑——这里清空以示已读
  store.lists = []
}

import { canTongzhiUpdate } from '@/module'
function canMarkRead(item){
  // 保持与原来一致的接口调用
  return canTongzhiUpdate(item).then(()=>{}).catch(()=>{})
}

function typeLabel(t){
  if(!t) return '通知'
  const map = { zan: '赞', shoucang: '收藏', guanzhu: '关注', pinglun: '评论', zhongcao: '种草' }
  return map[t] || t
}
</script>

<style scoped>
.notify-panel{
  position: fixed;
  left: 8px;
  top: 80px;
  z-index: 99999;
  pointer-events: auto;
}
.notify-toggle{
  cursor: pointer;
  background: #fff;
  padding: 8px 10px;
  border-radius: 20px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  display: flex;
  align-items: center;
}
.notify-toggle .fa{ font-size: 18px; color: #1890ff }
.badge{
  background: #f56c6c;
  color: #fff;
  border-radius: 10px;
  padding: 2px 6px;
  margin-left: 6px;
  font-size: 12px;
}
.notify-list{ padding: 8px }
.notify-item{ border-bottom: 1px solid #f0f0f0; padding: 8px 4px; cursor: pointer }
.notify-item .left-type{ font-weight: 600; color: #333 }
.notify-item .content{ color:#666; margin-top:4px }
.notify-item .time{ font-size:12px; color:#999; margin-top:6px }
</style>
