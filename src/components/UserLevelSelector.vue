<template>
    
    <div class="user-level-selector">
      <el-select v-model="selectedLevel" placeholder="请选择身份" @change="handleChange">
        <el-option v-for="level in levels" :key="level.value" :label="level.label" :value="level.value" />
      </el-select>
    </div>
  </template>
  
  <script>
  export default {
    name: 'UserLevelSelector',
    props: {
      levels: {
        type: Array,
        default: () => [
          { label: '管理员', value: 'admin' },
          { label: '普通用户', value: 'user' },
        ],
      },
    },
    data() {
      return {
        selectedLevel: null,
      };
    },
    methods: {
      handleChange(value) {
        this.$emit('level-selected', value);
      },
    },
  };
  </script>
  
  <style scoped>
  .user-level-selector {
    margin: 20px;
  }
  </style>
  