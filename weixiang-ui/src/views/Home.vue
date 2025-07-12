<template>
  <div class="home" v-if="showLayout">
    <el-container>
      <AppHeader />
      <el-container>
        <el-aside :width="isCollapsed ? '100px' : '196px'">
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
                :index="'/' + route.path"
              >
                <el-icon>
                  <component :is="route.meta.icon" :key="route.path" />
                </el-icon>
                <template #title>
                  <span>{{ route.meta.title }}</span>
                </template>
              </el-menu-item>
              <el-sub-menu v-else :index="'/' + route.path">
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
            <router-view />
          </el-main>
        </el-container>
      </el-container>
    </el-container>
  </div>
  <router-view v-else />
</template>

<script setup lang="ts">
import { useRouter, useRoute } from "vue-router";
import { ref, computed, watch } from "vue";
import { ArrowLeft, ArrowRight } from "@element-plus/icons-vue";
import AppHeader from "../components/AppHeader.vue";

const router = useRouter();
const route = useRoute();
const isCollapsed = ref(false);

// 计算属性：是否显示布局
const showLayout = computed(() => {
  return route.matched.some((record) => record.meta.requiresAuth);
});

// 获取菜单路由
const menuRoutes = computed(() => {
  const layoutRoute = router.options.routes.find((r) => r.name === "Layout");
  return (
    layoutRoute?.children?.filter(
      (route) => !route.meta?.hidden && route.meta?.title && route.meta?.icon
    ) || []
  );
});

const toggleCollapse = () => {
  isCollapsed.value = !isCollapsed.value;
};
</script>

<style scoped>
.home {
  height: 100vh;
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
  background-color: #fdfdfd;
  color: #e2e8f0;
  text-align: left;
  border-radius: 12px;
  margin-top: 16px;
  transition: all 0.3s ease;
  box-shadow: 2px 0 10px rgba(0, 0, 0, 0.1);
  position: relative;
}

.collapse-trigger {
  position: absolute;
  right: -10px;
  top: 30%;
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

.collapse-trigger .el-icon {
  font-size: 14px;
  color: #409eff;
}

.collapse-trigger:hover {
  background-color: #f5f5f5;
}

:deep(.el-sub-menu .el-menu-item) {
  padding-left: 60px !important;
}

:deep(.el-menu) {
  border-right: none !important;
  background-color: transparent !important;
}

:deep(.el-menu-item),
:deep(.el-sub-menu__title) {
  color: #94a3b8 !important;
  height: 50px !important;
  line-height: 50px !important;
  margin: 5px 10px !important;
  border-radius: 8px !important;
  transition: all 0.2s ease !important;
  white-space: nowrap;
}

:deep(.el-menu-item:hover),
:deep(.el-sub-menu__title:hover) {
  background-color: rgba(255, 255, 255, 0.1) !important;
}

:deep(.el-menu-item.is-active) {
  background-color: #b8dcff !important;
  color: #5c5c5c !important;
}

:deep(.el-menu-item .el-icon),
:deep(.el-sub-menu__title .el-icon) {
  margin-right: 12px !important;
  font-size: 18px !important;
}

/* 折叠状态下的样式调整 */
:deep(.el-menu--collapse) {
  width: 64px;
}

:deep(.el-menu--collapse .el-sub-menu) {
  position: relative;
}

:deep(.el-menu--collapse .el-sub-menu .el-sub-menu__title span),
:deep(.el-menu--collapse .el-menu-item span) {
  height: 0;
  width: 0;
  overflow: hidden;
  visibility: hidden;
  display: inline-block;
}

:deep(
    .el-menu--collapse .el-sub-menu .el-sub-menu__title .el-sub-menu__icon-arrow
  ) {
  display: none;
}
</style>
