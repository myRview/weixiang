<template>
  <div class="article-detail-container">
    <!-- 文章标题 -->
    <div class="article-title">
      <h1>{{ articleDetail.title }}</h1>
      <div class="title-meta">
        <el-tag
          v-if="articleDetail.categoryId"
          size="small"
          class="category-tag"
        >
          {{ getCategoryName(articleDetail.categoryId) }}
        </el-tag>

        <!-- 标题后的标签 -->
        <div
          class="title-tags"
          v-if="articleDetail.tagIds && articleDetail.tagIds.length > 0"
        >
          <el-tag
            v-for="tag in articleDetail.tagIds"
            :key="tag"
            size="small"
            class="article-tag"
          >
            {{ getTagName(tag) }}
          </el-tag>
        </div>
      </div>
    </div>

    <!-- 文章信息 -->
    <div class="article-info">
      <div class="author-info">
        <el-avatar
          :size="32"
          :src="articleDetail.userAvatar || defaultAvatar"
          class="author-avatar"
          @click="handleAvatarClick(articleDetail.userId)"
          style="cursor: pointer"
        ></el-avatar>
        <div class="author-details">
          <span class="author-name">{{ articleDetail.userName }}</span>
          <span class="publish-time">{{
            formatDate(articleDetail.createTime || new Date())
          }}</span>
        </div>
        <div class="follow-btn-container">
          <el-button
            v-if="showFollowBtn"
            :type="isFollowing ? 'default' : 'primary'"
            size="small"
            @click="handleFollow"
            :loading="loadingFollow"
          >
            {{ isFollowing ? "已关注" : "关注" }}
          </el-button>
        </div>
      </div>

      <div class="article-stats">
        <span class="stat-item"
          ><i class="el-icon-view"></i>
          阅读量：
          {{ viewCount || 0 }}</span
        >
        <span class="stat-item"
          ><i class="el-icon-thumbs-up"></i>
          点赞数：
          {{ likeCount || 0 }}</span
        >
        <span class="stat-item"
          ><i class="el-icon-comment"></i>
          评论数：
          {{ commentCount || 0 }}</span
        >
      </div>
    </div>

    <!-- 文章内容 -->
    <div class="article-content" v-html="articleDetail.content"></div>

    <!-- 文章操作图标 -->
    <div class="article-action-bar">
      <div
        class="action-btn"
        @click="handleEdit"
        title="编辑"
        v-show="showEditDelete"
      >
        <svg
          t="1753931545287"
          class="icon"
          viewBox="0 0 1024 1024"
          version="1.1"
          xmlns="http://www.w3.org/2000/svg"
          p-id="6335"
        >
          <path
            d="M181.11 816.38a27.6 27.6 0 0 1-26.79-34.32l55.24-221a27.58 27.58 0 0 1 7.26-12.84l441.94-441.9a27.61 27.61 0 0 1 39.06 0L863.55 272a27.62 272a27.62 0 0 1 0 39.07L421.61 753a27.66 27.66 0 0 1-12.83 7.27l-221 55.24a27.51 27.51 0 0 1-6.7 0.83z m80.19-234.47l-42.22 168.88L388 708.57l417-417-126.71-126.66z"
            p-id="6336"
          ></path>
          <path
            d="M761.16 402.06a27.55 27.55 0 0 1-19.54-8.06L575.9 228.24a27.62 27.62 0 0 1 39.1-39.06l165.69 165.73a27.62 27.62 0 0 1-19.53 47.15zM402.08 761.13a27.53 27.53 0 0 1-19.53-8.13L216.82 587.32a27.63 27.63 0 0 1 39.07-39.07L421.61 714a27.62 27.62 0 0 1-19.53 47.15z m497.18 165.73H457.32a27.62 27.62 0 1 1 0-55.24h441.94a27.62 27.62 0 0 1 0 55.24z m-552.42 0h-221a27.62 27.62 0 1 1 0-55.24h221a27.62 27.62 0 0 1 0 55.24z"
            p-id="6337"
          ></path>
        </svg>
        <span>编辑</span>
      </div>

      <div
        class="action-btn danger"
        @click="handleDelete"
        title="删除"
        v-show="showEditDelete"
      >
        <svg
          t="1753931575604"
          class="icon"
          viewBox="0 0 1024 1024"
          version="1.1"
          xmlns="http://www.w3.org/2000/svg"
          p-id="7351"
        >
          <path
            d="M519.620465 0c-103.924093 0-188.511256 82.467721-192.083349 185.820279H85.015814A48.91386 48.91386 0 0 0 36.101953 234.686512a48.91386 48.91386 0 0 0 48.913861 48.866232h54.010046V831.345116c0 102.852465 69.822512 186.844279 155.909954 186.844279h439.200744c86.087442 0 155.909953-83.491721 155.909954-186.844279V284.100465h48.91386a48.91386 48.91386 0 0 0 48.913861-48.890046 48.91386 48.91386 0 0 0-48.913861-48.866233h-227.756651A191.559442 191.559442 0 0 0 519.620465 0z m-107.234232 177.080558c3.548279-49.771163 46.627721-88.540279 99.851907-88.540279 53.224186 0 96.327442 38.745302 99.351813 88.540279h-199.20372z m-111.997024 752.044651c-30.981953 0-65.083535-39.15014-65.083535-95.041488V287.744h575.488v546.839814c0 55.915163-34.077767 95.041488-65.059721 95.041488H300.389209v-0.500093z"
            fill="#D81E06"
            p-id="7352"
          ></path>
          <path
            d="M368.116093 796.814884c24.361674 0 44.27014-21.670698 44.27014-48.818605v-278.623256c0-27.147907-19.908465-48.818605-44.27014-48.818604-24.33786 0-44.27014 21.670698-44.27014 48.818604v278.623256c0 27.147907 19.360744 48.818605 44.293954 48.818605z m154.933581 0c24.361674 0 44.293953-21.670698 44.293954-48.818605v-278.623256c0-27.147907-19.932279-48.818605-44.293954-48.818604-24.33786 0-44.27014 21.670698-44.270139 48.818604v278.623256c0 27.147907 19.932279 48.818605 44.293953 48.818605z m132.810419 0c24.33786 0 44.27014-21.670698 44.27014-48.818605v-278.623256c0-27.147907-19.932279-48.818605-44.27014-48.818604s-44.27014 21.670698-44.27014 48.818604v278.623256c0 27.147907 19.360744 48.818605 44.27014 48.818605z"
            fill="#D81E06"
            p-id="7353"
          ></path>
        </svg>
        <span>删除</span>
      </div>

      <div
        class="action-btn"
        :class="{ liked: isLiked }"
        @click="handleLike"
        title="点赞"
        v-show="showLikeComment"
      >
        <svg
          t="1753931175791"
          class="icon"
          viewBox="0 0 1024 1024"
          version="1.1"
          xmlns="http://www.w3.org/2000/svg"
          p-id="2989"
        >
          <path
            d="M64 483.04V872c0 37.216 30.144 67.36 67.36 67.36H192V416.32l-60.64-0.64A67.36 67.36 0 0 0 64 483.04zM857.28 344.992l-267.808 1.696c12.576-44.256 18.944-83.584 18.944-118.208 0-78.56-68.832-155.488-137.568-145.504-60.608 8.8-67.264 61.184-67.264 126.816v59.264c0 76.064-63.84 140.864-137.856 148L256 416.96v522.4h527.552a102.72 102.72 0 0 0 100.928-83.584l73.728-388.96a102.72 102.72 0 0 0-100.928-121.824z"
            p-id="2990"
          ></path>
        </svg>
        <span>点赞</span>
      </div>

      <div
        class="action-btn"
        @click="handleComment"
        title="评论"
        v-show="showLikeComment"
      >
        <svg
          t="1753931611023"
          class="icon"
          viewBox="0 0 1024 1024"
          version="1.1"
          xmlns="http://www.w3.org/2000/svg"
          p-id="8441"
        >
          <path
            d="M157.568 751.296c-11.008-18.688-18.218667-31.221333-21.802667-37.909333A424.885333 424.885333 0 0 1 85.333333 512C85.333333 276.362667 276.362667 85.333333 512 85.333333s426.666667 191.029333 426.666667 426.666667-191.029333 426.666667-426.666667 426.666667a424.778667 424.778667 0 0 1-219.125333-60.501334 2786.56 2786.56 0 0 0-20.053334-11.765333l-104.405333 28.48c-23.893333 6.506667-45.802667-15.413333-39.285333-39.296l28.437333-104.288z m65.301333 3.786667l-17.258666 63.306666 63.306666-17.258666a32 32 0 0 1 24.522667 3.210666 4515.84 4515.84 0 0 1 32.352 18.944A360.789333 360.789333 0 0 0 512 874.666667c200.298667 0 362.666667-162.368 362.666667-362.666667S712.298667 149.333333 512 149.333333 149.333333 311.701333 149.333333 512c0 60.586667 14.848 118.954667 42.826667 171.136 3.712 6.912 12.928 22.826667 27.370667 47.232a32 32 0 0 1 3.338666 24.714667z m145.994667-70.773334a32 32 0 1 1 40.917333-49.205333A159.189333 159.189333 0 0 0 512 672c37.888 0 73.674667-13.173333 102.186667-36.885333a32 32 0 0 1 40.917333 49.216A223.178667 223.178667 0 0 1 512 736a223.178667 223.178667 0 0 1-143.136-51.690667z"
            fill="#000000"
            p-id="8442"
          ></path>
        </svg>
        <span>评论</span>
        <span class="count">{{ commentCount || 0 }}</span>
      </div>
    </div>
    <!-- 评论抽屉组件 -->
    <el-drawer
      v-model="commentDrawerVisible"
      direction="rtl"
      size="50%"
      title="文章评论"
      :before-close="handleClose"
      class="comment-drawer-container"
    >
      <div class="comment-drawer-content">
        <!-- 评论输入区 -->
        <div class="comment-input-area">
          <el-avatar
            :size="40"
            :src="userAvatar || defaultAvatar"
            class="comment-avatar"
          ></el-avatar>
          <div class="input-wrapper">
            <el-input
              v-model="commentContent"
              type="textarea"
              placeholder="写下你的评论..."
              :rows="4"
              resize="none"
              class="comment-textarea"
            ></el-input>
            <div class="submit-btn">
              <el-button
                type="primary"
                :loading="submittingComment"
                @click="submitComment"
                :disabled="!commentContent.trim()"
                class="submit-button"
              >
                提交评论
              </el-button>
            </div>
          </div>
        </div>

        <!-- 评论列表 -->
        <div class="comment-list-container" v-if="commentList.length > 0">
          <h3 class="comment-title">全部评论 ({{ commentList.length }})</h3>
          <div class="comment-list">
            <div
              class="comment-item"
              v-for="comment in commentList"
              :key="comment.id"
            >
              <el-avatar
                :size="32"
                :src="comment.userAvatar || defaultAvatar"
                @click="handleAvatarClick(comment.userId)"
                class="comment-item-avatar"
              ></el-avatar>
              <div class="comment-item-content">
                <div class="comment-header">
                  <span class="comment-username">{{ comment.userName }}</span>
                  <span class="comment-time">{{
                    formatDate(comment.createTime || new Date())
                  }}</span>
                </div>
                <div class="comment-text">{{ comment.content }}</div>
              </div>
            </div>
          </div>
        </div>
        <div class="no-comment" v-else>
          <div class="no-comment-icon">
            <svg
              t="1753931611023"
              class="icon"
              viewBox="0 0 1024 1024"
              version="1.1"
              xmlns="http://www.w3.org/2000/svg"
              p-id="8441"
            >
              <path
                d="M157.568 751.296c-11.008-18.688-18.218667-31.221333-21.802667-37.909333A424.885333 424.885333 0 0 1 85.333333 512C85.333333 276.362667 276.362667 85.333333 512 85.333333s426.666667 191.029333 426.666667 426.666667-191.029333 426.666667-426.666667 426.666667a424.778667 424.778667 0 0 1-219.125333-60.501334 2786.56 2786.56 0 0 0-20.053334-11.765333l-104.405333 28.48c-23.893333 6.506667-45.802667-15.413333-39.285333-39.296l28.437333-104.288z m65.301333 3.786667l-17.258666 63.306666 63.306666-17.258666a32 32 0 0 1 24.522667 3.210666 4515.84 4515.84 0 0 1 32.352 18.944A360.789333 360.789333 0 0 0 512 874.666667c200.298667 0 362.666667-162.368 362.666667-362.666667S712.298667 149.333333 512 149.333333 149.333333 311.701333 149.333333 512c0 60.586667 14.848 118.954667 42.826667 171.136 3.712 6.912 12.928 22.826667 27.370667 47.232a32 32 0 0 1 3.338666 24.714667z m145.994667-70.773334a32 32 0 1 1 40.917333-49.205333A159.189333 159.189333 0 0 0 512 672c37.888 0 73.674667-13.173333 102.186667-36.885333a32 32 0 0 1 40.917333 49.216A223.178667 223.178667 0 0 1 512 736a223.178667 223.178667 0 0 1-143.136-51.690667z"
                fill="#ccc"
                p-id="8442"
              ></path>
            </svg>
          </div>
          <p>暂无评论，快来抢沙发吧！</p>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, computed, watch, onUnmounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import {
  ElMessage,
  ElMessageBox,
  ElDrawer,
  ElInput,
  ElButton,
} from "element-plus";
import dayjs from "dayjs";
import {
  selectArticleDetail,
  deleteArticle,
  articleLikeStatus,
  likeArticle,
  articleLikeCount,
  articleViewCount,
  addArticleViewCount,
} from "@/api/wenzhangguanli";
import { getCategoryList } from "@/api/wenzhangfenleiguanli";
import { getTagList } from "@/api/wenzhangbiaoqianguanli";
import { useLoginUserStore } from "@/store/loginUser";
import {
  addArticleComment,
  getArticleCommentList,
} from "@/api/wenzhangpinglunguanli";
import { changStatus, status } from "@/api/guanzhuguanli";
// 获取登录用户存储
const loginUserStore = useLoginUserStore();
// 使用存储中的响应式用户信息
const user = ref<API.UserVO>(loginUserStore.loginUser);
// 路由和导航
const route = useRoute();
const router = useRouter();

