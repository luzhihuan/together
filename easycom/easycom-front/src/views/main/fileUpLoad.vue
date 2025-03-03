<template>
  <div class="body">
    <span class="style-title">资料上传界面</span>
    <el-container>
      <el-steps 
        style="width: 800px"
        :active="stepsStatus" 
        align-center
      >
        <el-step title="步骤1"  description="德育资料上传"/>
        <el-step title="步骤2"  description="智育资料上传"/>
        <el-step title="步骤3"  description="体育资料上传"/>
        <el-step title="步骤4"  description="美育资料上传"/>
        <el-step title="步骤5"  description="劳育资料上传"/>
      </el-steps>
    </el-container>
    <!-- 走马灯组件 -->
    <el-carousel
      indicator-position="none"
      arrow="never"
      :autoplay="false"
      ref="carouselRef"
      height="82vh"
      :loop="false"
    >
      <el-carousel-item v-for="(item, index) in form" :key="index">
        <div class="carousel-content">
          <h2>{{ item.title }}</h2>
          
          <!-- 表单 -->
          <el-form class="style-form" :model="fileUploadForm" ref="fileUploadFormRef">
            <el-form-item>
              <!-- <el-upload
                v-model:file-list="fileUploadForm.fileList"
                action="https://your-backend-api.com/upload"
                list-type="picture-card"
                :on-preview="handlePictureCardPreview"
                :on-remove="handleRemove"
                :before-upload="beforeAvatarUpload"
                :on-success="handleAvatarSuccess"
              >
                <el-icon><Plus /></el-icon>
              </el-upload> -->

                <!-- 修改为文件列表式上传形式 -->
              <el-card class="style-upLoadCard">
                <el-upload
                  v-model:file-list="fileList"
                  class="upload-demo"
                  action="https://run.mocky.io/v3/9d059bf9-4660-45f2-925d-ce80ad6c4d15"
                  :on-change="handleChange"
                  :on-preview="handlePictureCardPreview"
                  :before-upload="beforeAvatarUpload"
                >
                  <el-tooltip
                    effect="light"
                    content="仅支持不超过5MB的jpg、png、docx、xlsx类型文件"
                    placement="right-start"
                  >
                    <el-button type="primary">点击上传资料</el-button>
                  </el-tooltip>
                  
                </el-upload>
              </el-card>
              
            </el-form-item>
            
            <el-form-item>
              <el-button v-if="item.buttons.length > 0" :type="item.buttons[0].type" @click="item.buttons[0].click">
                {{ item.buttons[0].text }}
              </el-button>
              <el-button v-if="item.buttons.length > 1" :type="item.buttons[1].type" @click="item.buttons[1].click">
                {{ item.buttons[1].text }}
              </el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-carousel-item>
    </el-carousel>
    
    <!-- 图片预览对话框 -->
    <el-dialog v-model="dialogVisible" width="50%">
      <img w-full :src="dialogImageUrl" alt="Preview Image" />
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';
import { ElMessage } from 'element-plus';
import { Plus } from '@element-plus/icons-vue';
import axios from 'axios';
const carouselRef = ref(null);
const fileUploadForm = reactive({ fileList: [] });
const dialogImageUrl = ref('');
const dialogVisible = ref(false);
const stepsStatus = ref(0);

// 表单数据
const form = reactive([
  { 
    title: '德育佐证资料上传', 
    buttons: [
      { text: '下一步', type: 'primary', click: () => nextStep(0) },
    ] 
  },
  { 
    title: '智育佐证资料上传', 
    buttons: [
      { text: '上一步', type: 'primary', click: () => lastStep() },
      { text: '下一步', type: 'primary', click: () => nextStep(1) },
    ] 
  },
  { 
    title: '体育佐证资料上传', 
    buttons: [
      { text: '上一步', type: 'primary', click: () => lastStep() },
      { text: '下一步', type: 'primary', click: () => nextStep(2) },
    ] 
  },
  { 
    title: '美育佐证资料上传', 
    buttons: [
      { text: '上一步', type: 'primary', click: () => lastStep() },
      { text: '下一步', type: 'primary', click: () => nextStep(3) },
    ] 
  },
  { 
    title: '劳育佐证资料上传', 
    buttons: [
      { text: '上一步', type: 'primary', click: () => lastStep() },
      { text: '提交', type: 'primary', click: () => submitData() },
    ] 
  },
]);
//上一步
const lastStep = async () => {
  if(carouselRef.value) carouselRef.value.prev();
  stepsStatus.value-- ;
}
// 走马灯切换下一页并上传数据
const nextStep = async (index) => {
  //await sendDataToServer(form[index]);
  if (carouselRef.value) carouselRef.value.next();
  stepsStatus.value++ ;
};

// 提交数据到服务器
// const sendDataToServer = async (data) => {
//   try {
//     await axios.post('', data);
//     ElMessage.success('数据提交成功'); 
//   } catch (error) {
//     ElMessage.error('提交失败，请重试');
//   }
// };

// 处理图片预览
const handlePictureCardPreview = (file) => {
  dialogImageUrl.value = file.url;
  dialogVisible.value = true;
};

// 处理文件上传
const beforeAvatarUpload = (file) => {
  if (file.type !== 'image/jpeg' && file.type !== 'image/png') {
    ElMessage.error('图片格式必须为 JPG 或 PNG');
    return false;
  } else if (file.size / 1024 / 1024 > 5) {
    ElMessage.error('图片大小不得超过 5MB');
    return false;
  }
  return true;
};

const fileList = ref([
  {
    name: '示例.jpg',
    url:'', 
  },
])

const handleChange = (uploadFiles) => {
  fileList.value = [...uploadFiles].slice(-3);
};

</script>

<style scoped>
.body {
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #ebf0ee;
  height: 100vh;
  justify-content: center;
}

.el-carousel {
  width: 90%;
  border-radius: 10px;
  background-color: aliceblue;
  box-shadow: 0px 4px 20px rgba(50, 75, 84, 0.4);
}

.carousel-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 82vh;
  padding: 20px;
}

.style-title{
  font-size: 30px;
  margin-bottom: 15px; 
}

.style-upLoadCard{
  width: 400px;
  height: 300px;
  overflow: auto;
}
</style>
