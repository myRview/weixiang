<template>
  <div class="data-analysis">
    <!-- 上半部分：两个饼图水平排列 -->
    <div class="chart-row">
      <!-- 订单状态饼图 -->
      <div class="chart-container">
        <div v-if="orderLoading" class="chart-loading">
          <div class="loading-spinner"></div>
          <div>加载订单数据中...</div>
        </div>
        <div v-else-if="!orderChartOption.series" class="chart-empty">
          <el-empty description="暂无订单数据" />
        </div>
        <v-chart v-else class="chart" :option="orderChartOption" autoresize />
      </div>

      <!-- 套餐分布饼图 -->
      <div class="chart-container">
        <div v-if="planLoading" class="chart-loading">
          <div class="loading-spinner"></div>
          <div>加载套餐数据中...</div>
        </div>
        <div v-else-if="!planChartOption.series" class="chart-empty">
          <el-empty description="暂无套餐数据" />
        </div>
        <v-chart v-else class="chart" :option="planChartOption" autoresize />
      </div>
    </div>

    <!-- 下半部分：留空给其他统计图 -->
    <div class="chart-container">
      <!-- 其他统计图将放在这里 -->
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, computed } from "vue";
import { count, planCount } from "@/api/dingdantongji";
import { use } from "echarts/core";
import { PieChart } from "echarts/charts";
import { CanvasRenderer } from "echarts/renderers";
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
} from "echarts/components";
import VChart from "vue-echarts";
import { ElEmpty } from "element-plus";

// 初始化ECharts组件
use([
  CanvasRenderer,
  PieChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
]);

// 加载状态
const orderLoading = ref(true);
const planLoading = ref(true);

// 订单状态映射
const statusMap = {
  0: "未支付",
  1: "已支付",
  2: "已取消",
  3: "退款",
};

// 优化后的颜色方案
const ORDER_COLORS = ["#FF6B6B", "#4ECDC4", "#FFD166", "#9D65C9"];
const PLAN_COLORS = ["#6A0572", "#AB83A1", "#5C7AFF", "#3AAFA9", "#F9C74F", "#90BE6D", "#F8961E", "#577590"];

// 订单统计数据
const OrderStatisticsData = ref<any[]>([]);
// 获取订单统计数据
const getOrderStatisticsData = async () => {
  try {
    orderLoading.value = true;
    const params = { date: "" };
    const response = await count(params);
    if (response.data.code === 200) {
      OrderStatisticsData.value = response.data.data || [];
    }
  } catch (error) {
    console.error("获取订单统计数据出错:", error);
    OrderStatisticsData.value = [];
  } finally {
    orderLoading.value = false;
  }
};

// 订单状态图表配置项
const orderChartOption = computed(() => {
  // 按状态分组统计数量和金额
  const statusData: Record<number, { count: number; amount: number }> = {
    0: { count: 0, amount: 0 },
    1: { count: 0, amount: 0 },
    2: { count: 0, amount: 0 },
    3: { count: 0, amount: 0 }
  };

  OrderStatisticsData.value.forEach((item) => {
    if (item.status !== undefined && statusData[item.status] !== undefined) {
      statusData[item.status].count += item.total || 0;
      statusData[item.status].amount += item.totalAmount || 0;
    }
  });

  // 转换为饼图数据格式
  const pieData = Object.entries(statusData).map(([status, data], index) => ({
    value: data.count,
    name: statusMap[Number(status) as 0 | 1 | 2 | 3],
    amount: data.amount,
    itemStyle: { color: ORDER_COLORS[index] }
  })).filter(item => item.value > 0); // 过滤掉数量为0的状态

  // 计算总数
  const totalCount = pieData.reduce((sum, item) => sum + item.value, 0);
  const totalAmount = pieData.reduce((sum, item) => sum + item.amount, 0);

  return {
    title: {
      text: "订单状态分布",
      left: "center",
      textStyle: {
        fontWeight: 'bold',
        fontSize: 16
      }
    },
    tooltip: {
      trigger: "item",
      formatter: (params: any) => {
        const { name, value, percent, data } = params;
        const amount = data.amount.toFixed(2);
        return `
          <div style="font-weight:bold;margin-bottom:5px">${name}</div>
          <div>订单数量: <b>${value}</b></div>
          <div>订单金额: <b>¥${amount}</b></div>
          <div>占比: <b>${percent}%</b></div>
        `;
      },
      backgroundColor: 'rgba(255,255,255,0.9)',
      borderColor: '#ddd',
      borderWidth: 1,
      padding: [10, 15],
      textStyle: {
        color: '#333'
      }
    },
    legend: {
      orient: "vertical",
      left: "left",
      data: pieData.map((item) => item.name),
      textStyle: {
        fontSize: 14
      }
    },
    color: ORDER_COLORS,
    graphic: [{
      type: 'text',
      left: 'center',
      top: '45%',
      style: {
        text: `订单总数\n${totalCount}`,
        textAlign: 'center',
        fill: '#333',
        fontSize: 14,
        fontWeight: 'bold',
        lineHeight: 24
      }
    }, {
      type: 'text',
      left: 'center',
      top: '55%',
      style: {
        text: `总金额\n¥${totalAmount.toFixed(2)}`,
        textAlign: 'center',
        fill: '#666',
        fontSize: 13
      }
    }],
    series: [
      {
        name: "订单状态",
        type: "pie",
        radius: ["40%", "65%"],
        center: ["50%", "50%"],
        data: pieData,
        avoidLabelOverlap: false,
        itemStyle: {
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: true,
          formatter: '{b}: {c}',
          fontSize: 12
        },
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: "rgba(0, 0, 0, 0.3)",
          },
          label: {
            show: true,
            fontWeight: 'bold'
          }
        },
      },
    ],
  };
});

