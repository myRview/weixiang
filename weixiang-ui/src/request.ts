import axios from "axios";

export const baseURL = process.env.VUE_APP_API_BASE_URL || "http://localhost:8016/api";

const myAxios = axios.create({
  baseURL: baseURL,
  timeout: 60000,
  withCredentials: true,
});

// 请求拦截器
myAxios.interceptors.request.use(
  (config) => {
    // 从本地存储获取 token
    const token = localStorage.getItem('token');
    // 如果 token 存在，添加到请求头
    if (token) {
      //自定义请求头
      config.headers['token']=token;
    }
    
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 响应拦截器（处理401未授权）
myAxios.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    if (error.response?.status === 401) {
      // 清除无效 token
      localStorage.removeItem('token');
      // 跳转到登录页
      window.location.href = "/login";
    }
    return Promise.reject(error);
  }
);

export default myAxios;