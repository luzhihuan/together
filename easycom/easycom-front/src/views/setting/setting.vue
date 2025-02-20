<template>
  <div class="setting-body">
    <span class="font-title">设置</span>
    <el-divider class="divider"></el-divider>
    <div>
      <span class="font-title">账号设置</span>
      <el-divider class="divider"></el-divider>
      <!-- <el-collapse v-model="activeName" accordion>

        <el-collapse-item title="修改当前绑定的邮箱" name="2">
          <div>
            <el-card class="setting-card">
              <span>当前绑定的邮箱：</span>
              <el-form >
                <el-form-item >
                  <div class="form-item">
                    <div>
                      <el-input 
                      prop="checkcode" 
                      placeholder="请输入收到的邮箱验证码" 
                      style="width: 290px; margin-top: 20px;"
                      />
                      <el-tooltip
                        effect="light"
                        content=
                        "向当前绑定的邮箱发送验证码，若当前绑定的邮箱无法登录，请联系管理员更换绑定邮箱"
                        placement="top-start"
                      >
                      <el-button type="primary" style="margin-top: 20px;margin-left: 7px;">发送验证码</el-button>
                      </el-tooltip>
                    </div>
                    <el-input prop="newEmail" placeholder="请输入新的邮箱账号" class="form-input" />
                    <el-button type="primary" class="form-button">确认重置</el-button>
                    <el-button class="form-button">取消</el-button>
                    
                  </div>
                </el-form-item>
              </el-form>
            </el-card>           
          </div>
        </el-collapse-item>
        <el-collapse-item title="修改密码" name="3">
          <div>
            <el-button type="primary">确认修改</el-button>
            <el-button type="primary">取消</el-button>
          </div>
        </el-collapse-item>
      </el-collapse> -->
      <div class="style-row">
      <el-container class="button-container">
        <el-button 
          class="style-button"
          @click="emailDialog"
        >
        <el-icon>
          <MessageBox />
        </el-icon>
        </el-button>
        <span class="button-title">修改绑定邮箱</span>
      </el-container>
      
      <el-container class="button-container">
        <el-button 
          class="style-button"
          @click="passwordDialog"
        >
        <el-icon>
          <Lock />
        </el-icon>
        </el-button>
        <span class="button-title">修改当前密码</span>
      </el-container> 

      <el-container class="button-container">
        <el-button 
          class="style-button"
        >
        <el-icon>
          <KnifeFork />
        </el-icon>
        </el-button>
        <span class="button-title">其它whatever</span>
      </el-container>
    </div>
    <div class="marginTop">
      <span class="font-title">
        敬请期待
      </span>
      <el-divider class="divider"></el-divider>
      <el-container class="button-container">
          <el-button 
            class="style-button"
          >
          <el-icon>
            <Cherry /> 
          </el-icon>
          </el-button>
          <span class="button-title">敬请期待</span>
      </el-container>
    </div>
      
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const activeName = ref('1')

</script>

<style lang="scss" scoped>

//分界线样式
.divider{
  border-width: 3px;
  border-color: rgb(110, 87, 2);
  box-shadow: 0 5px 5px rgba(0,0,0, 0.3);
}
//上下间距
.marginTop{
  margin-top: 30px;
}
//标题样式
.font-title{
    font-size: 30px;
    font-weight: bold;
    margin-top: 20px;
    color: #05a1f5;
    text-shadow: 0 5px 5px rgba(0,0,0, 0.3);
    margin-left: 30px;
}
//图标按钮样式
.style-button{
  border: 0px;
  font-size: 80px;
  width: 100px;
  height: 100px;
  color: #05a1f5;
  box-shadow: 0 5px 5px rgba(0,0,0, 0.3);
  margin-bottom: 10px;
  //background-color: ;
  transition: transform 0.3s ease;
}
.style-button:hover{
  transform: scale(1.2);
}
.button-container{
  display: flex;
  flex-direction: column;
  margin-left: 40px;
  position: relative;
  height: 100vh;
  max-height: 140px;
  max-width: 140px;
  align-items: center;
}
.button-title{
  margin-top: 10px;
  text-align: center;
}

.style-row{
  display: flex;
  flex-direction: row;
}

.style-selection{
  width: 100%;
}

// .setting-body{
//   display: flex;
//   overflow: auto;
//   flex-direction: column;

//   .setting-card{
//     border: 0px;
//     width: 100%;
//     height: 400px;
//     display: flex;
//     flex-direction: column;
//   }

//   .divider{
//   border-width: 3px;
//   border-color: rgb(110, 87, 2);
//   box-shadow: 0 5px 5px rgba(0,0,0, 0.3);
//   }

//   .font-title{
//     font-size: 30px;
//     font-weight: bold;
//     margin-top: 20px;
//     color: #05a1f5;
//     text-shadow: 0 5px 5px rgba(0,0,0, 0.3);
//     margin-left: 30px;
//   }

//   .form-item{
//     display: flex;
//     flex-direction: column;
//     gap: 10px;
//   }

//   .form-input{
//     margin-top: 20px;
//     width: 400px;
//   }

//   .form-button{
//     margin-left: 0px ;
//     width: 100%;
//   }
// }

</style>