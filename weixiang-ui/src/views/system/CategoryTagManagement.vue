<template>
  <div class="category-tag-management">
    <!-- 标签页导航 -->
    <el-tabs
      v-model="activeTab"
      type="card"
      class="custom-tabs"
      @tab-change="handleTabChange"
    >
      <el-tab-pane label="分类管理" name="category">
        <template #label>
          <span>分类管理</span>
        </template>
      </el-tab-pane>
      <el-tab-pane label="标签管理" name="tag">
        <template #label>
          <span>标签管理</span>
        </template>
      </el-tab-pane>
    </el-tabs>
    <!-- 分类管理内容 -->
    <div v-show="activeTab === 'category'" class="tab-content">
      <!-- 分类搜索区域 -->
      <div class="search-container">
        <el-card class="search-card">
          <el-form
            class="search-form"
            :model="categorySearchParam"
            label-width="80px"
          >
            <el-row :gutter="24" class="search-row">
              <el-col :span="10" :xs="24" class="input-col">
                <el-form-item label="分类名称">
                  <el-input
                    v-model="categorySearchParam.categoryName"
                    placeholder="请输入分类名称"
                    clearable
                    @keyup.enter="handleCategorySearch"
                    class="search-input"
                  ></el-input>
                </el-form-item>
              </el-col>
              <!-- 操作按钮区域 -->
              <el-col :span="6" :xs="24" class="action-col">
                <div class="action-buttons">
                  <el-button
                    type="primary"
                    @click="handleCategorySearch"
                    :loading="categoryLoading"
                    class="search-btn"
                  >
                    <el-icon><Search /></el-icon>
                    搜索
                  </el-button>
                  <el-button @click="resetCategorySearch" class="reset-btn">
                    重置
                  </el-button>
                </div>
              </el-col>
              <el-col :span="4" :xs="24" class="add-col">
                <el-button
                  type="success"
                  :icon="Plus"
                  @click="handleAddCategory"
                  class="add-btn"
                >
                  新增分类
                </el-button>
              </el-col>
            </el-row>
          </el-form>
        </el-card>
      </div>
      <div class="card-container">
        <el-table
          :data="categoryTableData"
          stripe
          border
          style="width: 100%"
        >
          <el-table-column
            prop="id"
            label="分类ID"
            width="200"
          ></el-table-column>
          <el-table-column
            prop="name"
            label="分类名称"
            min-width="140"
          ></el-table-column>
          <!-- <el-table-column
            prop="description"
            label="分类描述"
            min-width="200"
          ></el-table-column> -->
          <el-table-column label="创建日期" min-width="180">
            <template #default="scope">{{
              formatDate(scope.row.createTime)
            }}</template>
          </el-table-column>
          <el-table-column label="操作" min-width="160" fixed="right">
            <template #default="scope">
              <el-button
                type="primary"
                size="small"
                @click="handleCategoryEdit(scope.row)"
                class="edit-btn"
              >
                编辑
              </el-button>
              <el-button
                type="danger"
                size="small"
                @click="handleCategoryDelete(scope.row.id)"
                class="delete-btn"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          :current-page="categoryCurrentPage"
          :page-size="categoryPageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="categoryTotal"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleCategorySizeChange"
          @current-change="handleCategoryCurrentChange"
          class="pagination"
        />
      </div>
    </div>
    <!-- 标签管理内容 -->
    <div v-show="activeTab === 'tag'" class="tab-content">
      <!-- 标签搜索区域 -->
      <div class="search-container">
        <el-card class="search-card">
          <el-form
            class="search-form"
            :model="tagSearchParam"
            label-width="80px"
          >
            <el-row :gutter="24" class="search-row">
              <!-- 输入框区域 -->
              <el-col :span="10" :xs="24" class="input-col">
                <el-form-item label="标签名称">
                  <el-input
                    v-model="tagSearchParam.tagName"
                    placeholder="请输入标签名称"
                    clearable
                    @keyup.enter="handleTagSearch"
                    class="search-input"
                  ></el-input>
                </el-form-item>
              </el-col>
              <!-- 操作按钮区域 -->
              <el-col :span="6" :xs="24" class="action-col">
                <div class="action-buttons">
                  <el-button
                    type="primary"
                    @click="handleTagSearch"
                    :loading="tagLoading"
                    class="search-btn"
                  >
                    <el-icon><Search /></el-icon>
                    搜索
                  </el-button>
                  <el-button @click="resetTagSearch" class="reset-btn">
                    重置
                  </el-button>
                </div>
              </el-col>
              <!-- 新增按钮区域 -->
              <el-col :span="4" :xs="24" class="add-col">
                <el-button
                  type="success"
                  :icon="Plus"
                  @click="handleAddTag"
                  class="add-btn"
                >
                  新增标签
                </el-button>
              </el-col>
            </el-row>
          </el-form>
        </el-card>
      </div>
      <div class="card-container">
        <el-table
          :data="tagTableData"
          stripe
          border
          style="width: 100%"
        >
          <el-table-column
            prop="id"
            label="标签ID"
            width="200"
          ></el-table-column>
          <el-table-column
            prop="name"
            label="标签名称"
            min-width="140"
          ></el-table-column>
          <el-table-column label="创建日期" min-width="180">
            <template #default="scope">{{
              formatDate(scope.row.createTime)
            }}</template>
          </el-table-column>
          <el-table-column label="操作" min-width="160" fixed="right">
            <template #default="scope">
              <el-button
                type="primary"
                size="small"
                @click="handleTagEdit(scope.row)"
                class="edit-btn"
              >
                编辑
              </el-button>
              <el-button
                type="danger"
                size="small"
                @click="handleTagDelete(scope.row.id)"
                class="delete-btn"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          :current-page="tagCurrentPage"
          :page-size="tagPageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="tagTotal"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleTagSizeChange"
          @current-change="handleTagCurrentChange"
          class="pagination"
        />
      </div>
    </div>
    <!-- 分类表单弹窗 -->
    <el-dialog
      v-model="categoryDialogVisible"
      :title="categoryDialogTitle"
      width="500px"
      @close="resetCategoryForm"
    >
      <el-form
        ref="categoryFormRef"
        :model="categoryForm"
        :rules="categoryRules"
        label-width="80px"
      >
        <el-form-item label="分类名称" prop="name">
          <el-input
            v-model="categoryForm.name"
            placeholder="请输入分类名称"
            maxlength="50"
          ></el-input>
        </el-form-item>
        <el-form-item label="分类描述" prop="description">
          <el-input
            v-model="categoryForm.description"
            placeholder="请输入分类描述"
            type="textarea"
            rows="4"
            maxlength="200"
          ></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="categoryDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCategorySave">确定</el-button>
      </template>
    </el-dialog>
    <!-- 标签表单弹窗 -->
    <el-dialog
      v-model="tagDialogVisible"
      :title="tagDialogTitle"
      width="500px"
      @close="resetTagForm"
    >
      <el-form
        ref="tagFormRef"
        :model="tagForm"
        :rules="tagRules"
        label-width="80px"
      >
        <el-form-item label="标签名称" prop="name">
          <el-input
            v-model="tagForm.name"
            placeholder="请输入标签名称"
            maxlength="50"
          ></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="tagDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleTagSave">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import {
  selectCategoryPage,
  deleteCategory,
  saveCategory,
  updateCategory,
} from "@/api/wenzhangfenleiguanli";
import {
  selectTagPage,
  deleteTag,
  saveTag,
  updateTag,
} from "@/api/wenzhangbiaoqianguanli";
import { ElMessage, ElMessageBox, ElLoading, ElForm } from "element-plus";
import { ref, reactive, getCurrentInstance, onUnmounted, onMounted } from "vue";
import dayjs from "dayjs";
import { Search, Plus } from "@element-plus/icons-vue";

