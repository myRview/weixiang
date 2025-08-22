<template>
  <div class="member-center-container">
    <el-card class="member-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <span>会员中心</span>
          <el-button
            type="primary"
            size="small"
            v-if="!isMember"
            @click="getPlanList"
            >立即开通</el-button
          >
          <el-button type="success" size="small" v-else>已开通会员</el-button>
        </div>
      </template>
      <div class="member-info">
        <el-avatar :src="user.avatar" size="large"></el-avatar>
        <div class="user-basic-info">
          <h3>{{ user.userName || user.account }}</h3>
          <p class="member-expiry" v-if="isMember">
            会员到期时间: {{ memberExpiryDate }}
          </p>
        </div>
      </div>
      <el-divider />
      <div class="member-benefits">
        <h4>会员权益</h4>
        <ul class="benefits-list">
          <li
            v-for="benefit in memberBenefits"
            :key="benefit.id"
            class="benefit-item"
          >
            <el-icon :class="benefit.icon"></el-icon>
            <span>{{ benefit.description }}</span>
          </li>
        </ul>
      </div>
      <el-divider />
    </el-card>

    <!-- 会员套餐列表弹框 -->
    <el-dialog
      v-model="planDialogVisible"
      title="会员套餐"
      :close-on-click-modal="false"
      width="800px"
    >
      <div class="plan-table-container">
        <el-table :data="planData" style="width: 100%">
          <el-table-column prop="name" label="套餐名称" width="220">
            <template #default="scope">
              <div class="plan-name-container">
                <span class="plan-name">{{ scope.row.name }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="price" label="价格" width="140">
            <template #default="scope">
              <div class="price-container">
                <span class="price-symbol">¥</span>
                <span class="price-value">{{ scope.row.price }}</span>
                <span class="price-unit">/{{ scope.row.validityDays }}天</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="validityDays" label="有效期" width="160">
            <template #default="scope">
              <div class="validity-container">
                <el-icon class="calendar-icon">
                  <Calendar />
                </el-icon>
                <span>{{ scope.row.validityDays }}天</span>
                <span class="validity-tip"
                  >({{ Math.round(scope.row.validityDays / 30) }}个月)</span
                >
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="180" fixed="right">
            <template #default="scope">
              <el-button
                type="primary"
                size="small"
                class="select-plan-btn"
                :class="{ 'recommended-btn': scope.row.recommended }"
                @click="handleSelectPlan(scope.row.id)"
              >
                <span>选择此套餐</span>
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-dialog>

    <!-- 支付二维码弹框 -->
    <el-dialog
      v-model="payDialogVisible"
      title="扫码支付"
      width="400px"
      :close-on-click-modal="false"
      :before-close="handlePayClose"
    >
      <div class="pay-dialog-content">
        <div v-if="orderStatusText === '支付成功'" class="payment-success">
          <el-icon color="#67C23A" :size="60"><CircleCheckFilled /></el-icon>
          <div class="success-text">支付成功！</div>
          <div class="success-tip">会员已开通，即将自动关闭...</div>
        </div>
        <div v-else>
          <div class="qr-code-container">
            <img :src="qrCodeImage" alt="支付二维码" class="qr-code-img" />
            <div class="order-info">
              <div class="order-item">
                <span class="order-label">套餐名称：</span>
                <span class="order-value">{{ currentPlanName }}</span>
              </div>
              <div class="order-item">
                <span class="order-label">订单金额：</span>
                <span class="order-value price">¥{{ currentPlanPrice }}</span>
              </div>
              <div class="order-item">
                <span class="order-label">订单号：</span>
                <span class="order-value order-id">{{ currentOrderId }}</span>
              </div>
              <div class="order-item">
                <span class="order-label">有效时间：</span>
                <span class="order-value">{{ countdownText }}</span>
              </div>
            </div>
          </div>
          <div class="payment-status">
            <div class="status-text">{{ orderStatusText }}</div>
            <div class="status-tip">请使用支付宝扫描上方二维码完成支付</div>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useLoginUserStore } from "@/store/loginUser";
import {
  User,
  Calendar,
  ArrowRight,
  ShoppingCart,
  Star,
  CircleCheckFilled,
} from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
import { getPayPlan, payPlan } from "@/api/yonghutaocanbiaoguanli";
import { selectPlanList } from "@/api/huiyuantaocanguanli";
import { getOrderStatusById } from "@/api/dingdanguanli";
import QRCode from "qrcode";
import { useRouter } from "vue-router";

const router = useRouter();

// 获取登录用户存储
const loginUserStore = useLoginUserStore();
const user = ref<API.UserVO>(loginUserStore.loginUser);

// 会员信息
const isMember = ref(false);
const memberExpiryDate = ref("");
const memberBenefits = ref<
  Array<{
    id: number;
    description: string;
    icon: string;
  }>
