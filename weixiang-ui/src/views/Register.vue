<template>
  <div class="page-background">
    <div class="register-container">
      <div class="header">
        <img src="../assets/logo.png" alt="系统logo" class="logo" />
        <h1>维享空间</h1>
        <p>创建新账号，开启您的平台之旅</p>
      </div>

      <!-- 注册方式切换按钮 -->
      <div class="register-tabs">
        <el-button
          :class="{ 'active-tab': registerType === 'account' }"
          @click="switchRegisterType('account')"
        >
          账号注册
        </el-button>
        <el-button
          :class="{ 'active-tab': registerType === 'phone' }"
          @click="switchRegisterType('phone')"
        >
          手机号注册
        </el-button>
        <el-button
          :class="{ 'active-tab': registerType === 'email' }"
          @click="switchRegisterType('email')"
        >
          邮箱注册
        </el-button>
      </div>

      <el-form
        ref="registerForm"
        :model="form"
        :rules="rules"
        label-position="top"
      >
        <!-- 账号注册字段 -->
        <el-form-item
          v-if="registerType === 'account'"
          label="账号"
          prop="account"
        >
          <el-input
            v-model="form.account"
            placeholder="请输入账号"
            autocomplete="off"
          ></el-input>
        </el-form-item>

        <!-- 手机号注册字段 -->
        <el-form-item
          v-if="registerType === 'phone'"
          label="手机号"
          prop="phone"
        >
          <el-input
            v-model="form.phone"
            placeholder="请输入手机号"
            autocomplete="off"
          ></el-input>
        </el-form-item>

        <el-form-item
          v-if="registerType === 'phone'"
          label="验证码"
          prop="code"
        >
          <el-row :gutter="10">
            <el-col :span="16">
              <el-input
                v-model="form.code"
                placeholder="请输入验证码"
                autocomplete="off"
              ></el-input>
            </el-col>
            <el-col :span="8">
              <el-button
                type="default"
                :disabled="countDown > 0"
                @click="sendPhoneCode"
                class="code-btn"
              >
                {{ countDown > 0 ? `${countDown}秒后重新发送` : "获取验证码" }}
              </el-button>
            </el-col>
          </el-row>
        </el-form-item>

        <!-- 手机号注册的密码是可选的 -->
        <el-form-item
          v-if="registerType === 'phone'"
          label="密码 (可选)"
          prop="password"
        >
          <el-input
            type="password"
            v-model="form.password"
            placeholder="请输入密码"
            autocomplete="off"
            show-password
          ></el-input>
        </el-form-item>

        <!-- 邮箱注册字段 -->
        <el-form-item v-if="registerType === 'email'" label="邮箱" prop="email">
          <el-input
            v-model="form.email"
            placeholder="请输入邮箱"
            autocomplete="off"
          ></el-input>
        </el-form-item>

        <el-form-item
          v-if="registerType === 'email'"
          label="验证码"
          prop="code"
        >
          <el-row :gutter="10">
            <el-col :span="16">
              <el-input
                v-model="form.code"
                placeholder="请输入验证码"
                autocomplete="off"
              ></el-input>
            </el-col>
            <el-col :span="8">
              <el-button
                type="default"
                :disabled="countDown > 0"
                @click="sendEmailCode"
                class="code-btn"
              >
                {{ countDown > 0 ? `${countDown}秒后重新发送` : "获取验证码" }}
              </el-button>
            </el-col>
          </el-row>
        </el-form-item>

        <el-form-item
          v-if="registerType === 'email' || registerType === 'account'"
          label="密码"
          prop="password"
        >
          <el-input
            type="password"
            v-model="form.password"
            placeholder="请输入密码"
            autocomplete="off"
            show-password
          ></el-input>
        </el-form-item>

        <el-form-item
          v-if="registerType === 'email' || registerType === 'account'"
          label="确认密码"
          prop="confirmPassword"
        >
          <el-input
            type="password"
            v-model="form.confirmPassword"
            placeholder="请再次输入密码"
            autocomplete="off"
            show-password
          ></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleRegister" class="register-btn"
            >注册</el-button
          >
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
import { ref, computed, onUnmounted } from "vue";
import { ElMessage, FormInstance } from "element-plus";
import { register, sendCode } from "@/api/userRegisterController";

// 表单引用
const registerForm = ref<FormInstance>();

// 注册类型：account(账号)、phone(手机号)、email(邮箱)
const registerType = ref<"account" | "phone" | "email">("account");

// 表单数据
const form = ref<
  API.UserRegisterVO & { phone?: string; email?: string; code?: string }
>({
  account: "",
  password: "",
  confirmPassword: "",
  phone: "",
  email: "",
  code: "",
});

// 验证码倒计时
const countDown = ref(0);
const timer = ref<number | null>(null);

// 切换注册类型
const switchRegisterType = (type: "account" | "phone" | "email") => {
  registerType.value = type;
  // 重置当前表单字段
  if (registerForm.value) {
    registerForm.value.resetFields();
  }
  // 重置倒计时
  countDown.value = 0;
  if (timer.value) {
    clearInterval(timer.value);
    timer.value = null;
  }
};

