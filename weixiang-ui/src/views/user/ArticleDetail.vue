<template>
  <div class="article-detail">
    <!-- 文章标题 -->
    <div class="article-title">
      {{ articleDetail.title }}
      <el-tag v-if="articleDetail.categoryId" size="small" :style="{ backgroundColor: '#FFA500', color: '#FFF' }">
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
          style="margin-left: 8px; margin-bottom: 8px"
        >
          {{ getTagName(tag) }}
        </el-tag>
      </div>
    </div>
    <!-- 文章信息 -->
    <div class="article-info">
      <span class="author-info">
        <el-avatar
          :size="24"
          :src="articleDetail.authorAvatar || defaultAvatar"
        ></el-avatar>
        <span>{{ articleDetail.userName }}</span>
      </span>
      <span class="publish-time">{{
        formatDate(articleDetail.createTime || new Date())
      }}</span>
      <div class="article-stats">
        <span
          ><i class="el-icon-view"></i>
          阅读量：
          {{ articleDetail.viewCount || 0 }}</span
        >
        <span
          ><i class="el-icon-thumbs-up"></i>
          点赞数：
          {{ articleDetail.likeCount || 0 }}</span
        >
      </div>
    </div>
    <!-- 文章内容 -->
    <div class="article-content" v-html="articleDetail.content"></div>
    <!-- 文章操作图标 -->
    <div class="article-action-icons">
      <div class="icon-btn" @click="handleEdit" title="编辑" v-show="showEditDelete">
        <svg
          t="1753931545287"
          class="icon"
          viewBox="0 0 1024 1024"
          version="1.1"
          xmlns="http://www.w3.org/2000/svg"
          p-id="6335"
        >
          <path
            d="M181.11 816.38a27.6 27.6 0 0 1-26.79-34.32l55.24-221a27.58 27.58 0 0 1 7.26-12.84l441.94-441.9a27.61 27.61 0 0 1 39.06 0L863.55 272a27.62 27.62 0 0 1 0 39.07L421.61 753a27.66 27.66 0 0 1-12.83 7.27l-221 55.24a27.51 27.51 0 0 1-6.7 0.83z m80.19-234.47l-42.22 168.88L388 708.57l417-417-126.71-126.66z"
            p-id="6336"
          ></path>
          <path
            d="M761.16 402.06a27.55 27.55 0 0 1-19.54-8.06L575.9 228.24a27.62 27.62 0 0 1 39.1-39.06l165.69 165.73a27.62 27.62 0 0 1-19.53 47.15zM402.08 761.13a27.53 27.53 0 0 1-19.53-8.13L216.82 587.32a27.63 27.63 0 0 1 39.07-39.07L421.61 714a27.62 27.62 0 0 1-19.53 47.15z m497.18 165.73H457.32a27.62 27.62 0 1 1 0-55.24h441.94a27.62 27.62 0 0 1 0 55.24z m-552.42 0h-221a27.62 27.62 0 1 1 0-55.24h221a27.62 27.62 0 0 1 0 55.24z"
            p-id="6337"
          ></path>
        </svg>
      </div>
      <div class="icon-btn" @click="handleDelete" title="删除" v-show="showEditDelete">
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
      </div>
      <div
        class="icon-btn like-btn"
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
      </div>
      <div class="icon-btn" @click="handleComment" title="评论"  v-show="showLikeComment">
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
        <span class="count">{{ 0 }}</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, computed } from "vue";
import { useRoute, useRouter } from "vue-router";
import { ElMessage, ElMessageBox } from "element-plus";
import dayjs from "dayjs";
import { selectArticleDetail, deleteArticle } from "@/api/wenzhangguanli";
import { getCategoryList } from "@/api/wenzhangfenleiguanli";
import { getTagList } from "@/api/wenzhangbiaoqianguanli";
import { useLoginUserStore } from "@/store/loginUser";
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

// 是否显示编辑和删除按钮
const showEditDelete = computed(() => {
  return user.value.id && articleDetail.value.userId === user.value.id;
});

// 是否显示点赞和评论按钮
const showLikeComment = computed(() => {
  return  articleDetail.value.auditStatus === 1;
});

