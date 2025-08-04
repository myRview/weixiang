<template>
  <el-header class="app-header">
    <div class="logo">维享空间</div>
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
            <el-dropdown-item @click="updatePwd">
              <el-icon><Lock /></el-icon>
              <span>修改密码</span>
            </el-dropdown-item>
            <el-dropdown-item @click="goToHome">
              <el-icon><HomeFilled /></el-icon>
              <span>我的主页</span>
            </el-dropdown-item>
            <el-dropdown-item @click="goToMemberCenter">
              <el-icon><Postcard /></el-icon>
              <span>会员中心</span>
            </el-dropdown-item>
            <el-dropdown-item @click="handlerLogout" divided>
              <el-icon><SwitchButton /></el-icon>
              <span>退出登录</span>
            </el-dropdown-item>
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
import {
  ArrowDown,
  BellFilled,
  Lock,
  HomeFilled,
  SwitchButton,
  Postcard,
} from "@element-plus/icons-vue";
import { ref, onMounted, computed } from "vue";
import { useLoginUserStore } from "@/store/loginUser";
import { logout } from "@/api/loginController";
import { ElMessage, type FormInstance, type FormRules } from "element-plus";
import { getUserById, updatePassword } from "@/api/yonghuguanli";
import { useRouter } from "vue-router";

// 获取登录用户存储
const loginUserStore = useLoginUserStore();
// 使用存储中的响应式用户信息
const user = ref<API.UserVO>(loginUserStore.loginUser);
// 初始化路由器
const router = useRouter();

// 计算角色标签
const roleLabel = computed(() => {
  if (!user.value.roleCode) return "";
  const roles: Record<string, string> = {
    admin: "管理员",
    user: "普通用户",
    guest: "访客",
  };
  return roles[user.value.roleCode] || user.value.roleCode;
});

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

// 跳转到会员中心
const goToMemberCenter = () => {
  if (user.value && user.value.id) {
    router.push(
      "member-center",
    );
  } else {
    ElMessage.error("无法获取用户信息，无法进入会员中心");
  }
};
// 跳转到用户主页
const goToHome = () => {
  if (user.value && user.value.id) {
    router.push({
      path: "/user-home",
      query: { id: user.value.id },
    });
  }
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

const getUserInfo = async () => {
  try {
    const res = await getUserById({ id: loginUserStore.loginUser.id });
    if (res.data.code === 200 && res.data.data) {
      user.value = res.data.data;
      loginUserStore.setLoginUser(res.data.data);
    }
  } catch (error) {
    console.error("获取用户信息失败", error);
  }
};

// 组件挂载时获取最新用户信息
onMounted(async () => {
  await loginUserStore.fetchLoginUser();
  await getUserInfo();
});
</script>

<style scoped>
.app-header {
  background: linear-gradient(to right, #ffffff, #f8fbff);
  color: #333;
  padding: 0 30px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  height: 64px;
  transition: all 0.3s ease;
  border-bottom: 1px solid #eef1f6;
}

.logo {
  font-size: 22px;
  font-weight: 700;
  background: linear-gradient(135deg, #4a6cf7 0%, #3050e3 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  padding: 0 15px;
  height: 100%;
  display: flex;
  align-items: center;
  letter-spacing: 0.5px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.user-info {
  display: flex;
  align-items: center;
  gap: 16px;
}

.notification-icon {
  font-size: 22px;
  color: #7a7f9a;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  padding: 8px;
  border-radius: 50%;
}

.user-avatar {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 6px 12px;
  border-radius: 24px;
  transition: all 0.3s ease;
  background: rgba(255, 255, 255, 0.8);
  border: 1px solid #ebeef5;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.user-name {
  margin: 0 10px;
  font-size: 14px;
  color: #303133;
  font-weight: 500;
  max-width: 120px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.user-role-tag {
  background: linear-gradient(135deg, #4a6cf7 0%, #3050e3 100%);
  color: white;
  font-size: 12px;
  padding: 2px 8px;
  border-radius: 10px;
  margin-left: 8px;
  font-weight: 500;
}

.arrow-down {
  font-size: 14px;
  color: #909399;
  transition: transform 0.3s ease;
  margin-left: 4px;
}

.el-dropdown.open .arrow-down {
  transform: rotate(180deg);
}

.el-dropdown-menu {
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  animation: fadeIn 0.3s ease;
}

.el-dropdown-item {
  padding: 10px 16px;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: all 0.2s ease;

  &:hover {
    background-color: #f0f5ff;
    color: #4a6cf7;
  }
}

.el-dropdown-item .el-icon {
  font-size: 16px;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 响应式调整 */
@media (max-width: 768px) {
  .app-header {
    padding: 0 15px;
  }

  .logo {
    font-size: 18px;
    padding: 0 8px;
  }

  .user-name {
    display: none;
  }

  .user-role-tag {
    display: none;
  }

  .notification-icon {
    margin-right: 8px;
  }
}
</style>
