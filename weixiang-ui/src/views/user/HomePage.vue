<template>
  <div class="home-page">
    <!-- 搜索框 -->
    <div class="search-box">
      <input
        type="text"
        v-model="searchKeyword"
        placeholder="搜索文章标题或内容"
        @keyup.enter="handleSearch"
        class="search-input"
      />
      <el-button type="primary" @click="handleSearch" class="search-btn"
        >搜索</el-button
      >
      <el-button type="default" @click="handleReset" class="search-btn"
        >重置</el-button
      >
    </div>
    <!-- 标签导航 -->
    <div class="tag-nav-container">
      <button class="scroll-btn left" @click="handleScrollLeft">
        <el-icon>
          <ArrowLeft />
        </el-icon>
      </button>
      <div class="tag-nav" ref="tagNav">
        <a
          href="#"
          class="tag-item"
          :class="{ active: selectedTagId === -1 }"
          @click.prevent="handleTagClick(-1)"
        >
          全部
        </a>
        <a
          href="#"
          v-for="tag in tagData"
          :key="tag.id"
          class="tag-item"
          :class="{ active: selectedTagId === tag.id }"
          @click.prevent="handleTagClick(tag.id)"
        >
          {{ tag.name }}
        </a>
      </div>
      <button class="scroll-btn right" @click="handleScrollRight">
        <el-icon>
          <ArrowRight />
        </el-icon>
      </button>
    </div>
    <!-- 主体内容区 -->
    <div class="main-container">
      <!-- 左侧文章列表区 -->
      <div class="article-content">
        <!-- 文章列表 -->
        <div class="article-list-container" ref="articleListContainer">
          <div
            v-if="loading && articleList.length === 0"
            class="loading-container"
          >
            <el-spinner size="large" />
          </div>
          <div
            v-else-if="articleList.length === 0 && !loading"
            class="no-article-container"
          >
            <div class="no-article-icon">
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
            <p>暂无文章数据</p>
          </div>
          <div v-else class="article-list">
            <div
              v-for="article in articleList"
              :key="article.id"
              class="article-item"
              @click="navigateToDetail(article.id)"
            >
              <div class="article-header">
                <div class="article-title">{{ article.title }}</div>
              </div>
              <div class="article-summary">
                <div class="summary-content">{{ article.content }}</div>
              </div>

              <div class="article-meta">
                <div class="author-info">
                  <img
                    :src="
                      article.userAvatar
                        ? `${baseURL}${article.userAvatar}`
                        : defaultAvatar
                    "
                    alt="作者头像"
                    class="author-avatar"
                  />
                  <div class="author-details">
                    <span class="article-author">{{ article.userName }}</span>
                    <span class="article-date">{{
                      formatDate(article.createTime || "")
                    }}</span>
                  </div>
                </div>
              </div>

              <div class="article-tags">
                <el-tag
                  v-if="article.categoryId"
                  size="small"
                  class="category-tag"
                  >{{ getCategoryName(article.categoryId) }}</el-tag
                >
                <el-tag
                  v-for="tag in article.tagIds"
                  :key="tag"
                  size="small"
                  class="article-tag"
                  >{{ getTagName(tag) }}</el-tag
                >
              </div>
            </div>
          </div>
          <!-- 加载更多提示 -->
          <div v-if="articleList.length > 0" class="loading-more">
            <el-spinner v-if="loading" size="medium" />
            <p v-else-if="hasMore">滚动加载更多</p>
            <p v-else>没有更多数据了</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, onUnmounted, nextTick } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import dayjs from "dayjs";
import { selectPassArticlePage } from "@/api/wenzhangguanli";
import { getCategoryList } from "@/api/wenzhangfenleiguanli";
import { getTagList } from "@/api/wenzhangbiaoqianguanli";
import { ArrowLeft, ArrowRight } from "@element-plus/icons-vue";

// 路由导航
const router = useRouter();

// 文章列表数据
const articleList = ref<API.ArticleVO[]>([]);
const loading = ref(false);
const categoryData = ref<API.CategoryVO[]>([]);
const tagData = ref<API.TagVO[]>([]);
// 搜索框相关
const searchKeyword = ref("");
// 选中的标签ID，默认为全部(-1)
const selectedTagId = ref(-1);
const tagNav = ref<HTMLDivElement>(null); // 标签导航容器
import { baseURL } from "@/request";
// 默认头像
const defaultAvatar =
  "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png";

// 分页相关
const currentPage = ref(1);
const pageSize = ref(10);
const totalCount = ref(0);
const hasMore = ref(true); // 是否还有更多数据
const articleListContainer = ref<HTMLDivElement>(null); // 列表容器

// 格式化日期
const formatDate = (date: string | number | Date) => {
  return dayjs(date).format("YYYY-MM-DD");
};

// 处理滚动加载
const handleScroll = () => {
  if (loading.value || !hasMore.value) return;

  const container = articleListContainer.value;
  if (!container) return;

  const { scrollTop, clientHeight, scrollHeight } = container;
  // 当滚动到距离底部200px以内时加载更多
  if (scrollHeight - scrollTop - clientHeight <= 200) {
    loadMoreData();
  }
};

