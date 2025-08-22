<template>
  <div class="profile-container">
    <!-- 顶部用户信息卡片 -->
    <div class="user-card">
      <div class="avatar-section">
        <div class="avatar-wrapper">
          <img
            :src="user?.avatar || defaultAvatar"
            alt="用户头像"
            class="avatar"
          />
          <div class="avatar-badge" v-show="isVip">VIP</div>
        </div>
        <div class="follow-stats">
          <div class="stat-item hover-effect">
            <div class="stat-value">{{ flowData.length }}</div>
            <div class="stat-label">关注</div>
          </div>
          <div class="divider"></div>
          <div class="stat-item hover-effect">
            <div class="stat-value">{{ fensData.length }}</div>
            <div class="stat-label">粉丝</div>
          </div>
        </div>
      </div>
      <div class="user-meta">
        <h1 class="username">{{ user?.userName }}</h1>
        <div class="user-profile-grid">
          <div class="info-item">
            <span class="info-label">性别</span>
            <span class="info-value">{{
              user?.gender === 1 ? "男" : "女"
            }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">地址</span>
            <span class="info-value">{{
              user?.expandVo?.address || "暂无信息"
            }}</span>
          </div>
          <div class="info-item full-width">
            <span class="info-label">简介</span>
            <span class="info-value bio-text">{{
              user?.expandVo?.bio || "这个用户很懒，什么都没留下～"
            }}</span>
          </div>
        </div>
      </div>
      <div
        class="action-buttons"
        style="display: flex; align-items: center; gap: 10px"
      >
        <el-button type="default" class="action-btn" @click="handleEditProfile">
          <el-icon>
            <Edit />
          </el-icon>
          编辑资料
        </el-button>
        <el-button
          :type="isTodaySigned ? 'default' : 'success'"
          class="action-btn"
          :disabled="isTodaySigned"
          @click="handleSignIn"
        >
          <el-icon v-if="!isTodaySigned">
            <Check />
          </el-icon>
          <el-icon v-if="isTodaySigned">
            <CircleCheckFilled />
          </el-icon>
          {{ isTodaySigned ? "已签到" : "立即签到" }}
        </el-button>
        <el-button
          type="primary"
          class="action-btn"
          v-show="!isVip"
          @click="toMemberCenter"
        >
          <el-icon>
            <Star />
          </el-icon>
          开通会员
        </el-button>
      </div>
    </div>
    <div class="info-section">
      <div class="section-content">
        <div class="progress-section">
          <div class="submission-stats">
            <div class="submission-count">累计签到 {{ totalSignCount }} 天</div>
            <div class="stats-group">
              <div class="submission-days">
                <span>本月签到: {{ monthSignInDays }}天</span>
                <span>连续签到: {{ continuousSignCount }}天</span>
              </div>
              <div class="year-selector">
                <select v-model="selectedYear" @change="handleYearChange">
                  <option v-for="year in years" :key="year" :value="year">
                    {{ year }}
                  </option>
                </select>
              </div>
            </div>
          </div>
          <!-- 日历热力图容器 -->
          <div ref="calendarRef" class="calendar-chart"></div>
        </div>
      </div>
    </div>

    <!-- 用户信息弹框组件 -->
    <el-dialog
      v-model="editDialogVisible"
      title="编辑资料"
      class="edit-dialog"
      :close-on-click-modal="false"
    >
      <el-form :model="userInfo" class="user-form" style="padding: 0 20px">
        <el-form-item label="账号" class="form-item-account">
          <el-input v-model="userInfo.account" disabled class="input-account" />
        </el-form-item>
        <el-form-item label="头像">
          <FileUpload
            class="avatar-upload-small"
            style="margin-bottom: 15px"
            fileType="image"
            :autoUpload="true"
            :clearAfterUpload="false"
            @upload-success="handleAvatarSuccess"
          />
          <img v-if="userInfo.avatar" :src="userInfo.avatar" class="avatar" />
        </el-form-item>
        <el-form-item label="用户名">
          <el-input v-model="userInfo.userName" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="userInfo.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="userInfo.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="10">
            <el-form-item label="性别">
              <el-select v-model="userInfo.gender" placeholder="请选择">
                <el-option label="男" value="male" />
                <el-option label="女" value="female" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="14">
            <el-form-item label="生日">
              <el-date-picker
                v-model="userInfo.birthday"
                type="date"
                placeholder="选择日期"
                value-format="YYYY-MM-DD"
                class="date-picker"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item
          label="地址"
          class="form-item-address"
          style="margin-top: 15px"
        >
          <el-input v-model="userInfo.address" placeholder="请输入详细地址" />
        </el-form-item>
        <el-form-item label="简介">
          <el-input
            v-model="userInfo.bio"
            type="textarea"
            :rows="4"
            placeholder="介绍一下自己吧～"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer" style="text-align: center; width: 100%">
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitEdit">确认修改</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import {
  editUser,
  getContinuousSignCount,
  getMonthSignCount,
  getSignRecord,
  getUserById,
  sign,
} from "@/api/yonghuguanli";
import { onMounted, ref, computed } from "vue";
import { useLoginUserStore } from "@/store/loginUser";
import dayjs from "dayjs";
import * as echarts from "echarts";
import { ElMessage } from "element-plus";
import {
  Edit,
  Check,
  Calendar,
  CircleCheckFilled,
  Star,
} from "@element-plus/icons-vue";
import { getPayPlan } from "@/api/yonghutaocanbiaoguanli";
import FileUpload from "@/components/FileUpload.vue"; // 导入自定义文件上传组件
import { fansList, list } from "@/api/guanzhuguanli";
import { useRouter } from "vue-router";
const router = useRouter();
// 默认头像
const defaultAvatar =
  "http://gips2.baidu.com/it/u=195724436,3554684702&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=960";

