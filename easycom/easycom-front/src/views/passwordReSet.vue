<template>
  <div class="login-body">
    <div class="bg">
        <el-card class="login-card">
            <div class="login-title">密码重置</div>
            <el-form :model="resetForm" ref="resetFormRef" :rules="rules">
                <el-form-item prop="password" class="form_base">
                    <el-input 
                        v-mode="resetForm.password" 
                        placeholder="请输入新密码" 
                        v-model.trim="resetForm.password"
                        show-password
                        @focus="clearValidation('password')"
                        >
                            <template #prefix>
                                <span class="iconfont icon-password"></span>
                            </template>
                        </el-input>
                </el-form-item>

                <el-form-item prop="rePassword" class="form_base">
                    <el-input 
                        v-mode="resetForm.rePassword" 
                        placeholder="请确认新密码" 
                        v-model.trim="resetForm.rePassword"
                        show-password
                        @focus="clearValidation('rePassword')"
                        >
                            <template #prefix>
                                <span class="iconfont icon-password"></span>
                            </template>
                        </el-input>
                </el-form-item>
                <el-form-item class="form_base button">
                  <el-button type="primary">重置</el-button>
                  <el-button class="cancel">取消</el-button>
                </el-form-item>
                
            </el-form>
        </el-card>
    </div>
  </div>
</template>

<script setup>
  import { ref } from 'vue';
import message from '../utils/Message';

  const resetForm = ref({});
  const resetFormRef = ref();

  const rules = {
    password:[{required: true, message:'请输入要修改的密码', tigger:'blur'}],
    rePassword:[{required: true, message: '请再次输入密码', tigger:'blur'}],
  }

  const clearValidation = (field) =>{
    resetFormRef.value.clearValidate(field);
  }
</script>

<style lang="scss" scoped>
.login-body {
  height: calc(100vh);
  background-size: cover;
  background:linear-gradient(to right, #cef5c3, #7dfa88);

  display: flex;
  .bg{
    flex: 1;
    display: flex;
    background-size: cover;
    opacity: 0.8;
    align-items: center;
    justify-content: center;
  }


  .login-card{
    width: 500px;
    height: 350px;
    min-width: 300px;
    max-width: 800px;
    min-height: 350px;
    max-height: 400px;
    padding :10px;
    border-radius: 7px;
    justify-content: center;
    align-items: center;
    background-color: aquamarine;
    box-shadow: 0 4px 20px rgba(19, 179, 77, 0.9);
  }

  .login-title{
    text-align: center;
    font-size: 40px;
    font-weight: bold;
    margin-top: 20px;
    color: #333;
    text-shadow: 0 5px 5px rgba(0,0,0, 0.4);
  }

  .form_base{
    margin-top: 30px;
  }

  .button{
    display: grid;
    place-items: center;
  }
  .reset{
    margin-left: 80px;
  }

  .cancel{
    margin-left: 20px;
  }

}
</style>