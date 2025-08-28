<template>
  <div :class="['message-sidebar', { 'sidebar-open': isOpen }]">
    <!-- 侧边栏头部 -->
    <div class="sidebar-header">
      <h3>消息中心</h3>
      <div class="header-actions">
        <!-- 只有有消息且有选中时显示删除按钮 -->
        <el-button
          v-if="messageData.length > 0 && hasSelected"
          type="text"
          size="small"
          @click="deleteSelectedMessages"
          class="delete-btn"
        >
          删除所选
        </el-button>
        <el-button
          v-if="messageData.length > 0"
          type="text"
          size="small"
          @click="markAllAsRead"
        >
          全部已读
        </el-button>
      </div>
      <el-icon class="close-icon" @click="closeSidebar"><Close /></el-icon>
    </div>

    <!-- 侧边栏内容区 -->
    <div class="sidebar-content">
      <!-- 加载状态 -->
      <div v-if="loading" class="loading-state">
        <el-spinner size="small" />
        <span>加载中...</span>
      </div>

      <!-- 空状态 -->
      <div v-else-if="messageData.length === 0" class="empty-state">
        <el-icon><Message /></el-icon>
        <p>暂无消息</p>
        <p class="empty-hint">有新消息会在这里通知您~</p>
      </div>

      <!-- 消息列表（含全选栏） -->
      <div v-else class="message-container">
        <!-- 全选栏 -->
        <div class="select-all-bar">
          <el-checkbox
            v-model="isAllSelected"
            class="select-all-checkbox"
            :indeterminate="hasSelected && !isAllSelected"
          />
          <span class="select-all-text">全选</span>
        </div>

        <!-- 消息列表 -->
        <div class="message-list">
          <div
            v-for="message in messageData"
            :key="message.id"
            :class="[
              'message-item',
              { unread: message.readStatus === 0 },
              { selected: selectedMessageIds.includes(message.id) },
            ]"
            @mouseenter="isHoveringMessageId = message.id"
            @mouseleave="isHoveringMessageId = ''"
          >
            <!-- 消息复选框 -->
            <div class="message-checkbox">
              <el-checkbox
                v-model="selectedMessageIds"
                :value="message.id"
                :disabled="!message.id"
              />
            </div>

            <!-- 消息内容 -->
            <div
              class="message-content"
              @click.stop="handleMessageClick(message)"
            >
              <!-- 消息头部（时间+类型标签） -->
              <div class="message-header">
                <span class="send-time">{{
                  formatTime(message.createTime)
                }}</span>
                <el-tag
                  :type="
                    message.message.includes('已通过审核')
                      ? 'success'
                      : 'danger'
                  "
                  size="mini"
                  class="message-tag"
                >
                  {{
                    message.message.includes("已通过审核")
                      ? "审核通过"
                      : "审核未通过"
                  }}
                </el-tag>
              </div>
              <!-- 消息内容 -->
              <el-tooltip
                v-if="message.message.length > 30"
                :content="message.message"
                placement="right"
                effect="light"
                :visible="isHoveringMessageId === message.id"
              >
                <div class="message-text">
                  {{ truncateMessage(message.message) }}
                </div>
              </el-tooltip>
              <div v-else class="message-text">{{ message.message }}</div>
            </div>

            <!-- 未读红点 -->
            <div v-if="message.readStatus === 0" class="unread-dot"></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {
  ref,
  defineProps,
  defineEmits,
  onMounted,
  watch,
  computed,
  onUnmounted,
} from "vue";
import { Close, Message } from "@element-plus/icons-vue";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  selectMessageList,
  readMessage,
  deleteMessage,
} from "@/api/yonghuxiaoxiguanli";
import { useLoginUserStore } from "@/store/loginUser";

// 定义props
const props = defineProps<{
  isOpen: boolean;
}>();

// 定义emits
const emit = defineEmits<{
  (e: "close"): void;
  (e: "update:unreadCount", count: number): void;
  (e: "update:messages", messages: API.UserMessageVO[]): void;
}>();

