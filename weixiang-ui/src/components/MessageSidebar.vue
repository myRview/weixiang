<template>
  <div :class="['message-sidebar', { 'sidebar-open': isOpen }]">
    <div class="sidebar-header">
      <h3>消息中心</h3>
      <div class="header-actions">
        <el-button v-if="hasSelected" type="text" size="small" @click="deleteSelectedMessages">删除所选</el-button>
        <el-button type="text" size="small" @click="markAllAsRead">全部已读</el-button>
      </div>
      <el-icon class="close-icon" @click="closeSidebar"><Close /></el-icon>
    </div>
    <div class="sidebar-content">
      <div v-if="loading" class="loading-state">
        <el-spinner size="small" />
        <span>加载中...</span>
      </div>
      <div v-else-if="messageData.length === 0" class="empty-state">
        <el-icon><Message /></el-icon>
        <p>暂无消息</p>
      </div>
      <div v-else class="message-list">
        <div
          v-for="message in messageData"
          :key="message.id"
          :class="['message-item', { 'unread': message.readStatus === 0 }]"
        >
          <div class="message-checkbox">
            <el-checkbox
              v-model="selectedMessageIds"
              :value="message.id" 
            />
          </div>
          <div class="message-content" @click.stop="handleMessageClick(message)">
            <div class="message-sender">
              <span class="send-time">{{ formatTime(message.createTime) }}</span>
            </div>
            <div class="message-text">{{ message.message }}</div>
          </div>
          <div v-if="message.readStatus === 0" class="unread-dot"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, defineProps, defineEmits, onMounted, watch, computed } from "vue";
import { Close, Message } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
import { selectMessageList, readMessage, deleteMessage } from "@/api/yonghuxiaoxiguanli";
import { useLoginUserStore } from "@/store/loginUser";

// 定义props
const props = defineProps<{
  isOpen: boolean;
}>();

// 定义emits
const emit = defineEmits<{
  (e: 'close'): void;
  (e: 'update:unreadCount', count: number): void;
  (e: 'update:messages', messages: API.UserMessageVO[]): void;  // 新增：同步消息数据
}>();

// 引用
const messageData = ref<API.UserMessageVO[]>([]);
const loading = ref(false);
const loginUserStore = useLoginUserStore();
const selectedMessageIds = ref<string[]>([]);

// 计算属性，检查是否有选中的消息
const hasSelected = computed(() => selectedMessageIds.value.length > 0);

