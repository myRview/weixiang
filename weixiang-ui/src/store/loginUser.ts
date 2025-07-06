import { defineStore } from "pinia"; 
import { ref } from "vue"; 
import { getLoginUser } from "@/api/yonghuguanli"; 


export const useLoginUserStore = defineStore("loginUser", () => { 
  // 从本地存储初始化用户信息 
  const initLoginUser = () => { 
    const storedUser = sessionStorage.getItem('loginUser'); 
    if (storedUser) { 
      return JSON.parse(storedUser); 
    } 
    return { 
      userName: "未登录", 
    }; 
  }; 

  // 定义一个响应式变量 loginUser，初始值为一个包含 userName 属性的对象，初始值为 "未登录" 
  const loginUser = ref<API.UserVO>(initLoginUser()); 
  
  // 定义一个异步函数 fetchLoginUser，用于获取登录用户信息 
  async function fetchLoginUser() { 
    // 调用 getLoginUser 函数获取当前用户信息 
    const res = await getLoginUser(); 
    if (res.data.code === 200 && res.data.data) { 
      loginUser.value = res.data.data; 
      // 保存到本地存储 
      sessionStorage.setItem('loginUser', JSON.stringify(res.data.data)); 
    } else { 
      // 清除本地存储 
      sessionStorage.removeItem('loginUser'); 
    } 
  } 

  // 定义一个函数 setLoginUser，用于设置登录用户信息 
  function setLoginUser(newLoginUser: API.UserVO) { 
    // 将 loginUser 的值设置为传入的新登录用户信息 
    loginUser.value = newLoginUser; 
    // 保存到本地存储 
    sessionStorage.setItem('loginUser', JSON.stringify(loginUser.value)); 
  } 

  // 清除用户信息 
  function clearLoginUser() { 
    loginUser.value = { 
      userName: "未登录", 
    }; 
    sessionStorage.removeItem('loginUser'); 
  } 

  // 返回 loginUser、setLoginUser 和 fetchLoginUser，以便在组件中使用 
  return { loginUser, setLoginUser, fetchLoginUser, clearLoginUser }; 
}, { 
  persist: { 
    key: 'loginUser', 
    storage: sessionStorage 
  } 
});