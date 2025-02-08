<template>
    <div class="dashboard">
      <el-container>
        <el-header>
          <h1>欢迎来到仪表盘</h1>
        </el-header>
        <el-main>
          <p>这里是用户登录后的主页面内容。</p>
          <el-button type="primary" @click="handleLogout">退出登录</el-button>
        </el-main>
      </el-container>
    </div>
  </template>
  
  <script>
  export default {
    name: "Dashboard",
    methods: {
      handleLogout() {
        this.$router.push("/"); // 返回登录页
      },
    },
  };
  </script>
  
  <style scoped>
  .dashboard {
    text-align: center;
    margin: 20px auto;
  }
  </style>
  