// 格式化时间
const formatTime = (time: string) => {
  const date = new Date(time);
  const now = new Date();
  const diff = now.getTime() - date.getTime();

  // 小于1分钟
  if (diff < 60000) {
    return '刚刚';
  }
  // 小于1小时
  if (diff < 3600000) {
    return Math.floor(diff / 60000) + '分钟前';
  }
  // 小于24小时
  if (diff < 86400000) {
    return Math.floor(diff / 3600000) + '小时前';
  }
  // 小于30天
  if (diff < 2592000000) {
    return Math.floor(diff / 86400000) + '天前';
  }
  // 超过30天
  return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`;
};

// 获取消息列表
const getMessages = async () => {
  loading.value = true;
  try {
    const res = await selectMessageList({
      userId: loginUserStore.loginUser.id,
    });
    if (res.data.code === 200) {
      messageData.value = res.data.data || [];
      // 计算未读消息数量
      const unreadCount = messageData.value.filter(msg => msg.readStatus === 0).length;
      emit('update:unreadCount', unreadCount);
      emit('update:messages', messageData.value);  // 同步消息数据到父组件
    }
  } catch (error) {
    console.error('获取消息失败:', error);
  } finally {
    loading.value = false;
  }
};


// 处理消息点击
const handleMessageClick = async (message: API.UserMessageVO) => {
  // 如果是未读消息，更新状态
  if (message.readStatus === 0) {
    try {
      // 调用API标记为已读
      const res = await readMessage([message.id]);
      if (res.data.code === 200) {
        // 更新本地消息状态
        message.readStatus = 1;
        // 更新未读数量
        const unreadCount = messageData.value.filter(msg => msg.readStatus === 0).length;
        emit('update:unreadCount', unreadCount);
        emit('update:messages', messageData.value);  // 同步消息数据
        ElMessage.success('消息已标记为已读');
      } else {
        ElMessage.error('标记消息为已读失败');
      }
    } catch (error) {
      console.error('标记消息为已读失败:', error);
      ElMessage.error('标记消息为已读失败');
    }
  }
};

// 全部已读
const markAllAsRead = async () => {
  try {
    const unreadMessages = messageData.value.filter(msg => msg.readStatus === 0);
    if (unreadMessages.length === 0) {
      return;
    }

    const unreadIds = unreadMessages.map(msg => msg.id);
    const res = await readMessage(unreadIds);

    if (res.data.code === 200) {
      // 更新本地消息状态
      messageData.value.forEach(msg => {
        if (unreadIds.includes(msg.id)) {
          msg.readStatus = 1;
        }
      });
      // 更新未读数量
      const unreadCount = messageData.value.filter(msg => msg.readStatus === 0).length;
      emit('update:unreadCount', unreadCount);
      emit('update:messages', messageData.value);  // 同步消息数据
      ElMessage.success('已将所有消息标记为已读');
    }
  } catch (error) {
    ElMessage.error('标记全部已读失败');
  }
};

// 删除所选消息
const deleteSelectedMessages = async () => {
  if (selectedMessageIds.value.length === 0) {
    ElMessage.warning('请选择要删除的消息');
    return;
  }

  try {
    const res = await deleteMessage(selectedMessageIds.value);  // 修复：传递选中的ID
    if (res.data.code === 200) {
      // 重新获取消息列表
      await getMessages();
      // 清空选中状态
      selectedMessageIds.value = [];
      ElMessage.success('删除消息成功');
    }
  } catch (error) {
    ElMessage.error('删除消息失败');
  }
};

// 关闭侧边栏
const closeSidebar = () => {
  emit('close');
};

// 监听isOpen变化
watch(() => props.isOpen, (newVal) => {
  if (newVal) {
    getMessages();
  }
});

// 初始加载
onMounted(() => {
  if (props.isOpen) {
    getMessages();
  }
});
</script>

<style scoped>
/* 样式保持不变 */
.message-sidebar {
  position: fixed;
  top: 0;
  right: 0;
  width: 360px;
  height: 100vh;
  background: #fff;
  box-shadow: -2px 0 10px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  transform: translateX(100%);
  transition: transform 0.3s ease;
  display: flex;
  flex-direction: column;
}

.sidebar-open {
  transform: translateX(0);
}

.sidebar-header {
  padding: 16px 20px;
  border-bottom: 1px solid #f0f0f0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.sidebar-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 500;
  color: #303133;
}

.header-actions {
  display: flex;
  gap: 10px;
  margin-right: 10px;
}

.message-checkbox {
  margin-right: 10px;
  margin-top: 12px;
}

.close-icon {
  cursor: pointer;
  color: #909399;
  font-size: 18px;
  transition: color 0.2s;
}

.close-icon:hover {
  color: #303133;
}

.sidebar-content {
  flex: 1;
  overflow-y: auto;
  padding: 10px;
}

.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #909399;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #909399;
}

.empty-state .el-icon {
  font-size: 48px;
  margin-bottom: 16px;
  opacity: 0.3;
}

.message-list {
  padding: 5px 0;
}

.message-item {
  padding: 12px 15px;
  border-radius: 8px;
  margin-bottom: 8px;
  cursor: pointer;
  transition: background-color 0.2s;
  display: flex;
  align-items: flex-start;
}

.message-item:hover {
  background-color: #f5f7fa;
}

.message-item.unread {
  background-color: #f0f5ff;
}

.message-avatar {
  margin-right: 12px;
}

.message-content {
  flex: 1;
  overflow: hidden;
}

.message-sender {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 4px;
}

.sender-name {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
}

.send-time {
  font-size: 12px;
  color: #909399;
}

.message-text {
  font-size: 13px;
  color: #606266;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.unread-dot {
  width: 8px;
  height: 8px;
  background-color: #f56c6c;
  border-radius: 50%;
  margin-left: 8px;
  margin-top: 8px;
}

.sidebar-mask {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.3);
  z-index: 999;
}

@media (max-width: 768px) {
  .message-sidebar {
    width: 100%;
  }
}
</style>