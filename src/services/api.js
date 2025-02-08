import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:3000/api', // 后端接口地址
  timeout: 5000,
  headers:{
      "Content-Type":"application/json",
  },
});

// 请求拦截器
api.interceptors.request.use(
  (config) => {
    console.log('请求配置:', config);
    return config;
  },
  (error) => Promise.reject(error)
);

// 响应拦截器
api.interceptors.response.use(
  (response) => response.data,
  (error) => {
    console.error('响应错误:', error);
    return Promise.reject(error);
  }
);

export default api;