// 默认头像
const defaultAvatar =
  "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png";

// 文章详情数据
const articleDetail = ref<API.ArticleVO>({} as API.ArticleVO);
const loading = ref(false);
const loadingLike = ref(false);
const isLiked = ref(false);
// 关注相关状态
const isFollowing = ref(false);
const loadingFollow = ref(false);
const showFollowBtn = computed(() => {
  // 不显示自己的关注按钮
  return user.value.id && articleDetail.value.userId !== user.value.id;
});
// 评论抽屉状态
const commentDrawerVisible = ref(false);
// 评论数据
const commentList = ref<API.ArticleCommentVO[]>([]);
const commentContent = ref("");
const userAvatar = ref(user.value.avatar || "");
const submittingComment = ref(false);
const commentCount = ref(0);

// 是否显示编辑和删除按钮
const showEditDelete = computed(() => {
  return user.value.id && articleDetail.value.userId === user.value.id;
});

// 是否显示点赞和评论按钮
const showLikeComment = computed(() => {
  return articleDetail.value.auditStatus === 1;
});

// 格式化日期
const formatDate = (date: string | number | Date) => {
  return dayjs(date).format("YYYY-MM-DD HH:mm:ss");
};

// 获取文章详情
const getArticleDetail = async () => {
  const id = route.query.id;
  if (!id) {
    ElMessage.error("文章ID不存在");
    router.push("/my-articles");
    return;
  }

  loading.value = true;
  try {
    const res = await selectArticleDetail({ id: id });
    if (res.data.code === 200) {
      articleDetail.value = res.data.data || ({} as API.ArticleVO);
      getFlowStatus();
    } else {
      ElMessage.error(res.data.message || "获取文章详情失败");
    }
  } catch (err) {
    ElMessage.error("获取文章详情失败");
  } finally {
    loading.value = false;
  }
};
const getTagName = (tagId: number) => {
  const tag = tagData.value.find((t) => t.id === tagId);
  return tag ? tag.name : "未知标签";
};
const getCategoryName = (categoryId: number) => {
  const category = categoryData.value.find((c) => c.id === categoryId);
  return category ? category.name : "未知分类";
};