// 加载更多数据
const loadMoreData = () => {
  if (currentPage.value * pageSize.value >= totalCount.value) {
    hasMore.value = false;
    return;
  }
  currentPage.value++;
  getArticleList(true);
};

// 获取文章列表
const getArticleList = async (isLoadMore = false) => {
  loading.value = true;
  try {
    const res = await selectPassArticlePage({
      pageNum: currentPage.value,
      pageSize: pageSize.value,
      tagIds: selectedTagId.value === -1 ? [] : [selectedTagId.value],
      searchText: searchKeyword.value,
      userId: undefined,
    });

    if (res.data.code === 200) {
      const newData = res.data.data?.records || [];
      const total = Number(res.data.data?.total) || 0;

      // 如果是加载更多则追加数据，否则替换数据
      if (isLoadMore) {
        articleList.value = [...articleList.value, ...newData];
      } else {
        articleList.value = newData;
      }

      totalCount.value = total;
      // 判断是否还有更多数据
      hasMore.value = articleList.value.length < totalCount.value;
    } else {
      ElMessage.error(res.data.message || "获取文章列表失败");
    }
  } catch (err) {
    console.error("获取文章列表错误:", err);
    ElMessage.error("获取文章列表失败");
  } finally {
    loading.value = false;
  }
};

// 处理搜索
const handleSearch = () => {
  // 重置分页状态
  currentPage.value = 1;
  hasMore.value = true;
  getArticleList();
};

// 处理重置
const handleReset = () => {
  searchKeyword.value = "";
  selectedTagId.value = -1;
  // 重置分页状态
  currentPage.value = 1;
  hasMore.value = true;
  getArticleList();
};

// 处理滚动左
const handleScrollLeft = () => {
  if (tagNav.value) {
    tagNav.value.scrollLeft -= 100;
  }
};

// 处理滚动右
const handleScrollRight = () => {
  if (tagNav.value) {
    tagNav.value.scrollLeft += 100;
  }
};

// 处理标签点击
const handleTagClick = (tagId: number) => {
  selectedTagId.value = tagId;
  // 重置分页状态
  currentPage.value = 1;
  hasMore.value = true;
  getArticleList();
};

// 获取标签名称
const getTagName = (tagId: number) => {
  const tag = tagData.value.find((t) => t.id === tagId);
  return tag ? tag.name : "未知标签";
};

// 获取分类名称
const getCategoryName = (categoryId: number) => {
  const category = categoryData.value.find((c) => c.id === categoryId);
  return category ? category.name : "未知分类";
};

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

// 导航到文章详情页
const navigateToDetail = (id: number) => {
  router.push({
    path: "/article-detail",
    query: { id },
  });
};

// 页面加载时获取数据
onMounted(async () => {
  // 初始化滚动状态
  nextTick(() => {});
  await getCategoryData();
  await getTagData();
  getArticleList();

  // 等待DOM渲染完成后添加滚动监听
  nextTick(() => {
    const container = articleListContainer.value;
    if (container) {
      container.addEventListener("scroll", handleScroll);
    }
  });
});

// 页面卸载时移除滚动监听
onUnmounted(() => {
  const container = articleListContainer.value;
  if (container) {
    container.removeEventListener("scroll", handleScroll);
  }
});
</script>

<style scoped>
.home-page {
  font-family: "Segoe UI", "Microsoft YaHei", sans-serif;
  color: #333;
}

.nav-container {
  max-width: 1200px;
  padding: 0 20px;
  display: flex;
  gap: 20px;
}

.nav-item {
  color: #333;
  text-decoration: none;
  padding: 15px 0;
  display: inline-block;
  font-size: 16px;
  font-weight: 500;
}

.nav-item.active {
  color: #3498db;
  border-bottom: 2px solid #3498db;
}

.nav-item:hover {
  color: #3498db;
}

/* 搜索框样式 */
.search-box {
  max-width: 800px;
  display: flex;
  gap: 10px;
  padding: 0 120px;
}

.search-input {
  flex: 1;
  height: 40px;
  padding: 0 20px;
  border: 1px solid #e5e9f2;
  border-radius: 8px;
  font-size: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.search-input:focus {
  box-shadow: 0 0 0 2px rgba(52, 152, 219, 0.3);
  outline: none;
  border-color: #3498db;
}

.search-btn {
  height: 40px !important;
  padding: 0 24px !important;
  font-size: 16px !important;
  border-radius: 8px !important;
  transition: all 0.3s ease !important;
}

.search-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(52, 152, 219, 0.3);
}

/* 主体内容区优化 */
.main-container {
  max-width: 1000px;
  padding: 0 20px;
}

.article-content {
  width: 100%;
}

/* 文章列表容器优化 */
.article-list-container {
  border-radius: 16px;
  padding: 10px;
  max-height: calc(100vh - 200px); /* 固定容器高度，使其可滚动 */
  overflow-y: auto;

  /* 隐藏滚动条 - 兼容各浏览器 */
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* IE 和 Edge */
}

