<template>
  <div class="login-body">
    <div class="bg">
        <div class="left-part">
            <!-- 左半部分占位 -->
        </div>
        <div class="right-part">
            <el-card class="login-card">
                <div class="login-title">用户登录</div>
                
                <el-form :model="form" :rules="rules" ref="login" >
                    <el-form-item prop = "user_id">
                        <el-input v-model="form.user_id" placeholder="请输入账号"/>
                    </el-form-item>

                    <el-form-item prop="password">
                        <el-input v-mode="form.password" placeholder="请输入密码" show-password/>
                    </el-form-item>
                    
                    <el-form-item prop="checkcode">
                        <el-input v-model="form.checkcode" placholder="请输入验证码"/>
                    </el-form-item>

                    <el-form-item>
                        <el-button type="primary">登录</el-button>
                        <el-butt type="text">忘记密码</el-butt>
                    </el-form-item>

                </el-form>
                    
            </el-card>
        </div>
    </div>
  </div>


</template>


<script setup>
import { ref } from 'vue';
import { ElMessage } from 'element-plus';

    emits:[]
     const login = ref(null);
     const form = ref({
         user_id: '',
         password: '',
         checkcode: '',
     });

    const rules = {
        user_id:[{required: true, message:'请输入账号', trigger:'blur'}],
        password:[{required: true, message:'请输入密码', trigger:'blur'}],
        checkcode:[{required: true, message:'请输入验证码', trigger:'blur'}],
    };

</script>



<style lang="scss" scoped>
.login-body {
  height: calc(100vh);
  background-size: cover;

  display: flex;
  .bg{
    background-image: url("../assets/bg.png");
    //background-size: 800px;
    background-repeat: no-repeat;
    flex: 1;
    display: flex;
    background-size: cover;
    opacity: 0.8;
  }

  .left-part{
    flex: 1;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .right-part{
    flex: 1;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .login-card{
    width: 600px;
    height: 700px;
    min-width: 300px;
    max-width: 800px;
    min-height: 400px;
    max-height: 800px;
    padding :10px;
    border-radius: 5px;
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

}
</style>