const activeTab = ref("category");
// 加载状态管理（分类加载状态之前未正确控制，导致渲染异常）
const categoryLoading = ref(false);
const tagLoading = ref(false);

// 分类相关
const categorySearchParam = ref<API.CategorySearchParam>({});
const categoryCurrentPage = ref(1);
const categoryPageSize = ref(10);
const categoryTotal = ref(0);
const categoryTableData = ref<API.CategoryVO[]>([]);

// 标签相关
const tagSearchParam = ref<API.TagSearchParam>({});
const tagCurrentPage = ref(1);
const tagPageSize = ref(10);
const tagTotal = ref(0);
const tagTableData = ref<API.TagVO[]>([]);

// 分类表单相关
const categoryDialogVisible = ref(false);
const categoryDialogTitle = ref("新增分类");
const categoryForm = reactive<API.CategoryVO>({
  id: undefined,
  name: "",
  description: "",
});
const categoryRules = reactive({
  name: [
    { required: true, message: "请输入分类名称", trigger: "blur" },
    { max: 50, message: "分类名称不能超过50个字符", trigger: "blur" },
  ],
  description: [
    { max: 200, message: "分类描述不能超过200个字符", trigger: "blur" },
  ],
});

// 标签表单相关
const tagDialogVisible = ref(false);
const tagDialogTitle = ref("新增标签");
const tagForm = reactive<API.TagVO>({
  id: undefined,
  name: "",
});
const tagRules = reactive({
  name: [
    { required: true, message: "请输入标签名称", trigger: "blur" },
    { max: 50, message: "标签名称不能超过50个字符", trigger: "blur" },
  ],
});

