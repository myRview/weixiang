<template>
  <div class="page-background">
    <div class="login-container">
      <div class="header">
        <img src="../assets/logo.png" alt="系统logo" class="logo" />
        <h1>维享空间</h1>
        <p class="sub-title">欢迎回来，请登录您的账号</p>
      </div>

      <!-- 登录方式切换按钮 -->
      <div class="login-tabs">
        <el-button
          :class="{ 'active-tab': loginType === 'account' }"
          @click="switchLoginType('account')"
        >
          账号密码登录
        </el-button>
        <el-button
          :class="{ 'active-tab': loginType === 'phone' }"
          @click="switchLoginType('phone')"
        >
          短信验证码登录
        </el-button>
        <el-button
          :class="{ 'active-tab': loginType === 'email' }"
          @click="switchLoginType('email')"
        >
          邮箱密码登录
        </el-button>
      </div>

      <el-form
        ref="loginForm"
        :model="form"
        :rules="rules"
        label-position="top"
      >
        <!-- 账号密码登录 -->
        <el-form-item v-if="loginType === 'account'" label="请输入您的账号" prop="account">
          <el-input
            v-model="form.account"
            placeholder="请输入您的账号"
          ></el-input>
        </el-form-item>

        <!-- 短信验证码登录 -->
        <el-form-item v-if="loginType === 'phone'" label="请输入手机号" prop="phone">
          <el-input
            v-model="form.phone"
            placeholder="请输入手机号"
            type="number"
          ></el-input>
        </el-form-item>
        <el-form-item v-if="loginType === 'phone'" label="请输入验证码" prop="code">
          <el-row :gutter="10">
            <el-col :span="16">
              <el-input
                v-model="form.code"
                placeholder="请输入验证码"
              ></el-input>
            </el-col>
            <el-col :span="8">
              <el-button
                type="default"
                :disabled="countDown > 0"
                @click="sendCode"
                class="code-btn"
              >
                {{ countDown > 0 ? `${countDown}秒后重新发送` : '获取验证码' }}
              </el-button>
            </el-col>
          </el-row>
        </el-form-item>

        <!-- 邮箱密码登录 -->
        <el-form-item v-if="loginType === 'email'" label="请输入邮箱" prop="email">
          <el-input
            v-model="form.email"
            placeholder="请输入邮箱"
          ></el-input>
        </el-form-item>

        <!-- 密码字段（账号密码登录和邮箱密码登录需要） -->
        <el-form-item v-if="loginType === 'account' || loginType === 'email'" label="请输入密码" prop="password">
          <el-input
            type="password"
            v-model="form.password"
            placeholder="请输入密码"
            show-password
          ></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleLogin" class="login-btn"
            >登录</el-button
          >
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
import { ref, computed, onUnmounted } from "vue";
import { ElMessage } from "element-plus";
import { login } from "@/api/loginController";
import { useLoginUserStore } from "../store/loginUser";
import { useRouter } from "vue-router";
// 假设存在发送验证码的API
// import { sendVerificationCode } from "@/api/verification";

const router = useRouter();
// 登录类型：account(账号密码)、phone(短信验证码)、email(邮箱密码)
const loginType = ref<'account' | 'phone' | 'email'>('account');
// 表单数据
const form = ref<API.UserLoginVO & { phone?: string; email?: string; code?: string }>({
  account: "",
  password: "",
  phone: "",
  email: "",
  code: ""
});
// 验证码倒计时
const countDown = ref(0);
const timer = ref<number | null>(null);

// 切换登录类型
const switchLoginType = (type: 'account' | 'phone' | 'email') => {
  loginType.value = type;
  // 重置表单
  const loginForm = ref<HTMLFormElement | null>(null);
  if (loginForm.value) {
    loginForm.value.reset();
  }
  // 重置倒计时
  countDown.value = 0;
  if (timer.value) {
    clearInterval(timer.value);
    timer.value = null;
  }
};

// 发送验证码
const sendCode = async () => {
  if (!form.value.phone) {
    ElMessage.warning("请先输入手机号");
    return;
  }

  // 验证手机号格式
  const phonePattern = /^1[3-9]\d{9}$/;
  if (!phonePattern.test(form.value.phone)) {
    ElMessage.warning("请输入正确的手机号格式");
    return;
  }

  try {
    // 调用发送验证码API
    // await sendVerificationCode({ phone: form.value.phone });
    ElMessage.success("验证码发送成功");

    // 开始倒计时
    countDown.value = 60;
    timer.value = window.setInterval(() => {
      if (countDown.value > 0) {
        countDown.value--;
      } else {
        if (timer.value) {
          clearInterval(timer.value);
          timer.value = null;
        }
      }
    }, 1000);
  } catch (error) {
    ElMessage.error("验证码发送失败，请重试");
  }
};

// 组件卸载时清除定时器
onUnmounted(() => {
  if (timer.value) {
    clearInterval(timer.value);
    timer.value = null;
  }
});

// 动态验证规则
const rules = computed(() => {
  const rules: any = {};

  if (loginType.value === 'account') {
    rules.account = [
      { required: true, message: "请输入账号", trigger: "blur" }
    ];
    rules.password = [
      { required: true, message: "请输入密码", trigger: "blur" }
    ];
  } else if (loginType.value === 'phone') {
    rules.phone = [
      { required: true, message: "请输入手机号", trigger: "blur" },
      { pattern: /^1[3-9]\d{9}$/, message: "请输入正确的手机号格式", trigger: "blur" }
    ];
    rules.code = [
      { required: true, message: "请输入验证码", trigger: "blur" },
      { min: 4, max: 6, message: "验证码长度为4-6个字符", trigger: "blur" }
    ];
  } else if (loginType.value === 'email') {
    rules.email = [
      { required: true, message: "请输入邮箱", trigger: "blur" },
      { pattern: /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/, message: "请输入正确的邮箱格式", trigger: "blur" }
    ];
    rules.password = [
      { required: true, message: "请输入密码", trigger: "blur" }
    ];
  }

  return rules;
});

const handleLogin = async () => {
  try {
    // 准备登录参数
    const loginParams: any = {};

    if (loginType.value === 'account') {
      loginParams.account = form.value.account;
      loginParams.password = form.value.password;
    } else if (loginType.value === 'phone') {
      loginParams.phone = form.value.phone;
      loginParams.code = form.value.code;
    } else if (loginType.value === 'email') {
      loginParams.email = form.value.email;
      loginParams.password = form.value.password;
    }

    const res = await login(loginParams);
    if (res.data.code !== 200) {
      ElMessage.error(res.data.message);
      return;
    }
    
    const token = res.data.data;
    localStorage.setItem("token", token);
    const loginUserStore = useLoginUserStore();
    
    // 获取用户信息
    await loginUserStore.fetchLoginUser();
    
    // 使用路由跳转代替window.location.href，确保WebSocket能正确初始化
    router.push("/");
  } catch (error) {
    ElMessage.error("登录失败，请重试");
  }
};

const handleRegister = () => {
  // 跳转注册页
  router.push("/register");
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
  background-image: url("../assets/background.png");
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

.login-tabs {
  display: flex;
  justify-content: space-between;
  margin-bottom: 24px;
}

.login-tabs .el-button {
  flex: 1;
  margin: 0 5px;
}

.login-tabs .active-tab {
  background-color: #409eff;
  color: white;
}

.code-btn {
  width: 100%;
  height: 48px;
  font-size: 14px;
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