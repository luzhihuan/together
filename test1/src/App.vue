<template>
  <div id="app">
    <el-container>
      <!-- 顶部导航栏 -->
      <el-header class="app-header">
        <h1 class="app-title">综测管理系统</h1>
      </el-header>

      <!-- 主内容区域 -->
      <el-main>
        <router-view />
      </el-main>

      <!-- 页脚 -->
      <el-footer class="app-footer">
        <p>© 2025 综测管理系统 | 由 asshole 提供支持</p>
      </el-footer>
    </el-container>
  </div>
</template>

<script>
export default {
  name: "App",
};
</script>

<style scoped>
/* 全局布局样式 */
#app {
  height: 100%;
  display: flex;
  flex-direction: column;
}

/* 顶部导航栏样式 */
.app-header {
  background-color: #6a11cb;
  color: white;
  text-align: center;
  padding: 10px 0;
}

.app-title {
  font-size: 1.8rem;
  font-weight: bold;
  margin: 0;
}

/* 主内容区域样式 */
.el-main {
  flex: 1;
  padding: 20px;
  background: #f5f5f5;
  box-sizing: border-box;
}

/* 页脚样式 */
.app-footer {
  text-align: center;
  color: #6a11cb;
  padding: 10px 0;
  font-size: 0.9rem;
  background: #ffffff;
  border-top: 1px solid #e0e0e0;
}
</style>
