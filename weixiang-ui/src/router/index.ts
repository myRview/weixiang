import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import { routes } from "./routes";
import { useLoginUserStore } from '../store/loginUser'; 
import WebSocketService from "@/utils/websocket";

// 创建WebSocket服务实例（全局单例）
export const webSocketService = new WebSocketService();

// 创建路由实例
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
  const token = localStorage.getItem('token');
  
  // 2. 检查当前路由是否需要认证
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth);
  
  // 3. 白名单路由（无需登录）
  const authWhiteList = ["/login", "/register"];
  
  // 4. WebSocket连接逻辑
  if (isAuthenticated && token) {
    // 已登录状态且有token，确保WebSocket连接
    if (!webSocketService.isConnected()) {
      webSocketService.connect(token);
      
      // 可以在这里注册全局的WebSocket事件监听
      webSocketService.on('open', () => {
        console.log('全局监听: WebSocket连接已打开');
      });
      
      webSocketService.on('error', (error) => {
        console.error('全局监听: WebSocket错误', error);
      });
      
      webSocketService.on('close', (event) => {
        console.log('全局监听: WebSocket连接关闭', event);
      });
    }
  } else {
    // 未登录状态，断开WebSocket连接
    if (webSocketService.isConnected()) {
      webSocketService.disconnect();
    }
  }
  
  // 5. 路由访问控制逻辑
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