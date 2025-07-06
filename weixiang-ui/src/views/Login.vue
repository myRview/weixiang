<template>
  <div class="page-background">
    <div class="login-container">
      <div class="header">
        <img src="../assets/logo.png" alt="系统logo" class="logo"/>
        <h1>xxx应用系统</h1>
        <p class="sub-title">欢迎回来，请登录您的账号</p>
      </div>
      
      <el-form ref="loginForm" :model="form" :rules="rules" label-position="top">
        <el-form-item label="请输入用户名" prop="account">
          <el-input 
            v-model="form.account" 
            placeholder="请输入用户名"
          ></el-input>
        </el-form-item>
        <el-form-item label="请输入密码" prop="password">
          <el-input
            type="password"
            v-model="form.password"
            placeholder="请输入密码"
            show-password
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" class="login-btn">登录</el-button>
        </el-form-item>
      </el-form>
      
      <div class="footer">
        <span>还没有账号？</span>
        <el-link type="primary" @click="handleRegister">立即注册</el-link>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { ElMessage } from "element-plus";
import { login } from "@/api/loginController";
import { useLoginUserStore } from '../store/loginUser'; 
const form = ref<API.UserLoginVO>({ account: "", password: "" });
const rules = {
  account: [{ required: true, message: "请输入账号", trigger: "blur" }],
  password: [{ required: true, message: "请输入密码", trigger: "blur" }],
};

const handleLogin = async () => {
  // 登录逻辑
  const res = await login(form.value);
  if (res.data.code !== 200) {
    ElMessage.error(res.data.message);
    return;
  }
   // 保存 token 到本地存储
   const token = res.data.data; 
  localStorage.setItem('token', token);
  const loginUserStore = useLoginUserStore();
  loginUserStore.fetchLoginUser();
  ElMessage.success("登录成功");
  window.location.href = "/";
};

const handleRegister = () => {
  // 跳转注册页
  window.location.href = "/register";
};
</script>

<style scoped>
.logo {
  height: 60px;
  margin-bottom: 8px;
}
.page-background {
  height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  background-image:url('../assets/background.png') ;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
}

.login-container {
  width: 100%;
  max-width: 420px;
  padding: 48px 40px;
  border-radius: 16px;
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.08);
  background: rgba(255, 255, 255, 0.98);
  text-align: center;
  backdrop-filter: blur(8px);
}

.header {
  margin-bottom: 30px;
}

.header h1 {
  font-size: 28px;
  color: #2c3e50;
  margin: 16px 0 8px;
  font-weight: 600;
}

.header p.sub-title {
  font-size: 15px;
  color: #667587;
  margin-bottom: 24px;
}

.el-form-item {
  margin-bottom: 28px;
}

.el-form-item:last-child {
  margin-bottom: 10px;
}

:deep(.el-form-item__label) {
  display: block;
  text-align: left;
  padding-bottom: 12px;
  font-weight: 600;
  color: #2c3e50;
  font-size: 15px;
}

.el-input {
  height: 48px;
}

.login-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  border-radius: 8px;
  margin-top: 10px;
  background: #409eff;
  border: none;
  transition: all 0.3s;
}

.login-btn:hover {
  background: #66b1ff;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

.footer {
  margin-top: 30px;
  font-size: 14px;
  color: #7f8c8d;
  display: flex;
  justify-content: center;
  align-items: center;
}

.el-link {
  vertical-align: baseline;
  margin-left: 5px;
}
</style>