<template>
    <div class="password-recovery">
      <el-form :model="form" :rules="rules" ref="recoveryForm" label-width="100px">
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入注册邮箱" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleRecovery">找回密码</el-button>
        </el-form-item>
      </el-form>
    </div>
  </template>
  
  <script>
  import api from '../services/api';
  
  export default {
    name: 'PasswordRecovery',
    data() {
      return {
        form: {
          email: '',
        },
        rules: {
          email: [
            { required: true, message: '请输入邮箱', trigger: 'blur' },
            { type: 'email', message: '邮箱格式不正确', trigger: 'blur' },
          ],
        },
      };
    },
    methods: {
      async handleRecovery() {
        try {
          const valid = await this.$refs.recoveryForm.validate();
          if (valid) {
            await api.post('/recover-password', this.form);
            this.$message.success('密码重置邮件已发送，请检查邮箱！');
          }
        } catch (error) {
          console.error('找回密码失败:', error);
        }
      },
    },
  };
  </script>
  
  <style scoped>
  .password-recovery {
    max-width: 400px;
    margin: 0 auto;
  }
  </style>
  