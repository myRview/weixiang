<template>
  <div class="user-home">
    <!-- 用户信息区域 -->
    <div class="user-info-container">
      <div class="avatar-container">
        <el-avatar
          :src="user?.avatar ? `${baseURL}${user?.avatar}` : defaultAvatar"
          size="large"
          class="user-avatar"
        ></el-avatar>
      </div>
      <div class="user-basic-info">
        <span class="user-name">{{ user?.userName || user?.account }}</span>
        <p class="user-bio">{{ user?.bio || "暂无简介" }}</p>
        <div class="user-stats">
          <div class="stat-item">
            <span class="stat-count">{{ total }}</span>
            <span class="stat-label">文章</span>
          </div>
          <div class="stat-item">
            <span class="stat-count">{{ following.length }}</span>
            <span class="stat-label">关注</span>
          </div>
          <div class="stat-item">
            <span class="stat-count">{{ followers.length }}</span>
            <span class="stat-label">粉丝</span>
          </div>
        </div>
      </div>
      <div class="user-actions-wrapper">
        <div class="follow-button-container" v-if="showFollowBtn">
          <el-button
            v-if="isFollowing"
            type="default"
            size="medium"
            @click="toggleFollow(user?.id, 0)"
            class="follow-button"
          >
            已关注
          </el-button>
          <el-button
            v-else
            type="primary"
            size="medium"
            @click="toggleFollow(user?.id, 1)"
            class="follow-button"
          >
            关注
          </el-button>
        </div>
        <div class="user-actions-container" v-if="!showFollowBtn">
          <el-button
            type="primary"
            size="medium"
            @click="navigateToPersonalCenter"
            class="follow-button"
          >
            个人中心
          </el-button>
          <el-button
            type="primary"
            size="medium"
            @click="navigateToMemberCenter"
            class="follow-button"
          >
            会员中心
          </el-button>
        </div>
      </div>
    </div>

    <!-- 标签页区域 -->
    <div class="tabs-container">
      <div class="tabs">
        <div
          class="tab-item"
          :class="{ active: activeTab === 'articles' }"
          @click="switchTab('articles')"
        >
          我的文章
        </div>
        <div
          class="tab-item"
          :class="{ active: activeTab === 'follow' }"
          @click="switchTab('follow')"
        >
          关注/粉丝
        </div>
      </div>

      <!-- 标签页内容 -->
      <div class="tab-content">
        <!-- 文章列表 -->
        <div v-if="activeTab === 'articles'" class="articles-list">
          <div v-if="loadingArticles" class="loading-indicator">加载中...</div>
          <div v-else-if="articles.length === 0" class="empty-state">
            <div class="empty-state-content">
              <div class="empty-icon">
                <svg
                  width="48"
                  height="48"
                  viewBox="0 0 1024 1024"
                  xmlns="http://www.w3.org/2000/svg"
                  class="icon"
                >
                  <path
                    d="M832 256H768v-64c0-35.3 28.7-64 64-64h32c35.3 0 64 28.7 64 64v64h-64zm-64 0v-64h42.7v64h-42.7zM448 832H192V192h256v640zM256 256v512h128V256H256zm400 576H512V256h144v64h64v512zM640 320v448h64V320h-64zm160 0v448h96V320h-96zM160 192v640H64V192h96m-21.3 0H64c-35.3 0-64 28.7-64 64v640c0 35.3 28.7 64 64 64h96c35.3 0 64-28.7 64-64V256c0-35.3-28.7-64-64-64h-21.3zM960 832H832V256h128v576m-21.3 0H832V256c0-35.3 28.7-64 64-64h21.3c35.3 0 64 28.7 64 64v576c0 35.3-28.7 64-64 64z"
                    fill="#E5E6EB"
                  />
                </svg>
              </div>
              <div class="empty-text">暂无文章</div>
              <el-button
                type="primary"
                size="large"
                class="publish-button"
                @click="navigateToPublish"
                :icon="EditPen"
              >
                写文章
              </el-button>
            </div>
          </div>
          <div
            v-else
            class="article-item"
            v-for="article in articles"
            :key="article.id"
            @click="navigateToArticleDetail(article.id)"
            style="cursor: pointer"
          >
            <h3 class="article-title">{{ article.title }}</h3>
            <div class="article-meta">
              <span class="article-date">{{
                formatDate(article.createTime || new Date())
              }}</span>
            </div>
          </div>
        </div>

        <!-- 关注/粉丝区域 -->
        <div v-else-if="activeTab === 'follow'" class="follow-fans-container">
          <div class="sub-tabs">
            <div
              class="sub-tab-item"
              :class="{ active: followTab === 'following' }"
              @click="switchFollowTab('following')"
            >
              关注
            </div>
            <div
              class="sub-tab-item"
              :class="{ active: followTab === 'followers' }"
              @click="switchFollowTab('followers')"
            >
              粉丝
            </div>
          </div>

          <div class="follow-content">
            <!-- 关注列表 -->
            <div v-if="followTab === 'following'" class="following-list">
              <div v-if="loadingFollowing" class="loading-indicator">
                加载中...
              </div>
              <div v-else-if="following.length === 0" class="empty-state">
                暂无关注
              </div>
              <div
                v-else
                class="user-card"
                v-for="user in following"
                :key="user.id"
                @click="navigateToUserDetail(user.id)"
                style="cursor: pointer"
              >
                <img
                  :src="user.avatar ? `${baseURL}${user.avatar}` : defaultAvatar"
                  alt="用户头像"
                  class="card-avatar"
                />
                <div class="card-info">
                  <h4 class="card-username">{{ user.userName }}</h4>
                  <p class="card-bio">{{ user.bio || "暂无简介" }}</p>
                </div>
                <el-button
                  v-if="user.id !== loginUser?.id"
                  :type="isUserFollowing(user.id) ? 'default' : 'primary'"
                  size="medium"
                  class="follow-button"
                  @click.stop="
                    toggleFollow(user.id, isUserFollowing(user.id) ? 0 : 1)
                  "
                >
                  {{ isUserFollowing(user.id) ? "已关注" : "关注" }}
                </el-button>
              </div>
            </div>

            <!-- 粉丝列表 -->
            <div v-else-if="followTab === 'followers'" class="followers-list">
              <div v-if="loadingFollowers" class="loading-indicator">
                加载中...
              </div>
              <div v-else-if="followers.length === 0" class="empty-state">
                暂无粉丝
              </div>
              <div
                v-else
                class="user-card"
                v-for="user in followers"
                :key="user.id"
                @click="navigateToUserDetail(user.id)"
                style="cursor: pointer"
              >
                <img
                  :src="user.avatar ? `${baseURL}${user.avatar}` : defaultAvatar"
                  alt="用户头像"
                  class="card-avatar"
                />
                <div class="card-info">
                  <h4 class="card-username">{{ user.userName }}</h4>
                  <p class="card-bio">{{ user.bio || "暂无简介" }}</p>
                </div>
                <el-button
                  v-if="user.id !== loginUser?.id"
                  :type="isUserFollowing(user.id) ? 'default' : 'primary'"
                  size="medium"
                  class="follow-button"
                  @click.stop="
                    toggleFollow(user.id, isUserFollowing(user.id) ? 0 : 1)
                  "
                >
                  {{ isUserFollowing(user.id) ? "已关注" : "关注" }}
                </el-button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { getUserById } from "@/api/yonghuguanli";
