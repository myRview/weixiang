<template>
  <div class="login-log-container">
    <div class="card-container">
      <el-table :data="tableData" stripe border style="width: 100%">
        <el-table-column prop="id" label="日志ID" width="100"></el-table-column>
        <el-table-column prop="username" label="用户名" min-width="120"></el-table-column>
        <el-table-column prop="ipAddress" label="IP地址" width="140"></el-table-column>
        <el-table-column prop="loginLocation" label="登录地点" min-width="160"></el-table-column>
        <el-table-column prop="device" label="登录设备" width="120"></el-table-column>
        <el-table-column prop="loginTime" label="登录时间" width="180"></el-table-column>
        <el-table-column prop="status" label="登录状态" width="100">
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

// 定义登录日志数据接口
interface LoginLog {
  id: number;
  username: string;
  ipAddress: string;
  loginLocation: string;
  device: string;
  loginTime: string;
  status: 'success' | 'fail';
  description?: string;
}

// 分页相关数据
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(100);
const tableData = ref<LoginLog[]>([]);

// 生成随机IP地址
const getRandomIp = (): string => {
  return Array.from({ length: 4 }, () => Math.floor(Math.random() * 256)).join('.');
};

// 生成随机日期时间
const getRandomDateTime = (): string => {
  const date = new Date(Date.now() - Math.floor(Math.random() * 30 * 24 * 60 * 60 * 1000));
  return date.toISOString().replace('T', ' ').slice(0, 19);
};

// 登录地点列表
const loginLocations = [
  '北京市 电信', '上海市 联通', '广州市 移动', '深圳市 电信', '杭州市 联通',
  '南京市 移动', '武汉市 电信', '成都市 联通', '重庆市 移动', '西安市 电信'
];

// 登录设备列表
const devices = ['Windows PC', 'macOS', 'iPhone', 'Android', 'iPad', 'Linux'];

// 登录失败原因
const failureReasons = [
  '密码错误', '账号不存在', '账号被锁定', 'IP被限制', '验证码错误'
];

// 初始化登录日志数据
const initLoginLogs = () => {
  const logs: LoginLog[] = [];
  for (let i = 1; i <= 100; i++) {
    const status = Math.random() > 0.2 ? 'success' : 'fail';
    logs.push({
      id: i,
      username: `user${Math.floor(Math.random() * 50) + 1}`,
      ipAddress: getRandomIp(),
      loginLocation: loginLocations[Math.floor(Math.random() * loginLocations.length)],
      device: devices[Math.floor(Math.random() * devices.length)],
      loginTime: getRandomDateTime(),
      status,
      description: status === 'fail' ? failureReasons[Math.floor(Math.random() * failureReasons.length)] : undefined
    });
  }
  return logs;
};

// 所有日志数据
const allLogs = ref<LoginLog[]>(initLoginLogs());

// 加载数据并应用分页
const loadData = () => {
  const startIndex = (currentPage.value - 1) * pageSize.value;
  const endIndex = startIndex + pageSize.value;
  tableData.value = allLogs.value.slice(startIndex, endIndex);
  total.value = allLogs.value.length;
};

// 查看详情
const handleDetail = (log: LoginLog) => {
  console.log('查看登录详情:', log);
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
  .login-log-container {
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