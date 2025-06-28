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
              <el-form-item label="手机号">
                <el-input
                  v-model="searchForm.phone"
                  placeholder="请输入手机号"
                  clearable
                  size="medium"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24" style="height: 10px"></el-col>
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
            <el-col
              :span="6"
              style="
                gap: 10px;
                justify-content: flex-end;
                align-items: flex-end;
              "
            >
              <el-button type="primary" @click="handleSearch" size="medium"
                >搜索</el-button
              >
              <el-button @click="resetSearch" size="medium">重置</el-button>
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
        <el-table-column label="创建时间" width="120">
          <template #default="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">{{
              scope.row.status === 1 ? "正常" : "禁用"
            }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="240px">
          <template #default="scope">
            <el-button type="text" size="small" style="margin-right: 5px">详情</el-button>
            <el-button type="primary" size="small" style="margin-right: 5px">编辑</el-button>
            <el-button :type="scope.row.status === 1 ? 'danger' : 'success'" size="small" style="margin-right: 5px">
              {{ scope.row.status === 1 ? '禁用' : '启用' }}
            </el-button>
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
import { onMounted, reactive, ref } from "vue";
import { getUserList } from "@/api/yonghuguanli";
import { StatusEnums } from "@/enums/status.enum";
import dayjs from "dayjs";
const formatDate = (date: string | number | Date) => {
  return dayjs(date).format("YYYY-MM-DD HH:mm:ss");
};

const searchForm = ref<API.UserSearchParam>({});
// 表格数据
const tableData = ref<API.UserVO[]>([]);
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);

// 初始化用户数据
const loadUserList = async () => {
  try {
    const res = await getUserList(searchForm.value);
    if (res.data.code === 200) {
      tableData.value = res.data.data?.records;
      total.value = res.data.data?.total || 0;
    }
  } catch (error) {
    console.error("获取用户列表失败:", error);
  }
};

// 搜索处理
const handleSearch = async () => {
  currentPage.value = 1;
  await loadUserList();
};

// 重置搜索
const resetSearch = () => {
  searchForm.value = {};
  handleCurrentChange(1);
};

// 分页处理
const handleSizeChange = (val: number) => {
  pageSize.value = val;
  handleSearch();
};

const handleCurrentChange = (val: number) => {
  currentPage.value = val;
  handleSearch();
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
</style>
