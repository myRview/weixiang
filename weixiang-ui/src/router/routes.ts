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
import ArticleApproval from "@/views/system/ArticleApproval.vue";
import UserDetail from "../views/UserDetail.vue";
import Login from "../views/Login.vue";
import Register from "../views/Register.vue";
import MyArticles from "@/views/user/MyArticles.vue";
import ArticleDetail from "@/views/user/ArticleDetail.vue";
import EditArticle from "@/views/user/EditArticle.vue";
import {
  House,
  Edit,
  ShoppingCart,
  User,
  Document,
  TrendCharts,
  Setting,
  UserFilled,
  Menu,
  List,
  Reading,
  Goods,
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
      requiresAuth: false,
    },
  },
  {
    path: "/register",
    name: "Register",
    component: Register,
    meta: {
      title: "注册",
      hidden: true,
      requiresAuth: false,
    },
  },
  // 布局路由（需要登录）
  {
    path: "/",
    name: "Layout",
    component: Home,
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
          requiresAuth: true,
        },
      },
      {
        path: "my-articles",
        name: "MyArticles",
        component: MyArticles,
        meta: {
          title: "我的文章",
          icon: List,
          requiresAuth: true,
        },
      },
      {
        path: "article-detail/:id?",
        name: "ArticleDetail",
        component: ArticleDetail,
        meta: {
          title: "文章详情",
          requiresAuth: true,
          hidden: true
        },
      },
      {
        path: "edit-article/:id?",
        name: "EditArticle",
        component: EditArticle,
        meta: {
          title: "编辑文章",
          requiresAuth: true,
          hidden: true
        },
      },
      {
        path: "data-analysis",
        name: "DataAnalysis",
        component: DataAnalysis,
        meta: {
          title: "数据分析",
          icon: TrendCharts,
          requiresAuth: true,
          requiresAdmin: true, // 需要管理员权限
        },
      },
      {
        path: "order-management",
        name: "OrderManagement",
        component: OrderManagement,
        meta: {
          title: "订单管理",
          icon: ShoppingCart,
          requiresAuth: true,
          requiresAdmin: true, // 需要管理员权限
        },
      },
      {
        path: "plan-management",
        name: "PlanManagement",
        component: PlanManagement,
        meta: {
          title: "套餐管理",
          icon: Goods,
          requiresAuth: true,
          requiresAdmin: true, // 需要管理员权限
        },
      },
      {
        path: "article-approval",
        name: "ArticleApproval",
        component: ArticleApproval,
        meta: {
          title: "文章审批",
          icon: Reading,
          requiresAuth: true,
          requiresAdmin: true, // 需要管理员权限
        },
      },
      {
        path: "system-management",
        name: "systemManagement",
        component: systemManagement,
        meta: {
          title: "系统管理",
          icon: Setting,
          requiresAuth: true,
          requiresAdmin: true, // 需要管理员权限
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