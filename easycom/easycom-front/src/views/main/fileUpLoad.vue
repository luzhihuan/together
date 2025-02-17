<template>
    <div class="fullpage-container" ref="container">
        
        <section class="page" v-for="(page, i) in pages" :key="i">
            <h1 class="style-title">{{ page.title }}</h1>
            <span>资料模板</span>
            <div>
                <el-image
                    ref="imageRef"
                    style="width: 400px;height: 300px;"
                    :src = "page.image"
                    fit="cover"
                >
                </el-image>
            </div>
        </section>
    </div>
</template>
  
<script setup>
  import { ref, onMounted, onBeforeUnmount } from 'vue';
<<<<<<< Updated upstream

  import templateImage from "@/assets/sample.jpg"
  
  // 模拟多屏数据
  
=======
  import templateImage from "@/assets/sample.jpg"
  
  // 模拟多屏数据
>>>>>>> Stashed changes
  const pages = ref([
    { 
        title: '德育资料上传',   
        fontClass: 'style-title',
        image: templateImage,
    },
    { 
        title: '智育资料上传' 

    },
    { 
        title: '体育资料上传' 

    },
    { 
        title: '美育资料上传' 
        
    },
    { 
        title: '劳育资料上传' 
    },
    { 
        title: '其它资料上传' 
    },

  ]);


  const currentIndex = ref(0);
  const container = ref(null);
  
  // 防止一次滚轮事件多次触发
  let isScrolling = false;
  const scrollDuration = 800; // 切换动画时长（毫秒）
  
  const handleWheel = (e) => {
    // 阻止默认滚动
    e.preventDefault();
  
    if (isScrolling) return;
    isScrolling = true;
  
    // 判断滚轮方向
    if (e.deltaY > 0) {
      // 向下滚
      if (currentIndex.value < pages.value.length - 1) {
        currentIndex.value++;
      }
    } else if (e.deltaY < 0) {
      // 向上滚
      if (currentIndex.value > 0) {
        currentIndex.value--;
      }
    }
  
    // 执行切换动画
    scrollToIndex(currentIndex.value);
  
    // 等动画结束后，允许下一次滚动
    setTimeout(() => {
      isScrolling = false;
    }, scrollDuration);
  };
  
  const scrollToIndex = (index) => {
    const pageHeight = window.innerHeight;
    const targetOffset = pageHeight * index;
    container.value.style.transform = `translateY(-${targetOffset}px)`;
    container.value.style.transition = `transform ${scrollDuration}ms ease`;
  };
  
  onMounted(() => {
    // 监听滚轮事件
    window.addEventListener('wheel', handleWheel, { passive: false });
  });
  
  onBeforeUnmount(() => {
    window.removeEventListener('wheel', handleWheel);
  });
</script>
  
  

<style lang="scss" scoped>

  .fullpage-container {
    /* 容器本身不滚动，而是由 transform 位移 */
    width: 100%;
    height: 100%;
    overflow: hidden;
    position: relative;
    background-color: black;
  }
  
  .page {
    width: 100%;
    height: 100vh;
    /* 可以自定义每一屏的样式 */
    display: flex;
    // align-items: center;
    // justify-content: center;
    font-size: 2rem;
    flex-direction: column;
  }

  .style-title{
    font-size: 30px;
    margin-top: 30px;
    margin-left: 40px;
  }

    .page:nth-child(1) { background: lightcoral; }
    .page:nth-child(2) { background: lightpink; }
    .page:nth-child(3) { background: lightblue; }
    .page:nth-child(4) { background: lightyellow; }
    .page:nth-child(5) { background: lightsalmon; }
    .page:nth-child(6) { background: lightgreen; }

</style>
  