// 获取表单引用
const { proxy } = getCurrentInstance() as any;
const categoryFormRef = ref<InstanceType<typeof ElForm>>();
const tagFormRef = ref<InstanceType<typeof ElForm>>();

// 日期格式化
const formatDate = (date: string | number | Date) => {
  return dayjs(date).format("YYYY-MM-DD HH:mm:ss");
};

// 标签页切换时重新加载数据
const handleTabChange = (tabName: string) => {
  if (tabName === "category") {
    handleCategorySearch();
  } else {
    handleTagSearch();
  }
};

// 【核心修改】分类搜索：补充加载状态控制+错误提示，避免渲染循环
const handleCategorySearch = async () => {
  categoryLoading.value = true; // 开始加载：锁定状态，防止重复请求
  try {
    categorySearchParam.value.pageNum = categoryCurrentPage.value;
    categorySearchParam.value.pageSize = categoryPageSize.value;
    const res = await selectCategoryPage(categorySearchParam.value);

    if (res.data.code === 200) {
      categoryTableData.value = res.data.data?.records || [];
      categoryTotal.value = Number(res.data.data?.total) || 0;
    } else {
      ElMessage.error("获取分类数据失败：" + (res.data.message || "未知错误"));
      categoryTableData.value = []; // 清空异常数据，避免表格渲染混乱
      categoryTotal.value = 0;
    }
  } catch (error) {
    console.error("分类搜索接口异常：", error);
    ElMessage.error("网络错误，无法获取分类数据");
    categoryTableData.value = [];
    categoryTotal.value = 0;
  } finally {
    categoryLoading.value = false; // 无论成功/失败，都关闭加载状态
  }
};

// 重置分类搜索
const resetCategorySearch = () => {
  categorySearchParam.value = {};
  categoryCurrentPage.value = 1;
  handleCategorySearch();
};

// 分类分页大小改变
const handleCategorySizeChange = (val: number) => {
  categoryPageSize.value = val;
  categoryCurrentPage.value = 1;
  handleCategorySearch();
};

// 分类页码改变
const handleCategoryCurrentChange = (val: number) => {
  categoryCurrentPage.value = val;
  handleCategorySearch();
};

// 删除分类
const handleCategoryDelete = async (id: number) => {
  try {
    const confirmResult = await ElMessageBox.confirm(
      "确定要删除该分类吗？删除后相关数据可能受到影响",
      "警告",
      { type: "warning" }
    );
    if (confirmResult === "confirm") {
      const loading = ElLoading.service({ text: "删除中..." });
      const res = await deleteCategory({ id });
      if (res.data.code === 200) {
        ElMessage.success("分类删除成功");
        handleCategorySearch();
      } else {
        ElMessage.error("删除分类失败: " + (res.data.message || "未知错误"));
      }
      loading.close();
    }
  } catch (error) {
    if (error !== "cancel") {
      ElMessage.error("删除分类失败");
    }
  }
};