// 状态管理
const messageData = ref<API.UserMessageVO[]>([]);
const loading = ref(false);
const loginUserStore = useLoginUserStore();
const selectedMessageIds = ref<string[]>([]);
const isHoveringMessageId = ref(""); // 用于控制tooltip显示

/**
 * 全选状态计算属性
 * - get: 判断是否全选（选中数量=消息总数且不为0）
 * - set: 全选/取消全选（同步选中ID列表）
 */
const isAllSelected = computed({
  get() {
    return (
      messageData.value.length > 0 &&
      selectedMessageIds.value.length === messageData.value.length
    );
  },
  set(checked) {
    if (checked) {
      // 全选：将所有消息ID加入选中列表
      selectedMessageIds.value = messageData.value
        .map((msg) => msg.id)
        .filter((id) => !!id) as string[];
    } else {
      // 取消全选：清空选中列表
      selectedMessageIds.value = [];
    }
  },
});

/**
 * 是否有选中消息（计算属性）
 */
const hasSelected = computed(() => selectedMessageIds.value.length > 0);

/**
 * 时间格式化（优化显示精度）
 * @param time - 原始时间字符串
 * @returns 格式化后的时间（刚刚/XX分钟前/XX小时前/XX天前/YYYY-MM-DD）
 */
const formatTime = (time: string) => {
  if (!time) return "未知时间";

  const date = new Date(time);
  const now = new Date();
  const diff = now.getTime() - date.getTime();
  const secondDiff = Math.floor(diff / 1000);
  const minuteDiff = Math.floor(secondDiff / 60);
  const hourDiff = Math.floor(minuteDiff / 60);
  const dayDiff = Math.floor(hourDiff / 24);

  // 小于30秒
  if (secondDiff < 30) {
    return "刚刚";
  }
  // 小于1小时
  if (hourDiff < 1) {
    return `${minuteDiff}分钟前`;
  }
  // 小于24小时
  if (dayDiff < 1) {
    return `${hourDiff}小时前`;
  }
  // 小于30天
  if (dayDiff < 30) {
    return `${dayDiff}天前`;
  }
  // 超过30天（补零优化）
  return `${date.getFullYear()}-${(date.getMonth() + 1)
    .toString()
    .padStart(2, "0")}-${date.getDate().toString().padStart(2, "0")}`;
};

/**
 * 截断长消息
 */
const truncateMessage = (message: string) => {
  return message.length > 30 ? message.substring(0, 30) + "..." : message;
};

/**
 * 获取消息列表（优化错误提示）
 */
const getMessages = async () => {
  loading.value = true;
  try {
    const userId = loginUserStore.loginUser?.id;
    if (!userId) {
      ElMessage.warning("请先登录");
      loading.value = false;
      return;
    }

    const res = await selectMessageList({ userId });
    if (res.data.code === 200) {
      messageData.value = res.data.data || [];
      updateUnreadCount();
      emit("update:messages", messageData.value);
    } else {
      ElMessage.error(`获取消息失败：${res.data.msg || "未知错误"}`);
    }
  } catch (error) {
    console.error("获取消息失败:", error);
    ElMessage.error("网络异常，获取消息失败");
  } finally {
    loading.value = false;
  }
};

/**
 * 更新未读数量
 */
const updateUnreadCount = () => {
  const count = messageData.value.filter((msg) => msg.readStatus === 0).length;
  emit("update:unreadCount", count);
};

/**
 * 处理消息点击（优化加载状态提示）
 * @param message - 点击的消息对象
 */
const handleMessageClick = async (message: API.UserMessageVO) => {
  if (message.readStatus === 1) return; // 已读消息无需处理

  try {
    const res = await readMessage([message.id]);
    if (res.data.code === 200) {
      message.readStatus = 1;
      updateUnreadCount();
      emit("update:messages", messageData.value);
    } else {
      ElMessage.error(`标记失败：${res.data.msg || "操作异常"}`);
    }
  } catch (error) {
    console.error("标记消息已读失败:", error);
    ElMessage.error("网络异常，标记失败");
  }
};

