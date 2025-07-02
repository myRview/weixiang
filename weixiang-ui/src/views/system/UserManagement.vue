<template>
  <div class="user-management">
    <div class="search-container">
      <el-card class="search-card">
        <el-form :model="searchForm" class="search-form">
          <el-row :gutter="20">
            <el-col :span="6">
              <el-form-item label="用户名">
                <el-input
                  v-model="searchForm.userName"
                  placeholder="请输入用户名"
                  clearable
                  size="medium"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="账号">
                <el-input
                  v-model="searchForm.account"
                  placeholder="请输入账号"
                  clearable
                  size="medium"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="邮箱">
                <el-input
                  v-model="searchForm.email"
                  placeholder="请输入邮箱"
                  clearable
                  size="medium"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="状态">
                <el-select
                  v-model="searchForm.status"
                  placeholder="请选择状态"
                  clearable
                  size="medium"
                >
                  <el-option
                    v-for="(item, key) in StatusEnums"
                    :key="key"
                    :label="item.description"
                    :value="item.code"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="手机号">
                <el-input
                  v-model="searchForm.phone"
                  placeholder="请输入手机号"
                  clearable
                  size="medium"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col
              :span="6"
              style="
                gap: 10px;
                justify-content: flex-end;
                align-items: flex-end;
              "
            >
              <el-button type="primary" @click="loadUserList" size="medium"
                >搜索</el-button
              >
              <el-button @click="resetSearch" size="medium">重置</el-button>
              <el-button type="success" @click="handleAddUser" size="medium"
                >新增用户</el-button
              >
            </el-col>
          </el-row>
        </el-form>
      </el-card>
    </div>
    <div class="card-container">
      <el-table :data="tableData" stripe border style="width: 100%">
        <el-table-column
          prop="id"
          label="用户ID"
          width="100"
          show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="userName"
          label="用户姓名"
          show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="account"
          label="账号"
          show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="email"
          label="邮箱"
          show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="phone"
          label="手机号"
          width="120"
          show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="role"
          label="角色"
          width="120"
          show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="membershipLevel"
          label="会员等级"
          width="120"
          show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          label="创建时间"
          width="120"
          show-overflow-tooltip="true"
        >
          <template #default="scope">{{
            formatDate(scope.row.createTime)
          }}</template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? "正常" : "禁用" }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="300px">
          <template #default="scope">
            <el-button
              type="text"
              size="small"
              style="margin-right: 5px"
              @click="handleDetail(scope.row)"
              >详情</el-button
            >
            <el-button
              type="primary"
              size="small"
              style="margin-right: 5px"
              @click="handleEdit(scope.row)"
              >编辑</el-button
            >
            <el-button
              :type="scope.row.status === 1 ? 'danger' : 'success'"
              size="small"
              style="margin-right: 5px"
              @click="handleToggleStatus(scope.row)"
              >{{ scope.row.status === 1 ? "禁用" : "启用" }}</el-button
            >
            <el-button
              type="danger"
              size="small"
              style="margin-right: 5px"
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
    <el-dialog
      v-model="dialogVisible"
      width="600px"
      :before-close="handleClose"
      style="border-radius: 12px; box-shadow: 0 4px 24px rgba(0, 0, 0, 0.12)"
    >
      <template #header>
        <div class="custom-dialog-header" style="text-align: center">
          {{ isEdit ? "编辑用户" : "新增用户" }}
        </div>
      </template>
      <el-form
        :model="addForm"
        label-width="100px"
        ref="addFormRef"
        style="padding: 32px"
      >
        <el-form-item label="用户名" style="margin-bottom: 18px">
          <el-input
            v-model="addForm.userName"
            placeholder="请输入用户名"
            style="border-radius: 6px"
          ></el-input>
        </el-form-item>
        <el-form-item label="账号" style="margin-bottom: 18px" required>
          <el-input
            v-model="addForm.account"
            placeholder="请输入账号"
            style="border-radius: 6px"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="密码"
          style="margin-bottom: 18px"
          v-if="!isEdit"
          required
        >
          <el-input
            v-model="addForm.password"
            placeholder="请输入密码"
            style="border-radius: 6px"
            type="password"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="确认密码"
          style="margin-bottom: 18px"
          v-if="!isEdit"
          required
        >
          <el-input
            v-model="addForm.confirmPassword"
            placeholder="请输入确认密码"
            style="border-radius: 6px"
            type="password"
          ></el-input>
        </el-form-item>
        <el-form-item label="邮箱" style="margin-bottom: 18px">
          <el-input
            v-model="addForm.email"
            placeholder="请输入邮箱"
            style="border-radius: 6px"
          ></el-input>
        </el-form-item>
        <el-form-item label="手机号" style="margin-bottom: 18px">
          <el-input
            v-model="addForm.phone"
            placeholder="请输入手机号"
            style="border-radius: 6px"
          ></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span
          class="dialog-footer"
          style="
            display: flex;
            justify-content: center;
            gap: 16px;
            padding: 0 24px 24px;
          "
        >
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleAdd">确定</el-button>
        </span>
      </template>
    </el-dialog>
    <!-- 用户详情 -->
    <el-dialog
      v-model="detailDialogVisible"
      width="600px"
      :before-close="handleDetailClose"
      style="border-radius: 12px; box-shadow: 0 4px 24px rgba(0, 0, 0, 0.12)"
    >
      <template #header>
        <div
          class="custom-dialog-header"
          style="
            font-size: 20px;
            font-weight: 600;
            color: #2c3e50;
            text-align: center;
          "
        >
          用户详情
        </div>
      </template>
      <el-form
        :model="detailForm"
        label-width="100px"
        style="padding: 10px 24px 0"
        disabled
      >
        <el-form-item label="用户ID" class="detail-form-item">
          <el-input
            v-model="detailForm.id"
            placeholder="无"
            class="detail-input"
          />
        </el-form-item>
        <el-form-item label="用户名" class="detail-form-item">
          <el-input
            v-model="detailForm.userName"
            placeholder="无"
            class="detail-input"
          />
        </el-form-item>
        <el-form-item label="账号" class="detail-form-item">
          <el-input
            v-model="detailForm.account"
            placeholder="无"
            class="detail-input"
          />
        </el-form-item>
        <el-form-item label="邮箱" class="detail-form-item">
          <el-input
            v-model="detailForm.email"
            placeholder="无"
            class="detail-input"
          />
        </el-form-item>
        <el-form-item label="手机号" class="detail-form-item">
          <el-input
            v-model="detailForm.phone"
            placeholder="无"
            class="detail-input"
          />
        </el-form-item>
        <el-form-item label="角色" class="detail-form-item">
          <el-input
            v-model="detailForm.role"
            placeholder="无"
            class="detail-input"
          />
        </el-form-item>
        <el-form-item label="会员等级" class="detail-form-item">
          <el-input
            v-model="detailForm.membershipLevel"
            placeholder="无"
            class="detail-input"
          />
        </el-form-item>
        <el-form-item label="创建时间" class="detail-form-item">
          <el-input
            :value="formatDate(detailForm.createTime)"
            placeholder="无"
            class="detail-input"
          />
        </el-form-item>
        <el-form-item label="状态" class="detail-form-item">
          <el-tag
            :type="detailForm.status === 1 ? 'success' : 'danger'"
            class="status-tag"
            >{{ detailForm.status === 1 ? "正常" : "禁用" }}</el-tag
          >
        </el-form-item>
      </el-form>
      <template #footer>
        <div style="padding: 10px 0 20px; text-align: center">
          <el-button
            type="primary"
            @click="detailDialogVisible = false"
            style="width: 120px"
            >关闭</el-button
          >
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from "vue";
import {
  addUser,
  deleteUser,
  getUserById,
  getUserList,
  transStatus,
  updateUser,
} from "@/api/yonghuguanli";
import { StatusEnums } from "@/enums/status.enum";
import dayjs from "dayjs";
import { ElMessage, ElMessageBox } from "element-plus";
const formatDate = (date: string | number | Date) => {
  return dayjs(date).format("YYYY-MM-DD HH:mm:ss");
};
const dialogVisible = ref(false);
const isEdit = ref(false); // 是否为编辑状态
const addForm = ref<API.UserAddVO | API.UserEditVO>({});