// 编辑文章
const handleEdit = () => {
  router.push({
    path: "/edit-article",
    query: { id: articleDetail.value.id },
  });
};

// 删除文章
const handleDelete = () => {
  ElMessageBox.confirm("确定要删除这篇文章吗？此操作不可撤销。", "确认删除", {
    confirmButtonText: "确认",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(async () => {
      try {
        const res = await deleteArticle({ id: articleDetail.value.id });
        if (res.data.code === 200) {
          ElMessage.success("删除成功");
          router.push("/my-articles");
        } else {
          ElMessage.error(res.data.message || "删除失败");
        }
      } catch (err) {
        ElMessage.error("删除操作失败");
      }
    })
    .catch(() => {
      // 取消删除
    });
};

// 获取点赞状态
const getLikeStatus = async () => {
  const id = route.query.id;
  if (!id) {
    ElMessage.error("文章ID不存在");
    router.push("/my-articles");
    return;
  }
  try {
    const res = await articleLikeStatus({ articleId: id });
    if (res.data.code === 200) {
      isLiked.value = res.data.data ?? false;
    }
  } catch (err) {}
};
const likeCount = ref(0);
//获取点赞数量
const getLikeCount = async () => {
  const id = route.query.id;
  if (!id) {
    ElMessage.error("文章ID不存在");
    router.push("/my-articles");
    return;
  }
  try {
    const res = await articleLikeCount({ articleId: id });
    if (res.data.code === 200) {
      likeCount.value = res.data.data ?? 0;
    }
  } catch (err) {}
};
const viewCount = ref(0);
//获取阅读量
const getViewCount = async () => {
  const id = route.query.id;
  if (!id) {
    ElMessage.error("文章ID不存在");
    router.push("/my-articles");
    return;
  }
  try {
    const res = await articleViewCount({ articleId: id });
    if (res.data.code === 200) {
      viewCount.value = res.data.data ?? 0;
    }
  } catch (err) {}
};
// 关注/取消关注作者
const handleFollow = async () => {
  loadingFollow.value = true;
  try {
    const res = await changStatus({
      userId: articleDetail.value.userId,
      status: isFollowing.value ? 0 : 1,
    });
    if (res.data.code === 200) {
      isFollowing.value = !isFollowing.value;
      ElMessage.success(isFollowing.value ? "关注成功" : "取消关注成功");
    }
  } catch (err) {
    ElMessage.error("操作失败");
  } finally {
    loadingFollow.value = false;
  }
};
//获取关注状态
const getFlowStatus = async () => {
  const userId = articleDetail.value.userId;
  try {
    const res = await status({ userId: userId });
    if (res.data.code === 200) {
      isFollowing.value = res.data.data;
    }
  } catch (err) {
    console.log(err);
  }
};

// 点赞文章
const handleLike = async () => {
  loadingLike.value = true;
  try {
    //如果点赞状态是已点赞，取消点赞 isLiek=1表示点赞，isLike=0表示取消点赞
    const likeStatus = isLiked.value ? 0 : 1;
    const res = await likeArticle({
      articleId: articleDetail.value.id,
      isLike: likeStatus,
    });
    if (res.data.code === 200) {
      isLiked.value = !isLiked.value;
      getLikeCount();
    }
  } catch (err) {
    ElMessage.error("点赞操作失败");
  } finally {
    loadingLike.value = false;
  }
};

// 获取文章评论
const getArticleComments = async () => {
  const id = route.query.id;
  if (!id) {
    ElMessage.error("文章ID不存在");
    return;
  }
  try {
    const res = await getArticleCommentList({ articleId: id });
    if (res.data.code === 200) {
      commentList.value = res.data.data || [];
      commentCount.value = commentList.value.length;
    }
  } catch (err) {
    ElMessage.error("获取评论失败");
  }
};

// 提交评论
const submitComment = async () => {
  if (!commentContent.value.trim()) {
    ElMessage.warning("评论内容不能为空");
    return;
  }

  submittingComment.value = true;
  try {
    const res = await addArticleComment({
      articleId: articleDetail.value.id,
      content: commentContent.value,
    });
    if (res.data.code === 200) {
      commentContent.value = "";
      ElMessage.success("评论成功");
      getArticleComments(); // 重新获取评论列表
    } else {
      ElMessage.error(res.data.message || "评论失败");
    }
  } catch (err) {
    ElMessage.error("评论失败");
  } finally {
    submittingComment.value = false;
  }
};

// 打开评论抽屉
const handleComment = () => {
  commentDrawerVisible.value = true;
};

// 处理头像点击事件，跳转到用户主页
const handleAvatarClick = (userId: number) => {
  router.push({
    path: "/user-home",
    query: { id: userId },
  });
};

// 关闭抽屉的回调
const handleClose = () => {
  commentDrawerVisible.value = false;
};

const categoryData = ref<API.CategoryVO[]>([]); // 分类数据
const tagData = ref<API.TagVO[]>([]); // 标签数
const getCategoryData = async () => {
  const res = await getCategoryList({ categoryName: "" });
  if (res.data.code === 200) {
    categoryData.value = res.data.data || [];
  }
};
const getTagData = async () => {
  const res = await getTagList({ tagName: "" });
  if (res.data.code === 200) {
    tagData.value = res.data.data || [];
  }
};
// 页面加载时获取数据
// 定时器变量
let viewTimer: number | null = null;
// 是否已增加阅读量
const hasAddedView = ref(false);

onMounted(async () => {
  await Promise.all([getCategoryData(), getTagData()]);
  getArticleDetail();
  getLikeStatus();
  getLikeCount();
  getViewCount();
  getArticleComments();
  // 初始化用户头像
  userAvatar.value = user.value.avatar || "";

  // 监听文章审核状态变化，设置定时任务
  watch(
    () => articleDetail.value.auditStatus,
    (newStatus) => {
      if (newStatus === 1 && !hasAddedView.value && !viewTimer) {
        // 审核通过，设置10秒定时器增加阅读量
        viewTimer = window.setTimeout(async () => {
          if (!hasAddedView.value) {
            // 调用增加阅读量的接口
            try {
              const res = await addArticleViewCount({
                articleId: articleDetail.value.id,
              });
              console.log(res.data);
              if (res.data.code === 200 && res.data.data) {
                // 更新本地阅读量显示
                viewCount.value += 1;
              }
              hasAddedView.value = true;
            } catch (err) {
              console.error("增加阅读量失败:", err);
            }
          }
        }, 10000); // 10秒
      }
    }
  );
});

// 组件卸载时清除定时器
onUnmounted(() => {
  if (viewTimer) {
    clearTimeout(viewTimer);
    viewTimer = null;
  }
});
</script>

<style scoped>
.article-detail-container {
  max-height: 100%;
}

.article-title {
  margin-bottom: 8px;
  position: relative;
}

.article-title h1 {
  font-size: 28px;
  font-weight: 700;
  color: #1d2129;
  line-height: 1.3;
  margin-bottom: 8px;
  margin-top: 10px;
  transition: all 0.3s ease;
}

.title-meta {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 10px;
}

.category-tag {
  background-color: #3498db !important;
  color: #fff !important;
  border-radius: 4px;
  padding: 2px 8px;
}

.article-tag {
  background-color: #f5f5f5 !important;
  color: #555 !important;
  border-color: #e5e5e5 !important;
  border-radius: 4px;
  padding: 2px 8px;
  transition: all 0.2s ease;
}

.article-tag:hover {
  background-color: #e9e9e9 !important;
}

.article-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 0;
  border-top: 1px solid #f2f3f5;
  border-bottom: 1px solid #f2f3f5;
  margin-bottom: 30px;
  flex-wrap: wrap;
  gap: 15px;
}

.author-info {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
}

.follow-btn-container {
  margin-left: 20px;
}

.author-details {
  margin-left: 12px;
  display: flex;
  flex-direction: column;
}

.author-avatar {
  width: 32px !important;
  height: 32px !important;
  margin-right: 12px;
  border: 1px solid #f0f2f5;
}

.author-details {
  display: flex;
  flex-direction: column;
}

.author-name {
  color: #1d2129;
  font-weight: 500;
  margin-bottom: 4px;
}

.publish-time {
  color: #86909c;
  font-size: 13px;
}

.article-stats {
  display: flex;
  align-items: center;
  gap: 20px;
  color: #86909c;
  font-size: 14px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 5px;
  transition: color 0.2s ease;
}

.stat-item:hover {
  color: #1890ff;
}

.article-content {
  font-size: 16px;
  color: #333;
  line-height: 1.8;
}

.article-content p {
  margin-bottom: 1.2em;
}

.article-content img {
  max-width: 100%;
  border-radius: 8px;
  margin: 20px auto;
  display: block;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.article-content h2,
.article-content h3 {
  margin: 1.5em 0 0.8em;
  color: #1d2129;
}

.article-action-bar {
  display: flex;
  gap: 15px;
  padding-top: 20px;
  border-top: 1px solid #f2f3f5;
  padding-bottom: 15px;
}

.action-btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  padding: 8px 16px;
  border-radius: 20px;
  color: #606266;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.2s ease;
  background-color: #f5f7fa;
}

.action-btn:hover {
  background-color: #e9e9eb;
  color: #303133;
  transform: translateY(-1px);
}

.action-btn.danger {
  color: #f56c6c;
}

.action-btn.danger:hover {
  background-color: #fef0f0;
  color: #e53e3e;
}

.action-btn.liked {
  color: #f56c6c;
  background-color: #fef0f0;
}

.action-btn .icon {
  width: 18px;
  height: 18px;
}

.action-btn .count {
  background-color: rgba(0, 0, 0, 0.06);
  padding: 0 6px;
  border-radius: 10px;
  font-size: 12px;
}

/* 评论抽屉样式 */
.comment-drawer-container {
  --el-drawer-bg-color: #fafafa;
}

.comment-drawer-content {
  height: 100%;
  display: flex;
  flex-direction: column;
  padding: 20px;
}

.comment-input-area {
  display: flex;
  margin-bottom: 25px;
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;
}

.comment-avatar {
  margin-right: 15px;
  border: 1px solid #f0f2f5;
}

.input-wrapper {
  flex: 1;
}

.comment-textarea {
  border-radius: 8px !important;
  border-color: #e5e6eb !important;
  transition: all 0.2s ease !important;
}

.comment-textarea:focus {
  border-color: #1890ff !important;
  box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2) !important;
}

