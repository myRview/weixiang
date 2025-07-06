<template>
  <el-header class="app-header">
    <div class="logo">xxxxx系统管理平台</div>
    <div class="user-info">
      <el-icon class="notification-icon">
        <BellFilled />
      </el-icon>
      <el-dropdown trigger="click">
        <div class="user-avatar">
          <el-avatar :src="user.avatar" size="medium"></el-avatar>
          <span class="user-name">{{ user.userName || user.account }}</span>
          <el-icon class="arrow-down"><ArrowDown /></el-icon>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="updatePwd">修改密码</el-dropdown-item>
            <el-dropdown-item @click="handlerLogout" divided
              >退出登录</el-dropdown-item
            >
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>

    <!-- 修改密码对话框 -->
    <el-dialog
      v-model="showPwdDialog"
      title="修改密码"
      width="30%"
      :before-close="handleClose"
    >
      <el-form
        :model="passwordForm"
        :rules="rules"
        ref="passwordFormRef"
        label-width="100px"
      >
        <el-form-item label="新密码" prop="newPassword">
          <el-input
            v-model="passwordForm.newPassword"
            type="password"
            placeholder="请输入新密码"
            show-password
          ></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
            v-model="passwordForm.confirmPassword"
            type="password"
            placeholder="请再次输入新密码"
            show-password
          ></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showPwdDialog = false">取消</el-button>
          <el-button type="primary" @click="handleUpdatePwd">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </el-header>
</template>

<script setup lang="ts">
import { ArrowDown, BellFilled } from "@element-plus/icons-vue";
import { ref, onMounted } from "vue";
import { useLoginUserStore } from "@/store/loginUser";
import { logout } from "@/api/loginController";
import { ElMessage, type FormInstance, type FormRules } from "element-plus";
import { updatePassword } from "@/api/yonghuguanli";

// 获取登录用户存储
const loginUserStore = useLoginUserStore();
// 使用存储中的响应式用户信息
const user = ref<API.UserVO>(loginUserStore.loginUser);

// 退出登录
const handlerLogout = async () => {
  try {
    const res = await logout();
    // 清空本地存储
    localStorage.removeItem("token");
    loginUserStore.clearLoginUser();
    // 跳转到登录页
    window.location.href = "/login";
  } catch (error) {
    console.error("退出登录失败:", error);
  }
};

// 修改密码相关
const showPwdDialog = ref(false);
const passwordFormRef = ref<FormInstance>();
const passwordForm = ref({
  newPassword: "",
  confirmPassword: "",
});

// 密码验证规则
const validatePass = (rule: any, value: string, callback: any) => {
  if (value === "") {
    callback(new Error("请输入密码"));
  } else if (value.length < 6) {
    callback(new Error("密码长度不能少于6位"));
  } else {
    if (passwordForm.value.confirmPassword !== "") {
      passwordFormRef.value?.validateField("confirmPassword");
    }
    callback();
  }
};

const validatePass2 = (rule: any, value: string, callback: any) => {
  if (value === "") {
    callback(new Error("请再次输入密码"));
  } else if (value !== passwordForm.value.newPassword) {
    callback(new Error("两次输入密码不一致!"));
  } else {
    callback();
  }
};

const rules = ref<FormRules>({
  newPassword: [{ required: true, validator: validatePass, trigger: "blur" }],
  confirmPassword: [
    { required: true, validator: validatePass2, trigger: "blur" },
  ],
});

// 打开修改密码对话框
const updatePwd = () => {
  showPwdDialog.value = true;
};

// 处理密码修改
const handleUpdatePwd = () => {
  passwordFormRef.value?.validate(async (valid) => {
    if (valid) {
      try {
        const res = await updatePassword({
          password: passwordForm.value.newPassword,
        });
        if (res.data.code !== 200) {
          ElMessage.error(res.data.message);
          return;
        }
        ElMessage.success("密码修改成功");
        showPwdDialog.value = false;
        passwordFormRef.value?.resetFields();
        
      } catch (error) {
        console.error("密码修改失败:", error);
        ElMessage.error("密码修改失败");
      }
    }
  });
};

// 对话框关闭前的回调
const handleClose = (done: () => void) => {
  passwordFormRef.value?.resetFields();
  done();
};

// 组件挂载时获取最新用户信息
onMounted(async () => {
  await loginUserStore.fetchLoginUser();
  user.value = loginUserStore.loginUser;
});
</script>

<style scoped>
.app-header {
  background-color: #fdfdfd;
  color: #333;
  padding: 0 30px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  height: 64px;
  transition: all 0.3s ease;
}

.logo {
  font-size: 22px;
  font-weight: 600;
  color: #1890ff;
  padding: 0 15px;
  height: 100%;
  display: flex;
  align-items: center;
  letter-spacing: 0.5px;
}

.user-info {
  display: flex;
  align-items: center;
}

.notification-icon {
  margin-right: 16px;
  font-size: 20px;
  color: #909399;
  cursor: pointer;
  transition: color 0.2s ease;
}

.notification-icon:hover {
  color: #1890ff;
}

.user-avatar {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 20px;
  transition: background-color 0.2s ease;
}

.user-avatar:hover {
  background-color: #f5f7fa;
}

.user-name {
  margin: 0 10px;
  font-size: 14px;
  color: #303133;
  font-weight: 500;
}

.arrow-down {
  font-size: 14px;
  color: #909399;
  transition: transform 0.2s ease;
}

.el-dropdown.open .arrow-down {
  transform: rotate(180deg);
}
</style>
