<template>
  <div class="login-log-container">
    <div class="search-container">
      <el-card class="search-card">
        <!-- 搜索表单 -->
        <el-form :model="searchParam" class="search-form" inline>
          <el-form-item label="用户名">
            <el-input
              v-model="searchParam.username"
              placeholder="请输入用户名"
              clearable
            />
          </el-form-item>
          <el-form-item label="IP地址">
            <el-input
              v-model="searchParam.ipAddress"
              placeholder="请输入IP地址"
              clearable
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
    <div class="card-container">
      <el-table :data="tableData" stripe border style="width: 100%">
        <el-table-column
          prop="id"
          label="日志ID"
          width="100"
          show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="username"
          label="用户名"
          min-width="120"
          show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="ipAddress"
          label="IP地址"
          width="140"
          show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="location"
          label="登录地点"
          min-width="160"
          show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="device"
          label="登录设备"
          width="120"
          show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          label="登录时间"
          width="180"
          show-overflow-tooltip="true"
        >
          <template #default="scope">
            {{ formatDate(scope.row.loginTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="登录状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">{{
              scope.row.status === 1 ? "成功" : "失败"
            }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template #default="scope">
            <el-button type="text" size="small" @click="handleDetail(scope.row)"
              >详情</el-button
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
    <!-- 详情对话框 -->
    <div class="detail-dialog">
      <el-dialog v-model="dialogVisible" width="600">
        <span
          slot="title"
          style="display: block; text-align: center; margin-bottom: 10px"
        >
          <h3>登录日志详情</h3>
        </span>
        <el-form :model="currentLog" label-width="100px">
          <el-form-item label="用户名">
            <el-input v-model="currentLog.username" disabled></el-input>
          </el-form-item>
          <el-form-item label="IP地址">
            <el-input v-model="currentLog.ipAddress" disabled></el-input>
          </el-form-item>
          <el-form-item label="登录地点">
            <el-input v-model="currentLog.location" disabled></el-input>
          </el-form-item>
          <el-form-item label="登录设备">
            <el-input v-model="currentLog.device" disabled></el-input>
          </el-form-item>
          <el-form-item label="登录时间">
            <el-input
              :value="formatDate(currentLog.loginTime)"
              disabled
            ></el-input>
          </el-form-item>
          <el-form-item label="登录状态">
            <el-tag :type="currentLog.status === 1 ? 'success' : 'danger'">
              {{ currentLog.status === 1 ? "成功" : "失败" }}
            </el-tag>
          </el-form-item>
        </el-form>
      </el-dialog>
    </div>
  </div>
</template>

<script setup lang="ts">
import { selectLoginPage } from "@/api/denglurizhi";
import { onMounted, ref } from "vue";
import dayjs from "dayjs";
const formatDate = (date: string | number | Date) => {
  return dayjs(date).format("YYYY-MM-DD HH:mm:ss");
};
// 分页相关数据
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(100);
const tableData = ref<API.LoginLogVO[]>([]);
const searchParam = ref<API.LogSearchParam>({});

const selectPage = async () => {
  try {
    searchParam.value = {
      ...searchParam.value,
      pageNum: currentPage.value,
      pageSize: pageSize.value,
    };
    const res = await selectLoginPage(searchParam.value);
    if (res.data.code === 200) {
      tableData.value = res.data.data?.records;
      total.value = Number(res.data.data?.total) || 0;
    }
  } catch (error) {}
};

// 弹框控制
const dialogVisible = ref(false);
const currentLog = ref<API.LoginLogVO>({});
// 查看详情
const handleDetail = (log: any) => {
  currentLog.value = log;
  dialogVisible.value = true;
};

// 分页事件处理
const handleSizeChange = (val: number) => {
  pageSize.value = val;
  selectPage();
};

const handleCurrentChange = (val: number) => {
  currentPage.value = val;
  selectPage();
};

// 搜索事件
const handleSearch = () => {
  currentPage.value = 1;
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
.login-log-container {
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

/* 弹框层级调整 */
::v-deep .el-dialog {
  z-index: 2000 !important; /* 确保弹框显示在最上层 */
}

/* 详情对话框样式优化 */
.detail-dialog {
  .el-dialog__header {
    text-align: center; /* 标题整体居中 */
  }
  .el-dialog__title {
    display: block; /* 确保标题为块级元素 */
  }
  .el-dialog__body {
    padding: 30px 40px; /* 增加内边距提升空间感 */
  }
  .el-form {
    max-width: 600px; /* 限制表单最大宽度 */
    margin: 0 auto; /* 表单整体居中 */
  }
  .el-form-item {
    max-width: 500px; /* 每行最大宽度限制 */
    margin: 0 auto 15px; /* 行间距调整+居中 */
  }
  .el-form-item__label {
    text-align: center; /* 标签文字居中 */
  }
  .el-input {
    .el-input__inner {
      text-align: center; /* 输入框内容居中 */
      max-width: 400px; /* 输入框最大宽度 */
      overflow: hidden; /* 溢出隐藏 */
      text-overflow: ellipsis; /* 溢出显示省略号 */
      white-space: nowrap; /* 强制单行显示 */
    }
  }
}

/* 响应式调整 */
@media (max-width: 768px) {
  .login-log-container {
    padding: 10px;
  }

  .card-container {
    padding: 15px;
  }

  .search-form {
    gap: 12px;
  }
  .search-form .el-form-item__label {
    width: 60px;
  }
  .search-form .el-input {
    width: 160px;
  }

  ::v-deep .el-table {
    font-size: 13px;
  }

  .pagination-container {
    margin-top: 12px;
  }
}
</style>