const handleAddUser = () => {
  isEdit.value = false;
  addForm.value = {};
  dialogVisible.value = true;
};

const handleClose = () => {
  dialogVisible.value = false;
  addForm.value = {};
};

const handleAdd = async () => {
  try {
    let res;
    // 校验表单 如果手机号和邮箱不为空对手机号和邮箱进行校验
    const phoneReg = /^1[3-9]\d{9}$/; // 手机号正则（中国大陆）
    const emailReg = /^[^s@]+@[^s@]+\.[^s@]+$/; // 邮箱正则

    // 校验手机号
    if (addForm.value.phone && !phoneReg.test(addForm.value.phone)) {
      return ElMessage.error("手机号格式不正确（11位数字，以13-19开头）");
    }

    // 校验邮箱
    if (addForm.value.email && !emailReg.test(addForm.value.email)) {
      return ElMessage.error("邮箱格式不正确");
    }

    if (isEdit.value) {
      res = await updateUser(addForm.value);
    } else {
      //添加用户之前对密码进行校验
      if (addForm.value.password !== addForm.value.confirmPassword) {
        return ElMessage.error("两次输入的密码不一致");
      }
      res = await addUser(addForm.value);
    }
    if (res.data.code !== 200) {
      return ElMessage.error(res.data.message);
    }
    ElMessage.success(res.data.message);
    loadUserList();
    dialogVisible.value = false;
  } catch (error) {
    console.error("操作失败:", error);
  }
};

