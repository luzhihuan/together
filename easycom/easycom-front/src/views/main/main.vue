<template>
  <div class="style-body">
    <span class="font-title">
      综测服务
    </span>
    <el-divider class="divider marginTop"></el-divider>
    <div class="style-row">
      <el-container class="button-container">
        <el-button 
          class="style-button"
          @click="yearSelection"
        >
          <el-icon >
            <Management />
          </el-icon>
        </el-button>
        <span class="button-title">年度综合评测</span>
      </el-container>
      
      <el-container class="button-container">
        <el-button 
          class="style-button"
          @click="openEvaluationResult"
        >
        <el-icon>
          <Checked /> 
        </el-icon>
        </el-button>
        <span class="button-title">评测结果查询</span>
      </el-container>

      <el-container class="button-container">
        <el-button 
          class="style-button"
          @click="openPrePerformance"
        >
        <el-icon>
          <TrendCharts />
        </el-icon>
        </el-button>
        <span class="button-title">往期综测成绩</span>
      </el-container> 

      
    </div>
    <div class="marginTop">
      <span class="font-title">
        管理接口
      </span>
      <el-divider class="divider"></el-divider>
      <div class="style-row">
        <el-container class="button-container">
            <el-button 
              class="style-button"
              @click="openUnprocessedTask"
            >
            <el-icon>
              <List />
            </el-icon>
            </el-button>
            <span class="button-title">未处理审核任务</span>
        </el-container>
        <el-container class="button-container">
            <el-button 
              class="style-button"
              @click="fileUpLoad"
            >
            <el-icon>
              <Cherry /> 
            </el-icon>
            </el-button>
            <span class="button-title">敬请期待</span>
        </el-container>
      </div>
    </div>

    <Dialog
      :show = "dialog.show"
      :title = "dialog.title"
      :buttons = "dialog.buttons"
      width = "500px"
      :showCancel = "false"
      @close = "dialog.show=false"
    >
      <el-form :model="formData" ref="formDataRef" :rules="rules">
        <el-form-item prop="yearSelection">
          <el-select 
          v-model="formData.yearSelection"
          placeholder = "请选择当期评测时间"
          class="style-selection"
          >
            <el-option
              v-for = "item in years"
              :key = "item.value"
              :label = "item.label"
              :value = "item.value"
              :disabled = "item.disabled"       
            />
          </el-select>
        </el-form-item>
      </el-form>
    </Dialog>

  </div>
</template>

<script setup>
  import { ref } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { reactive } from 'vue';
  import message from '../../utils/Message';

  const route = useRoute()
  const router = useRouter()

  const formData = ref({})
  const formDataRef = ref()
  const rules = {
    yearSelection: [{required: true, message: '请选择评测年份', trigger: 'blur'}],
  }
//评定年度选择
const yearSelectionConfirm = () =>{
    formDataRef.value.validate (async (valid) => {
      if(!valid){
        return;
      }else{
        const route = router.resolve({name: '文件上传'});
        window.open(route.href, '_blank');
      }
    })
  };
//打开往期综测成绩界面
const openPrePerformance = () =>{
    const route = router.resolve({name: '往期综测成绩'});
    window.open(route.href, '_blank');
}

//打开评定结果查询界面
const openEvaluationResult = () =>{
    const route = router.resolve({name: '评定结果'});
    window.open(route.href, '_blank');
}

//打开尚未处理任务界面
const openUnprocessedTask = () =>{
    const route = router.resolve({name: '未处理任务'});
    window.open(route.href, '_blank');
}

//弹窗元素初始化
  const dialog = reactive({
    show: false,
    title: '评测年度选择',
    buttons: [
      {
        type:"primary",
        text:"确认",
        click: (e)=>{
          yearSelectionConfirm();
        }
      },
      {
        type:"primary",
        text:"取消",
        click: (e) =>{
          dialog.show = false;
        }
      }
    ]
  })
//选择器元素初始化
  const years = [
    {
      value : '2024-2025',
      label : '2024-2025年度评测',
      disabled: true,
    },
    {
      value : '2023-2024',
      label : '2023-2024年度评测',
    },
    {
      value : '2022-2023',
      label : '2022-2023年度评测',
      disabled: true,
    },
  ]

  const yearSelection = () => {
    dialog.show = true;
  }
</script>

<style lang="scss" scoped>

.style-body{
  margin-top: 20px;
}
//分界线样式
.divider{
  border-width: 3px;
  border-color: rgb(110, 87, 2);
  box-shadow: 0 5px 5px rgba(0,0,0, 0.3);

}
//上下间距
.marginTop{
  margin-top: 20px;
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

</style>