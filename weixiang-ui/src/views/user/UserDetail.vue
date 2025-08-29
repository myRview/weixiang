<template>
  <div class="profile-container">
    <div class="user-card">
      <div class="avatar-section">
        <div class="avatar-wrapper">
          <img
            :src="user?.avatar ? `${baseURL}${user?.avatar}` : defaultAvatar"
            alt="用户头像"
            class="avatar"
            @mouseover="showAvatarHover = true"
            @mouseleave="showAvatarHover = false"
          />
          <div class="avatar-badge" v-show="isVip">
            <el-icon class="vip-icon"><Star /></el-icon>
            <span>VIP</span>
          </div>
        </div>
      </div>

      <div class="user-meta">
        <div class="user-header">
          <h1 class="username">{{ user?.userName }}</h1>
        </div>

        <div class="user-profile-grid">
          <div class="info-item gender-item">
            <div class="gender-icon-container">
              <svg
                t="1756448510246"
                class="icon male-icon"
                viewBox="0 0 1024 1024"
                version="1.1"
                xmlns="http://www.w3.org/2000/svg"
                p-id="9967"
                width="24"
                height="24"
                v-if="user?.gender === 0"
              >
                <path
                  d="M512 44.5c258.2 0 467.5 209.3 467.5 467.5S770.2 979.5 512 979.5 44.5 770.2 44.5 512 253.8 44.5 512 44.5z"
                  fill="#FF696B"
                  p-id="9968"
                ></path>
                <path
                  d="M466.7 600.3l-75.2 75.2 100.4 100.4-43.1 43.1-100.5-100.4-101 101-42.9-42.9 101.1-101.1L205 575.2l43.1-43.1 100.4 100.4 75.1-75.1c-65-86-58-209.3 20.9-288.1 86.2-86.2 225.5-86.6 311.1-1 85.7 85.7 85.2 224.9-0.9 311.1-78.8 78.8-202.1 85.9-288 20.9z m245.8-288.8c-61.9-61.9-162.6-61.6-224.9 0.7s-62.6 163-0.7 224.9c62 62 162.6 61.6 224.9-0.7 62.3-62.2 62.6-162.9 0.7-224.9z"
                  fill="#FFFFFF"
                  p-id="9969"
                ></path>
              </svg>
              <svg
                t="1756448557749"
                class="icon female-icon"
                viewBox="0 0 1024 1024"
                version="1.1"
                xmlns="http://www.w3.org/2000/svg"
                p-id="10111"
                width="24"
                height="24"
                v-if="user?.gender === 1"
              >
                <path
                  d="M962.816366 269.238857a512.365714 512.365714 0 0 0-50.541715-75.958857C818.505509 75.629714 674.231223 0 512.000366 0 349.80608 0 205.531794 75.629714 111.72608 193.28c-18.944 23.734857-35.986286 49.005714-50.505143 75.885714A509.549714 509.549714 0 0 0 0.000366 512c0 87.844571 22.198857 170.569143 61.220571 242.870857 14.518857 26.88 31.597714 52.150857 50.505143 75.885714C205.531794 948.443429 349.842651 1024 512.000366 1024c162.230857 0 306.468571-75.556571 400.274285-193.243429 18.944-23.734857 35.986286-49.042286 50.541715-75.958857A509.074286 509.074286 0 0 0 1024.000366 512c0-87.844571-22.125714-170.532571-61.184-242.761143z m-186.112 144.713143a35.84 35.84 0 1 1-71.606857 0v-47.286857l-111.506286 111.36c21.284571 31.268571 32.841143 68.132571 32.841143 106.678857a189.366857 189.366857 0 0 1-55.698286 134.692571 189.330286 189.330286 0 0 1-134.619429 55.661715 189.403429 189.403429 0 0 1-134.619428-55.661715 190.610286 190.610286 0 0 1 0-269.202285 188.964571 188.964571 0 0 1 134.619428-55.771429c38.692571 0 75.483429 11.556571 106.715429 32.841143l106.020571-106.093714-36.571428-0.036572h-0.146286a35.181714 35.181714 0 0 1-12.397714-2.486857c-0.475429-0.109714-0.987429-0.109714-1.462857-0.292571-0.768-0.329143-1.389714-1.024-2.157715-1.426286a36.498286 36.498286 0 0 1-7.131428-4.827429c-0.658286-0.512-1.499429-0.804571-2.084572-1.426285-0.365714-0.329143-0.512-0.841143-0.804571-1.243429a35.364571 35.364571 0 0 1-6.656-9.837714l-0.182857-0.256-0.036572-0.036572v-0.036571a35.328 35.328 0 0 1-2.816-13.897143c0-2.852571 0.877714-5.412571 1.609143-8.045714-0.694857 2.633143-1.609143 5.156571-1.609143 8.009143 0-4.169143 1.060571-8.045714 2.340572-11.776l0.146286-0.694857a6.838857 6.838857 0 0 1 0.329142-1.499429c0.292571-0.731429 0.804571-1.353143 1.316572-1.974857a5.888 5.888 0 0 0 0.877714-1.316572c2.816-5.046857 6.875429-9.252571 11.556572-12.470857l2.304-1.462857c0.804571-0.475429 1.536-1.097143 2.377142-1.499428 0.219429-0.073143 0.365714-0.256 0.548572-0.365715 0.658286-0.256 1.462857-0.256 2.157714-0.438857a32.914286 32.914286 0 0 1 11.739429-2.340571h0.109714l123.355429-0.036572a35.364571 35.364571 0 0 1 0 0.036572h-0.073143a35.730286 35.730286 0 0 1 15.835428 4.096 34.377143 34.377143 0 0 1 7.753143 5.229714c0.877714 0.768 1.792 1.426286 2.523429 2.304 0.804571 0.877714 1.609143 1.718857 2.304 2.706286 7.643429 6.582857 12.726857 15.981714 12.726857 26.843428v123.282286z"
                  fill="#66B4EA"
                  p-id="10112"
                ></path>
                <path
                  d="M436.151223 466.176c-31.670857 0-61.476571 12.397714-83.858286 34.742857a118.747429 118.747429 0 0 0 0 167.753143 118.125714 118.125714 0 0 0 83.858286 34.669714 118.125714 118.125714 0 0 0 83.858286-34.669714 118.125714 118.125714 0 0 0 34.742857-83.968c0-31.597714-12.361143-61.44-34.742857-83.821714a117.906286 117.906286 0 0 0-83.858286-34.706286z"
                  fill="#66B4EA"
                  p-id="10113"
                ></path>
              </svg>
            </div>
          </div>

          <div class="info-item">
            <div class="location-container">
              <svg
                t="1756448125008"
                class="icon location-icon"
                viewBox="0 0 1024 1024"
                version="1.1"
                xmlns="http://www.w3.org/2000/svg"
                p-id="4472"
                width="20"
                height="20"
              >
                <path
                  d="M712.0896 726.09792a30.72 30.72 0 0 1 39.79264-17.42848c73.48224 28.672 118.51776 70.28736 118.51776 121.91744 0 97.75104-166.2976 162.69312-368.64 162.69312s-368.64-64.94208-368.64-162.69312c0-51.63008 45.056-93.22496 118.51776-121.91744a30.72 30.72 0 0 1 22.34368 57.22112c-52.4288 20.48-79.42144 45.40416-79.42144 64.7168 0 48.0256 136.27392 101.23264 307.2 101.23264s307.2-53.20704 307.2-101.25312c0-19.29216-26.97216-44.21632-79.42144-64.69632a30.72 30.72 0 0 1-17.44896-39.79264z"
                  fill="#888"
                  p-id="4473"
                ></path>
                <path
                  d="M501.76 307.2a143.36 143.36 0 1 0 0 286.72 143.36 143.36 0 0 0 0-286.72z m0 61.44a81.92 81.92 0 1 1 0 163.84 81.92 81.92 0 0 1 0-163.84z"
                  fill="#888"
                  p-id="4475"
                ></path>
              </svg>
              <span class="info-value">{{
                user?.expandVo?.address || "暂无信息"
              }}</span>
            </div>
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
          class="action-btn vip-btn"
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

    <!-- 其他内容保持不变 -->
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
import {
  Edit,
  Check,
  CircleCheckFilled,
  Star,
  Camera,
} from "@element-plus/icons-vue";
import { getPayPlan } from "@/api/yonghutaocanbiaoguanli";
import { useRouter } from "vue-router";
import { baseURL } from "@/request";
const router = useRouter();