// 编辑用户处理
const handleEdit = (row: API.UserVO) => {
  isEdit.value = true;
  addForm.value = {
    userId: row.id,
    userName: row.userName,
    account: row.account,
    email: row.email,
    phone: row.phone,
  };
  dialogVisible.value = true;
};
const detailDialogVisible = ref(false);
const detailForm = ref<API.UserVO>({});
const handleDetailClose = () => {
  detailDialogVisible.value = false;
  detailForm.value = {};
};
const handleDetail = async (row: API.UserVO) => {
  const res = await getUserById({ id: row.id });
  if (res.data.code === 200) {
    detailForm.value = res.data.data || {};
  } else {
    ElMessage.error(res.data.message);
  }
  detailDialogVisible.value = true;
};
// 表格数据
const tableData = ref<API.UserVO[]>([]);
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);
const searchForm = ref<API.UserSearchParam>({});
// 初始化用户数据
const loadUserList = async () => {
  try {
    searchForm.value.pageNum = currentPage.value;
    searchForm.value.pageSize = pageSize.value;
    const res = await getUserList(searchForm.value);
    if (res.data.code === 200) {
      tableData.value = res.data.data?.records || [];
      total.value = Number(res.data.data?.total) || 0;
    }
  } catch (error) {
    console.error("获取用户列表失败:", error);
  }
};
const handleToggleStatus = async (row: API.UserVO) => {
  const newStatus = row.status === 1 ? 0 : 1; // 切换状态
  const res = await transStatus({ userId: row.id, status: newStatus });
  if (res.data.code === 200) {
    ElMessage.success(res.data.message);
    loadUserList(); // 刷新列表
  }
};

const handleDelete = async (row: API.UserVO) => {
  try {
    await ElMessageBox.confirm(`确定要删除「${row.userName}」吗？`, "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    });
    const res = await deleteUser({ id: row.id });
    if (res.data.code === 200) {
      ElMessage.success("删除成功");
      loadUserList();
    }
  } catch (error) {
    console.error("删除失败:", error);
  }
};

// 重置搜索
const resetSearch = () => {
  searchForm.value = {};
  handleCurrentChange(1);
};

// 分页处理
const handleSizeChange = (val: number) => {
  pageSize.value = val;
  loadUserList();
};

const handleCurrentChange = (val: number) => {
  currentPage.value = val;
  loadUserList();
};
onMounted(() => {
  loadUserList();
});
</script>

<style scoped>
.search-container {
  margin-bottom: 20px;
}

.card-container {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  padding: 20px;
  margin-bottom: 20px;
}

.pagination-container {
  margin-top: 15px;
  text-align: right;
}

/* 用户详情弹窗样式优化 */
.detail-form-item {
  margin-bottom: 12px;
}

.detail-form-item :deep(.el-form-item__label) {
  font-weight: 600;
  color: #606266;
}

.detail-input {
  border-radius: 8px;
  background-color: #f5f7fa;
}

.detail-input :deep(.el-input__inner) {
  background-color: #f5f7fa;
  color: #606266;
  border: none;
  cursor: default;
  padding: 10px 12px;
}

.status-tag {
  padding: 6px 12px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
}
</style>