// 添加今天是否签到的状态
const isTodaySigned = ref(false);
const user = ref<API.UserVO>();
const calendarRef = ref<HTMLDivElement | null>(null);
// 图表实例
const chartInstance = ref<echarts.ECharts | null>(null);
// 本月签到次数
const monthSignInDays = ref(0);
// 获取连续签到天数
const continuousSignCount = ref(0);
// 总签到次数（过去一年）
const totalSignCount = ref(0);
// 签到记录
const signInRecords = ref<Record<string, boolean>>({});
const isVip = ref(false);
const loginUserStore = useLoginUserStore();
// 编辑资料处理
const editDialogVisible = ref(false);
const userInfo = ref({
  account: "",
  userName: "",
  phone: "",
  email: "",
  gender: "",
  birthday: "",
  address: "",
  bio: "",
  avatar: defaultAvatar,
});

const handleEditProfile = () => {
  userInfo.value = {
    account: user.value?.account || "",
    userName: user.value?.userName || "",
    avatar: user.value?.avatar || defaultAvatar,
    phone: user.value?.phone || "",
    email: user.value?.email || "",
    gender: user.value?.gender ? "male" : "female",
    birthday: user.value?.expandVo?.birthday || "",
    address: user.value?.expandVo?.address || "",
    bio: user.value?.expandVo?.bio || "",
  };
  editDialogVisible.value = true;
};

const handleAvatarSuccess = (data: any) => {
  // 更新头像URL
  console.log(data);
  userInfo.value.avatar = data;
  ElMessage.success("头像上传成功");
};