>([]);

// 会员套餐相关
const planData = ref<API.MemberPlanVO[]>([]);
const planDialogVisible = ref(false);

// 支付弹窗控制
const payDialogVisible = ref(false);
// 二维码图片数据
const qrCodeImage = ref("");
// 支付有效时间倒计时（毫秒）
const countdown = ref(0);
// 倒计时显示文本（分:秒）
const countdownText = ref("");
// 订单状态查询定时器
const orderTimer = ref<NodeJS.Timeout | null>(null);
// 倒计时定时器
const countdownTimer = ref<NodeJS.Timeout | null>(null);
// 当前订单ID
const currentOrderId = ref(0);
// 订单状态信息
const orderStatusText = ref("等待支付...");
// 当前套餐名称
const currentPlanName = ref("");
// 当前套餐价格
const currentPlanPrice = ref(0);
// 订单开始时间
const startTime = ref(Date.now());

// 获取会员信息
const getMemberInfo = async () => {
  try {
    const res = await getPayPlan({ userId: user.value.id });
    if (res.data.code === 200 && res.data.data) {
      isMember.value = true;
      memberExpiryDate.value = res.data.data.endDate || "";
    }
    setMemberBenefits();
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
        icon: "Award",
      },
      {
        id: 2,
        description: "优先审批文章",
        icon: "ArrowRight",
      },
      {
        id: 3,
        description: "专属会员标识",
        icon: "Vip",
      },
      {
        id: 4,
        description: "会员到期提醒",
        icon: "Calendar",
      },
    ];
  } else {
    memberBenefits.value = [
      {
        id: 1,
        description: "开通会员解锁全部权益",
        icon: "Vip",
      },
    ];
  }
};

// 获取套餐列表
const getPlanList = async () => {
  try {
    const res = await selectPlanList();
    if (res.data.code === 200) {
      planData.value = res.data.data;
      planDialogVisible.value = true;
    } else {
      ElMessage.error(res.data.message);
    }
  } catch (error) {
    console.error("获取会员列表失败", error);
    ElMessage.error("获取会员列表失败");
  }
};

// 选择套餐
const handleSelectPlan = async (planId: number) => {
  try {
    // 找到选中的套餐
    const plan = planData.value.find((p) => p.id === planId);
    if (!plan) {
      ElMessage.error("未找到套餐信息");
      return;
    }

    // 保存套餐信息
    currentPlanName.value = plan.name;
    currentPlanPrice.value = plan.price;

    // 调用支付接口
    const res = await payPlan({ planId, userId: user.value.id });
    if (res.data.code === 200) {
      const qrCodeUrl = res.data.data?.qrCodeUrl;
      currentOrderId.value = res.data.data?.id || 0;
      // 获取有效时间（后端返回毫秒）
      const validTime = res.data.data?.validTime || 900000;
      countdown.value = validTime;
      // 初始化倒计时文本
      const minutes = Math.floor(validTime / 60000);
      const seconds = Math.floor((validTime % 60000) / 1000);
      countdownText.value = `${minutes}分${seconds
        .toString()
        .padStart(2, "0")}秒`;

      // 记录开始时间
      startTime.value = Date.now();

      // 生成二维码
      QRCode.toDataURL(
        qrCodeUrl,
        {
          width: 300,
          margin: 2,
          color: {
            dark: "#000000",
            light: "#ffffff",
          },
        },
        (err, url) => {
          if (err) {
            ElMessage.error("二维码生成失败");
            return;
          }
          qrCodeImage.value = url;
          payDialogVisible.value = true;
          planDialogVisible.value = false;

          // 重置支付状态
          orderStatusText.value = "等待支付...";
          // 启动订单状态查询定时器
          if (orderTimer.value) {
            clearInterval(orderTimer.value);
          }
          // 启动订单状态查询定时器
          orderTimer.value = setInterval(checkOrderStatus, 1500);
          // 启动倒计时定时器
          countdownTimer.value = setInterval(() => {
            countdown.value -= 1000;
            const minutes = Math.floor(countdown.value / 60000);
            const seconds = Math.floor((countdown.value % 60000) / 1000);
            countdownText.value = `${minutes}分${seconds
              .toString()
              .padStart(2, "0")}秒`;
            if (countdown.value <= 0) {
              clearInterval(countdownTimer.value as NodeJS.Timeout);
              orderStatusText.value = "订单已过期";
              ElMessage.warning("支付超时，订单已过期");
              // 清除订单状态定时器
              if (orderTimer.value) {
                clearInterval(orderTimer.value);
                orderTimer.value = null;
              }
            }
          }, 1000);
        }
      );
    } else {
      ElMessage.error(res.data.message);
    }
  } catch (error) {
    console.error("开通会员失败", error);
    ElMessage.error("开通会员失败");
  }
};

