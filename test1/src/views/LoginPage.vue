<template>
    <div class="login-page">
      <h1>用户登录</h1>
      <UserLevelSelector @level-selected="handleLevelSelect" />
      <LoginForm v-if="!isRecovering" @forgot-password="toggleRecovery" @login-success="handleLoginSuccess" />
      <PasswordRecovery v-else @cancel="toggleRecovery" />
    </div>
  </template>
  
  <script>
  import UserLevelSelector from '../components/UserLevelSelector.vue';
  import LoginForm from '../components/LoginForm.vue';
  import PasswordRecovery from '../components/PasswordRecovery.vue';
  
  export default {
    name: 'LoginPage',
    components: {
      UserLevelSelector,
      LoginForm,
      PasswordRecovery,
    },
    data() {
      return {
        isRecovering: false,
      };
    },
    methods: {
      handleLevelSelect(level) {
        console.log('选择的身份:', level);
      },
      toggleRecovery() {
        this.isRecovering = !this.isRecovering;
      },
      handleLoginSuccess(data) {
        this.$router.push('/dashboard'); // 跳转到仪表盘页面
      },
    },
  };
  </script>
  
  <style scoped>
  .login-page {
    text-align: center;
    margin: 50px auto;
  }
  </style>
  