.submit-btn {
  display: flex;
  justify-content: flex-end;
  margin-top: 12px;
}

.submit-button {
  border-radius: 20px !important;
  padding: 6px 20px !important;
  transition: all 0.2s ease !important;
}

.comment-list-container {
  flex: 1;
  overflow-y: auto;
}

.comment-title {
  font-size: 16px;
  font-weight: 600;
  color: #1d2129;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #f2f3f5;
}

.comment-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.comment-item {
  display: flex;
  padding: 10px 0;
  border-bottom: 1px solid #f7f8fa;
}

.comment-item:last-child {
  border-bottom: none;
}

.comment-item-avatar {
  margin-right: 12px;
  border: 1px solid #f0f2f5;
}

.comment-item-content {
  flex: 1;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.comment-username {
  font-weight: 500;
  color: #1d2129;
  font-size: 14px;
}

.comment-time {
  font-size: 12px;
  color: #86909c;
}

.comment-text {
  color: #333;
  line-height: 1.6;
  font-size: 14px;
  word-break: break-word;
}

.no-comment {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #86909c;
  font-size: 14px;
  padding: 40px 0;
}

.no-comment-icon .icon {
  width: 60px;
  height: 60px;
  margin-bottom: 15px;
  opacity: 0.5;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .article-detail-container {
    padding: 15px 0;
  }

  .article-title h1 {
    font-size: 22px;
  }

  .article-info {
    flex-direction: column;
    align-items: flex-start;
  }

  .article-stats {
    width: 100%;
    justify-content: space-between;
  }

  .article-content {
    font-size: 15px;
  }

  .action-btn span {
    display: none;
  }

  .action-btn {
    padding: 8px;
    border-radius: 50%;
  }

  .comment-drawer-container {
    width: 100% !important;
  }
}
</style>