const submitEdit = async () => {
  try {
    const res = await editUser({
      userId: loginUserStore.loginUser.id,
      userName: userInfo.value.userName,
      phone: userInfo.value.phone,
      email: userInfo.value.email,
      gender: userInfo.value.gender === "male" ? 1 : 0,
      avatar: userInfo.value.avatar,
      birthday: userInfo.value.birthday,
      address: userInfo.value.address,
      bio: userInfo.value.bio,
    });
    if (res.data.code === 200) {
      ElMessage.success("资料更新成功");
      await getUserInfo();
      editDialogVisible.value = false;
    } else {
      ElMessage.error(res.data.message);
    }
  } catch (error) {
    console.error("资料更新失败", error);
    ElMessage.error("资料更新失败");
  }
};
// 签到处理
const handleSignIn = async () => {
  try {
    const res = await sign();
    if (res.data.code === 200) {
      ElMessage.success("签到成功");
      // 签到成功后刷新数据
      await getSignInRecords();
      await getContinuousCount();
      await getMonthSignInDays();
      // 更新今天签到状态
      isTodaySigned.value = true;
    }
  } catch (error) {
    console.error("签到失败", error);
  }
};

// 检查今天是否已签到
const checkTodaySign = () => {
  const today = dayjs().format("YYYY-MM-DD");
  isTodaySigned.value = !!signInRecords.value[today];
};

const toMemberCenter = () => {
  router.push({
    path: "/member-center",
  });
};

//获取用户购买的套餐
const getVipInfo = async () => {
  try {
    const res = await getPayPlan({ userId: loginUserStore.loginUser.id });
    if (res.data.code === 200) {
      res.data.data?.status === 1
        ? (isVip.value = true)
        : (isVip.value = false);
    }
  } catch (error) {
    console.error("获取用户购买的套餐失败", error);
  }
};

// 当前年份
const currentYear = ref(dayjs().year());
// 选中的年份
const selectedYear = ref(currentYear.value);
// 近三年的年份数组
const years = computed(() => {
  return [currentYear.value - 2, currentYear.value - 1, currentYear.value];
});

const getUserInfo = async () => {
  try {
    const res = await getUserById({ id: loginUserStore.loginUser.id });
    user.value = res.data.data;
  } catch (error) {
    console.error("获取用户信息失败", error);
  }
};

// 获取本月签到次数
const getMonthSignInDays = async () => {
  try {
    const res = await getMonthSignCount();
    monthSignInDays.value = res.data.data;
  } catch (error) {
    console.error("获取本月签到次数失败", error);
  }
};

// 获取连续签到天数
const getContinuousCount = async () => {
  try {
    const res = await getContinuousSignCount();
    continuousSignCount.value = res.data.data;
  } catch (error) {
    console.error("获取连续签到天数失败", error);
  }
};

// 年份变更处理
const handleYearChange = () => {
  getSignInRecords();
};

// 获取签到记录
const getSignInRecords = async () => {
  try {
    const res = await getSignRecord({ year: selectedYear.value });
    signInRecords.value = res.data.data;
    // 计算总签到次数
    totalSignCount.value = Object.keys(signInRecords.value).length;

    // 检查今天是否已签到
    checkTodaySign();

    // 渲染热力图
    renderCalendarHeatmap();
  } catch (error) {
    console.error("获取签到记录失败", error);
  }
};

// 生成热力图数据
const generateHeatmapData = () => {
  const data: [string, number][] = [];

  // 确定日期范围
  let startDate, endDate;
  if (selectedYear.value === currentYear.value) {
    // 当前年：从今天往前推一年
    endDate = dayjs();
    startDate = endDate.subtract(1, "year");
  } else {
    // 非当前年：整年数据
    startDate = dayjs(`${selectedYear.value}-01-01`);
    endDate = dayjs(`${selectedYear.value}-12-31`);
  }

  // 生成日期范围内的数据
  for (
    let day = startDate;
    day.isBefore(endDate) || day.isSame(endDate, "day");
    day = day.add(1, "day")
  ) {
    const dateStr = day.format("YYYY-MM-DD");
    const value = signInRecords.value[dateStr] ? 1 : 0;
    data.push([dateStr, value]);
  }

  return data;
};