import { ElMessage } from "element-plus";
import { ref, onMounted, watch, computed } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useLoginUserStore } from "@/store/loginUser";
import dayjs from "dayjs";
import { selectArticlePageByAuthor } from "@/api/wenzhangguanli";
import { EditPen } from "@element-plus/icons-vue";
import { changStatus, fansList, list, status } from "@/api/guanzhuguanli";
import { baseURL } from "@/request";
const formatDate = (date: string | number | Date) => {
  return dayjs(date).format("YYYY-MM-DD HH:mm:ss");
};
const user = ref<API.UserVO>();
const articles = ref<API.ArticleVO[]>([]);
const total = ref(0);
const following = ref<API.UserVO[]>([]);
const followers = ref<API.UserVO[]>([]);
const activeTab = ref<"articles" | "follow">("articles");
const followTab = ref<"following" | "followers">("following");
const loadingArticles = ref(false);
const loadingFollowing = ref(false);
const loadingFollowers = ref(false);
const defaultAvatar =
  "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png";
const loginUserStore = useLoginUserStore();
const loginUser = loginUserStore.loginUser;
const showFollowBtn = computed(() => {
  // 不显示自己的关注按钮
  return user.value?.id !== loginUser?.id;
});
// 关注状态
const isFollowing = ref(false);
// 新增：跟踪所有用户的关注状态
const followStatusMap = ref<Record<number, boolean>>({});

// 新增：判断是否关注了某个用户
const isUserFollowing = (userId: number) => {
  return followStatusMap.value[userId] || false;
};
const navigateToPersonalCenter = () => {
  router.push("/profile");
};
const navigateToMemberCenter = () => {
  router.push("/member-center");
};