// 发送手机验证码
const sendPhoneCode = async () => {
  if (!form.value.phone) {
    ElMessage.warning("请先输入手机号");
    return;
  }

  // 简单验证手机号格式
  const phonePattern = /^1[3-9]\d{9}$/;
  if (!phonePattern.test(form.value.phone)) {
    ElMessage.warning("请输入正确的手机号格式");
    return;
  }

  try {
    // 调用发送验证码API
    const res = await sendCode({ phone: form.value.phone });
    if (res.data.code !== 200) {
      ElMessage.error(res.data.message);
      return;
    }
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

// 发送邮箱验证码
const sendEmailCode = async () => {
  if (!form.value.email) {
    ElMessage.warning("请先输入邮箱");
    return;
  }

  // 简单验证邮箱格式
  const emailPattern = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
  if (!emailPattern.test(form.value.email)) {
    ElMessage.warning("请输入正确的邮箱格式");
    return;
  }

  try {
    // 调用发送验证码API
    const res = await sendCode({ phone: form.value.phone });
    if (res.data.code !== 200) {
      ElMessage.error(res.data.message);
      return;
    }
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
  // 基础规则
  const baseRules: any = {};

  // 账号注册规则
  if (registerType.value === "account") {
    baseRules.account = [
      { required: true, message: "请输入账号", trigger: "blur" },
      { min: 2, max: 16, message: "长度在 2 到 16 个字符", trigger: "blur" },
    ];
    baseRules.password = [
      { required: true, message: "请输入密码", trigger: "blur" },
      { min: 6, max: 20, message: "长度在 6 到 20 个字符", trigger: "blur" },
    ];
    baseRules.confirmPassword = [
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
    ];
  }
  // 手机号注册规则
  else if (registerType.value === "phone") {
    baseRules.phone = [
      { required: true, message: "请输入手机号", trigger: "blur" },
      {
        pattern: /^1[3-9]\d{9}$/,
        message: "请输入正确的手机号格式",
        trigger: "blur",
      },
    ];
    baseRules.code = [
      { required: true, message: "请输入验证码", trigger: "blur" },
      { min: 4, max: 6, message: "验证码长度为4-6个字符", trigger: "blur" },
    ];
    // 手机号注册密码是可选的
    baseRules.password = [
      { min: 6, max: 20, message: "长度在 6 到 20 个字符", trigger: "blur" },
    ];
    // 如果填写了密码，则需要确认密码
    baseRules.confirmPassword = [
      {
        validator: (rule: any, value: string, callback: any) => {
          if (form.value.password && value !== form.value.password) {
            callback(new Error("两次输入的密码不一致"));
          } else {
            callback();
          }
        },
        trigger: "blur",
      },
    ];
  }
  // 邮箱注册规则
  else if (registerType.value === "email") {
    baseRules.email = [
      { required: true, message: "请输入邮箱", trigger: "blur" },
      {
        pattern: /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/,
        message: "请输入正确的邮箱格式",
        trigger: "blur",
      },
    ];
    baseRules.code = [
      { required: true, message: "请输入验证码", trigger: "blur" },
      { min: 4, max: 6, message: "验证码长度为4-6个字符", trigger: "blur" },
    ];
    baseRules.password = [
      { required: true, message: "请输入密码", trigger: "blur" },
      { min: 6, max: 20, message: "长度在 6 到 20 个字符", trigger: "blur" },
    ];
    baseRules.confirmPassword = [
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
    ];
  }

  return baseRules;
});

const handleRegister = async () => {
  // 1. 表单校验
  if (!registerForm.value) return;

  const isValid = await registerForm.value.validate().catch(() => false);

  // 2. 校验失败则终止
  if (!isValid) {
    ElMessage.warning("请正确填写表单信息");
    return;
  }

  // 3. 准备注册参数
  const registerParams: any = {};

  // 根据注册类型添加对应的参数
  if (registerType.value === "account") {
    registerParams.account = form.value.account;
    registerParams.password = form.value.password;
    registerParams.confirmPassword = form.value.confirmPassword;
  } else if (registerType.value === "phone") {
    registerParams.phone = form.value.phone;
    registerParams.code = form.value.code;
    // 密码是可选的
    if (form.value.password) {
      registerParams.password = form.value.password;
      registerParams.confirmPassword = form.value.confirmPassword;
    }
  } else if (registerType.value === "email") {
    registerParams.email = form.value.email;
    registerParams.code = form.value.code;
    registerParams.password = form.value.password;
    registerParams.confirmPassword = form.value.confirmPassword;
  }

  // 4. 注册逻辑
  try {
    const res = await register(registerParams);
    if (res.data.code !== 200) {
      ElMessage.error(res.data.message);
      return;
    }

    ElMessage.success("注册成功");
    // 清空表单
    registerForm.value.resetFields();
    // 重置倒计时
    countDown.value = 0;
    if (timer.value) {
      clearInterval(timer.value);
      timer.value = null;
    }
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
  background-image: url("../assets/background.png");
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
  margin-bottom: 20px;
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

/* 注册方式切换按钮样式 */
.register-tabs {
  display: flex;
  justify-content: center;
  margin-bottom: 30px;
  gap: 10px;
}

.register-tabs .el-button {
  flex: 1;
  background-color: #f5f7fa;
  color: #34495e;
}

.register-tabs .el-button.active-tab {
  background-color: #67c23a;
  color: white;
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

.code-btn {
  width: 100%;
  height: 48px;
  font-size: 14px;
}

.optional-hint {
  font-size: 12px;
  color: #909399;
  margin-top: 6px;
  text-align: left;
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
