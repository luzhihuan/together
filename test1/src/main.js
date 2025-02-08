// import { createApp } from 'vue'
// import './assets/style.css'
// import App from './App.vue'

// createApp(App).mount('#app')

import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import ElementPlus from "element-plus";
import "element-plus/dist/index.css";
import "./assets/style.css"; // 全局样式

const app = createApp(App);

app.use(router);
app.use(ElementPlus);
app.mount("#app");