// 渲染日历热力图
const renderCalendarHeatmap = () => {
  if (!calendarRef.value) return;

  // 初始化图表实例
  if (!chartInstance.value) {
    chartInstance.value = echarts.init(calendarRef.value);

    // 响应式调整
    window.addEventListener("resize", () => {
      chartInstance.value?.resize();
    });
  }

  // 确定日期范围
  let rangeStart, rangeEnd;
  if (selectedYear.value === currentYear.value) {
    rangeStart = dayjs().subtract(1, "year").format("YYYY-MM-DD");
    rangeEnd = dayjs().format("YYYY-MM-DD");
  } else {
    rangeStart = `${selectedYear.value}-01-01`;
    rangeEnd = `${selectedYear.value}-12-31`;
  }

  const option = {
    tooltip: {
      position: "top",
      formatter: function (params: any) {
        return `${params.value[0]}<br/>签到: ${
          params.value[1] > 0 ? "是" : "否"
        }`;
      },
    },
    visualMap: {
      show: false,
      min: 0,
      max: 1,
      inRange: {
        color: ["#f3f3f3", "#4af07caa"], // 未签到/签到
      },
    },
    calendar: {
      range: [rangeStart, rangeEnd],
      top: 30,
      bottom: 10,
      left: 40,
      right: 20,
      cellSize: 14,
      itemStyle: {
        borderWidth: 1.5, // 移除边框
        borderColor: "transparent", // 透明边框
      },
      dayLabel: {
        nameMap: ["日", "一", "二", "三", "四", "五", "六"],
        margin: 4,
        fontSize: 12,
        color: "#8c8c8c",
      },
      monthLabel: {
        position: "start",
        nameMap: "cn",
        fontSize: 12,
        margin: 5,
        color: "#595959",
        fontWeight: "bold",
      },
      yearLabel: {
        show: false, // 隐藏年份标签
      },
    },
    series: {
      type: "heatmap",
      coordinateSystem: "calendar",
      data: generateHeatmapData(),
      emphasis: {
        itemStyle: {
          borderWidth: 1,
          borderColor: "#333",
          shadowBlur: 3,
          shadowColor: "rgba(0,0,0,0.3)",
        },
      },
      itemStyle: {
        gap: 3, // 方块之间的间距
      },
    },
  };
  chartInstance.value.setOption(option);
};

const flowData = ref<API.UserVO[]>([]);
const fensData = ref<API.UserVO[]>([]);
const getFlowList = async () => {
  try {
    const res = await list({ userId: loginUserStore.loginUser.id });
    if (res.data.code === 200) {
      flowData.value = res.data.data || [];
    }
  } catch (error) {
    console.error("获取用户关注列表", error);
  }
};
const getFensList = async () => {
  try {
    const res = await fansList({ userId: loginUserStore.loginUser.id });
    if (res.data.code === 200) {
      fensData.value = res.data.data || [];
    }
  } catch (error) {
    console.error("获取用户粉丝列表", error);
  }
};

onMounted(async () => {
  await getUserInfo();
  await getMonthSignInDays();
  await getContinuousCount();
  await getSignInRecords();
  await getVipInfo();
  await getFlowList();
  await getFensList();
});
</script>

<style scoped>
.avatar-upload-small {
  max-width: 180px;
  width: 100%;
}
</style>

<style scoped>
.profile-container {
  margin: 0 auto;
  padding: 20px;
}

.user-card {
  display: flex;
  gap: 20px;
  background: linear-gradient(135deg, #ffffff 0%, #f0f5ff 100%);
  border-radius: 16px;
  padding: 16px;
  border: 1px solid rgba(255, 255, 255, 0.7);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.08),
    0 0 0 1px rgba(255, 255, 255, 0.8) inset;
  margin-bottom: 16px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
  backdrop-filter: blur(5px);
  max-height: 280px;
}

.user-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 28px rgba(0, 0, 0, 0.12),
    0 0 0 1px rgba(255, 255, 255, 0.8) inset;
}

