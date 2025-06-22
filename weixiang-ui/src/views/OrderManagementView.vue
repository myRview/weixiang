<template>
  <div class="order-management">
    <div class="search-container">
      <el-row :gutter="20">
        <el-col :span="6" :xs="24" :sm="12" :md="6">
          <el-input
            v-model="searchOrderNo"
            placeholder="请输入订单编号"
            clearable
          ></el-input>
        </el-col>
        <el-col :span="6" :xs="24" :sm="12" :md="6">
          <el-input
            v-model="searchUserName"
            placeholder="请输入用户姓名"
            clearable
          ></el-input>
        </el-col>
        <el-col :span="6" :xs="24" :sm="12" :md="6">
          <el-input
            v-model="searchProduct"
            placeholder="请输入商品名称"
            clearable
          ></el-input>
        </el-col>
        <el-col :span="6" :xs="24" :sm="12" :md="6">
          <el-select
            v-model="searchOrderStatus"
            placeholder="请选择订单状态"
            clearable
          >
            <el-option
              v-for="status in orderStatuses"
              :key="status.value"
              :label="status.label"
              :value="status.value"
            ></el-option>
          </el-select>
        </el-col>
      </el-row>
      <el-row :gutter="20" style="margin-top: 15px;">
        <el-col :span="12" :xs="24" :sm="16" :md="16">
          <el-date-picker
            v-model="searchDateRange"
            type="daterange"
            range-separator="至"  
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            clearable
          ></el-date-picker>
        </el-col>
        <el-col :span="8" :xs="24" :sm="8" :md="8" style="display: flex; gap: 10px; justify-content: flex-end;">
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-col>
      </el-row>
    </div>
    <div class="card-container">
      <el-table :data="tableData" stripe border style="width: 100%">
        <el-table-column prop="id" label="订单ID" width="100"></el-table-column>
        <el-table-column
          prop="orderNo"
          label="订单编号"
          min-width="140"
        ></el-table-column>
        <el-table-column
          prop="userName"
          label="用户姓名"
          min-width="100"
        ></el-table-column>
        <el-table-column
          prop="productName"
          label="商品名称"
          min-width="180"
        ></el-table-column>
        <el-table-column
          prop="orderDate"
          label="下单日期"
          width="120"
        ></el-table-column>
        <el-table-column
          prop="amount"
          label="订单金额"
          width="120"
          :formatter="formatCurrency"
        ></el-table-column>
        <el-table-column prop="status" label="订单状态" width="120">
          <template #default="scope">
            <el-tag
              :type="getOrderStatusType(scope.row.status)"
              >{{ getOrderStatusLabel(scope.row.status) }}</el-tag
            >
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default>
            <el-button type="primary" size="small" style="margin-right: 5px"
              >编辑</el-button
            >
            <el-button type="danger" size="small">删除</el-button>
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

// 订单状态选项
const orderStatuses = ref([
  { value: 'pending', label: '待付款', type: 'warning' },
  { value: 'paid', label: '已付款', type: 'primary' },
  { value: 'shipped', label: '已发货', type: 'info' },
  { value: 'completed', label: '已完成', type: 'success' },
  { value: 'cancelled', label: '已取消', type: 'danger' }
]);

// 搜索条件
const searchOrderNo = ref("");
const searchUserName = ref("");
const searchProduct = ref("");
const searchOrderStatus = ref("");
const searchDateRange = ref<[Date | null, Date | null]>([null, null]);

// 定义订单数据接口
interface Order {
  id: number;
  orderNo: string;
  userName: string;
  productName: string;
  orderDate: string;
  amount: number;
  status: 'pending' | 'paid' | 'shipped' | 'completed' | 'cancelled';
}

// 生成完整模拟数据
const allOrders = ref<Order[]>([]);


// 初始化所有订单数据 - 临时写死示例数据，后续将从后台接口获取
const initAllOrders = () => {
  const data: Order[] = [
    { id: 1, orderNo: 'ORD23000001', userName: '张三', productName: '智能手表', orderDate: '2023-10-15', amount: 1299.00, status: 'completed' },
    { id: 2, orderNo: 'ORD23000002', userName: '李四', productName: '无线耳机', orderDate: '2023-10-16', amount: 899.00, status: 'shipped' },
    { id: 3, orderNo: 'ORD23000003', userName: '王五', productName: '机械键盘', orderDate: '2023-10-17', amount: 499.00, status: 'paid' },
    { id: 4, orderNo: 'ORD23000004', userName: '赵六', productName: '游戏鼠标', orderDate: '2023-10-18', amount: 299.00, status: 'pending' },
    { id: 5, orderNo: 'ORD23000005', userName: '钱七', productName: '蓝牙耳机', orderDate: '2023-10-19', amount: 399.00, status: 'cancelled' },
    { id: 6, orderNo: 'ORD23000006', userName: '孙八', productName: '移动电源', orderDate: '2023-10-20', amount: 199.00, status: 'completed' },
    { id: 7, orderNo: 'ORD23000007', userName: '周九', productName: '笔记本电脑', orderDate: '2023-10-21', amount: 5999.00, status: 'shipped' },
    { id: 8, orderNo: 'ORD23000008', userName: '吴十', productName: '平板电脑', orderDate: '2023-10-22', amount: 3299.00, status: 'paid' }
  ];
  allOrders.value = data;
};

// 获取订单状态标签
const getOrderStatusLabel = (status: string) => {
  const statusObj = orderStatuses.value.find(item => item.value === status);
  return statusObj ? statusObj.label : status;
};

// 获取订单状态标签类型
const getOrderStatusType = (status: string) => {
  const statusObj = orderStatuses.value.find(item => item.value === status);
  return statusObj ? statusObj.type : 'default';
};

// 格式化金额为货币格式
const formatCurrency = (row: Order, column: any) => {
  return '¥' + row.amount.toFixed(2);
};

// 分页相关数据
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(100); // 模拟总数据量

// 模拟订单数据
const tableData = ref<Order[]>([]);

// 加载数据并应用搜索过滤和分页
const loadData = () => {
  // 应用搜索过滤
  let filteredData = [...allOrders.value];

  if (searchOrderNo.value) {
    filteredData = filteredData.filter((item) =>
      item.orderNo.includes(searchOrderNo.value)
    );
  }
  if (searchUserName.value) {
    filteredData = filteredData.filter((item) =>
      item.userName.includes(searchUserName.value)
    );
  }
  if (searchProduct.value) {
    filteredData = filteredData.filter((item) =>
      item.productName.includes(searchProduct.value)
    );
  }
  if (searchOrderStatus.value) {
    filteredData = filteredData.filter(
      (item) => item.status === searchOrderStatus.value
    );
  }
  if (searchDateRange.value[0] && searchDateRange.value[1]) {
    const startDate = new Date(searchDateRange.value[0]);
    const endDate = new Date(searchDateRange.value[1]);
    endDate.setHours(23, 59, 59, 999);
    filteredData = filteredData.filter((item) => {
      const orderDate = new Date(item.orderDate);
      return orderDate >= startDate && orderDate <= endDate;
    });
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
  searchOrderNo.value = "";
  searchUserName.value = "";
  searchProduct.value = "";
  searchOrderStatus.value = "";
  searchDateRange.value = [null, null];
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
initAllOrders();

loadData();
</script>

<style scoped>
.order-management {
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