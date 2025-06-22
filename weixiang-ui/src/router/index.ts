import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import HomeView from "../views/HomeView.vue";
import DataAnalysisView from "../views/DataAnalysisView.vue";
import OrderManagementView from "../views/OrderManagementView.vue";
import SystemManagementView from "../views/SystemManagementView.vue";
import OperationLog from "../views/system/OperationLog.vue";
import LoginLog from "../views/system/LoginLog.vue";
import UserManagement from "../views/system/UserManagement.vue";
import {
  House,
  DataAnalysis,
  ShoppingCart,
  Sell,
  Setting,
  Document,
  UserFilled,
  DocumentCopy,
} from "@element-plus/icons-vue";
import PlanManagement from "../views/system/PlanManagement.vue";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    component: HomeView,
    meta: {
      title: "首页",
      icon: House,
    },
    children: [
      {
        path: "",
        name: "首页",
        redirect: "data-analysis",
      },
      {
        path: "data-analysis",
        name: "数据分析",
        component: DataAnalysisView,
        meta: {
          title: "数据分析",
          icon: DataAnalysis,
        },
      },
      {
        path: "order-management",
        name: "订单管理",
        component: OrderManagementView,
        meta: {
          title: "订单管理",
          icon: ShoppingCart,
        },
      },
      {
        path: "system-management",
        name: "系统管理",
        component: SystemManagementView,
        meta: {
          title: "系统管理",
          icon: Setting,
        },
        children: [
          {
            path: "user-management",
            name: "用户管理",
            component: UserManagement,
            meta: {
              title: "用户管理",
              icon: UserFilled,
            },
          },
          {
            path: "plan-management",
            name: "套餐管理",
            component: PlanManagement,
            meta: {
              title: "套餐管理",
              icon: Sell,
            },
          },
          {
            path: "operation-log",
            name: "操作日志",
            component: OperationLog,
            meta: {
              title: "操作日志",
              icon: Document,
            },
          },
          {
            path: "login-log",
            name: "登录日志",
            component: LoginLog,
            meta: {
              title: "登录日志",
              icon: DocumentCopy,
            },
          },
          
        ],
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
