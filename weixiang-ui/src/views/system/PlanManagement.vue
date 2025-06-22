<template>
  <div class="plan-management">
    <div class="card-container">
      <el-table :data="tableData" stripe border style="width: 100%">
        <el-table-column prop="id" label="套餐ID" width="100"></el-table-column>
        <el-table-column prop="name" label="套餐名称" min-width="150"></el-table-column>
        <el-table-column prop="price" label="套餐价格" width="120">
          <template #default="scope">
            ¥{{ scope.row.price.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="description" label="套餐描述" min-width="200"></el-table-column>
        <el-table-column prop="duration" label="有效期(天)" width="120"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 'active' ? 'success' : 'info'">{{ scope.row.status === 'active' ? '启用' : '禁用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" width="180">
          <template #default="scope">
            <el-button type="primary" size="small" style="margin-right: 5px" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { ElMessage } from 'element-plus';

// 定义套餐数据接口
interface Plan {
  id: number;
  name: string;
  price: number;
  description: string;
  duration: number;
  status: 'active' | 'inactive';
  createTime: string;
}

// 套餐数据
const tableData = ref<Plan[]>([]);

// 生成随机日期时间
const getRandomDateTime = (): string => {
  const date = new Date(Date.now() - Math.floor(Math.random() * 30 * 24 * 60 * 60 * 1000));
  return date.toISOString().replace('T', ' ').slice(0, 19);
};

// 初始化套餐数据
const initPlans = () => {
  const plans: Plan[] = [
    {
      id: 1,
      name: '基础套餐',
      price: 99.00,
      description: '适合个人用户的基础功能套餐',
      duration: 30,
      status: 'active',
      createTime: getRandomDateTime()
    },
    {
      id: 2,
      name: '高级套餐',
      price: 199.00,
      description: '包含更多高级功能的套餐',
      duration: 30,
      status: 'active',
      createTime: getRandomDateTime()
    },
    {
      id: 3,
      name: '企业套餐',
      price: 499.00,
      description: '适合小型企业使用的套餐',
      duration: 90,
      status: 'active',
      createTime: getRandomDateTime()
    },
    {
      id: 4,
      name: '尊享套餐',
      price: 999.00,
      description: '包含全部功能的尊享套餐',
      duration: 180,
      status: 'inactive',
      createTime: getRandomDateTime()
    },
    {
      id: 5,
      name: '试用套餐',
      price: 0.00,
      description: '新用户免费试用7天',
      duration: 7,
      status: 'active',
      createTime: getRandomDateTime()
    }
  ];
  return plans;
};

// 编辑套餐
const handleEdit = (plan: Plan) => {
  ElMessage.info(`编辑套餐: ${plan.name}`);
  // 这里可以添加编辑逻辑
};

// 删除套餐
const handleDelete = (plan: Plan) => {
  ElMessage.warning(`删除套餐: ${plan.name}`);
  // 这里可以添加删除逻辑
};

// 初始化数据
tableData.value = initPlans();
</script>

<style scoped>
.plan-management {
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