// 评论数据
// const commentList = ref<API.CommentVO[]>([]);
const commentContent = ref("");
const userAvatar = ref("");

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

// 点赞文章
const handleLike = async () => {
  loadingLike.value = true;
  try {
    isLiked.value = !isLiked.value;
    ElMessage.success(isLiked.value ? "点赞成功" : "取消点赞成功");
  } catch (err) {
    ElMessage.error("点赞操作失败");
  } finally {
    loadingLike.value = false;
  }
};

// 提交评论
const submitComment = async () => {
  if (!commentContent.value.trim()) {
    ElMessage.warning("评论内容不能为空");
    return;
  }

  try {
    commentContent.value = "";
    ElMessage.success("评论成功");
  } catch (err) {
    ElMessage.error("评论失败");
  }
};

// 评论按钮点击事件（占位实现）
const handleComment = () => {
  // 评论功能的占位实现
  // 后续可以在这里打开评论对话框或跳转到评论页面
  ElMessage.info("评论功能待实现");
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
onMounted(async () => {
  await Promise.all([getCategoryData(), getTagData()]);
  getArticleDetail();
});
</script>

<style scoped>
.article-detail {
  padding: 20px;
  margin: 0 auto;
}

.article-card {
  margin-bottom: 20px;
  border-radius: 8px;
  overflow: hidden;
}

.article-title {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin: 20px 0;
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
  display: flex;
  flex-wrap: wrap;
  align-items: flex-end;
}

.title-tags {
  margin-left: 10px;
  display: flex;
  flex-wrap: wrap;
  margin-bottom: -8px; /* 抵消tag的margin-bottom */
}

.article-info {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  margin-bottom: 20px;
  color: #999;
  font-size: 14px;
}

.author-info {
  display: flex;
  align-items: center;
  margin-right: 20px;
}

.author-info .el-avatar {
  margin-right: 8px;
}

.publish-time {
  margin-right: 20px;
}

.article-stats {
  display: flex;
  margin-left: auto;
}

.article-stats span {
  margin-left: 15px;
  display: flex;
  align-items: center;
}

.article-stats i {
  margin-right: 5px;
}

.article-content {
  margin: 20px 0;
  line-height: 1.8;
  font-size: 16px;
  color: #333;
}

.article-content img {
  max-width: 100%;
  margin: 15px 0;
}

.article-tags {
  margin: 20px 0;
  padding-top: 15px;
  border-top: 1px solid #eee;
}

.tag-label {
  color: #999;
  margin-right: 10px;
}

.article-action-icons {
  display: flex;
  justify-content: flex-end;
  gap: 15px;
  margin-top: 20px;
  padding-top: 15px;
  border-top: 1px solid #eee;
}

.icon-btn {
  display: flex;
  align-items: center;
  cursor: pointer;
  color: #606266;
  transition: color 0.3s;
  padding: 4px;
  border-radius: 50%;
}

.icon-btn:hover {
  color: #409eff;
  background-color: #f5f7fa;
}

.icon-btn i {
  margin-right: 5px;
  font-size: 16px;
}

.icon-btn .count {
  margin-left: 5px;
  font-size: 12px;
  color: #909399;
}

.icon-btn.liked {
  color: #f56c6c;
}

/* 点赞按钮背景切换 */
.like-btn.liked {
  background-color: #fef0f0 !important;
}

/* 图标大小统一设置为16px */
.icon {
  width: 20px;
  height: 20px;
}

@media (max-width: 768px) {
  .article-detail {
    padding: 10px;
  }

  .article-title {
    font-size: 20px;
  }

  .article-info {
    flex-direction: column;
    align-items: flex-start;
  }

  .article-info > span {
    margin-bottom: 8px;
  }

  .article-stats {
    margin-left: 0;
    margin-top: 10px;
  }

  .article-actions {
    flex-direction: column;
  }

  .article-actions .el-button {
    width: 100%;
  }

  .comment-input {
    flex-direction: column;
  }

  .comment-input .el-avatar {
    margin-bottom: 10px;
  }
}
</style>