// 新增：刷新关注状态映射表
const refreshFollowStatusMap = async () => {
  if (!loginUser?.id) return;

  try {
    // 获取当前登录用户的所有关注
    const res = await list({ userId: loginUser.id });
    if (res.data.code === 200) {
      const myFollowing = res.data.data || [];
      const statusMap: Record<number, boolean> = {};

      // 标记已关注的用户
      myFollowing.forEach((user) => {
        statusMap[user.id] = true;
      });

      followStatusMap.value = statusMap;
    }
  } catch (error) {
    console.error("刷新关注状态映射表失败", error);
  }
};

// 修复：切换关注状态方法
const toggleFollow = async (userId: number, status: number) => {
  if (!userId || !loginUser?.id) return;

  // 防止重复点击
  const buttonEl = document.activeElement as HTMLButtonElement;
  if (buttonEl) buttonEl.disabled = true;

  try {
    const res = await changStatus({ userId: userId, status: status });
    if (res.data.code !== 200) {
      ElMessage.error(res.data.message || "操作失败");
      return;
    }

    ElMessage.success(status === 1 ? "关注成功" : "取消关注成功");

    // 立即更新状态映射表
    followStatusMap.value[userId] = status === 1;

    // 如果是当前页面用户的关注状态变更，同步更新
    if (userId === Number(route.query.id)) {
      isFollowing.value = status === 1;
    }

    // 重新加载列表数据
    await Promise.all([getFlowList(), getFollowerList()]);
    getFlowStatus();
  } catch (error) {
    ElMessage.error("操作失败，请重试");
    console.error("关注操作失败", error);
  } finally {
    if (buttonEl) buttonEl.disabled = false;
  }
};

const route = useRoute();
const router = useRouter();

// 跳转到发布文章页面
const navigateToPublish = () => {
  router.push("edit-article");
};

// 跳转到文章详情页
const navigateToArticleDetail = (articleId: number) => {
  router.push({
    path: "/article-detail",
    query: { id: articleId },
  });
};

// 跳转到用户详情页
const navigateToUserDetail = (userId: number) => {
  router.push({
    path: "/user-home",
    query: { id: userId },
  });
};

const getUserInfo = async () => {
  const userId = route.query.id;
  try {
    if (userId) {
      const res = await getUserById({ id: userId });
      if (res.data.code === 200) {
        user.value = res.data.data;
      } else {
        ElMessage.error(res.data.message);
      }
    }
  } catch (error) {
    ElMessage.error("获取用户信息失败");
  }
};
const searchParam = ref<API.ArticleSearchParam>({
  pageNum: 1,
  pageSize: 10,
  userId: 0,
});
const fetchArticles = async () => {
  loadingArticles.value = true;
  try {
    const res = await selectArticlePageByAuthor({
      ...searchParam.value,
      auditStatus: 1,
      userId: route.query.id,
    });
    if (res.data.code === 200) {
      articles.value = res.data.data?.records || [];
      total.value = Number(res.data.data?.total) || 0;
    }
  } catch (error) {
    console.error("获取文章列表失败", error);
  } finally {
    loadingArticles.value = false;
  }
};

// 修复：切换标签页时确保加载数据
const switchTab = (tab: "articles" | "follow") => {
  activeTab.value = tab;
  if (tab === "articles" && articles.value.length === 0) {
    fetchArticles();
  } else if (tab === "follow") {
    // 确保加载关注/粉丝数据
    getFlowList();
    getFollowerList();
  }
};

// 修复：切换关注/粉丝子标签时确保加载数据
const switchFollowTab = (tab: "following" | "followers") => {
  followTab.value = tab;
  if (tab === "following") {
    getFlowList();
  } else {
    getFollowerList();
  }
};

const getFlowStatus = async () => {
  try {
    const targetUserId = route.query.id;
    if (targetUserId && loginUser?.id) {
      // 获取当前用户对目标用户的关注状态
      const res = await status({ userId: targetUserId });
      if (res.data.code === 200) {
        isFollowing.value = res.data.data;
      }
      // 初始化关注状态映射表
      await refreshFollowStatusMap();
    }
  } catch (error) {
    console.error("获取关注状态失败", error);
  }
};

// 修复：关注列表获取
const getFlowList = async () => {
  const targetUserId = route.query.id;
  if (!targetUserId) return;

  loadingFollowing.value = true;
  try {
    const res = await list({ userId: targetUserId });
    if (res.data.code === 200) {
      following.value = res.data.data || [];
      // 刷新关注状态映射
      await refreshFollowStatusMap();
    }
  } catch (error) {
    console.error("获取关注列表失败", error);
    ElMessage.error("获取关注列表失败");
  } finally {
    loadingFollowing.value = false;
  }
};

