import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import { routes } from "./routes";
import { useLoginUserStore } from '../store/loginUser'; 
const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

// 全局前置守卫
router.beforeEach((to, from, next) => {
  // 使用 Pinia store
  const loginUserStore = useLoginUserStore();
  
  // 1. 获取登录状态 - 检查用户id是否存在
  const isAuthenticated = loginUserStore.loginUser?.id !== undefined;
  
  // 2. 检查当前路由是否需要认证
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth);
  
  // 3. 白名单路由（无需登录）
  const authWhiteList = ["/login", "/register"];
  
  // 4. 判断逻辑
  if (requiresAuth && !isAuthenticated) {
    // 需要认证但未登录 → 重定向到登录页
    next({
      path: "/login",
    });
  } else if (isAuthenticated && authWhiteList.includes(to.path)) {
    // 已登录但访问登录/注册页 → 重定向到首页
    next("/");
  } else {
    // 放行其他情况
    next();
  }
});

export default router;