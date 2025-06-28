<template>
  <div class="home">
    <el-container>
      <AppHeader />
      <el-container>
        <el-aside :width="180">
          <el-menu
            :default-active="$route.path"
            class="el-menu-vertical-demo"
            router
            :collapse="isCollapsed"
          >
            <template v-for="route in routes" :key="route.path">
              <el-menu-item
                v-if="!route.children || route.children.length === 0"
                :index="'/' + route.path"
              >
                <el-icon>
                  <component :is="route.meta.icon" :key="route.path" />
                </el-icon>
                <span>{{ route.meta.title }}</span>
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
</template>

<script setup lang="ts">
import { useRouter } from "vue-router";
import { ref } from "vue";
import {
  House,
  DataAnalysis,
  Goods,
  Grid,
  Edit,
  ShoppingCart,
  User,
  Setting,
  Expand,
  Fold,
} from "@element-plus/icons-vue";
import AppHeader from "../components/AppHeader.vue";

const router = useRouter();
const isCollapsed = ref(false);

const getCollapseIcon = () => isCollapsed.value ? Expand : Fold;

const routes =
  router.options.routes
    .find((route) => route.path === "/")
    ?.children?.filter(
      (route) => !route.redirect && route.meta?.title && route.meta?.icon
    ) || [];
</script>

<style scoped>
html,
body {
  height: 100%;
  margin: 0;
}

.home {
  height: 98vh;
}

.el-container {
  height: 100%;
  min-height: 0;
  display: flex;
  flex-direction: column;
}

::v-deep .el-container > .el-container {
  flex-direction: row;
}

::v-deep .el-container > .el-container > .el-container {
  flex-direction: column;
}

.footer-content {
  display: flex;
  justify-content: center;
  width: 100%;
}

.footer-left,
.footer-right {
  font-size: 14px;
  line-height: 30px;
  margin: 0 10px;
}

.el-main {
  background-color: #ffffff;
  padding: 24px;
  border-radius: 12px;
  margin: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}

.el-aside {
  background-color: #fdfdfd;
  color: #e2e8f0;
  text-align: left;
  border-radius: 12px;
  margin-top: 16px;
  width: 240px !important;
  transition: all 0.3s ease;
  box-shadow: 2px 0 10px rgba(0, 0, 0, 0.1);
}

::v-deep .el-sub-menu .el-menu-item {
  padding-left: 60px !important;
}

::v-deep .el-menu {
  border-right: none !important;
  background-color: transparent !important;
}

::v-deep .el-menu-item,
::v-deep .el-sub-menu__title {
  color: #94a3b8 !important;
  height: 50px !important;
  line-height: 50px !important;
  margin: 5px 10px !important;
  border-radius: 8px !important;
  transition: all 0.2s ease !important;
}

::v-deep .el-menu-item:hover,
::v-deep .el-sub-menu__title:hover {
  background-color: rgba(255, 255, 255, 0.1) !important;
}

::v-deep .el-menu-item.is-active {
  background-color: #b8dcff !important;
  color: #5c5c5c !important;
}

::v-deep .el-menu-item .el-icon,
::v-deep .el-sub-menu__title .el-icon {
  margin-right: 12px !important;
  font-size: 18px !important;
}
</style>