/**
 * 全部已读（优化空判断）
 */
const markAllAsRead = async () => {
  const unreadMessages = messageData.value.filter(
    (msg) => msg.readStatus === 0
  );
  if (unreadMessages.length === 0) {
    ElMessage.info("暂无未读消息");
    return;
  }

  try {
    const unreadIds = unreadMessages
      .map((msg) => msg.id)
      .filter((id) => !!id) as string[];
    const res = await readMessage(unreadIds);
    if (res.data.code === 200) {
      messageData.value.forEach((msg) => {
        if (unreadIds.includes(msg.id)) msg.readStatus = 1;
      });
      updateUnreadCount();
      emit("update:messages", messageData.value);
      ElMessage.success("所有消息已标记为已读");
    } else {
      ElMessage.error(`操作失败：${res.data.msg || "服务器异常"}`);
    }
  } catch (error) {
    console.error("标记全部已读失败:", error);
    ElMessage.error("网络异常，操作失败");
  }
};

/**
 * 删除所选消息（新增确认弹窗、优化状态同步）
 */
const deleteSelectedMessages = async () => {
  if (selectedMessageIds.value.length === 0) {
    ElMessage.warning("请选择要删除的消息");
    return;
  }

  try {
    // 新增删除确认
    await ElMessageBox.confirm(
      `确定要删除选中的${selectedMessageIds.value.length}条消息吗？`,
      "删除确认",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }
    );

    const res = await deleteMessage(selectedMessageIds.value);
    if (res.data.code === 200) {
      // 更新本地消息列表
      messageData.value = messageData.value.filter(
        (msg) => !selectedMessageIds.value.includes(msg.id)
      );
      selectedMessageIds.value = []; // 清空选中状态
      updateUnreadCount();
      emit("update:messages", messageData.value);
      ElMessage.success("删除成功");
    } else {
      ElMessage.error(`删除失败：${res.data.msg || "操作异常"}`);
    }
  } catch (error: any) {
    // 取消删除时不提示错误
    if (error.name !== "Cancel") {
      console.error("删除消息失败:", error);
      ElMessage.error("网络异常，删除失败");
    }
  }
};

/**
 * 关闭侧边栏
 */
const closeSidebar = () => {
  emit("close");
  selectedMessageIds.value = []; // 关闭时清空选中状态
};

/**
 * 监听侧边栏开关状态
 */
watch(
  () => props.isOpen,
  (newVal) => {
    if (newVal) {
      getMessages();
    } else {
      selectedMessageIds.value = []; // 关闭时重置选中
    }
  }
);

/**
 * 监听用户登录状态变化
 */
watch(
  () => loginUserStore.loginUser,
  (newUser) => {
    if (newUser && props.isOpen) {
      getMessages();
    } else {
      messageData.value = [];
      selectedMessageIds.value = [];
    }
  },
  { immediate: true }
);

/**
 * 组件生命周期
 */
onMounted(() => {
  if (props.isOpen) getMessages();
});

onUnmounted(() => {
  // 清理操作
});
</script>

<style scoped>
/* 基础布局优化 */
.message-sidebar {
  position: fixed;
  top: 0;
  right: 0;
  width: 380px; /* 加宽20px提升舒适度 */
  height: 100vh;
  background: #fff;
  box-shadow: -2px 0 12px rgba(0, 0, 0, 0.12); /* 加深阴影提升层次感 */
  z-index: 1000;
  transform: translateX(100%);
  transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1); /* 优化过渡曲线 */
  display: flex;
  flex-direction: column;
}

.sidebar-open {
  transform: translateX(0);
}

/* 头部样式优化 */
.sidebar-header {
  padding: 16px 20px;
  border-bottom: 1px solid #f5f5f5; /* 加深边框色 */
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #fafafa; /* 头部浅背景区分 */
}

.sidebar-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 500;
  color: #1f2937; /* 加深文字色提升对比度 */
}

.header-actions {
  display: flex;
  gap: 16px; /* 加大间距 */
  margin-right: 8px;
}

