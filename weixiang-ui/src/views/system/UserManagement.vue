<template>
  <div class="user-management">
    <div class="search-container">
      <el-card class="search-card">
        <el-row :gutter="20">
          <!-- 第一行搜索条件 -->
          <el-col :span="6">
            <el-input
              v-model="searchName"
              placeholder="请输入用户名"
              clearable
              size="medium"
            ></el-input>
          </el-col>
          <el-col :span="6">
            <el-input
              v-model="searchAccount"
              placeholder="请输入账号"
              clearable
              size="medium"
            ></el-input>
          </el-col>
          <el-col :span="6">
            <el-input
              v-model="searchEmail"
              placeholder="请输入邮箱"
              clearable
              size="medium"
            ></el-input>
          </el-col>
          <el-col :span="6">
            <el-input
              v-model="searchPhone"
              placeholder="请输入手机号"
              clearable
              size="medium"
            ></el-input>
          </el-col>

          <el-col :span="24" style="height: 10px;"></el-col>

          <!-- 第二行搜索条件 -->
          <el-col :span="6">
            <el-select
              v-model="searchMembershipLevel"
              placeholder="请选择会员等级"
              clearable
              size="medium"
            >
              <el-option
                v-for="level in membershipLevels"
                :key="level"
                :label="level"
                :value="level"
              ></el-option>
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-select
              v-model="searchrole"
              placeholder="请选择角色"
              clearable
              size="medium"
            >
              <el-option
                v-for="role in roles"
                :key="role"
                :label="role"
                :value="role"
              ></el-option>
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-select v-model="searchStatus" placeholder="请选择状态" clearable size="medium">
              <el-option label="活跃" value="active"></el-option>
              <el-option label="禁用" value="inactive"></el-option>
            </el-select>
          </el-col>
          <el-col :span="6" style="gap: 10px; justify-content: flex-end; align-items: flex-end;">
            <el-button type="primary" @click="handleSearch" size="medium">搜索</el-button>
            <el-button @click="resetSearch" size="medium">重置</el-button>
          </el-col>
        </el-row>
      </el-card>
    </div>
    <div class="card-container">
      <el-table :data="tableData" stripe border style="width: 100%">
        <el-table-column prop="id" label="用户ID" width="100"></el-table-column>
        <el-table-column
          prop="name"
          label="用户姓名"
          min-width="100"
        ></el-table-column>
        <el-table-column
          prop="account"
          label="账号"
          min-width="100"
        ></el-table-column>
        <el-table-column
          prop="email"
          label="邮箱"
          min-width="180"
        ></el-table-column>
        <el-table-column
          prop="phone"
          label="手机号"
          width="120"
        ></el-table-column>
        <el-table-column
          prop="role"
          label="角色"
          width="120"
        ></el-table-column>
        <el-table-column
          prop="membershipLevel"
          label="会员等级"
          width="120"
        ></el-table-column>
        <el-table-column
          prop="createDate"
          label="创建时间"
          width="120"
        ></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag
              :type="scope.row.status === 'active' ? 'success' : 'danger'"
              >{{ scope.row.status === "active" ? "活跃" : "禁用" }}</el-tag
            >
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default>
            <el-button type="primary" size="small" style="margin-right: 5px"
              >编辑</el-button
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
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";

// 会员等级选项
const roles = ref(["管理员", "普通用户"]);

// 搜索条件
const searchName = ref("");
const searchEmail = ref("");
const searchPhone = ref("");
const searchrole = ref("");
const searchStatus = ref("");
const searchAccount = ref(""); 
const searchMembershipLevel = ref("");
// 定义会员数据接口
interface User {
  id: number;
  name: string;
  account: string;
  email: string;
  phone: string;
  createDate: string;
  role: string;
  membershipLevel: string;
  status: "active" | "inactive";
}
// 会员等级选项
const membershipLevels = ref(["普通会员", "银卡会员", "金卡会员", "钻石会员"]);
// 生成完整模拟数据
const allUsers = ref<User[]>([]);


