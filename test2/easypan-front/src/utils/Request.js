// 导入axios库，一个基于Promise的HTTP客户端，用于浏览器和node.js
import axios from 'axios';

// 从element-plus UI框架导入ElLoading组件和textProps
import { ElLoading } from 'element-plus';

import router from "@/router";

// 导入自定义的消息提示工具
import Message from './Message';

import { getCurrentInstance } from 'vue';
const proxy = getCurrentInstance()

// 导入API配置文件，其中包含不同环境的域名等信息
import Api from './Api';
import VueCookie from "vue-cookies";

// 设置表单请求的内容类型
const contentTypeForm = 'application/x-www-form-urlencoded;charset=UTF-8';

// 设置JSON请求的内容类型
const contentTypeJson = 'application/json';

// 设置响应的预期类型为JSON
const responseTypeJson = 'json';

// 初始化一个变量用于存储加载状态的引用
let loading = null;

// 使用axios.create()创建一个axios实例，用于后续的HTTP请求
const instance = axios.create({
    // 配置axios实例以允许跨域请求携带凭证（如Cookies）
    withCredentials: true,
    // 设置axios实例的基础URL，根据环境变量PROD决定使用生产环境还是开发环境的域名
    // import.meta.env.PROD是一个在Vite或Webpack中定义的环境变量，用于区分生产环境和开发环境
    baseURL: "/api",
    // 设置请求的超时时间为10秒（1000毫秒）
    timeout: 10 * 1000
});


// 请求前拦截器，用于在发送请求之前执行一些操作
instance.interceptors.request.use(
    (config) => {
        // 如果配置中指定了显示加载动画（showLoading为true），则显示加载动画
        if (config.showLoading) {
            loading = ElLoading.service({
                lock: true, // 锁定屏幕，防止滚动
                text: '加载中.......', // 加载动画中的文本
                background: 'rgba(0,0,0,0.7)', // 加载动画的背景色
            });
        }
        // 返回配置，继续执行请求
        return config;
    },
    (error) => {
        // 请求失败时执行的操作
        if (config.showLoading && loading) {
            loading.close(); // 关闭加载动画
        }
        Message.error('请求发送失败'); // 显示错误消息
        return Promise.reject('请求发送失败'); // 抛出错误，终止请求
    }
);



// 请求后拦截器，用于在接收到响应后执行一些操作
instance.interceptors.response.use(
    (response) => {
        const { showLoading, errorCallback, showError = true, responseType } = response.config;
        // 如果配置中指定了显示加载动画（showLoading为true），则关闭加载动画
        if (showLoading && loading) {
            loading.close();
        }
        const responseData = response.data;

        // 如果响应类型是arraybuffer或blob，直接返回响应数据
        if (responseType == 'arraybuffer' || responseType == 'blob') {
            return responseData;
        }

        // 正常请求处理
        if (responseData.code == 1) {
            return responseData; // 返回响应数据
        }else {
            // 其他错误处理
            if (errorCallback) {
                errorCallback(responseData); // 调用错误回调函数
            }
            return Promise.reject({ showError: showError, msg: responseData.msg }); // 抛出错误信息
        }
    },
    (error) => {
        // 响应失败时执行的操作
        if (error.config.showLoading && loading) {
            loading.close(); // 关闭加载动画
        }
        return Promise.reject({ showError: true, msg: '网络异常' }); // 抛出网络异常错误
    }
);


/**
 * 封装axios请求的函数，用于发送HTTP请求。
 * @param {Object} config - 请求配置对象，包含请求的详细信息。
 */
const request = (config) => {
    // 从config中解构出url、params、dataType等属性
    const { url, params, dataType, showLoading = true, responseType = responseTypeJson, showError = true } = config;

    // 设置请求的内容类型，默认为表单类型
    let contentType = contentTypeForm;

    // 创建一个FormData对象，用于构建请求体
    let formData = new FormData();

    // 遍历params对象，将其添加到FormData中
    for (let key in params) {
        formData.append(key, params[key] == undefined ? "" : params[key]);
    }

    // 如果dataType为'json'，则设置请求头的内容类型为JSON类型
    if (dataType != null && dataType == 'json') {
        contentType = contentTypeJson;
    }

    // 从localStorage中获取token
    const token = VueCookie.get('token');

    // 设置请求头
    let headers = {
        'Content-Type': contentType, // 设置内容类型
        'X-Requested-With': 'XMLHttpRequest', // 设置请求类型为XMLHttpRequest
        'token': token // 设置token
    };

    // 使用axios实例发送POST请求
    return instance.post(url, formData, {
        onUploadProgress:(event)=>{
            if(config.uploadProgressCallback){
                config.uploadProgressCallback(event);
            }
        },
        headers: headers, // 设置请求头
        showLoading: showLoading, // 是否显示加载动画
        errorCallback: config.errorCallback, // 自定义错误回调
        showError: showError, // 是否显示错误信息
        responseType: responseType // 设置响应类型
    }).catch(error => {
        console.log(error)
        // 捕获请求错误，并根据配置决定是否显示错误信息
        if (error.showError) {
            Message.error(error.msg);
        }
        return null; // 返回null，表示请求失败
    });
};

export default request;
