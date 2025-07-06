<template>
  <div class="role-management">
    <div class="card-container">
      <el-button type="success" @click="handleAdd" style="margin-bottom: 10px">添加角色</el-button>
      <el-table :data="tableData" stripe border style="width: 100%">
        <el-table-column prop="id" label="角色ID" />
        <el-table-column prop="roleName" label="角色名称" min-width="150" />
        <el-table-column prop="roleCode" label="角色编码" min-width="200" />
        <el-table-column label="操作" >
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleAddPermission(scope.row)">添加权限</el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog v-model="dialogVisible" width="600px" :before-close="handleDialogClose">
      <template #header>
        <div class="custom-dialog-header">{{ dialogType === "add" ? "新增角色" : "编辑角色" }}</div>
      </template>
      <el-form :model="formData" label-width="80px">
        <el-form-item label="角色名称">
          <el-input v-model="formData.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色编码">
          <el-select v-model="formData.roleCode" placeholder="请选择角色编码">
            <el-option
              v-for="option in USER_ROLE_OPTIONS"
              :key="option.value"
              :label="option.label"
              :value="option.value"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleDialogConfirm">确认</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 添加权限对话框 -->
    <el-dialog v-model="permissionDialogVisible" title="分配权限" width="800px">
      <div class="permission-dialog-container">
        <div class="current-role-info">
          <span style="font-weight: 600;color: #303133;">当前角色: {{ currentRole?.roleName || '未知角色' }}</span>
          <el-tag type="info" effect="light">{{ currentRole?.roleCode || '未知编码' }}</el-tag>
        </div>
        <el-scrollbar height="400px" class="permission-scrollbar">
          <div class="permission-list">
            <el-checkbox v-model="checkAll" @change="(val) => checkedPermissionIds = val ? permissionData.map(p => p.id) : []">全选</el-checkbox>
            <el-checkbox-group v-model="checkedPermissionIds">
              <div v-for="permission in permissionData" :key="permission.id" class="permission-item">
                <el-checkbox :label="permission.id" @change="() => checkAll = checkedPermissionIds.length === permissionData.length">
                  <div class="permission-info">
                    {{ permission.permissionName }}({{ permission.permissionCode }})
                  </div>
                </el-checkbox>
              </div>
            </el-checkbox-group>
          </div>
        </el-scrollbar>
      </div>
      <template #footer>
        <div class="permission-footer">
          <el-button @click="permissionDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="savePermissions">保存</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import dayjs from "dayjs";
import {
  deleteRole,
  getPermissionList,
  getRolePermission,
  saveRole,
  selectAllRole,
  saveRolePermission
} from "@/api/jiaosequanxianguanli";
import { StatusEnums } from "@/enums/status.enum";
import { USER_ROLE_OPTIONS } from "@/enums/user.enum";

const formatDate = (date: string | number | Date) =>
  dayjs(date).format("YYYY-MM-DD HH:mm:ss");

const dialogVisible = ref(false);
const permissionDialogVisible = ref(false);
const dialogType = ref<"add" | "edit">("add");
const formData = ref<API.RoleVO>({});
const tableData = ref<API.RoleVO[]>([]);
const permissionData = ref<API.PermissionVO[]>([]);
const checkedPermissionIds = ref<number[]>([]);
const currentRole = ref<API.RoleVO | null>(null);

const selectPage = async () => {
  try {
    const res = await selectAllRole();
    if (res.data.code === 200) {
      tableData.value = res.data.data || [];
    }
  } catch (err) {}
};

const handleAddPermission = async (role: API.RoleVO) => {
  try {
    currentRole.value = role;
    const res = await getRolePermission({ id: role.id });
    if (res.data.code === 200) {
      // 当前角色的权限数据，存储为权限ID数组
      checkedPermissionIds.value = res.data.data?.map(p => p.id) || [];
      permissionDialogVisible.value = true;
    }
  } catch (err) {
    ElMessage.error("获取权限失败");
  }
};

const handleDelete = async (role: API.RoleVO) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除角色「${role.roleName}」吗？`,
      "提示",
      { confirmButtonText: "确定", cancelButtonText: "取消", type: "warning" }
    );
    const res = await deleteRole({ id: role.id });
    if (res.data.code === 200) {
      ElMessage.success("删除成功");
      selectPage();
    }
  } catch (err) {}
};

const handleDialogClose = () => (dialogVisible.value = false);

const handleDialogConfirm = async () => {
  if (dialogType.value === "add") {
    try{
      const res = await saveRole(formData.value);
    if (res.data.code !== 200) {
      ElMessage.error(res.data.message);
      return;
    }
    ElMessage.success("添加成功");
    }catch(err){
      ElMessage.error("添加失败");
    }
  }
  dialogVisible.value = false;
  selectPage();
};

const handleAdd = () => {
  dialogVisible.value = true;
  dialogType.value = "add";
  formData.value = {};
};

const getAllPermissions = async () => {
  try {
    const res = await getPermissionList();
    if (res.data.code === 200) {
      permissionData.value = res.data.data || [];
    }
  } catch (err) {}
};
const checkAll = ref(false);

const savePermissions = async () => {
  if (!currentRole.value?.id) return;
  
  try {
    const res = await saveRolePermission({
      roleId: currentRole.value.id,
      permissionIds: checkedPermissionIds.value
    });
    
    if (res.data.code === 200) {
      ElMessage.success("权限分配成功");
      permissionDialogVisible.value = false;
    } else {
      ElMessage.error(res.data.message || "权限分配失败");
    }
  } catch (err) {
    ElMessage.error("保存权限失败");
  }
};

onMounted(() => {
  selectPage();
  getAllPermissions();
});
</script>

<style scoped>
.role-management {
  padding: 20px;
  max-width: 1400px;
  margin: 0 auto;
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
  .role-management {
    padding: 10px;
  }
  .card-container {
    padding: 15px;
  }
  ::v-deep .el-table {
    font-size: 13px;
  }
}
.current-role-info {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 6px;
  font-size: 16px;
  font-weight: 500;
}
.permission-scrollbar {
  border: 1px solid #ebeef5;
  border-radius: 6px;
  padding: 10px;
}
.permission-list {
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
  padding: 10px;
}
.permission-item {
  padding: 14px 16px;
  border: 1px solid #f0f2f5;
  border-radius: 8px;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
}
.permission-item:hover {
  background-color: #f8f9fa;
  border-color: #e4e7ed;
  transform: translateX(4px);
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
}
.permission-info {
  padding-left: 12px;
  font-size: 14px;
  color: #606266;
}
.permission-footer {
  display: flex;
  justify-content: center;
  gap: 16px;
}

/* 响应式调整 */
@media (max-width: 992px) {
  .permission-list {
    grid-template-columns: 1fr;
  }
}
</style>