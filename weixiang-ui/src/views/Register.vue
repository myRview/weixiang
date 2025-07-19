<template>
  <div class="page-background">
    <div class="register-container">
      <div class="header">
        <img src="../assets/logo.png" alt="系统logo" class="logo"/>
        <h1>维享空间</h1>
        <p>创建新账号，开启您的平台之旅</p>
      </div>
      
      <el-form ref="registerForm" :model="form" :rules="rules" label-position="top">
        <!-- <el-form-item label="用户名" prop="username">
          <el-input 
            v-model="form.username" 
            placeholder="请输入用户名"
            autocomplete="off"
          ></el-input>
        </el-form-item> -->
        
        <el-form-item label="账号" prop="account">
          <el-input 
            v-model="form.account" 
            placeholder="请输入账号"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        
        <el-form-item label="密码" prop="password">
          <el-input
            type="password"
            v-model="form.password"
            placeholder="请输入密码"
            autocomplete="off"
            show-password
          ></el-input>
        </el-form-item>
        
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
            type="password"
            v-model="form.confirmPassword"
            placeholder="请再次输入密码"
            autocomplete="off"
            show-password
          ></el-input>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleRegister" class="register-btn">注册</el-button>
        </el-form-item>
      </el-form>
      
      <div class="footer">
        <span>已有账号？</span>
        <el-link type="primary" @click="handleLogin">立即登录</el-link>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { ElMessage, FormInstance } from "element-plus";
import { register } from "@/api/userRegisterController";

// 表单引用
const registerForm = ref<FormInstance>();
const form = ref<API.UserRegisterVO>({
  account: "",
  password: "",
  confirmPassword: "",
});

// 验证规则
const rules = {
  account: [
    { required: true, message: "请输入账号", trigger: "blur" },
    { min: 2, max: 16, message: "长度在 2 到 16 个字符", trigger: "blur" },
  ],
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
    { min: 6, max: 20, message: "长度在 6 到 20 个字符", trigger: "blur" },
  ],
  confirmPassword: [
    { required: true, message: "请再次输入密码", trigger: "blur" },
    {
      validator: (rule: any, value: string, callback: any) => {
        if (value !== form.value.password) {
          callback(new Error("两次输入的密码不一致"));
        } else {
          callback();
        }
      },
      trigger: "blur",
    },
  ],
};

const handleRegister = async () => {
  // 1. 表单校验
  if (!registerForm.value) return;

  const isValid = await registerForm.value.validate().catch(() => false);

  // 2. 校验失败则终止
  if (!isValid) {
    ElMessage.warning("请正确填写表单信息");
    return;
  }

  // 3. 注册逻辑
  try {
    const res = await register({
      account: form.value.account,
      password: form.value.password,
      confirmPassword: form.value.confirmPassword,
    });
    if (res.data.code !== 200) {
      ElMessage.error(res.data.message);
      return;
    }

    ElMessage.success("注册成功");
    // 清空表单
    registerForm.value.resetFields();
    // 跳转到登录页
    handleLogin();
  } catch (error) {
    ElMessage.error("注册失败，请重试");
  }
};

const handleLogin = () => {
  window.location.href = "/login";
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

.register-container {
  width: 100%;
  max-width: 480px;
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
  background: #ffffff;
  text-align: center;
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
  margin-bottom: 24px;
}

:deep(.el-form-item__label) {
  display: block;
  text-align: left;
  padding-bottom: 8px;
  font-weight: 500;
  color: #34495e;
}

.el-input {
  height: 48px;
}

.register-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  border-radius: 8px;
  margin-top: 10px;
  background: #67c23a;
  border: none;
  transition: all 0.3s;
}

.register-btn:hover {
  background: #85ce61;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(103, 194, 58, 0.3);
}

.footer {
  margin-top: 20px;
  font-size: 14px;
  color: #7f8c8d;
}

.el-link {
  vertical-align: baseline;
  margin-left: 5px;
}
</style>