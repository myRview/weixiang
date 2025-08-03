<template>
  <div class="my-articles">
    <div class="search-container">
      <el-card class="search-card">
        <!-- 搜索表单 -->
        <el-form :model="searchParam" class="search-form" inline>
          <el-form-item label="文章标题">
            <el-input
              v-model="searchParam.title"
              placeholder="请输入文章标题"
              clearable
            />
          </el-form-item>
          <el-form-item label="文章分类">
            <el-select
              v-model="searchParam.categoryId"
              style="width: 140px"
              placeholder="请选择分类"
              clearable
            >
              <el-option
                v-for="category in categoryData"
                :key="category.id"
                :label="category.name"
                :value="category.id"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="审核状态">
            <el-select
              v-model="searchParam.auditStatus"
              style="width: 180px"
              placeholder="请选择审核状态"
              clearable
            >
              <el-option value="0" label="待审核"></el-option>
              <el-option value="1" label="通过"></el-option>
              <el-option value="2" label="驳回"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="selectPage">搜索</el-button>
            <el-button @click="handleReset">重置</el-button>
            <el-button type="success" @click="handleAddArticle"
              >新增文章</el-button
            >
          </el-form-item>
        </el-form>
      </el-card>
    </div>
    <div class="card-container">
      <el-table :data="tableData" stripe border style="width: 100%">
        <el-table-column
          prop="id"
          label="文章ID"
          width="100"
          show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="title"
          label="文章标题"
          min-width="200"
          show-overflow-tooltip="true"
        >
          <template #default="scope">
            <div class="title-cell" @click="handleView(scope.row)">{{ scope.row.title }}</div>
          </template>
        </el-table-column>
        <el-table-column label="分类" width="100" show-overflow-tooltip="true">
          <template #default="scope">
            {{ getCategoryName(scope.row.categoryId) }}
          </template>
        </el-table-column>
        <el-table-column prop="publishStatus" label="发布状态" width="120">
          <template #default="scope">
            <el-tag :type="getStatusTagType(scope.row.publishStatus)">
              {{ getStatusText(scope.row.publishStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="auditStatus" label="审核状态" width="140">
          <template #default="scope">
            <el-tag 
              :type="getAuditStatusTagType(scope.row.auditStatus)"
              @click="handleAuditReason(scope.row)"
              :class="scope.row.auditStatus === 2 ? 'clickable-tag' : ''"
            >
              {{ getAuditStatusText(scope.row.auditStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          label="创建时间"
          width="180"
          show-overflow-tooltip="true"
        >
          <template #default="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="240">
          <template #default="scope">
            <el-button
              type="primary"
              size="small"
              @click="handleView(scope.row)"
              >查看</el-button
            >
            <el-button
              type="success"
              size="small"
              @click="handleEdit(scope.row)"
              >编辑</el-button
            >
            <el-button
              type="danger"
              size="small"
              @click="handleDelete(scope.row)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-container">
        <el-pagination
          :current-page="currentPage"
          :page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>
    <!-- 删除确认弹窗 -->
    <el-dialog v-model="deleteDialogVisible" title="确认删除" width="30%">
      <span>确定要删除这篇文章吗？此操作不可撤销。</span>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="deleteDialogVisible = false">取消</el-button>
          <el-button type="danger" @click="handleDeleteConfirm"
            >确认删除</el-button
          >
        </span>
      </template>
    </el-dialog>

    <!-- 审核原因弹窗 -->
    <el-dialog v-model="auditReasonDialogVisible" title="审核不通过原因" width="40%">
      <div class="audit-reason-content">
        <p>{{ currentAuditReason }}</p>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="auditReasonDialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref} from "vue";
import { ElMessage } from "element-plus";
import dayjs from "dayjs";
import {
  deleteArticle,
  selectArticlePageByAuthor,
} from "@/api/wenzhangguanli";
import { getCategoryList } from "@/api/wenzhangfenleiguanli";
import router from "@/router";

// 格式化日期
const formatDate = (date: string | number | Date) => {
  return dayjs(date).format("YYYY-MM-DD HH:mm:ss");
};

// 获取状态文本
const getStatusText = (status: number) => {
  switch (status) {
    case 0:
      return "草稿";
    case 1:
      return "已发布";
    default:
      return "未知";
  }
};

// 获取状态标签类型
const getStatusTagType = (status: number) => {
  switch (status) {
    case 0:
      return "warning";
    case 1:
      return "success";
    case 2:
      return "info";
    default:
      return "info";
  }
};
// 获取审核状态标签类型
const getAuditStatusTagType = (status: number) => {
  switch (status) {
    case 0:
      return "warning";
    case 1:
      return "success";
    case 2:
      return "danger";
    default:
      return "info";
  }
};

// 获取审核状态文本
const getAuditStatusText = (status: number) => {
  switch (status) {
    case 0:
      return "待审核";
    case 1:
      return "通过";
    case 2:
      return "驳回";
    default:
      return "未知";
  }
};

const getCategoryName = (categoryId: number) => {
  const category = categoryData.value.find((cat) => cat.id === categoryId);
  return category?.name || "未知分类";
};

// 搜索参数
const searchParam = ref<API.ArticleSearchParam>({
  pageNum: 1,
  pageSize: 10,
});

// 表格数据
const tableData = ref<API.ArticleVO[]>([]);
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);
const categoryData = ref<API.CategoryVO[]>([]); // 分类数据

// 加载文章列表
const selectPage = async () => {
  try {
    searchParam.value.pageNum = currentPage.value;
    searchParam.value.pageSize = pageSize.value;
    const res = await selectArticlePageByAuthor({ ...searchParam.value });
    if (res.data.code === 200) {
      tableData.value = res.data.data?.records || [];
      total.value = Number(res.data.data?.total) || 0;
    }
  } catch (err) {
    ElMessage.error("获取文章列表失败");
  }
};

// 分页处理
const handleSizeChange = (val: number) => {
  pageSize.value = val;
  selectPage();
};

const handleCurrentChange = (val: number) => {
  currentPage.value = val;
  selectPage();
};

// 重置搜索
const handleReset = () => {
  searchParam.value = {
    pageNum: 1,
    pageSize: 10,
    categoryId: undefined,
  };
  currentPage.value = 1;
  selectPage();
};
const deleteDialogVisible = ref(false);
const auditReasonDialogVisible = ref(false);
const currentArticle = ref<API.ArticleVO>();
const currentAuditReason = ref('');

// 查看审核原因
const handleAuditReason = (row: API.ArticleVO) => {
  if (row.auditStatus === 2) {
    currentArticle.value = row;
    currentAuditReason.value = row.auditReason || '未提供审核原因';
    auditReasonDialogVisible.value = true;
  }
};

// 查看文章
const handleView = async (row: API.ArticleVO) => {
  router.push({
    path: '/article-detail',
    query: { id: row.id }
  });
};

// 新增文章
const handleAddArticle = () => {
  router.push({
    path: '/edit-article'
  });
};

// 编辑文章
const handleEdit = async (row: API.ArticleVO) => {
  router.push({
    path: '/edit-article',
    query: { id: row.id }
  });
};

// 删除文章
const handleDelete = (row: API.ArticleVO) => {
  currentArticle.value = row;
  deleteDialogVisible.value = true;
};

// 确认删除
const handleDeleteConfirm = async () => {
  if (!currentArticle.value?.id) {
    ElMessage.error("文章ID不存在");
    return;
  }

  try {
    const res = await deleteArticle({id: currentArticle.value.id});
    if (res.data.code === 200) {
      ElMessage.success("删除成功");
      deleteDialogVisible.value = false;
      selectPage();
    } else {
      ElMessage.error(res.data.message || "删除失败");
    }
  } catch (err) {
    ElMessage.error("删除操作失败");
  }
};

const getCategoryData = async () => {
  try {
    const res = await getCategoryList({ categoryName: "" });
    if (res.data.code === 200) {
      categoryData.value = res.data.data || [];
    }
  } catch (err) {
    ElMessage.error("获取分类列表失败");
  }
};

onMounted(() => {
  selectPage();
  getCategoryData();
});
</script>

<style scoped>
.my-articles {
  padding: 20px;
  max-width: 1400px;
  margin: 0 auto;
}

.search-container {
  margin-bottom: 20px;
}

/* 搜索表单优化 */
.search-form {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  align-items: center;
}

.search-form .el-form-item {
  margin-bottom: 0;
}

.search-form .el-form-item__label {
  width: 80px;
  text-align: right;
}

.search-form .el-input {
  width: 200px;
}

/* 按钮组样式 */
.search-form .el-form-item:last-child {
  margin-left: 10px;
}

.search-form .el-button {
  padding: 8px 16px;
}

/* 文章标题单元格样式 */
.title-cell {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  cursor: pointer;
  color: #1890ff;
}

/* 卡片容器样式 */
.card-container {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  padding: 20px;
  margin-bottom: 20px;
}

/* 表格样式优化 */
::v-deep .el-table {
  border-radius: 6px;
  overflow: hidden;
}

::v-deep .el-table th {
  background-color: #f5f7fa;
  font-weight: 500;
  color: #303133;
}

::v-deep .el-table tr:hover > td {
  background-color: #f9fafc !important;
}

::v-deep .el-table__row--striped td {
  background-color: #fafafa;
}

.pagination-container {
  margin-top: 15px;
  text-align: right;
}

.clickable-tag {
  cursor: pointer;
  text-decoration: underline;
  text-decoration-style: dashed;
}

@media (max-width: 768px) {
  .my-articles {
    padding: 10px;
  }

  .card-container {
    padding: 15px;
  }

  ::v-deep .el-table {
    font-size: 13px;
  }
}
</style>