.delete-btn {
  color: #f56c6c; /* 删除按钮红色强调 */
}

.delete-btn:hover {
  color: #e64942; /* hover加深 */
}

/* 关闭按钮优化 */
.close-icon {
  cursor: pointer;
  color: #6b7280;
  font-size: 18px;
  transition: all 0.2s;
  padding: 4px;
  border-radius: 4px;
}

.close-icon:hover {
  color: #1f2937;
  background-color: #f3f4f6;
}

/* 内容区样式 */
.sidebar-content {
  flex: 1;
  overflow-y: auto;
  padding: 12px;
  background-color: #f9fafb; /* 内容区浅背景 */
}

/* 加载状态优化 */
.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 200px; /* 固定高度避免跳动 */
  color: #6b7280;
  gap: 8px;
}

/* 空状态优化 */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 200px;
  color: #6b7280;
  gap: 8px;
}

.empty-state .el-icon {
  font-size: 48px;
  margin-bottom: 12px;
  opacity: 0.25; /* 降低图标透明度 */
}

.empty-hint {
  font-size: 12px;
  color: #9ca3af;
  margin-top: 4px;
}

/* 消息容器（含全选栏） */
.message-container {
  gap: 8px;
  display: flex;
  flex-direction: column;
}

/* 全选栏样式 */
.select-all-bar {
  display: flex;
  align-items: center;
  padding: 8px 15px;
  border-radius: 6px;
  background-color: #fff;
  margin-bottom: 4px;
}

.select-all-checkbox {
  margin-right: 8px;
}

.select-all-text {
  font-size: 13px;
  color: #6b7280;
}

/* 消息列表样式 */
.message-list {
  display: flex;
  flex-direction: column;
  gap: 6px; /* 消息项间距 */
}

/* 消息项核心样式优化 */
.message-item {
  padding: 12px 15px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease; /* 优化过渡效果 */
  display: flex;
  align-items: flex-start;
  background-color: #fff;
  border: 1px solid transparent; /* 透明边框避免跳动 */
}

/* 鼠标悬浮效果 */
.message-item:hover {
  background-color: #f3f4f6;
  border-color: #e5e7eb;
}

/* 未读消息样式 */
.message-item.unread {
  background-color: #f0f7ff;
  border-left: 3px solid #409eff; /* 左侧蓝边强调 */
}

/* 选中消息样式 */
.message-item.selected {
  background-color: #e6f4ff;
  border-color: #91c9ff;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
}

/* 复选框位置优化 */
.message-checkbox {
  margin-right: 10px;
  margin-top: 3px; /* 垂直居中调整 */
}

/* 消息内容区 */
.message-content {
  flex: 1;
  overflow: hidden;
}

/* 消息头部（时间+标签） */
.message-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 6px;
}

/* 时间样式优化 */
.send-time {
  font-size: 12px;
  color: #9ca3af;
}

/* 消息标签样式 */
.message-tag {
  margin-left: 8px;
  padding: 1px 6px;
  height: 18px;
  line-height: 16px;
}

/* 消息内容样式 */
.message-text {
  font-size: 13px;
  color: #374151;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  line-height: 1.5; /* 优化行高 */
}

/* 未读红点优化 */
.unread-dot {
  width: 8px;
  height: 8px;
  background-color: #f56c6c;
  border-radius: 50%;
  margin-left: 8px;
  margin-top: 8px;
  box-shadow: 0 0 0 2px rgba(245, 108, 108, 0.2); /* 红点阴影提升视觉感 */
}

/* 响应式优化（小屏幕占满宽度） */
@media (max-width: 768px) {
  .message-sidebar {
    width: 100%;
    box-shadow: -2px 0 20px rgba(0, 0, 0, 0.15);
  }

  .sidebar-content {
    padding: 8px;
  }

  .message-item {
    padding: 10px 12px;
  }
}

/* 滚动条美化 */
.sidebar-content::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

.sidebar-content::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.sidebar-content::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.sidebar-content::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>
