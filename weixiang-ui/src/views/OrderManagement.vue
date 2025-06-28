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
import { ref, onMounted } from "vue";

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

// 分页相关
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);
const tableData = ref([]);

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

// 所有订单数据
const allOrders = ref<Order[]>([]);

// 初始化所有订单数据
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
  total.value = data.length;
  tableData.value = data;
};

// 获取订单状态标签
const getOrderStatusLabel = (status: string) => {
  const found = orderStatuses.value.find(item => item.value === status);
  return found ? found.label : status;
};

// 获取订单状态类型
const getOrderStatusType = (status: string) => {
  const found = orderStatuses.value.find(item => item.value === status);
  return found ? found.type : '';
};

// 格式化金额
const formatCurrency = (row: any, column: any, cellValue: number) => {
  return `¥${cellValue.toFixed(2)}`;
};

// 搜索处理
const handleSearch = () => {
  // 这里添加搜索逻辑
  let filtered = allOrders.value;
  
  if (searchOrderNo.value) {
    filtered = filtered.filter(order => order.orderNo.includes(searchOrderNo.value));
  }
  
  if (searchUserName.value) {
    filtered = filtered.filter(order => order.userName.includes(searchUserName.value));
  }
  
  if (searchProduct.value) {
    filtered = filtered.filter(order => order.productName.includes(searchProduct.value));
  }
  
  if (searchOrderStatus.value) {
    filtered = filtered.filter(order => order.status === searchOrderStatus.value);
  }
  
  // 日期范围搜索
  if (searchDateRange.value[0] && searchDateRange.value[1]) {
    const startDate = new Date(searchDateRange.value[0]);
    const endDate = new Date(searchDateRange.value[1]);
    
    filtered = filtered.filter(order => {
      const orderDate = new Date(order.orderDate);
      return orderDate >= startDate && orderDate <= endDate;
    });
  }
  
  total.value = filtered.length;
  tableData.value = filtered;
};

// 重置搜索
const resetSearch = () => {
  searchOrderNo.value = '';
  searchUserName.value = '';
  searchProduct.value = '';
  searchOrderStatus.value = '';
  searchDateRange.value = [null, null];
  currentPage.value = 1;
  
  // 重置表格数据
  tableData.value = allOrders.value;
  total.value = allOrders.value.length;
};

// 分页大小变化
const handleSizeChange = (val: number) => {
  pageSize.value = val;
};

// 当前页码变化
const handleCurrentChange = (val: number) => {
  currentPage.value = val;
};

// 页面加载时初始化数据
onMounted(() => {
  initAllOrders();
});
</script>

<style scoped>
.order-management {
  padding: 20px;
}

.search-container {
  background-color: #f5f7fa;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.card-container {
  background-color: #fff;
  border-radius: 4px;
  padding: 20px;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}
</style>