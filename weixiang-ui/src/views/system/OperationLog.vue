<template>
  <div class="operation-log-container">
    <div class="card-container">
      <el-table :data="tableData" stripe border style="width: 100%">
        <el-table-column prop="id" label="日志ID" width="100"></el-table-column>
        <el-table-column prop="operator" label="操作人员" min-width="120"></el-table-column>
        <el-table-column prop="operation" label="操作内容" min-width="200"></el-table-column>
        <el-table-column prop="module" label="操作模块" width="120"></el-table-column>
        <el-table-column prop="ipAddress" label="IP地址" width="140"></el-table-column>
        <el-table-column prop="operationAddress" label="操作地址" min-width="160"></el-table-column>
        <el-table-column prop="operationTime" label="操作时间" width="180"></el-table-column>
        <el-table-column prop="status" label="操作状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 'success' ? 'success' : 'danger'">{{ scope.row.status === 'success' ? '成功' : '失败' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template #default="scope">
            <el-button type="text" size="small" @click="handleDetail(scope.row)">详情</el-button>
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

// 定义操作日志数据接口
interface OperationLog {
  id: number;
  operator: string;
  operation: string;
  module: string;
  ipAddress: string;
  operationAddress: string;
  operationTime: string;
  status: 'success' | 'fail';
}

// 分页相关数据
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(100);
const tableData = ref<OperationLog[]>([]);

// 生成随机IP地址
const getRandomIp = (): string => {
  return Array.from({ length: 4 }, () => Math.floor(Math.random() * 256)).join('.');
};

// 生成随机日期时间
const getRandomDateTime = (): string => {
  const date = new Date(Date.now() - Math.floor(Math.random() * 30 * 24 * 60 * 60 * 1000));
  return date.toISOString().replace('T', ' ').slice(0, 19);
};

// 操作模块列表
const modules = ['用户管理', '商品管理', '订单管理', '系统设置', '数据统计'];

// 操作地址列表
const operationAddresses = [
  '/user/list', '/product/edit', '/order/detail', '/system/setting', '/data/analysis',
  '/login', '/dashboard', '/member/card', '/order/refund', '/log/operation'
];

// 操作内容列表
const operations = [
  '新增数据', '编辑信息', '删除记录', '查询数据', '导出报表', '导入数据', '修改密码', '登录系统', '退出系统'
];

// 初始化操作日志数据
const initOperationLogs = () => {
  const logs: OperationLog[] = [];
  for (let i = 1; i <= 100; i++) {
    logs.push({
      id: i,
      operator: `用户${Math.floor(Math.random() * 50) + 1}`,
      operation: operations[Math.floor(Math.random() * operations.length)],
      module: modules[Math.floor(Math.random() * modules.length)],
      ipAddress: getRandomIp(),
      operationAddress: operationAddresses[Math.floor(Math.random() * operationAddresses.length)],
      operationTime: getRandomDateTime(),
      status: Math.random() > 0.1 ? 'success' : 'fail'
    });
  }
  return logs;
};

// 所有日志数据
const allLogs = ref<OperationLog[]>(initOperationLogs());

// 加载数据并应用分页
const loadData = () => {
  const startIndex = (currentPage.value - 1) * pageSize.value;
  const endIndex = startIndex + pageSize.value;
  tableData.value = allLogs.value.slice(startIndex, endIndex);
  total.value = allLogs.value.length;
};

// 查看详情
const handleDetail = (log: OperationLog) => {
  console.log('查看详情:', log);
  // 这里可以添加详情弹窗逻辑
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

// 初始加载数据
loadData();
</script>

<style scoped>
.operation-log-container {
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