// 检查订单状态
const checkOrderStatus = async () => {
  try {
    // 调用后端API查询订单状态
    const res = await getOrderStatusById({ id: currentOrderId.value });

    if (res.data.code === 200) {
      const status = res.data.data;
      switch (status) {
        case 1: // 支付成功
          orderStatusText.value = "支付成功";
          // 3秒后自动关闭弹窗
          setTimeout(() => {
            payDialogVisible.value = false;
            isMember.value = true;
            getMemberInfo(); // 刷新会员信息
            ElMessage.success("会员开通成功！");
          }, 3000);

          // 清除定时器
          if (orderTimer.value) {
            clearInterval(orderTimer.value);
            orderTimer.value = null;
          }
          return;

        case 2: // 支付失败
          orderStatusText.value = "支付失败";
          ElMessage.error("支付失败，请重新尝试或联系客服");

          // 清除定时器
          if (orderTimer.value) {
            clearInterval(orderTimer.value);
            orderTimer.value = null;
          }
          return;

        case 3: // 订单已关闭
          orderStatusText.value = "订单已关闭";
          ElMessage.warning("支付超时，订单已关闭");

          // 清除定时器
          if (orderTimer.value) {
            clearInterval(orderTimer.value);
            orderTimer.value = null;
          }
          return;

        default: // 未支付
          // 根据等待时间更新状态文本
          const elapsedSeconds = Math.floor(
            (Date.now() - startTime.value) / 1000
          );
          if (elapsedSeconds > 120) {
            orderStatusText.value = "支付处理中...";
          } else {
            orderStatusText.value = "等待支付...";
          }
      }
    } else {
      ElMessage.error(res.data.message || "查询订单状态失败");
    }
  } catch (error) {
    console.error("查询订单状态失败", error);
  }
};

// 关闭支付弹窗的处理
const handlePayClose = (done: () => void) => {
  if (orderTimer.value) {
    clearInterval(orderTimer.value);
    orderTimer.value = null;
  }
  // 清除倒计时定时器
  if (countdownTimer.value) {
    clearInterval(countdownTimer.value);
    countdownTimer.value = null;
  }
  done();
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

/* 会员套餐相关样式 */
.plan-table-container {
  margin: 10px;
}

.plan-name-container {
  display: flex;
  align-items: center;
  gap: 8px;
}

.hot-tag {
  background-color: #ff4d4f;
  color: white;
  font-size: 12px;
  padding: 2px 6px;
  border-radius: 4px;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0% {
    box-shadow: 0 0 0 0 rgba(255, 77, 79, 0.4);
  }
  70% {
    box-shadow: 0 0 0 6px rgba(255, 77, 79, 0);
  }
  100% {
    box-shadow: 0 0 0 0 rgba(255, 77, 79, 0);
  }
}

.price-container {
  display: flex;
  align-items: baseline;
}

.price-symbol {
  font-size: 14px;
  color: #77ff4d;
}

.price-value {
  font-size: 20px;
  font-weight: bold;
  color: #ff4d4f;
}

.price-unit {
  font-size: 12px;
  color: #8c8c8c;
  margin-left: 4px;
}

.validity-container {
  display: flex;
  align-items: center;
  gap: 6px;
}

.calendar-icon {
  color: #1890ff;
}

.validity-tip {
  font-size: 12px;
  color: #8c8c8c;
}

.select-plan-btn {
  width: 120px;
}

.recommended-btn {
  background: linear-gradient(135deg, #ff4d4f 0%, #ff7a45 100%);
  border: none;
}

/* 支付弹窗样式 */
.pay-dialog-content {
  text-align: center;
  padding: 10px;
}

.qr-code-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 20px;
}

.qr-code-img {
  width: 220px;
  height: 220px;
  border: 1px solid #eee;
  padding: 10px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 15px;
}

.order-info {
  width: 100%;
  text-align: left;
  padding: 0 10px;
}

.order-item {
  display: flex;
  margin-bottom: 10px;
  font-size: 14px;
}

.order-label {
  color: #606266;
  width: 80px;
  text-align: right;
}

.order-value {
  flex: 1;
  color: #303133;
  font-weight: 500;
}

.price {
  color: #f56c6c;
  font-size: 18px;
  font-weight: bold;
}

.order-id {
  font-family: monospace;
  letter-spacing: 1px;
}

.payment-status {
  margin-top: 15px;
}

.status-text {
  font-size: 16px;
  color: #606266;
  margin-bottom: 10px;
  font-weight: 500;
}

.status-tip {
  margin-top: 15px;
  color: #909399;
  font-size: 13px;
}

.payment-success {
  padding: 30px 0;
}

.success-text {
  font-size: 22px;
  color: #67c23a;
  font-weight: bold;
  margin: 15px 0;
}

.success-tip {
  color: #909399;
  font-size: 14px;
}
</style>