// 新增分类
const handleAddCategory = () => {
  categoryDialogTitle.value = "新增分类";
  resetCategoryForm();
  categoryDialogVisible.value = true;
};

// 编辑分类
const handleCategoryEdit = (row: API.CategoryVO) => {
  categoryDialogTitle.value = "编辑分类";
  categoryForm.id = row.id;
  categoryForm.name = row.name || "";
  categoryForm.description = row.description || "";
  categoryDialogVisible.value = true;
};

// 重置分类表单
const resetCategoryForm = () => {
  categoryForm.id = undefined;
  categoryForm.name = "";
  categoryForm.description = "";
  categoryFormRef.value?.resetFields();
};

// 保存分类
const handleCategorySave = async () => {
  if (!categoryFormRef.value) return;
  try {
    await categoryFormRef.value.validate();
    const loading = ElLoading.service({ text: "保存中..." });
    let res;
    if (categoryForm.id) {
      // 修改分类
      res = await updateCategory(categoryForm);
    } else {
      // 新增分类
      res = await saveCategory(categoryForm);
    }
    if (res.data.code === 200) {
      ElMessage.success(categoryForm.id ? "分类修改成功" : "分类添加成功");
      categoryDialogVisible.value = false;
      handleCategorySearch();
    } else {
      ElMessage.error(
        res.data.message || (categoryForm.id ? "分类修改失败" : "分类添加失败")
      );
    }
    loading.close();
  } catch (error) {
    if (typeof error !== "string") {
      ElMessage.error("表单验证失败，请检查输入");
    }
  }
};

// 标签搜索（原有逻辑正确，保持不变）
const handleTagSearch = async () => {
  tagLoading.value = true;
  try {
    tagSearchParam.value.pageNum = tagCurrentPage.value;
    tagSearchParam.value.pageSize = tagPageSize.value;
    const res = await selectTagPage(tagSearchParam.value);
    if (res.data.code === 200) {
      tagTableData.value = res.data.data?.records || [];
      tagTotal.value = Number(res.data.data?.total) || 0;
    } else {
      ElMessage.error("获取标签数据失败");
    }
  } catch (error) {
    ElMessage.error("网络错误，获取标签数据失败");
  } finally {
    tagLoading.value = false;
  }
};

// 重置标签搜索
const resetTagSearch = () => {
  tagSearchParam.value = {};
  tagCurrentPage.value = 1;
  handleTagSearch();
};

// 标签分页大小改变
const handleTagSizeChange = (val: number) => {
  tagPageSize.value = val;
  tagCurrentPage.value = 1;
  handleTagSearch();
};

// 标签页码改变
const handleTagCurrentChange = (val: number) => {
  tagCurrentPage.value = val;
  handleTagSearch();
};

// 删除标签
const handleTagDelete = async (id: number) => {
  try {
    const confirmResult = await ElMessageBox.confirm(
      "确定要删除该标签吗？删除后相关数据可能受到影响",
      "警告",
      { type: "warning" }
    );
    if (confirmResult === "confirm") {
      const loading = ElLoading.service({ text: "删除中..." });
      const res = await deleteTag({ id });
      if (res.data.code === 200) {
        ElMessage.success("标签删除成功");
        handleTagSearch();
      } else {
        ElMessage.error("删除标签失败: " + (res.data.message || "未知错误"));
      }
      loading.close();
    }
  } catch (error) {
    if (error !== "cancel") {
      ElMessage.error("删除标签失败");
    }
  }
};

// 新增标签
const handleAddTag = () => {
  tagDialogTitle.value = "新增标签";
  resetTagForm();
  tagDialogVisible.value = true;
};

// 编辑标签
const handleTagEdit = (row: API.TagVO) => {
  tagDialogTitle.value = "编辑标签";
  tagForm.id = row.id;
  tagForm.name = row.name || "";
  tagDialogVisible.value = true;
};

// 重置标签表单
const resetTagForm = () => {
  tagForm.id = undefined;
  tagForm.name = "";
  tagFormRef.value?.resetFields();
};

