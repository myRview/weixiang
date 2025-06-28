import axios from "axios";

const myAxios = axios.create({
  baseURL: "http://localhost:8015/api",
  timeout: 60000,
  withCredentials: true,
  // headers: {'X-Custom-Header': 'foobar'}
  //将token放入请求头
  headers: {
    user_token: sessionStorage.getItem("token"),
  },
});

// 添加请求拦截器
myAxios.interceptors.request
  .use
  //   function (config) {
  //     const token = sessionStorage.getItem("token");
  //     if (token) {
  //       config.headers["user_token"] = token;
  //     }
  //     return config;
  //   },
  //   function (error) {
  //     // 对请求错误做些什么
  //     return Promise.reject(error);
  //   }
  ();

// 添加响应拦截器
myAxios.interceptors.response
  .use
  //   function (response) {
  //     const currentPath = window.location.pathname;
  //     const isLoginOrRegister = currentPath === '/login' || currentPath === '/register';
  //     if (response.data.code === 40100 && !isLoginOrRegister) {
  //       // 跳转到登录页
  //       window.location.href = "/login";
  //     }
  //     return response;
  //   },
  //   function (error) {
  //     // 超出 2xx 范围的状态码都会触发该函数。
  //     // 对响应错误做点什么
  //     return Promise.reject(error);
  //   }
  ();
export default myAxios;

// function fetchLoginUser() {
//   throw new Error("Function not implemented.");
// }