// 默认头像
const defaultAvatar =
  "http://gips2.baidu.com/it/u=195724436,3554684702&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=960";

// 状态
const showAvatarHover = ref(false);

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
  gap: 24px;
  background: #ffffff;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
  margin-bottom: 24px;
  transition: all 0.3s ease;
  align-items: center;
  position: relative;
  overflow: hidden;
}

.user-card::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  border-radius: 16px 16px 0 0;
}

.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 10px;
  position: relative;
}

.avatar-wrapper {
  position: relative;
  margin-bottom: 10px;
}

.avatar {
  width: 128px;
  height: 128px;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid #f5f5f5;
  box-shadow: 0 3px 10px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

/* 头像悬停效果 */
.avatar:hover {
  transform: scale(1.02);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.12);
}

/* VIP标识优化 */
.avatar-badge {
  position: absolute;
  bottom: 0;
  right: 0;
  background: #ff9800;
  color: white;
  border-radius: 50%;
  width: 34px;
  height: 34px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: bold;
  border: 2px solid white;
  box-shadow: 0 2px 5px rgba(255, 152, 0, 0.2);
  transition: all 0.2s ease;
}

.avatar-badge:hover {
  transform: scale(1.05);
}

.vip-icon {
  font-size: 14px;
  margin-right: 2px;
}

