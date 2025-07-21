import { RouteRecordRaw } from "vue-router";
import Home from "../views/Home.vue";
import OrderManagement from "../views/system/OrderManagement.vue";
import DataAnalysis from "../views/system/DataAnalysis.vue";
import systemManagement from "../views/system/SystemManagementView.vue";
import LoginLog from "../views/system/LoginLog.vue";
import OperationLog from "../views/system/OperationLog.vue";
import UserManagement from "../views/system/UserManagement.vue";
import PlanManagement from "@/views/system/PlanManagement.vue";
import RoleManagement from "@/views/system/RoleManagement.vue";
import PermissionManagement from "@/views/system/PermissionManagement.vue";
import UserDetail from "../views/UserDetail.vue";
import Login from "../views/Login.vue";
import Register from "../views/Register.vue";
import {
  House,
  Edit,
  ShoppingCart,
  User,
  Ticket,
  Document,
  TrendCharts,
  Setting,
  UserFilled,
  Menu,
} from "@element-plus/icons-vue";

export const routes: Array<RouteRecordRaw> = [
  // 登录注册（独立页面）
  {
    path: "/login",
    name: "Login",
    component: Login,
    meta: {
      title: "登录",
      hidden: true,
      requiresAuth: false, // 添加认证标识
    },
  },
  {
    path: "/register",
    name: "Register",
    component: Register,
    meta: {
      title: "注册",
      hidden: true,
      requiresAuth: false, // 添加认证标识
    },
  },
  // 布局路由（需要登录）
  {
    path: "/",
    name: "Layout",
    component: Home,
    meta: {
      requiresAuth: true, // 需要登录
    },
    children: [
      {
        path: "",
        redirect: "/profile"
      },
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
        path: "data-analysis",
        name: "DataAnalysis",
        component: DataAnalysis,
        meta: {
          title: "数据分析",
          icon: TrendCharts,
          requiresAdmin: true,
        },
      },
      {
        path: "order-management",
        name: "OrderManagement",
        component: OrderManagement,
        meta: {
          title: "订单管理",
          icon: ShoppingCart,
          requiresAdmin: true,
        },
      },
      {
        path: "plan-management",
        name: "PlanManagement",
        component: PlanManagement,
        meta: {
          title: "套餐管理",
          icon: Ticket,
          requiresAdmin: true,
        },
      },
      {
        path: "system-management",
        name: "systemManagement",
        component: systemManagement,
        meta: {
          title: "系统管理",
          icon: Setting,
          requiresAdmin: true,
        },
        children: [
          {
            path: "user-management",
            name: "UserManagement",
            component: UserManagement,
            meta: {
              title: "用户管理",
              icon: User,
            },
          },
          {
            path: "role-management",
            name: "RoleManagement",
            component: RoleManagement,
            meta: {
              title: "角色管理",
              icon: UserFilled,
            },
          },
          {
            path: "permission-management",
            name: "PermissionManagement",
            component: PermissionManagement,
            meta: {
              title: "权限管理",
              icon: Menu,
            },
          },
          {
            path: "login-log",
            name: "LoginLog",
            component: LoginLog,
            meta: {
              title: "登录日志",
              icon: Document,
            },
          },
          {
            path: "operation-log",
            name: "OperationLog",
            component: OperationLog,
            meta: {
              title: "操作日志",
              icon: Edit,
            },
          },
        ],
      },
    ],
  },
  // 未匹配路由重定向到登录页
  {
    path: "/:pathMatch(.*)*",
    redirect: "/login",
  },
];