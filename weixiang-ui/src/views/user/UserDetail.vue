<template>
  <div class="profile-container">
    <!-- 顶部用户信息卡片 -->
    <div class="user-card">
      <div class="avatar-section">
        <div class="avatar-wrapper">
          <img
             :src="user?.avatar ? `${baseURL}${user?.avatar}` : defaultAvatar"
            alt="用户头像"
            class="avatar"
          />
          <div class="avatar-badge" v-show="isVip">VIP</div>
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
      <div class="action-buttons">
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
  </div>
</template>

<script setup lang="ts">
import {
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
import { Edit, Check, CircleCheckFilled, Star } from "@element-plus/icons-vue";
import { getPayPlan } from "@/api/yonghutaocanbiaoguanli";
import { useRouter } from "vue-router";
import { baseURL } from "@/request";
const router = useRouter();
// 默认头像
const defaultAvatar =
  "http://gips2.baidu.com/it/u=195724436,3554684702&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=960";

// 签到相关状态
const isTodaySigned = ref(false);
const user = ref<API.UserVO>();
const calendarRef = ref<HTMLDivElement | null>(null);
const chartInstance = ref<echarts.ECharts | null>(null);
const monthSignInDays = ref(0);
const continuousSignCount = ref(0);
const totalSignCount = ref(0);
const signInRecords = ref<Record<string, boolean>>({});
const isVip = ref(false);
const loginUserStore = useLoginUserStore();

// 编辑资料跳转
const handleEditProfile = () => {
  router.push("/edit-profile");
};

// 签到处理
const handleSignIn = async () => {
  try {
    const res = await sign();
    if (res.data.code === 200) {
      ElMessage.success("签到成功");
      await getSignInRecords();
      await getContinuousCount();
      await getMonthSignInDays();
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

// 跳转到会员中心
const toMemberCenter = () => {
  router.push({ path: "/member-center" });
};

// 获取用户VIP信息
const getVipInfo = async () => {
  try {
    const res = await getPayPlan({ userId: loginUserStore.loginUser.id });
    if (res.data.code === 200) {
      isVip.value = res.data.data?.status === 1;
    }
  } catch (error) {
    console.error("获取用户套餐信息失败", error);
  }
};

// 年份相关
const currentYear = ref(dayjs().year());
const selectedYear = ref(currentYear.value);
const years = computed(() => [
  currentYear.value - 2,
  currentYear.value - 1,
  currentYear.value,
]);

// 获取用户信息
const getUserInfo = async () => {
  try {
    const res = await getUserById({ id: loginUserStore.loginUser.id });
    if (res.data.code !== 200) {
      ElMessage.error("获取用户信息失败");
      return;
    }
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
    totalSignCount.value = Object.keys(signInRecords.value).length;
    checkTodaySign();
    renderCalendarHeatmap();
  } catch (error) {
    console.error("获取签到记录失败", error);
  }
};

// 生成热力图数据
const generateHeatmapData = () => {
  const data: [string, number][] = [];
  let startDate, endDate;
  if (selectedYear.value === currentYear.value) {
    endDate = dayjs();
    startDate = endDate.subtract(1, "year");
  } else {
    startDate = dayjs(`${selectedYear.value}-01-01`);
    endDate = dayjs(`${selectedYear.value}-12-31`);
  }
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
  if (!chartInstance.value) {
    chartInstance.value = echarts.init(calendarRef.value);
    window.addEventListener("resize", () => {
      chartInstance.value?.resize();
    });
  }
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
      inRange: { color: ["#f3f3f3", "#4af07caa"] },
    },
    calendar: {
      range: [rangeStart, rangeEnd],
      top: 30,
      bottom: 10,
      left: 40,
      right: 20,
      cellSize: 14,
      itemStyle: { borderWidth: 1.5, borderColor: "transparent" },
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
      yearLabel: { show: false },
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
      itemStyle: { gap: 3 },
    },
  };
  chartInstance.value.setOption(option);
};

// 页面加载时初始化数据
onMounted(async () => {
  await getUserInfo();
  await getMonthSignInDays();
  await getContinuousCount();
  await getSignInRecords();
  await getVipInfo();
});
</script>

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
  align-items: center;
}

.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 10px;
}

.avatar-wrapper {
  position: relative;
  margin-bottom: 10px;
}

.avatar {
  width: 112px;
  height: 112px;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid white;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.avatar-badge {
  position: absolute;
  bottom: 0;
  right: 0;
  background-color: #ff7d00;
  color: white;
  border-radius: 50%;
  width: 28px;
  height: 28px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: bold;
  border: 2px solid white;
}

.user-meta {
  flex: 1;
  padding: 10px;
  display: flex;
  flex-direction: column;
}

.username {
  margin: 0 0 15px 0;
  font-size: 22px;
  font-weight: 600;
  color: #2c3e50;
}

.user-profile-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
  margin-bottom: 15px;
}

.info-item {
  display: flex;
  flex-direction: column;
}

.info-item.full-width {
  grid-column: span 2;
}

.info-label {
  font-size: 13px;
  color: #888;
  margin-bottom: 4px;
}

.info-value {
  font-size: 15px;
  color: #333;
  word-break: break-word;
}

.bio-text {
  line-height: 1.5;
  max-height: 40px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.action-buttons {
  flex: 0 0 180px;
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding: 8px 0;
  margin-left: auto;
  justify-content: center;
  margin-right: 20px;
}

.action-btn {
  width: 100%;
  min-height: 36px;
  border-radius: 8px;
  font-size: 14px;
  padding: 8px 0;
  transition: all 0.2s ease;
}

.action-btn:not(:disabled):hover {
  opacity: 0.9;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.12);
  transform: translateY(-1px);
}

.action-btn:disabled {
  background-color: #f0f9eb !important;
  color: #52c41a !important;
  border-color: #b7eb8f !important;
  cursor: default;
}

.action-btn.el-button--primary {
  font-weight: 500;
}

.action-btn.el-button--primary:hover {
  box-shadow: 0 2px 8px rgba(64, 169, 250, 0.3);
}

.info-section {
  background-color: white;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}

.section-content {
  margin-top: 15px;
}

.progress-section {
  margin-top: 20px;
}

.submission-stats {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.submission-count {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.stats-group {
  display: flex;
  align-items: center;
  gap: 20px;
}

.submission-days {
  display: flex;
  gap: 15px;
  color: #666;
  font-size: 14px;
}

.year-selector select {
  padding: 5px 10px;
  border-radius: 4px;
  border: 1px solid #ddd;
  font-size: 14px;
  color: #333;
  background-color: white;
}

.calendar-chart {
  width: 100%;
  height: 220px;
  margin-top: 10px;
}
</style>
