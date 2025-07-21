<template>
  <div class="plan-management">
    <div class="search-container">
      <el-card class="search-card">
        <!-- 搜索表单 -->
        <el-form :model="searchParam" class="search-form" inline>
          <el-form-item label="套餐名称">
            <el-input
              v-model="searchParam.name"
              placeholder="请输入套餐名称"
              clearable
            />
          </el-form-item>
          <el-form-item label="状态">
            <el-select
              v-model="searchParam.status"
              style="width: 120px"
              placeholder="请选择状态"
              clearable
            >
              <el-option
                v-for="(item, key) in StatusEnums"
                :key="key"
                :value="item.code"
                :label="item.description"
              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="selectPage">搜索</el-button>
            <el-button @click="handleReset">重置</el-button>
            <el-button
              type="success"
              @click="handleAdd"
              style="margin-left: 10px"
              >添加套餐</el-button
            >
          </el-form-item>
        </el-form>
      </el-card>
    </div>
    <div class="card-container">
      <el-table :data="tableData" stripe border style="width: 100%">
        <el-table-column
          prop="id"
          label="套餐ID"
          width="100"
          show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="name"
          label="套餐名称"
          min-width="150"
          show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column prop="price" label="套餐价格" width="120">
          <template #default="scope">
            ¥{{ scope.row.price.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column
          prop="description"
          label="套餐描述"
          min-width="200"
          show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="validityDays"
          label="有效期(天)"
          width="120"
        ></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">{{
              scope.row.status === 1 ? "启用" : "禁用"
            }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" width="180">
          <template #default="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="140">
          <template #default="scope">
            <el-button
              type="primary"
              size="small"
              style="margin-right: 5px"
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
    <!-- 套餐新增和编辑弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      width="600px"
      :before-close="handleDialogClose"
    >
      <template #header>
        <div class="custom-dialog-header">
          {{ dialogType === "add" ? "新增套餐" : "编辑套餐" }}
        </div>
      </template>
      <el-form :model="formData" label-width="80px">
        <el-form-item label="套餐名称">
          <el-input v-model="formData.name" placeholder="请输入套餐名称" />
        </el-form-item>
        <el-form-item label="套餐价格">
          <el-input
            type="number"
            v-model="formData.price"
            placeholder="请输入套餐价格"
          />
        </el-form-item>
        <el-form-item label="套餐描述">
          <el-input
            type="textarea"
            v-model="formData.description"
            placeholder="请输入套餐描述"
          />
        </el-form-item>
        <el-form-item label="有效期(天)">
          <el-input
            type="number"
            min="0"
            v-model="formData.validityDays"
            placeholder="请输入有效期"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="formData.status" placeholder="请选择状态">
            <el-option
              v-for="(item, key) in StatusEnums"
              :key="key"
              :value="item.code"
              :label="item.description"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleDialogConfirm"
            >确认</el-button
          >
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import dayjs from "dayjs";
import {
  addPlan,
  deletePlan,
  selectPlanPage,
  updatePlan,
} from "@/api/huiyuantaocanguanli";
import { StatusEnums } from "@/enums/status.enum";
const formatDate = (date: string | number | Date) => {
  return dayjs(date).format("YYYY-MM-DD HH:mm:ss");
};

// 弹框相关变量
const dialogVisible = ref(false);
const dialogType = ref<"add" | "edit">("add");
const formData = ref<API.MemberPlanVO>({});

// 套餐数据
const tableData = ref<API.MemberPlanVO[]>([]);
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);
const searchParam = ref<API.PlanSearchParam>({});
const selectPage = async () => {
  try {
    searchParam.value.pageNum = currentPage.value;
    searchParam.value.pageSize = pageSize.value;
    const res = await selectPlanPage(searchParam.value);
    if (res.data.code === 200) {
      tableData.value = res.data.data?.records||[];
      total.value = Number(res.data.data?.total) || 0;
    }
  } catch (err) {}
};

// 编辑套餐
const handleEdit = (plan: API.MemberPlanVO) => {
  dialogVisible.value = true;
  dialogType.value = "edit";
  formData.value = { ...plan };
};

// 删除套餐
const handleDelete = async (plan: API.MemberPlanVO) => {
  try {
    await ElMessageBox.confirm(`确定要删除套餐「${plan.name}」吗？`, "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    });
    const res = await deletePlan({ id: plan.id });
    if (res.data.code === 200) {
      ElMessage.success("删除成功");
      selectPage();
    }
  } catch (err) {
    // 用户取消操作，无需处理
  }
};

// 关闭弹框
const handleDialogClose = () => {
  dialogVisible.value = false;
};
// 确认提交
const handleDialogConfirm = async () => {
  if (dialogType.value === "add") {
    // 添加逻辑
    const res = await addPlan(formData.value);
    if (res.data.code !== 200) {
      ElMessage.error(res.data.message);
      return;
    }
    ElMessage.success("添加成功");
  } else if (dialogType.value === "edit") {
    // 编辑逻辑
    if (formData.value.id === undefined) {
      ElMessage.error("编辑失败");
      return;
    }
    formData.value.createTime = undefined;
    const res = await updatePlan(formData.value);
    if (res.data.code !== 200) {
      ElMessage.error(res.data.message);
      return;
    }
    ElMessage.success("编辑成功");
  }
  dialogVisible.value = false;
  selectPage();
};
// 添加套餐
const handleAdd = () => {
  dialogVisible.value = true;
  dialogType.value = "add";
  formData.value = {};
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
// 重置事件
const handleReset = () => {
  searchParam.value = {};
  currentPage.value = 1;
  selectPage();
};
onMounted(() => {
  selectPage();
});
</script>

<style scoped>
.plan-management {
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
.custom-dialog-header {
  text-align: center;
  font-size: 18px;
  font-weight: bold;
  padding: 20px 0 10px;
}
:deep(.el-dialog__header) {
  padding: 0;
}
::v-deep .el-dialog {
  border-radius: 12px;
  box-shadow: 0 4px 24px rgba(0, 0, 0, 0.12);
}
::v-deep .el-dialog__header {
  padding: 20px 24px 16px;
  border-bottom: 1px solid #f0f2f5;
}
::v-deep .el-dialog__title {
  font-size: 18px;
  font-weight: 600;
  color: #1f2d3d;
  text-align: center;
}
::v-deep .el-dialog__body {
  padding: 24px;
}
::v-deep .el-form-item {
  margin-bottom: 18px;
}
::v-deep .el-input__inner {
  border-radius: 6px;
}
::v-deep .el-input--textarea .el-textarea__inner {
  border-radius: 6px;
}
::v-deep .el-select {
  width: 100%;
}
::v-deep .el-dialog__footer {
  padding: 0 24px 24px;
}
.dialog-footer {
  display: flex;
  justify-content: center;
  gap: 16px;
}
::v-deep .el-button--primary {
  background-color: #409eff;
  border-color: #409eff;
}
::v-deep .el-button--primary:hover {
  background-color: #66b1ff;
  border-color: #66b1ff;
}

@media (max-width: 768px) {
  .plan-management {
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
