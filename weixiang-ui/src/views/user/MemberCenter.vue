<template>
  <div class="member-center-container">
    <el-card class="member-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <span>会员中心</span>
          <el-button type="primary" size="small" v-if="!isMember" @click="goToPurchase">立即开通</el-button>
          <el-button type="success" size="small" v-else>已开通会员</el-button>
        </div>
      </template>
      <div class="member-info">
        <el-avatar :src="user.avatar" size="large"></el-avatar>
        <div class="user-basic-info">
          <h3>{{ user.userName || user.account }}</h3>
          <p class="member-expiry" v-if="isMember">会员到期时间: {{ memberExpiryDate }}</p>
        </div>
      </div>
      <el-divider />
      <div class="member-benefits">
        <h4>会员权益</h4>
        <ul class="benefits-list">
          <li v-for="benefit in memberBenefits" :key="benefit.id" class="benefit-item">
            <el-icon :class="benefit.icon"></el-icon>
            <span>{{ benefit.description }}</span>
          </li>
        </ul>
      </div>
      <el-divider />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useLoginUserStore } from "@/store/loginUser";
import { User, Calendar, ArrowRight, ShoppingCart }
  from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
import { getPayPlan } from "@/api/yonghutaocanbiaoguanli";

// 获取登录用户存储
const loginUserStore = useLoginUserStore();
const user = ref<API.UserVO>(loginUserStore.loginUser);

// 会员信息
const isMember = ref(false);
const memberExpiryDate = ref("");
const memberBenefits = ref<Array<{
  id: number;
  description: string;
  icon: string;
}>>([]);

// 获取会员信息
const getMemberInfo = async () => {
  try {
    const res = await getPayPlan({ userId: user.value.id });
    if (res.data.code === 200 && res.data.data) {
      isMember.value = true;
      memberExpiryDate.value = res.data.data.endDate || "";
    }
  } catch (error) {
    ElMessage.error("获取会员信息失败");
  }
};

// 设置会员权益
const setMemberBenefits = () => {
  if (isMember.value) {
    memberBenefits.value = [
      {
        id: 1,
        description: "无限次文章查看",
        icon: "Award"
      },
      {
        id: 2,
        description: "优先审批文章",
        icon: "ArrowRight"
      },
      {
        id: 3,
        description: "专属会员标识",
        icon: "Vip"
      },
      {
        id: 4,
        description: "会员到期提醒",
        icon: "Calendar"
      }
    ];
  } else {
    memberBenefits.value = [
      {
        id: 1,
        description: "开通会员解锁全部权益",
        icon: "Vip"
      }
    ];
  }
};

// 前往购买会员
const goToPurchase = () => {
  // 跳转到会员购买页面
  window.location.href = "/plan-management";
};

// 组件挂载时获取会员信息
onMounted(async () => {
  await getMemberInfo();
});
</script>

<style scoped>
.member-center-container {
  padding: 20px;
}

.member-card {
  max-width: 800px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.member-info {
  display: flex;
  padding: 20px 0;
  align-items: center;
}

.member-info .el-avatar {
  margin-right: 20px;
}

.user-basic-info {
  flex: 1;
}

.user-basic-info h3 {
  margin: 0 0 10px 0;
  font-size: 20px;
  font-weight: 600;
}

.member-level,
.member-expiry {
  margin: 5px 0;
  color: #606266;
}

.member-benefits {
  padding: 10px 0;
}

.benefits-list {
  list-style: none;
  padding: 0;
}

.benefit-item {
  display: flex;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px dashed #ebeef5;
}

.benefit-item .el-icon {
  margin-right: 10px;
  color: #4a6cf7;
  font-size: 18px;
}

.member-actions {
  padding: 10px 0;
  display: flex;
  gap: 10px;
}
</style>