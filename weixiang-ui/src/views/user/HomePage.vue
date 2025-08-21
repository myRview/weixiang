<template>
  <div class="home-page">
    <!-- 搜索框 -->
    <div class="search-box">
      <input
        type="text"
        v-model="searchKeyword"
        placeholder="搜索文章标题"
        @keyup.enter="handleSearch"
        class="search-input"
      />
      <el-button type="primary" @click="handleSearch" class="search-btn"
        >搜索</el-button
      >
    </div>
    <!-- 标签导航 -->
    <div class="tag-nav">
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
    <!-- 主体内容区 -->
    <div class="main-container">
      <!-- 左侧文章列表区 -->
      <div class="article-content">
        <!-- 文章列表 -->
        <div class="article-list-container">
          <div v-if="loading" class="loading-container">
            <el-spinner size="large" />
          </div>
          <div
            v-else-if="articleList.length === 0"
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
                <div v-if="article.id % 3 === 0" class="article-img">
                  <img
                    src="https://picsum.photos/120/90?random=1"
                    alt="文章封面"
                  />
                </div>
                <div class="summary-content">{{ article.content }}</div>
              </div>

              <div class="article-meta">
                <div class="author-info">
                  <img
                    :src="article.userAvatar || defaultAvatar"
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

          <!-- 分页 -->
          <div class="pagination-container" v-if="articleList.length > 0">
            <el-pagination
              @current-change="handlePageChange"
              :current-page="currentPage"
              :page-size="pageSize"
              layout="prev, pager, next"
              :total="totalCount"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import dayjs from "dayjs";
import { selectPassArticlePage } from "@/api/wenzhangguanli";
import { getCategoryList } from "@/api/wenzhangfenleiguanli";
import { getTagList } from "@/api/wenzhangbiaoqianguanli";

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

// 默认头像
const defaultAvatar =
  "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png";

// 分页相关
const currentPage = ref(1);
const pageSize = ref(10);
const totalCount = ref(0);

// 格式化日期
const formatDate = (date: string | number | Date) => {
  return dayjs(date).format("YYYY-MM-DD");
};

// 获取文章列表
const getArticleList = async () => {
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
      // 模拟数据添加统计信息
      articleList.value = res.data.data?.records || [];
      totalCount.value = res.data.data?.total || 0;
    } else {
      ElMessage.error(res.data.message || "获取文章列表失败");
    }
  } catch (err) {
    ElMessage.error("获取文章列表失败");
  } finally {
    loading.value = false;
  }
};

// 处理搜索
const handleSearch = () => {
  currentPage.value = 1;
  getArticleList();
};

// 处理分页变化
const handlePageChange = (page: number) => {
  currentPage.value = page;
  getArticleList();
};

// 处理标签点击
const handleTagClick = (tagId: number) => {
  selectedTagId.value = tagId;
  currentPage.value = 1; // 重置到第一页
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
  getArticleList();
  getCategoryData();
  getTagData();
});
</script>

<style scoped>
/* 全局样式优化 */
.home-page {
  font-family: "Segoe UI", "Microsoft YaHei", sans-serif;
  color: #333;
}

.nav-container {
  max-width: 1200px;
  /* margin: 0 auto; */
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
  /* margin: 20px auto; */
  display: flex;
  gap: 10px;
  padding: 0 20px;
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
  max-width: 1200px;
  /* margin: 0 auto; */
  padding: 0 20px;
}

.article-content {
  width: 100%;
}

/* 文章列表容器优化 */
.article-list-container {
  border-radius: 16px;
  /* box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05); */
  padding: 10px;
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

/* 标签导航样式优化 */
.tag-nav {
  max-width: 1200px;
  display: flex;
  overflow-x: auto;
  gap: 12px;
  padding: 10px 20px;
  scrollbar-width: thin;
  scrollbar-color: #d1e5fe #f5f7fa;
  margin-bottom: 10px;
  margin-top: 10px;
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

/* 分页优化 */
.pagination-container {
  margin-top: 30px;
  display: flex;
  justify-content: center;
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

  .search-input, .search-btn {
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
}
</style>
