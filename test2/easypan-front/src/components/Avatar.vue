<template>
  <span class="avatar" :style="{width:width+'px',height:width+'px'}">
  <img
      :src="
          avatar&&avatar != ''
          ?avatar:`/api/${proxy.globalInfo.avatarUrl}${userId}?${timestamp}`
      "
      alt=""
      v-if="userId"
  />
  </span>
</template>


<script setup>
import { ref,reactive,getCurrentInstance,nextTick } from 'vue';
const {proxy} = getCurrentInstance()

const props = defineProps({
  userId:{
    type:String,
  },
  avatar:{
    type:String,
  },
  timestamp:{
    type:Number,
    default:0
  },
  width:{
    type:Number,
    default:40
  }
})

</script>



<style scoped lang="scss">
.avatar{
  display: flex;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  overflow: hidden;
  img{
    width: 100%;
    object-fit: cover;
  }
}
</style>
