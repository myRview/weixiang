<template>
  <div class="plan-management">
    <div class="search-container">
      <el-card class="search-card">
        <!-- 搜索表单 -->
        <el-form :model="searchParam" class="search-form" inline>
          <el-form-item label="套餐名称">
            <el-input
              v-model="searchParam.name"
              placeholder="请输入套餐名称"
              clearable
            />
          </el-form-item>
          <el-form-item label="状态" >
            <el-select
              v-model="searchParam.status"
              style="width: 120px"
              placeholder="请选择状态"
              clearable
            >
              <el-option
                v-for="(item, key) in StatusEnums"
                :key="key"
                :value="item.code"
                :label="item.description"
              />
            </el-select>
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
        <el-table-column prop="id" label="套餐ID" width="100"></el-table-column>
        <el-table-column
          prop="name"
          label="套餐名称"
          min-width="150"
        ></el-table-column>
        <el-table-column prop="price" label="套餐价格" width="120">
          <template #default="scope">
            ¥{{ scope.row.price.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column
          prop="description"
          label="套餐描述"
          min-width="200"
        ></el-table-column>
        <el-table-column
          prop="validityDays"
          label="有效期(天)"
          width="120"
        ></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">{{
              scope.row.status === 1 ? "启用" : "禁用"
            }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" width="180">
          <template #default="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template #default="scope">
            <el-button
              type="primary"
              size="small"
              style="margin-right: 5px"
              @click="handleEdit(scope.row)"
              >编辑</el-button
            >
            <el-button
              type="danger"
              size="small"
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
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { ElMessage } from "element-plus";
import dayjs from "dayjs";
import { selectPlanPage } from "@/api/huiyuantaocanguanli";
import { StatusEnums } from '@/enums/status.enum';
const formatDate = (date: string | number | Date) => {
  return dayjs(date).format("YYYY-MM-DD HH:mm:ss");
};
const searchParam = ref<API.PlanSearchParam>({});

// 套餐数据
const tableData = ref<API.MemberPlanVO[]>([]);
  const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);
const selectPage = async () => {
  const res = await selectPlanPage(searchParam.value);
  if (res.data.code === 200) {
    tableData.value = res.data.data?.records;
    total.value = res.data.data?.total || 0;
  }
};

// 编辑套餐
const handleEdit = (plan: API.MemberPlanVO) => {
  ElMessage.info(`编辑套餐: ${plan.name}`);
  // 这里可以添加编辑逻辑
};

// 删除套餐
const handleDelete = (plan: API.MemberPlanVO) => {
  ElMessage.warning(`删除套餐: ${plan.name}`);
  // 这里可以添加删除逻辑
};
// 搜索事件
const handleSearch = () => {
  // currentPage.value = 1;
  selectPage();
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
.plan-management {
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

.pagination-container {
  margin-top: 15px;
  text-align: right;
}
/* 响应式调整 */
@media (max-width: 768px) {
  .plan-management {
    padding: 10px;
  }

  .card-container {
    padding: 15px;
  }

  ::v-deep .el-table {
    font-size: 13px;
  }
}
</style>
