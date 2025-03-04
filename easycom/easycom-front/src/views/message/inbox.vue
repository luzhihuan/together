<template>
    <div class="body">
      <el-divider direction="vertical" class="style-divider" />
  
      <el-card class="style-card">
        <el-card
          class="style-emailCard"
          v-for="item in emailList"
          :key="item.key"
          @click="messageDialog(item)" 
        >
          <h3 class="font-title">{{ item.title }}</h3>
          <span class="font-time">{{ item.time }}</span>
          <div>
            <span class="font-content">{{ item.content }}</span>
          </div>
        </el-card>
      </el-card>
  
      <el-divider direction="vertical" class="style-divider" />
      <h2 class="font-monthTime">三月</h2>
  
      
      <el-dialog
        v-if="currentEmail"  
        v-model="dialogVisible"
        @close="dialogVisible = false"
      >
        <h2>{{ currentEmail.title }}</h2>
        <p>{{ currentEmail.content }}</p>
      </el-dialog>
    </div>
</template>
  
<script setup>
import { ref } from 'vue';


const emailList = ref([
{ key: '1', time: '时间1', sender: '发件人1', title: '信件1', content: '内容1' },
{ key: '2', time: '时间2', sender: '发件人2', title: '信件2', content: '内容2' },
{ key: '3', time: '时间3', sender: '发件人3', title: '信件3', content: '内容3' },
{ key: '4', time: '时间4', sender: '发件人4', title: '信件4', content: '内容4' },
]);

const dialogVisible = ref(false); 
const currentEmail = ref(null); 


const messageDialog = (item) => {
currentEmail.value = item;
dialogVisible.value = true;
};
</script>
  

<style lang="scss" scoped>

.body{
    display: flex;
    flex-direction: row;
    overflow: auto;
    height: 100vh;
}
.style-divider{
    border-color: rgb(142, 198, 225);
    border-width: 5px;
    margin: 50px;
    height: 100%;
}

.style-card{
    // display: flex;
    // justify-content: center;
    margin: 60px;
    background-color: rgb(244, 248, 248);
    width: 65%;
    height: 100%;
    transition: all 0.3s;
}

.style-card:hover{
    box-shadow: 0 3px 5px rgba(0,0,0, $alpha: 0.5);
}

.style-emailCard{
    display: flex;
    // align-items: center;
    //justify-content: center;
    width: 90%;
    height: 125px;
    border-radius: 6px;
    background-color: rgb(244, 248, 248);
    margin: 30px;
    transition: all 0.3s ease-in-out;
}
.style-emailCard:hover{
    transform: scale(1.05);
}

.style-dialog{
    width: 600px;
    height: 800px;
    overflow: auto;
}

.font-monthTime{
    font-size: 35px;
    color: lightblue;
    margin: 30px;
}

.font-title{
    font-size: 20px;
    margin: 5px;
}

.font-time{
    margin-left: 10px;
}

.font-content{
    margin-left: 15px;
}
</style>