/* 更换头像提示优化 */
.avatar-hover {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
  cursor: pointer;
}

.avatar-hover:hover {
  opacity: 1;
}

.hover-icon {
  font-size: 20px;
  margin-bottom: 5px;
}

.hover-text {
  font-size: 12px;
}

.user-meta {
  flex: 1;
  padding: 10px;
  display: flex;
  flex-direction: column;
}

/* 用户名样式优化 */
.user-header {
  margin-bottom: 20px;
}

.username {
  margin: 0;
  font-size: 22px;
  font-weight: 600;
  color: #333;
  position: relative;
  display: inline-block;
}

.username::after {
  content: "";
  position: absolute;
  bottom: -5px;
  left: 0;
  width: 0;
  height: 2px;
  background-color: #66b4ea;
  transition: width 0.3s ease;
}

.user-card:hover .username::after {
  width: 100%;
}

/* 信息网格布局优化 */
.user-profile-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 18px;
  margin-bottom: 10px;
}

.info-item {
  display: flex;
  flex-direction: column;
}

.info-item.full-width {
  grid-column: span 2;
}

/* 性别和地址图标容器优化 */
.gender-icon-container,
.location-container {
  display: flex;
  align-items: center;
  margin-top: 4px;
}

.location-icon {
  margin-right: 6px;
  flex-shrink: 0;
  color: #888;
  transition: color 0.2s ease;
}

.info-item:hover .location-icon {
  color: #66b4ea;
}

/* 标签样式优化 */
.info-label {
  font-size: 13px;
  color: #888;
  margin-bottom: 4px;
  display: flex;
  align-items: center;
}

.info-label::after {
  content: ":";
  margin-left: 4px;
}

/* 信息值样式优化 */
.info-value {
  font-size: 15px;
  color: #333;
  word-break: break-word;
  transition: color 0.2s ease;
}

.info-item:hover .info-value {
  color: #66b4ea;
}

/* 简介文本优化 */
.bio-text {
  line-height: 1.6;
  max-height: 44px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  padding: 4px 0;
}

/* 按钮区域优化 */
.action-buttons {
  flex: 0 0 180px;
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding: 8px 0;
  margin-left: auto;
  justify-content: center;
  margin-right: 10px;
}

/* 按钮样式优化 */
.action-btn {
  width: 100%;
  min-height: 38px;
  border-radius: 8px;
  font-size: 14px;
  padding: 8px 0;
  transition: all 0.25s ease;
  position: relative;
  overflow: hidden;
}

/* 按钮悬停效果 */
.action-btn:not(:disabled):hover {
  transform: translateY(-2px);
  box-shadow: 0 3px 8px rgba(0, 0, 0, 0.1);
}

/* 按钮点击反馈 */
.action-btn:not(:disabled):active {
  transform: translateY(0);
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

/* 已签到按钮样式 */
.action-btn:disabled {
  background-color: #f0f9eb !important;
  color: #52c41a !important;
  border-color: #b7eb8f !important;
  cursor: default;
}

/* VIP按钮样式 */
.vip-btn {
  background-color: #ff9800;
  border-color: #ff9800;
  color: white;
}

.vip-btn:hover {
  background-color: #fb8c00;
  border-color: #fb8c00;
  box-shadow: 0 3px 8px rgba(255, 152, 0, 0.2);
}

/* 其他内容区域保持原有样式 */
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
  transition: all 0.2s ease;
}

.year-selector select:hover {
  border-color: #66b4ea;
}

.year-selector select:focus {
  outline: none;
  border-color: #66b4ea;
  box-shadow: 0 0 0 2px rgba(102, 180, 234, 0.2);
}

.calendar-chart {
  width: 100%;
  height: 220px;
  margin-top: 10px;
}

/* 响应式优化 */
@media (max-width: 768px) {
  .user-card {
    flex-direction: column;
    padding: 16px;
  }

  .user-header {
    justify-content: center;
    margin-top: 10px;
    text-align: center;
  }

  .user-profile-grid {
    grid-template-columns: 1fr;
  }

  .info-item.full-width {
    grid-column: span 1;
  }

  .action-buttons {
    width: 100%;
    margin: 10px 0 0 0;
    flex: none;
  }
}
</style>
