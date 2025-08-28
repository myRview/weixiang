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
        <el-avatar
          :src="user.avatar ? `${baseURL}${user.avatar}` : defaultAvatar"
          size="large"
        ></el-avatar>
        <div class="user-basic-info">
          <h3>{{ user.userName || user.account }}</h3>
          <p class="member-expiry" v-if="isMember">
            会员到期时间: {{ memberExpiryDate }}
          </p>
        </div>
        <div class="member-action" v-if="isMember">
          <el-button type="primary" size="small" @click="getPlanList">
            立即续费
          </el-button>
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
import { ref, onMounted, onUnmounted, watch } from "vue";
import { useLoginUserStore } from "@/store/loginUser";
import {
  Calendar,
  ArrowRight,
  CircleCheckFilled,
} from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
import { getPayPlan, payPlan } from "@/api/yonghutaocanbiaoguanli";
import { selectPlanList } from "@/api/huiyuantaocanguanli";
import QRCode from "qrcode";
import { useRouter } from "vue-router";
import { PushTypeEnum } from "@/enums/message.enum";
import { baseURL } from "@/request";
import { webSocketService } from "@/router/index";

const router = useRouter();

const defaultAvatar =
  "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png";

// 登录用户状态
const loginUserStore = useLoginUserStore();
const user = ref<API.UserVO>(loginUserStore.loginUser);

// 会员状态
const isMember = ref(false);
const memberExpiryDate = ref("");
const memberBenefits = ref<{ id: number; description: string; icon: string }[]>(
  []
);

// 套餐弹窗
const planData = ref<API.MemberPlanVO[]>([]);
const planDialogVisible = ref(false);

// 支付弹窗相关
const payDialogVisible = ref(false);
const qrCodeImage = ref("");
const countdown = ref(0);
const countdownText = ref("");
const countdownTimer = ref<NodeJS.Timeout | null>(null);
const currentOrderId = ref("");
const orderStatusText = ref("等待支付...");
const currentPlanName = ref("");
const currentPlanPrice = ref(0);

// WebSocket消息处理
const handleWebSocketMessage = (data: any) => {
  console.log(
      "收到订单消息，但不匹配当前订单:",
      data.orderId,
      "当前:",
      currentOrderId.value
    );
  handleOrderStatusChange(data.orderStatus);
};

// 初始化WebSocket监听
const initWebSocketListeners = () => {
  webSocketService.on(PushTypeEnum.ORDER, handleWebSocketMessage);
};

// 清理WebSocket监听
const cleanupWebSocketListeners = () => {
  webSocketService.off(PushTypeEnum.ORDER, handleWebSocketMessage);
};

// 订单状态变化处理
const handleOrderStatusChange = (status: number) => {
  switch (status) {
    case 1: // 支付成功
      getMemberInfo().then(() => {
        if (countdownTimer.value) {
          clearInterval(countdownTimer.value);
          countdownTimer.value = null;
        }
        // 关闭支付弹框
        setTimeout(() => {
          payDialogVisible.value = false;
        }, 1000);
      });
      break;

    case 2: // 支付失败
      orderStatusText.value = "支付失败";
      ElMessage.error("支付失败，请重新尝试或联系客服");
      break;

    case 3: // 订单已关闭
      orderStatusText.value = "订单已关闭";
      ElMessage.warning("支付超时，订单已关闭");
      break;
  }
};

// 获取会员信息
const getMemberInfo = async () => {
  try {
    const res = await getPayPlan({ userId: user.value.id });
    if (res.data.code === 200 && res.data.data) {
      isMember.value = true;
      memberExpiryDate.value = res.data.data.endDate || "";
    } else {
      isMember.value = false;
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
      planData.value = res.data.data || [];
      planDialogVisible.value = true;
    } else {
      ElMessage.error(res.data.message);
    }
  } catch (error) {
    console.error("获取会员列表失败", error);
    ElMessage.error("获取会员列表失败");
  }
};

// 选择套餐处理
const handleSelectPlan = async (planId: number) => {
  try {
    const plan = planData.value.find((p) => p.id === planId);
    if (!plan) {
      ElMessage.error("未找到套餐信息");
      return;
    }

    currentPlanName.value = plan.name;
    currentPlanPrice.value = plan.price;

    // 调用支付接口
    const res = await payPlan({ planId, userId: user.value.id });
    if (res.data.code === 200) {
      const orderInfo = res.data.data;
      const qrCodeUrl = orderInfo?.qrCodeUrl;
      currentOrderId.value = orderInfo?.id || "";
      const validTime = orderInfo?.validTime || 900000; // 默认15分钟
      countdown.value = validTime;

      // 更新倒计时显示
      updateCountdown();

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

          // 启动倒计时定时器
          if (countdownTimer.value) {
            clearInterval(countdownTimer.value);
          }
          countdownTimer.value = setInterval(() => {
            countdown.value -= 1000;
            updateCountdown();

            if (countdown.value <= 0) {
              clearInterval(countdownTimer.value as NodeJS.Timeout);
              orderStatusText.value = "订单已过期";
              ElMessage.warning("支付超时，订单已过期");
            }
          }, 1000);
        }
      );
    } else {
      ElMessage.error(res.data.message);
    }
  } catch (error) {
    ElMessage.error("开通会员失败");
  }
};

// 更新倒计时显示文本
const updateCountdown = () => {
  const minutes = Math.floor(countdown.value / 60000);
  const seconds = Math.floor((countdown.value % 60000) / 1000);
  countdownText.value = `${minutes}分${seconds.toString().padStart(2, "0")}秒`;
};

// 关闭支付弹窗处理
const handlePayClose = (done: () => void) => {
  if (countdownTimer.value) {
    clearInterval(countdownTimer.value);
    countdownTimer.value = null;
  }
  done();
};

// 监听用户登录状态变化
watch(
  () => loginUserStore.loginUser,
  (newUser) => {
    if (newUser) {
      initWebSocketListeners();
      getMemberInfo();
    } else {
      cleanupWebSocketListeners();
    }
  },
  { immediate: true }
);

// 组件生命周期
onMounted(async () => {
  await getMemberInfo();
  initWebSocketListeners();
});

onUnmounted(() => {
  cleanupWebSocketListeners();
  if (countdownTimer.value) {
    clearInterval(countdownTimer.value);
  }
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
  justify-content: space-between;
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

.member-expiry {
  margin: 5px 0;
  color: #606266;
}

.member-action {
  margin-left: 20px;
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

/* 会员套餐相关样式 */
.plan-table-container {
  margin: 10px;
}

.plan-name-container {
  display: flex;
  align-items: center;
  gap: 8px;
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
