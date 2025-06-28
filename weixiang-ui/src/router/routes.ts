import { RouteRecordRaw } from "vue-router";
import Home from "../views/Home.vue";
import OrderManagement from "../views/OrderManagement.vue";
import DataAnalysis from "../views/DataAnalysis.vue";
import systemManagement from "../views/SystemManagementView.vue";
import LoginLog from "../views/system/LoginLog.vue";
import OperationLog from "../views/system/OperationLog.vue";
import UserManagement from "../views/system/UserManagement.vue";
import PlanManagement from "@/views/system/PlanManagement.vue";
import UserDetail from "../views/UserDetail.vue"; // 新增导入
import {
  House,
  Edit,
  ShoppingCart,
  User,
  Ticket,
  Document,
  TrendCharts,
  Setting,
} from "@element-plus/icons-vue";
export const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "Home",
    component: Home,
    meta: {
      title: "首页",
      icon: House,
    },
    children: [
      {
        path: "data-analysis",
        name: "DataAnalysis",
        component: DataAnalysis,
        meta: {
          title: "数据分析",
          icon: TrendCharts,
        },
      },
      {
        path: "order-management",
        name: "OrderManagement",
        component: OrderManagement,
        meta: {
          title: "订单管理",
          icon: ShoppingCart,
        },
      },
      // 新增个人中心路由
      {
        path: "profile",
        name: "Profile",
        component: UserDetail,
        meta: {
          title: "个人中心",
          icon: User,
        },
      },
      {
        path: "system-management",
        name: "systemManagement",
        component: systemManagement,
        meta: {
          title: "系统管理",
          icon: Setting,
        },
        children: [
          {
            path: "user-management",
            name: "UserManagement",
            component: UserManagement,
            meta: {
              title: "用户管理",
              icon: User
            },
          },
          {
            path: "plan-management",
            name: "PlanManagement",
            component: PlanManagement,
            meta: {
              title: "套餐管理",
              icon: Ticket
            },
          },
          {
            path: "login-log",
            name: "LoginLog",
            component: LoginLog,
            meta: {
              title: "登录日志",
              icon: Document
            },
          },
          {
            path: "operation-log",
            name: "OperationLog",
            component: OperationLog,
            meta: {
              title: "操作日志",
              icon: Edit
            },
          },
          
        ],
      },
    ],
  },
];