/* Chrome、Safari 和 Opera 隐藏滚动条 */
.article-list-container::-webkit-scrollbar {
  display: none;
}

/* 滚动加载提示优化 */
.loading-more {
  text-align: center;
  padding: 20px;
  color: #666;
  font-size: 14px;
}

/* 加载状态优化 */
.loading-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 300px;
}

/* 无文章状态 */
.no-article-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 300px;
  color: #86909c;
}

.no-article-icon {
  width: 64px;
  height: 64px;
  margin-bottom: 16px;
}

/* 文章列表 */
.article-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* 文章项优化 */
.article-item {
  padding: 20px;
  border-radius: 12px;
  background-color: #fff;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  cursor: pointer;
  transition: all 0.3s ease;
}

.article-item:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}

.article-header {
  margin-bottom: 12px;
}

.article-title {
  font-size: 19px;
  font-weight: 600;
  color: #1d2129;
  margin-bottom: 0;
  transition: color 0.2s;
  line-height: 1.5;
}

.article-item:hover .article-title {
  color: #1890ff;
}

/* 文章摘要优化 */
.article-summary {
  display: flex;
  margin-bottom: 15px;
  align-items: center;
  color: #4e5969;
  line-height: 1.7;
}

.article-img {
  width: 160px;
  height: 120px;
  margin-right: 16px;
  border-radius: 8px;
  overflow: hidden;
  flex-shrink: 0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.summary-content {
  flex: 1;
  font-size: 14px;
  color: #666;
  line-height: 1.8;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

/* 文章元数据优化 */
.article-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
  font-size: 13px;
  color: #86909c;
}

.author-info {
  display: flex;
  align-items: center;
}

.author-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  margin-right: 10px;
  object-fit: cover;
}

.author-details {
  display: flex;
  flex-direction: column;
}

.article-author {
  color: #4e5969;
  font-weight: 500;
  margin-bottom: 2px;
}

.article-date {
  color: #86909c;
  font-size: 12px;
}

.article-stats {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-item {
  display: flex;
  align-items: center;
  color: #86909c;
  font-size: 12px;
}

.stat-item i {
  margin-right: 4px;
  font-size: 14px;
}

.stat-item:hover {
  color: #1890ff;
}

/* 标签导航容器样式 */
.tag-nav-container {
  max-width: 1000px;
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 10px;
  margin-top: 10px;
}

/* 滚动按钮样式 */
.scroll-btn {
  background: #fff;
  border: 1px solid #e5e9f2;
  border-radius: 4px;
  padding: 0 12px;
  height: 32px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.scroll-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.scroll-btn:hover:not(:disabled) {
  background: #e8f4fd;
  color: #1890ff;
  border-color: #d1e5fe;
}

/* 标签导航样式优化 */
.tag-nav {
  flex: 1;
  display: flex;
  overflow-x: hidden;
  gap: 12px;
  padding: 10px 20px;
  scrollbar-width: thin;
  scrollbar-color: #d1e5fe #f5f7fa;
}

/* 滚动条样式 - 仅适用于WebKit浏览器 */
.tag-nav::-webkit-scrollbar {
  height: 6px;
}

.tag-nav::-webkit-scrollbar-track {
  background: #f5f7fa;
  border-radius: 10px;
}

.tag-nav::-webkit-scrollbar-thumb {
  background-color: #d1e5fe;
  border-radius: 10px;
}

.tag-item {
  padding: 8px 16px;
  white-space: nowrap;
  color: #666;
  text-decoration: none;
  border-radius: 20px;
  font-size: 14px;
  transition: all 0.3s ease;
  background-color: #fff;
  border: 1px solid #e5e9f2;
}

.tag-item.active {
  background-color: #1890ff;
  color: #fff;
  border-color: #1890ff;
}

.tag-item:hover {
  background-color: #e8f4fd;
  color: #1890ff;
  border-color: #d1e5fe;
}

/* 标签样式优化 */
.article-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 10px;
}

.category-tag {
  background-color: #e6f7ff !important;
  color: #1890ff !important;
  border-color: #bae7ff !important;
  border-radius: 4px;
  padding: 2px 8px !important;
  font-size: 12px !important;
}

.article-tag {
  background-color: #f5f7fa !important;
  color: #666 !important;
  border-color: #e5e9f2 !important;
  border-radius: 4px;
  padding: 2px 8px !important;
  font-size: 12px !important;
}

/* 响应式适配优化 */
@media (max-width: 992px) {
  .category-nav {
    padding: 10px;
    gap: 10px;
  }

  .category-item {
    padding: 5px 10px;
    font-size: 13px;
  }
}

@media (max-width: 576px) {
  .article-summary {
    flex-direction: column;
  }

  .article-img {
    width: 100%;
    height: auto;
    margin: 0 0 10px 0;
  }

  .search-box {
    flex-direction: column;
  }

  .search-input,
  .search-btn {
    width: 100%;
  }

  .article-meta {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .nav-container::-webkit-scrollbar {
    display: none;
  }

  .article-list-container {
    max-height: calc(100vh - 250px);
  }
}
</style>
