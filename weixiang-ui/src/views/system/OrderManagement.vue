<template>
  <div class="order-management">
    <div class="search-container">
      <el-card class="search-card">
        <el-form class="search-form">
          <el-row :gutter="20">
            <el-col :span="6" :xs="24" :sm="12" :md="8" :lg="6">
              <el-form-item label="订单编号">
                <el-input
                  v-model="searchParam.orderNo"
                  placeholder="请输入订单编号"
                  clearable
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6" :xs="24" :sm="12" :md="8" :lg="6">
              <el-form-item label="用户姓名">
                <el-input
                  v-model="searchParam.userName"
                  placeholder="请输入用户姓名"
                  clearable
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6" :xs="24" :sm="12" :md="8" :lg="6">
              <el-form-item label="套餐名称">
                <el-input
                  v-model="searchParam.planName"
                  placeholder="请输入套餐名称"
                  clearable
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6" :xs="24" :sm="12" :md="8" :lg="6">
              <el-form-item label="订单状态">
                <el-select
                  v-model="searchParam.status"
                  placeholder="请选择订单状态"
                  clearable
                  style="width: 100%"
                >
                  <el-option
                    v-for="item in ORDER_STATUS_OPTIONS"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20" style="margin-top: 15px">
            <el-col :span="12" :xs="24" :sm="14" :md="14" :lg="14">
              <el-form-item label="下单日期" class="compact-date-picker">
                <el-date-picker
                  v-model="searchDateRange"
                  type="daterange"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  clearable
                  value-format="YYYY-MM-DD"
                ></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col
              :span="12"
              :xs="24"
              :sm="10"
              :md="10"
              :lg="10"
              class="action-buttons"
            >
              <el-button type="primary" @click="handleSearch">搜索</el-button>
              <el-button @click="resetSearch">重置</el-button>
            </el-col>
          </el-row>
        </el-form>
      </el-card>
    </div>
    <div class="card-container">
      <el-table :data="tableData" stripe border style="width: 100%">
        <el-table-column
          prop="id"
          label="订单ID"
          width="100"
          show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="orderNumber"
          label="订单编号"
          min-width="140"
          show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="userName"
          label="用户姓名"
          min-width="100"
          show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="planName"
          label="套餐名称"
          show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column label="下单日期" show-overflow-tooltip="true">
          <template #default="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="订单金额" width="120">
          <template #default="scope">
            ¥{{ scope.row.amount.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="订单状态" width="100">
          <template #default="scope">
            <el-tag :type="getTagType(scope.row.status)">
              {{ ORDER_STATUS_MAP[scope.row.status] }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button
              type="danger"
              size="small"
              @click="handleDelete(scope.row.id)"
              >删除
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
import { selectOrderPage, deleteOrder } from "@/api/dingdanguanli";
import { ORDER_STATUS_MAP, ORDER_STATUS_OPTIONS } from "@/enums/order.enum";
import { ElMessage, ElMessageBox } from "element-plus";
import { ref, onMounted } from "vue";
import dayjs from "dayjs";

const formatDate = (date: string | number | Date) => {
  return dayjs(date).format("YYYY-MM-DD HH:mm:ss");
};

const searchDateRange = ref<[Date | null, Date | null]>([null, null]);

const getTagType = (status: string) => {
  switch (status) {
    case "1":
      return "success";
    case "0":
      return "info";
    case "32":
      return "warning";
    default:
      return "";
  }
};

// 分页相关
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);
const tableData = ref<API.OrderVO[]>([]);
const searchParam = ref<API.OrderSearchParam>({});

// 初始化所有订单数据
const initAllOrders = async () => {
  try {
    searchParam.value.startDate = searchDateRange.value[0]
      ? dayjs(searchDateRange.value[0]).format("YYYY-MM-DD")
      : undefined;
    searchParam.value.endDate = searchDateRange.value[1]
      ? dayjs(searchDateRange.value[1]).format("YYYY-MM-DD")
      : undefined;
    searchParam.value.pageNum = currentPage.value;
    searchParam.value.pageSize = pageSize.value;
    const res = await selectOrderPage(searchParam.value);
    if (res.data.code === 200) {
      tableData.value = res.data.data?.records || [];
      total.value = Number(res.data.data?.total) || 0;
    }
  } catch (error) {
    console.error("获取订单列表失败", error);
  }
};

// 搜索处理
const handleSearch = () => {
  initAllOrders();
};

// 重置搜索
const resetSearch = () => {
  searchParam.value = {};
  searchDateRange.value = [null, null];
  handleSearch();
};

// 分页大小变化
const handleSizeChange = (val: number) => {
  pageSize.value = val;
  handleSearch();
};

// 当前页码变化
const handleCurrentChange = (val: number) => {
  currentPage.value = val;
  handleSearch();
};

// 删除订单
const handleDelete = async (id: number) => {
  try {
    const confirmResult = await ElMessageBox.confirm(
      "确定要删除该订单吗？",
      "警告",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }
    );

    if (confirmResult === "confirm") {
      const res = await deleteOrder({ id: id });
      if (res.data.code === 200) {
        ElMessage.success("订单删除成功");
        handleSearch();
      }
    }
  } catch (error) {
    if (error === "cancel") return;
    console.error("删除订单失败", error);
    ElMessage.error("删除订单失败");
  }
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
  margin-bottom: 20px;
}
.search-card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  padding: 20px;
}
.search-form {
  margin: -10px;
}

.card-container {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  padding: 20px;
  margin-bottom: 20px;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.action-buttons {
  display: flex;
  align-items: flex-end;
  justify-content: flex-end;
  gap: 10px;
  height: 100%;
}

.compact-date-picker :deep(.el-date-editor) {
  width: 360px; /* 缩小后的宽度 */
}

@media (max-width: 1200px) {
  .compact-date-picker :deep(.el-date-editor) {
    width: 320px;
  }
}

@media (max-width: 992px) {
  .compact-date-picker :deep(.el-date-editor) {
    width: 280px;
  }
}

@media (max-width: 768px) {
  .action-buttons {
    justify-content: flex-start;
    margin-top: 15px;
  }

  .search-form :deep(.el-form-item) {
    margin-bottom: 15px;
  }

  .compact-date-picker :deep(.el-date-editor) {
    width: 100%;
  }
}
</style>
