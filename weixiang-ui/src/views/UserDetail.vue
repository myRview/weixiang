<template>
  <div class="profile-container">
    <!-- 顶部用户信息卡片 -->
    <div class="user-card">
      <div class="avatar-section">
        <img :src="defaultAvatar" alt="用户头像" class="avatar" />
      </div>
      <div class="user-basic">
        <div class="username">{{ user?.userName }}</div>
      </div>
      <div class="follow-stats">
        <div class="stat-item">
          <div class="stat-value">0</div>
          <div class="stat-label">关注了</div>
        </div>
        <div class="divider"></div>
        <div class="stat-item">
          <div class="stat-value">0</div>
          <div class="stat-label">关注者</div>
        </div>
      </div>
      <div class="action-buttons">
        <button class="btn edit-btn">编辑个人资料</button>
      </div>
    </div>
    <div class="info-section">
      <div class="section-content">
        <div class="progress-section">
          <div class="submission-stats">
            <div class="submission-count">
              {{ selectedYear === currentYear ? "过去一年" : selectedYear + "年" }}共签到 {{ totalSignCount }} 次
            </div>
            <div class="stats-group">
              <div class="submission-days">
                <span>本月签到天数: {{ monthSignInDays }}</span>
                <span>连续签到: {{ continuousSignCount }}</span>
              </div>
              <div class="year-selector">
                <select v-model="selectedYear" @change="handleYearChange">
                  <option v-for="year in years" :key="year" :value="year">{{ year }}年</option>
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
} from "@/api/yonghuguanli";
import { onMounted, ref, computed } from "vue";
import { useLoginUserStore } from "@/store/loginUser";
import dayjs from "dayjs";
import * as echarts from "echarts";

// 默认头像
const defaultAvatar = "https://pic.leetcode-cn.com/1631796689-legoLO.png";

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

const loginUserStore = useLoginUserStore();

// 当前年份
const currentYear = ref(dayjs().year());
// 选中的年份
const selectedYear = ref(currentYear.value);
// 近三年的年份数组
const years = computed(() => {
  return [currentYear.value - 2, currentYear.value - 1, currentYear.value];
});

// 获取用户信息
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
        color: ["#f3f3f3", "#30a14e"], // 未签到/签到
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

onMounted(async () => {
  await getUserInfo();
  await getMonthSignInDays();
  await getContinuousCount();
  await getSignInRecords();
});
</script>

<style scoped>
.user-card {
  display: flex;
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  margin-bottom: 20px;
}

.avatar-section {
  margin-right: 24px;
}

.avatar {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid #f3f3f3;
}

.user-basic {
  flex: 1;
  padding-right: 24px;
  border-right: 1px solid #eee;
}

.username {
  font-size: 24px;
  font-weight: 600;
  color: #262626;
}

.follow-stats {
  display: flex;
  align-items: center;
  padding: 0 24px;
  border-right: 1px solid #eee;
}

.stat-item {
  text-align: center;
  padding: 0 16px;
}

.stat-value {
  font-size: 20px;
  font-weight: 600;
  color: #262626;
}

.stat-label {
  font-size: 14px;
  color: #8c8c8c;
  margin-top: 4px;
}

.divider {
  width: 1px;
  height: 40px;
  background: #eee;
}

.action-buttons {
  display: flex;
  flex-direction: column;
  margin-left: 24px;
}

.btn {
  padding: 8px 16px;
  border-radius: 4px;
  border: none;
  font-size: 14px;
  cursor: pointer;
  margin-bottom: 12px;
  transition: all 0.3s;
}

.edit-btn {
  background: #fff;
  border: 1px solid #e8e8e8;
  color: #595959;
}

.edit-btn:hover {
  background: #f5f5f5;
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
  height: 220px;
  margin-top: 10px;
}

@media (max-width: 768px) {
  .calendar-chart {
    height: 180px;
  }

  .user-card {
    flex-direction: column;
  }

  .avatar-section {
    margin-right: 0;
    margin-bottom: 16px;
    text-align: center;
  }

  .user-basic {
    padding-right: 0;
    padding-bottom: 16px;
    border-right: none;
    border-bottom: 1px solid #eee;
    margin-bottom: 16px;
    text-align: center;
  }

  .follow-stats {
    padding: 16px 0;
    border-right: none;
    border-bottom: 1px solid #eee;
    margin-bottom: 16px;
    justify-content: center;
  }

  .action-buttons {
    margin-left: 0;
    flex-direction: row;
    justify-content: center;
    gap: 16px;
  }

  .btn {
    margin-bottom: 0;
  }

  .submission-stats {
    flex-direction: column;
    gap: 8px;
  }

  .submission-days {
    display: flex;
    flex-wrap: wrap;
    gap: 16px;
  }
}

@media (max-width: 480px) {
  .calendar-chart {
    height: 160px;
  }

  .submission-stats {
    flex-direction: column;
    gap: 12px;
  }

  .stats-group {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  .submission-days {
    flex-direction: column;
    gap: 8px;
  }
}
</style>