.user-card::before {
  content: "";
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(
    circle at 50% 50%,
    rgba(255, 255, 255, 0.2) 0%,
    transparent 70%
  );
  transform: rotate(30deg);
  animation: shimmer 8s infinite linear;
}

@keyframes shimmer {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  min-width: 180px;
  position: relative;
}

.avatar-wrapper {
  position: relative;
  margin-bottom: 16px;
}

.avatar {
  width: 112px;
  height: 112px;
  border-radius: 50%;
  object-fit: cover;
  border: 4px solid #fff;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12), 0 0 0 1px rgba(255, 255, 255, 0.5);
  transition: transform 0.4s ease, box-shadow 0.4s ease;
}

.avatar:hover {
  transform: scale(1.05) rotate(3deg);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.16),
    0 0 0 1px rgba(255, 255, 255, 0.8);
}

.avatar-badge {
  position: absolute;
  bottom: 8px;
  right: -4px;
  background: linear-gradient(135deg, #ff6b6b, #ff8787);
  color: white;
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 600;
  box-shadow: 0 2px 8px rgba(255, 107, 107, 0.3);
}

.hover-effect:hover {
  transform: translateY(-2px);
  transition: transform 0.2s ease;
}

.user-meta {
  flex: 1;
  min-width: 0;
}

.username {
  font-size: 24px;
  font-weight: 700;
  color: #1a1a1a;
  position: relative;
  display: inline-block;
  background: linear-gradient(135deg, #2c3e50 0%, #3498db 100%);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 16px;
}

.user-profile-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
  margin-bottom: 16px;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  transition: all 0.3s;
  border: 1px solid rgba(255, 255, 255, 0.5);
}

.info-item:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
  border-color: rgba(145, 213, 255, 0.5);
  background: rgba(255, 255, 255, 1);
}

.info-label {
  font-weight: 500;
  color: #595959;
  min-width: 50px;
  font-size: 14px;
}

.info-value {
  color: #1a1a1a;
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 500px;
  display: block;
  font-size: 14px;
}

.full-width {
  grid-column: 1 / -1;
}

.follow-stats {
  display: flex;
  align-items: center;
  border-radius: 12px;
  padding: 10px 16px;
  backdrop-filter: blur(4px);
  width: 100%;
  justify-content: center;
  margin-bottom: 16px;
}

.stat-item {
  text-align: center;
  padding: 0 14px;
}

.stat-value {
  font-size: 18px;
  font-weight: 600;
  color: #262626;
}

.stat-label {
  font-size: 13px;
  color: #8c8c8c;
  margin-top: 2px;
}

.divider {
  width: 1px;
  height: 30px;
  background: #e8e8e8;
}

.action-buttons {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 16px;
  min-width: 180px;
  margin-left: 24px;
}

.action-btn {
  width: 180px !important;
  height: 44px !important;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 15px;
  margin: 0 auto;
  border-radius: 22px !important;
  font-weight: 500 !important;
  transition: all 0.3s !important;
  position: relative !important;
  overflow: hidden !important;
}

.action-btn::before {
  content: "" !important;
  position: absolute !important;
  top: 0 !important;
  left: -100% !important;
  width: 100% !important;
  height: 100% !important;
  background: linear-gradient(
    90deg,
    transparent,
    rgba(255, 255, 255, 0.2),
    transparent
  ) !important;
  transition: all 0.5s !important;
}

.action-btn:hover::before {
  left: 100% !important;
}

.action-btn:hover {
  transform: translateY(-2px) !important;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15) !important;
}

.action-btn.primary {
  background: linear-gradient(135deg, #409eff 0%, #3375b9 100%) !important;
  border: none !important;
}

@media (max-width: 768px) {
  .action-buttons {
    grid-template-columns: 1fr;
    width: 100%;
    margin-left: 0;
    margin-top: 20px;
  }
}

.btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 10px 16px;
  border-radius: 8px;
  font-weight: 500;
  font-size: 14px;
  transition: all 0.2s;
  white-space: nowrap;
  border: none;
  cursor: pointer;
  width: 100%;
}

