<template>
  <div class="permission-management">
    <div class="card-container">
      <el-button type="success" @click="handleAdd" style="margin-bottom: 10px">添加权限</el-button>
      <el-table :data="tableData" stripe border style="width: 100%">
        <el-table-column prop="id" label="权限ID" />
        <el-table-column prop="permissionName" label="权限名称" min-width="150" />
        <el-table-column prop="permissionCode" label="权限编码" min-width="200" />
        <el-table-column label="操作" >
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
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
    <el-dialog v-model="dialogVisible" width="600px" :before-close="handleDialogClose">
      <template #header>
        <div class="custom-dialog-header">{{ dialogType === "add" ? "新增权限" : "编辑权限" }}</div>
      </template>
      <el-form :model="formData" label-width="80px">
        <el-form-item label="权限名称">
          <el-input v-model="formData.permissionName" placeholder="请输入权限名称" />
        </el-form-item>
        <el-form-item label="权限编码">
          <el-input v-model="formData.permissionCode" placeholder="请输入权限编码" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleDialogConfirm">确认</el-button>
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
  deletePermission, 
  getPermissionPage, 
  savePermission, 
} from "@/api/jiaosequanxianguanli";
import { StatusEnums } from "@/enums/status.enum";

const formatDate = (date: string | number | Date) => 
  dayjs(date).format("YYYY-MM-DD HH:mm:ss");

const dialogVisible = ref(false);
const dialogType = ref<"add" | "edit">("add");
const formData = ref<API.PermissionVO>({});
const tableData = ref<API.PermissionVO[]>([]);
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);
const searchParams = ref<API.PermissionSearchParam>({
  
})

const selectPage = async () => {
  try {
    searchParams.value = {
      ...searchParams.value,
      pageNum: currentPage.value,
      pageSize: pageSize.value,
    };
    const res = await getPermissionPage(searchParams.value);
    if (res.data.code === 200) {
      tableData.value = res.data.data?.records || [];
      total.value = Number(res.data.data?.total) || 0;
    }
  } catch (err) {}
};
const handleSizeChange = (val: number) => {
  pageSize.value = val;
  currentPage.value = 1;
  selectPage();
}
const handleCurrentChange = (val: number) => {
  currentPage.value = val;
  selectPage();
}

const handleEdit = (permission: API.PermissionVO) => {
  ElMessage.success(`编辑权限功能尚未实现`);
}

const handleDelete = async (permission: API.PermissionVO) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除权限「${permission.permissionName}」吗？`,
      "提示",
      { confirmButtonText: "确定", cancelButtonText: "取消", type: "warning" }
    );
    const res = await deletePermission([permission.id!]);
    if (res.data.code === 200) {
      ElMessage.success("删除成功");
      selectPage();
    }
  } catch (err) {}
};

const handleDialogClose = () => (dialogVisible.value = false);

const handleDialogConfirm = async () => {
  if (dialogType.value === "add") {
    const res = await savePermission(formData.value);
    if (res.data.code !== 200) {
      ElMessage.error(res.data.message);
      return;
    }
    ElMessage.success("添加成功");
  }
  dialogVisible.value = false;
  selectPage();
};

const handleAdd = () => {
  dialogVisible.value = true;
  dialogType.value = "add";
  formData.value = {};
};

onMounted(() => selectPage());
</script>

<style scoped>
.permission-management {
  padding: 20px;
  max-width: 1400px;
  margin: 0 auto;
}


.pagination-container {
  margin-top: 15px;
  text-align: right;
}
.card-container {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  padding: 20px;
  margin-bottom: 20px;
}
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
  .permission-management {
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