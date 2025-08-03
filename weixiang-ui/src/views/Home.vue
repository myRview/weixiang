<template>
  <div class="home" v-if="showLayout">
    <el-container>
      <AppHeader />
      <el-container>
        <el-aside :width="isCollapsed ? '80px' : '196px'">
          <div class="collapse-trigger" @click="toggleCollapse">
            <el-icon>
              <component :is="isCollapsed ? ArrowRight : ArrowLeft" />
            </el-icon>
          </div>
          <el-menu
            :default-active="$route.path"
            class="el-menu-vertical-demo"
            router
            :collapse="isCollapsed"
          >
            <template v-for="route in menuRoutes" :key="route.path">
              <el-menu-item
                v-if="!route.children || route.children.length === 0"
                :index="`/${route.path}`"
              >
                <el-icon>
                  <component :is="route.meta.icon" :key="route.path" />
                </el-icon>
                <template #title>
                  <span>{{ route.meta.title }}</span>
                </template>
              </el-menu-item>
              <el-sub-menu v-else :index="`/${route.path}`">
                <template #title>
                  <el-icon>
                    <component :is="route.meta.icon" />
                  </el-icon>
                  <span>{{ route.meta.title }}</span>
                </template>
                <el-menu-item
                  v-for="child in route.children"
                  :key="child.path"
                  :index="`/${route.path}/${child.path}`"
                >
                  <el-icon>
                    <component :is="child.meta.icon" :key="child.path" />
                  </el-icon>
                  <span>{{ child.meta.title }}</span>
                </el-menu-item>
              </el-sub-menu>
            </template>
          </el-menu>
        </el-aside>
        <el-container>
          <el-main>
            <router-view /> <!-- 首页将在这里展示 -->
          </el-main>
        </el-container>
      </el-container>
    </el-container>
  </div>
  <router-view v-else />
</template>

<script setup lang="ts">
import { useRouter, useRoute } from "vue-router";
import { ref, computed } from "vue";
import { ArrowLeft, ArrowRight } from "@element-plus/icons-vue";
import AppHeader from "@/components/AppHeader.vue";
import { useLoginUserStore } from "@/store/loginUser";

const router = useRouter();
const route = useRoute();
const isCollapsed = ref(false);
const store = useLoginUserStore();

// 计算属性：是否显示布局
const showLayout = computed(() => {
  return route.matched.some((record) => record.meta.requiresAuth);
});

// 计算角色标签
const roleLabel = computed(() => {
  if (!store.loginUser.roleCode) return "未设置角色";
  const roles: Record<string, string> = {
    admin: "管理员",
    user: "普通用户",
  };
  return roles[store.loginUser.roleCode] || store.loginUser.roleCode;
});

// 检查用户权限
const hasPermission = (requiresAdmin: boolean | undefined) => {
 // 如果路由明确需要管理员权限，则仅管理员可访问
 if (requiresAdmin) {
    return store.loginUser.roleCode === "admin";
  }
  // 其他情况（包括requiresAdmin为false或undefined）均允许访问
  return true;
};

// 获取菜单路由（根据权限过滤）
const menuRoutes = computed(() => {
  const layoutRoute = router.options.routes.find((r) => r.name === "Layout");
  const routes = layoutRoute?.children || [];

  return routes.filter((route) => {
    // 添加空对象保护
    if (!route.meta) return false;

    // 过滤隐藏的路由
    if (route.meta?.hidden) return false;
    
    // 检查路由权限
    if (!hasPermission(route.meta?.requiresAdmin)) return false;
    
    // 处理子路由
    if (route.children) {
      route.children = route.children.filter((child) => {
        if (!child.meta) return false;
        return !child.meta?.hidden && hasPermission(child.meta?.requiresAdmin);
      });
      // 只保留有子路由的父路由
      return route.children.length > 0;
    }

    return true;
  });
});

const toggleCollapse = () => {
  isCollapsed.value = !isCollapsed.value;
};
</script>

<style scoped>
.home {
  height: 100%;
}
.el-container {
  height: 100%;
  min-height: 0;
  display: flex;
  flex-direction: column;
}
:deep(.el-container > .el-container) {
  flex-direction: row;
}
:deep(.el-container > .el-container > .el-container) {
  flex-direction: column;
}
.el-main {
  background-color: #ffffff;
  padding: 24px;
  border-radius: 12px;
  margin: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  transition: margin-left 0.3s ease;
}
.el-aside {
  color: #e2e8f0;
  text-align: left;
  border-radius: 12px;
  margin: 16px 0 16px 0;
  transition: all 0.3s ease;
  box-shadow: 2px 0 10px rgba(0, 0, 0, 0.1);
  position: relative;
  overflow: hidden;
}
.collapse-trigger {
  position: absolute;
  right: -10px;
  top: 50%; /* 垂直居中 */
  transform: translateY(-50%); /* 垂直居中修正 */
  width: 30px;
  height: 30px;
  background-color: #ffffff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  z-index: 10;
  transition: all 0.3s ease;
}
/* 优化菜单项交互效果 */
:deep(.el-menu-item),
:deep(.el-sub-menu__title) {
  color: #64748b !important; /* 加深默认文字色 */
  height: 52px !important;
  line-height: 52px !important;
  margin: 4px 10px !important;
  border-radius: 8px !important;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1) !important; /* 更流畅的过渡 */
}
:deep(.el-menu-item:hover),
:deep(.el-sub-menu__title:hover) {
  background-color: rgba(184, 220, 255, 0.2) !important; /* 淡蓝色hover效果 */
  color: #3b82f6 !important; /* hover时文字变色 */
}
:deep(.el-menu-item.is-active) {
  background-color: #b8dcff !important;
  color: #1e40af !important; /* 加深激活态文字色 */
  font-weight: 500;
  box-shadow: 0 2px 4px rgba(184, 220, 255, 0.5) !important; /* 激活态阴影 */
}
/* 图标样式优化 */
:deep(.el-menu-item .el-icon),
:deep(.el-sub-menu__title .el-icon) {
  margin-right: 12px !important;
  font-size: 18px !important;
  width: 24px !important; /* 固定图标宽度，对齐更整齐 */
  text-align: center;
}
/* 折叠状态优化 */
:deep(.el-menu--collapse) {
  width: 80px !important; /* 略微加宽折叠状态宽度 */
}
:deep(.el-menu--collapse .el-menu-item,
.el-menu--collapse .el-sub-menu__title) {
  display: flex;
  justify-content: center; /* 图标居中显示 */
  padding: 0 !important;
}
:deep(.el-menu--collapse .el-icon) {
  margin-right: 0 !important; /* 折叠状态去掉图标右边距 */
}
/* 隐藏所有滚动条 */
:deep(.el-menu),
.el-aside {
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* IE and Edge */
}
:deep(.el-menu)::-webkit-scrollbar,
.el-aside::-webkit-scrollbar {
  display: none; /* Chrome, Safari, Opera */
}
</style>