.info-section {
  background: white;
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.section-content {
  padding: 0 8px;
}

.progress-section {
  background: #f9f9f9;
  border-radius: 8px;
  padding: 16px;
}

.submission-stats {
  display: flex;
  gap: 24px;
  align-items: baseline;
  margin-bottom: 16px;
  font-size: 14px;
  color: #262626;
  flex-wrap: wrap;
}

.submission-days {
  display: flex;
  gap: 24px;
  align-items: center;
}

.submission-count {
  font-weight: 500;
  color: #1d1d1d;
}

.stats-group {
  display: flex;
  gap: 24px;
  align-items: center;
}

.year-selector {
  margin-bottom: 15px;
}

.year-selector select {
  padding: 8px 12px;
  border: 1px solid #e8e8e8;
  border-radius: 4px;
  background-color: #fff;
  font-size: 14px;
  color: #595959;
  outline: none;
  cursor: pointer;
  transition: border-color 0.3s;
}

.year-selector select:hover {
  border-color: #40a9ff;
}

.calendar-chart {
  width: 100%;
  height: 160px;
  margin-top: 10px;
}

@media (max-width: 768px) {
  .user-card {
    flex-direction: column;
    align-items: center;
    text-align: center;
    padding: 16px;
    max-height: none;
  }

  .avatar-section {
    width: 100%;
  }

  .user-profile-grid {
    grid-template-columns: 1fr;
  }

  .action-buttons {
    width: 100%;
    max-width: 300px;
    margin: 0 auto;
  }

  .calendar-chart {
    height: 140px;
  }

  .follow-stats {
    max-width: 280px;
    margin: 0 auto 16px;
  }
}

@media (max-width: 480px) {
  .user-card {
    padding: 16px;
  }

  .avatar {
    width: 90px;
    height: 90px;
  }

  .username {
    font-size: 22px;
  }

  .follow-stats {
    padding: 8px 12px;
  }

  .stat-item {
    padding: 0 10px;
  }

  .stat-value {
    font-size: 16px;
  }

  .calendar-chart {
    height: 160px;
  }

  .stats-group {
    flex-direction: column;
    gap: 12px;
    align-items: flex-start;
  }

  .submission-days {
    flex-direction: column;
    gap: 8px;
    align-items: flex-start;
  }
}

.user-form .el-form-item {
  margin-bottom: 22px;
  --el-form-label-font-size: 14px;
  --el-form-label-font-weight: 500;
}

.user-form .el-input__wrapper {
  border-radius: 8px;
  transition: all 0.2s;
}

.user-form .el-input__wrapper:hover {
  box-shadow: 0 0 0 1px var(--el-color-primary) inset;
}

.user-form .el-input,
.user-form .el-select,
.user-form .el-date-editor {
  width: 100%;
}

.form-item-account .input-account {
  background-color: #f8f9fa;
}

.avatar-uploader {
  --el-uploader-picture-card-size: 100px;
  border: 2px dashed var(--el-border-color);
  border-radius: 12px;
  transition: border-color 0.3s;
}

.avatar-uploader:hover {
  border-color: var(--el-color-primary);
}

.avatar-uploader-icon {
  font-size: 24px;
  color: var(--el-text-color-secondary);
}

.date-picker.el-date-editor {
  max-width: 300px;
}

.dialog-footer .el-button {
  padding: 10px 24px;
  border-radius: 8px;
  transition: all 0.2s;
}

.dialog-footer .el-button--primary {
  background: linear-gradient(135deg, #409eff, #3375b9);
  border: none;
}

.dialog-footer .el-button--primary:hover {
  opacity: 0.9;
  transform: translateY(-1px);
}

.feature-item {
  margin-bottom: 4px;
  padding-left: 18px;
  position: relative;
}

.feature-item:before {
  content: "✓";
  color: #52c41a;
  position: absolute;
  left: 0;
  top: 2px;
}
</style>