// 保存标签
const handleTagSave = async () => {
  if (!tagFormRef.value) return;
  try {
    await tagFormRef.value.validate();
    const loading = ElLoading.service({ text: "保存中..." });
    let res;
    if (tagForm.id) {
      // 修改标签
      res = await updateTag(tagForm);
    } else {
      // 新增标签
      res = await saveTag(tagForm);
    }
    if (res.data.code === 200) {
      ElMessage.success(tagForm.id ? "标签修改成功" : "标签添加成功");
      tagDialogVisible.value = false;
      handleTagSearch();
    } else {
      ElMessage.error(
        res.data.message || (tagForm.id ? "标签修改失败" : "标签添加失败")
      );
    }
    loading.close();
  } catch (error) {
    if (typeof error !== "string") {
      ElMessage.error("表单验证失败，请检查输入");
    }
  }
};

// 初始化数据
onMounted(() => {
  handleCategorySearch();
});

// 组件卸载时清理弹窗状态
onUnmounted(() => {
  categoryDialogVisible.value = false;
  tagDialogVisible.value = false;
});
</script>
<style scoped>
.category-tag-management {
  padding: 20px;
}
/* 标签页样式优化 */
.custom-tabs {
  margin-bottom: 20px;
  --el-tabs-card-active-color: #409eff;
}
.el-tabs__item.is-active {
  font-weight: 600;
}
.tab-content {
  animation: fadeIn 0.3s ease-in-out;
}
/* 搜索区域优化样式 */
.search-container {
  margin-bottom: 20px;
}
.search-card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}
.search-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}
.search-form {
  width: 100%;
}
.search-row {
  display: flex;
  align-items: center;
}
.input-col {
  display: flex;
  align-items: center;
}
/* 输入框样式优化 */
.search-input {
  width: 100%;
  transition: all 0.2s;
}
::v-deep .search-input .el-input__input {
  height: 40px;
  line-height: 40px;
}
::v-deep .search-input .el-input__input:focus {
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}
/* 按钮区域样式 */
.action-col {
  display: flex;
  align-items: center;
}
.action-buttons {
  display: flex;
  align-items: center;
  gap: 12px;
}
.add-col {
  display: flex;
  align-items: center;
  justify-content: flex-start;
}
.add-btn {
  width: 100%;
  height: 40px;
}
.search-btn,
.reset-btn {
  height: 40px;
  padding: 0 16px;
}
/* 表格容器样式 */
.card-container {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  padding: 20px;
  margin-bottom: 20px;
  transition: all 0.3s ease;
}
.card-container:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
}
/* 表格样式优化 */
::v-deep .el-table {
  border-radius: 6px;
  overflow: hidden;
}
::v-deep .el-table th {
  background-color: #f9fafb;
  font-weight: 600;
  color: #333;
}
::v-deep .el-table__row {
  height: 52px;
  transition: background-color 0.2s ease;
}
::v-deep .el-table__row:hover > td {
  background-color: #f0f7ff !important;
}
::v-deep .el-table--striped .el-table__body tr.el-table__row--striped td {
  background-color: #fafafa;
}
::v-deep .el-table__current-row > td {
  background-color: #e6f7ff !important;
}
/* 操作按钮样式 */
.edit-btn {
  margin-right: 8px;
  background-color: #409eff;
  border-color: #409eff;
}
.edit-btn:hover {
  background-color: #66b1ff;
  border-color: #66b1ff;
}
.delete-btn {
  background-color: #ff4d4f;
  border-color: #ff4d4f;
}
.delete-btn:hover {
  background-color: #ff7875;
  border-color: #ff7875;
}
/* 分页样式 */
.pagination {
  margin-top: 20px;
  text-align: right;
  padding: 10px 0;
}
/* 动画效果 */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
/* 响应式调整 */
@media (max-width: 768px) {
  .category-tag-management {
    padding: 10px;
  }
  .search-row {
    flex-direction: column;
    gap: 12px;
  }
  .action-col,
  .add-col {
    width: 100%;
    margin-top: 8px;
  }
  .action-buttons {
    width: 100%;
    flex-wrap: wrap;
  }
  .search-btn,
  .reset-btn,
  .add-btn {
    flex: 1;
    min-width: 120px;
  }
}
</style>
