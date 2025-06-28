<template>
  <div class="operation-log-container">
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
        <el-table-column prop="id" label="日志ID" width="100"></el-table-column>
        <el-table-column
          prop="username"
          label="操作人员"
          min-width="120"
        ></el-table-column>
        <el-table-column
          prop="operationContent"
          label="操作内容"
          min-width="200"
        ></el-table-column>
        <el-table-column
          prop="operationModule"
          label="操作模块"
          width="120"
        ></el-table-column>
        <el-table-column
          prop="ipAddress"
          label="IP地址"
          width="140"
        ></el-table-column>
        <el-table-column
          prop="operationAddress"
          label="操作地址"
          min-width="160"
        ></el-table-column>
        <el-table-column label="操作时间" width="180">
          <template #default="scope">
            {{ formatDate(scope.row.operationTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="操作状态" width="100">
          <template #default="scope">
            <el-tag
              :type="scope.row.status === 1 ? 'success' : 'danger'"
              >{{ scope.row.status === 1 ? "成功" : "失败" }}</el-tag
            >
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
          <h3>操作日志详情</h3>
        </span>
        <el-form :model="currentLog" label-width="100px">
          <el-form-item label="用户名">
            <el-input v-model="currentLog.username" disabled></el-input>
          </el-form-item>
          <el-form-item label="IP地址">
            <el-input v-model="currentLog.ipAddress" disabled></el-input>
          </el-form-item>
          <el-form-item label="操作内容">
            <el-input v-model="currentLog.operationContent" disabled></el-input>
          </el-form-item>
          <el-form-item label="操作模块">
            <el-input v-model="currentLog.operationModule" disabled></el-input>
          </el-form-item>
          <el-form-item label="操作地址">
            <el-input v-model="currentLog.operationAddress" disabled></el-input>
          </el-form-item>
          <el-form-item label="操作时间">
            <el-input
              :value="formatDate(currentLog.operationTime)"
              disabled
            ></el-input>
          </el-form-item>
          <el-form-item label="操作状态">
            <el-tag
              :type="currentLog.status === 1 ? 'success' : 'danger'"
            >
              {{ currentLog.status === 1 ? "成功" : "失败" }}
            </el-tag>
          </el-form-item>
        </el-form>
      </el-dialog>
    </div>
  </div>
</template>

<script setup lang="ts">
import { selectOperaLogPage } from "@/api/caozuorizhiguanli";
import { onMounted, ref } from "vue";
import dayjs from "dayjs";
const formatDate = (date: string | number | Date) => {
  return dayjs(date).format("YYYY-MM-DD HH:mm:ss");
};
// 分页相关数据
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(100);
const tableData = ref<API.OperationLogVO[]>([]);
const searchParam = ref<API.LogSearchParam>({});
const selectPage = async () => {
  searchParam.value = {
    ...searchParam.value,
    pageNum: currentPage.value,
    pageSize: pageSize.value,
  };
  const res = await selectOperaLogPage(searchParam.value);
  if (res.data.code === 200) {
    tableData.value = res.data.data?.records;
    total.value = res.data.data?.total || 0;
  }
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

const dialogVisible = ref(false);
const currentLog = ref<API.OperationLogVO>({});
// 查看详情
const handleDetail = (log: any) => {
  console.log("查看详情:", log);
  // 这里可以添加详情弹窗逻辑
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
onMounted(() => {
  selectPage();
});
</script>

<style scoped>
.operation-log-container {
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

/* 分页容器样式 */
.pagination-container {
  margin-top: 16px;
  text-align: right;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .operation-log-container {
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
