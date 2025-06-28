import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import { createPinia } from "pinia";
import ElementPlus from "element-plus";
import "element-plus/dist/index.css";
import zhCn from "element-plus/es/locale/lang/zh-cn";
import piniaPluginPersistedstate from "pinia-plugin-persistedstate";
import VChart from "vue-echarts";

const app = createApp(App);
const pinia = createPinia();
pinia.use(piniaPluginPersistedstate);
app.component("v-chart", VChart);
app.use(router);
app.use(ElementPlus, {
  locale: zhCn,
});
app.use(pinia);
app.mount("#app");
