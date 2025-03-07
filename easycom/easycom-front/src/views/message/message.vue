<template>
  <div class="style-body"> 
    <el-badge
      :value="200"
      :max="99"
    >
      <el-button 
        class="style-button" 
        @click="openInbox"
      >
        <template #icon>
          <el-icon><MessageBox /></el-icon>
        </template>
        <span class="style-font">收件箱</span>
      </el-button>
    </el-badge>
  

    <!-- </div>
    <div class="oneThirdPart">
      <el-container class="button-container"> 
        <el-button class="style-button" >
        <template #icon>
          <el-icon><Message /></el-icon>
        </template>
          <span class="style-font">写信件</span>       
        </el-button>
      </el-container>
    </div>
    <div class="oneThirdPart">
      <el-container class="button-container">
      
        <el-button class="style-button" >
        <template #icon>
          <el-icon><Edit /></el-icon>
        </template>
        <span class="style-font">草稿箱</span>
        </el-button>
      </el-container> -->  
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const router = useRouter()
const route = useRoute()

const openInbox = () => {
  const route = router.resolve({name:'信箱'});
  window.open(route.href, '_blank');
}

</script>

<style lang="scss" scoped>

//每个小部分的样式
// .oneThirdPart{
//   display: flex;
//   justify-content: center;
//   align-items: center;
//   margin: 0;
//   padding: 0;
// }

//按钮容器及按钮样式
// .button-container{
//   display: flex;
//   justify-content: center;
//   align-items: center;
//   height: 100%;
//   flex-direction: row;
// }

.style-body{
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.style-button{
  display: flex;
  border: 0px;
  font-size: 100px;
  width: 250px;
  height: 400px;
  color: #05a1f5;
  box-shadow: 0 5px 5px rgba(0,0,0, 0.3);
  
  flex-direction: column;
  transition: transform 0.3s ease;
  
}
.style-button:hover{
transform: scale(1.2);
}

.style-font{
  
  margin-top: 10px;
  font-size: 30px;
  
}

  
</style>