// 套餐统计数据
const planStatisticsData = ref<any[]>([]);
// 获取套餐统计数据
const getPlanStatisticsData = async () => {
  try {
    planLoading.value = true;
    const params = { date: "" };
    const response = await planCount(params);
    if (response.data.code === 200) {
      planStatisticsData.value = response.data.data || [];
    }
  } catch (error) {
    console.error("获取套餐统计数据出错:", error);
    planStatisticsData.value = [];
  } finally {
    planLoading.value = false;
  }
};

// 套餐分布图表配置项
const planChartOption = computed(() => {
  // 处理套餐数据
  const pieData = planStatisticsData.value.map((item, index) => ({
    value: item.total || 0,
    name: item.planName || "未知套餐",
    itemStyle: { color: PLAN_COLORS[index % PLAN_COLORS.length] }
  }));

  // 计算总数
  const totalCount = pieData.reduce((sum, item) => sum + item.value, 0);

  return {
    title: {
      text: "套餐购买分布",
      left: "center",
      textStyle: {
        fontWeight: 'bold',
        fontSize: 16
      }
    },
    tooltip: {
      trigger: "item",
      formatter: (params: any) => {
        const { name, value, percent } = params;
        return `
          <div style="font-weight:bold;margin-bottom:5px">${name}</div>
          <div>购买数量: <b>${value}</b></div>
          <div>占比: <b>${percent}%</b></div>
        `;
      },
      backgroundColor: 'rgba(255,255,255,0.9)',
      borderColor: '#ddd',
      borderWidth: 1,
      padding: [10, 15],
      textStyle: {
        color: '#333'
      }
    },
    legend: {
      orient: "vertical",
      right: "10%",
      data: pieData.map((item) => item.name),
      textStyle: {
        fontSize: 12
      }
    },
    color: PLAN_COLORS,
    graphic: [{
      type: 'text',
      left: 'center',
      top: '50%',
      style: {
        text: `套餐总数\n${totalCount}`,
        textAlign: 'center',
        fill: '#333',
        fontSize: 14,
        fontWeight: 'bold'
      }
    }],
    series: [
      {
        name: "套餐分布",
        type: "pie",
        radius: ["40%", "65%"],
        center: ["50%", "50%"],
        data: pieData,
        avoidLabelOverlap: false,
        itemStyle: {
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: true,
          formatter: '{b}: {c}',
          fontSize: 12
        },
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: "rgba(0, 0, 0, 0.3)",
          },
          label: {
            show: true,
            fontWeight: 'bold'
          }
        },
      },
    ],
  };
});

// 初始化加载数据
onMounted(() => {
  getOrderStatisticsData();
  getPlanStatisticsData();
});
</script>

<style scoped>
.chart-row {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  margin-bottom: 20px;
}

.chart-container {
  flex: 1;
  min-height: 400px;
  min-width: 45%;
  position: relative;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}


</style>