// 修复：粉丝列表获取
const getFollowerList = async () => {
  const targetUserId = route.query.id;
  if (!targetUserId) return;

  loadingFollowers.value = true;
  try {
    const res = await fansList({ userId: targetUserId });
    if (res.data.code === 200) {
      followers.value = res.data.data || [];
      // 刷新关注状态映射
      await refreshFollowStatusMap();
    }
  } catch (error) {
    console.error("获取粉丝列表失败", error);
    ElMessage.error("获取粉丝列表失败");
  } finally {
    loadingFollowers.value = false;
  }
};

// 监听路由变化，重新获取用户信息和文章列表
watch(
  () => route.query.id,
  async () => {
    await Promise.all([
      getUserInfo(),
      fetchArticles(),
      getFlowStatus(),
      getFlowList(),
      getFollowerList(),
    ]);
  }
);

// 初始化加载时增加错误处理
onMounted(async () => {
  try {
    await Promise.all([
      getUserInfo(),
      fetchArticles(),
      getFlowStatus(),
      getFlowList(),
      getFollowerList(),
    ]);
  } catch (error) {
    console.error("页面初始化失败", error);
  }
});
</script>

<style scoped>
.user-home {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

/* 用户信息区域样式 */
.user-info-container {
  background-color: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 30px 20px;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  margin-bottom: 30px;
}

.avatar-container {
  margin-right: 30px;
}

.user-avatar {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid #f5f5f5;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
}

.user-avatar:hover {
  transform: scale(1.05);
}

.user-basic-info {
  flex: 1;
  min-width: 0;
}

.user-name {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 8px;
  color: #333;
  display: block;
}

.user-bio {
  font-size: 14px;
  color: #666;
  margin-bottom: 15px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

/* 用户操作按钮容器样式 - 确保垂直对齐 */
.user-actions-wrapper {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 15px; /* 按钮组之间的垂直间距 */
}

.follow-button-container {
  display: flex;
  align-items: center;
}

.follow-button {
  min-width: 120px;
  padding: 10px 20px;
  border-radius: 50px;
  font-size: 16px;
  transition: all 0.3s ease;
}

.follow-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(66, 185, 131, 0.3);
}

/* 个人中心和会员中心按钮容器 */
.user-actions-container {
  display: flex;
  flex-direction: column;
  gap: 8px; /* 按钮之间的垂直间距 */
  align-items: flex-end; /* 按钮右对齐 */
}

.user-stats {
  display: flex;
  gap: 30px;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-count {
  font-size: 18px;
  font-weight: bold;
  color: #333;
}

.stat-label {
  font-size: 14px;
  color: #666;
}

/* 标签页样式 */
.tabs-container {
  margin-top: 20px;
}

.tabs {
  display: flex;
  border-bottom: 1px solid #eee;
}

.tab-item {
  padding: 10px 20px;
  cursor: pointer;
  font-size: 16px;
  color: #666;
  border-bottom: 2px solid transparent;
}

.tab-item.active {
  color: #333;
  font-weight: bold;
  border-bottom-color: #42b983;
}

/* 标签页内容样式 */
.tab-content {
  padding: 20px 0;
}

/* 文章列表样式 */
.articles-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.article-item {
  padding: 15px;
  border: 1px solid #eee;
  border-radius: 8px;
  transition: box-shadow 0.3s;
}

.article-item:hover {
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.article-title {
  font-size: 18px;
  margin-bottom: 8px;
  color: #333;
}

.article-excerpt {
  font-size: 14px;
  color: #666;
  margin-bottom: 10px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.article-meta {
  display: flex;
  gap: 15px;
  font-size: 12px;
  color: #999;
}

/* 关注/粉丝样式 */
.follow-fans-container {
  display: flex;
  flex-direction: column;
}

.sub-tabs {
  display: flex;
  margin-bottom: 20px;
  border-bottom: 1px solid #eee;
}

.sub-tab-item {
  padding: 8px 15px;
  cursor: pointer;
  font-size: 14px;
  color: #666;
  border-bottom: 2px solid transparent;
}

.sub-tab-item.active {
  color: #333;
  font-weight: bold;
  border-bottom-color: #42b983;
}

.user-card {
  display: flex;
  align-items: center;
  padding: 15px;
  border: 1px solid #eee;
  border-radius: 8px;
  margin-bottom: 15px;
  transition: box-shadow 0.3s;
}

.user-card:hover {
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.card-avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  object-fit: cover;
  margin-right: 15px;
}

.card-info {
  flex: 1;
}

.card-username {
  font-size: 16px;
  margin-bottom: 5px;
  color: #333;
}

.card-bio {
  font-size: 14px;
  color: #666;
}
</style>