// 生成随机日期 helper 函数
const getRandomDate = (start: Date, end: Date): string => {
  const date = new Date(start.getTime() + Math.random() * (end.getTime() - start.getTime()));
  return date.toISOString().split('T')[0];
};

// 初始化所有会员数据
const initAllUsers = () => {
  allUsers.value = Array.from({ length: 50 }, (_, i) => ({
    id: i + 1,
    name: `用户${i + 1}`,
    account: `user${i + 1}`,
    email: `user${i + 1}@example.com`,
    phone: `13${Math.floor(Math.random() * 100000000).toString().padStart(8, '0')}`,
    createDate: getRandomDate(new Date(2023, 0, 1), new Date()),
    role: roles.value[Math.floor(Math.random() * roles.value.length)],
    membershipLevel: ["普通会员", "银卡会员", "金卡会员", "钻石会员"][
        Math.floor(Math.random() * 4)
      ],
    status: Math.random() > 0.3 ? 'active' : 'inactive'
  }));
};

// 分页相关数据
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(100); // 模拟总数据量

// 模拟会员数据
const tableData = ref<User[]>([]);

// 加载数据并应用搜索过滤和分页
const loadData = () => {
  // 应用搜索过滤
  let filteredData = [...allUsers.value];

  if (searchName.value) {
    filteredData = filteredData.filter((item) =>
      item.name.includes(searchName.value)
    );
  }
  if (searchEmail.value) {
    filteredData = filteredData.filter((item) =>
      item.email.includes(searchEmail.value)
    );
  }
  if (searchPhone.value) {
    filteredData = filteredData.filter((item) =>
      item.phone.includes(searchPhone.value)
    );
  }
  if (searchrole.value) {
    filteredData = filteredData.filter(
      (item) => item.role === searchrole.value
    );
  }
  if (searchStatus.value) {
    filteredData = filteredData.filter(
      (item) => item.status === searchStatus.value
    );
  }
  if (searchMembershipLevel.value) {
    filteredData = filteredData.filter(
      (item) => item.membershipLevel === searchMembershipLevel.value
    );
  }
  if (searchAccount.value) {
    filteredData = filteredData.filter(
      (item) => item.account === searchAccount.value
    );
  }

  // 更新总条数
  total.value = filteredData.length;

  // 分页处理
  const startIndex = (currentPage.value - 1) * pageSize.value;
  const endIndex = startIndex + pageSize.value;
  tableData.value = filteredData.slice(startIndex, endIndex);
};

// 搜索处理
const handleSearch = () => {
  currentPage.value = 1; // 搜索时重置到第一页
  loadData();
};

// 重置搜索条件
const resetSearch = () => {
  searchName.value = "";
  searchEmail.value = "";
  searchPhone.value = "";
  searchrole.value = "";
  searchStatus.value = "";
  currentPage.value = 1;
  loadData();
};

// 分页事件处理
const handleSizeChange = (val: number) => {
  pageSize.value = val;
  loadData();
};

const handleCurrentChange = (val: number) => {
  currentPage.value = val;
  loadData();
};

// 初始化所有会员数据
initAllUsers();

// 初始加载数据
loadData();
</script>

<style scoped>
.user-management {
  padding: 20px;
  max-width: 1400px;
  margin: 0 auto;
}

/* 卡片容器样式 */
.card-container {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  padding: 20px;
  margin-bottom: 20px;
}

.search-container {
  margin-bottom: 20px;
}

.search-card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  padding: 15px;
  margin-bottom: 20px;
}

/* 标题样式 */
.page-title {
  font-size: 20px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #f0f0f0;
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

/* 分页容器样式 */
.pagination-container {
  margin-top: 16px;
  text-align: right;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .member-management {
    padding: 10px;
  }

  .card-container {
    padding: 15px;
  }

  ::v-deep .el-table {
    font-size: 13px;
  }

  .pagination-container {
    margin-top: 12px;
  }
}
</style>