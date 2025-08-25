<template>
  <div class="edit-container">
    <el-form :model="user" class="user-form" label-width="80px" ref="formRef">
      <el-form-item label="头像" prop="avatar">
        <div class="avatar-upload-container">
          <FileUpload
            ref="avatarUploadRef"
            class="avatar-upload"
            fileType="image"
            :autoUpload="true"
            :clearAfterUpload="false"
            @upload-success="handleAvatarSuccess"
            @upload-error="handleAvatarError"
          />
          <!-- 头像预览容器 -->
          <div class="avatar-preview-container">
            <img
              v-if="user.avatar"
              :src="user?.avatar ? `${baseURL}${user?.avatar}` : ''"
              class="avatar"
              :class="{ 'avatar-loading': avatarUploading }"
            />
            <div v-else class="avatar-placeholder">
              <el-icon class="placeholder-icon"><User /></el-icon>
              <div class="placeholder-text">头像预览</div>
            </div>
          </div>
          <!-- 头像下方上传按钮 -->
          <el-button
            type="text"
            class="avatar-upload-btn"
            @click="triggerAvatarUpload"
          >
            点击上传头像
          </el-button>
        </div>
      </el-form-item>
      <!-- 昵称 -->
      <el-form-item label="昵称" prop="userName">
        <el-input
          v-model="user.userName"
          placeholder="请输入昵称"
          @input="handleInput"
        />
      </el-form-item>
      <!-- 性别 -->
      <el-form-item label="性别" prop="gender">
        <el-radio-group v-model="user.gender" class="gender-radio-group">
          <el-radio :label="0">女</el-radio>
          <el-radio :label="1">男</el-radio>
        </el-radio-group>
      </el-form-item>
      <!-- 简介 -->
      <el-form-item label="简介" prop="expandVo.bio">
        <el-input
          type="textarea"
          v-model="user.expandVo.bio"
          :rows="4"
          placeholder="请输入个人简介"
          :maxlength="200"
          show-word-limit
        />
      </el-form-item>
      <!-- 生日 -->
      <el-form-item label="生日" prop="expandVo.birthday">
        <el-date-picker
          v-model="user.expandVo.birthday"
          type="date"
          placeholder="选择生日"
          :disabled-date="disableFutureDate"
          value-format="YYYY-MM-DD"
        />
      </el-form-item>
      <!-- 手机号 -->
      <el-form-item label="手机" prop="phone">
        <div class="phone-container">
          <el-input
            v-model="formattedPhone"
            placeholder="请输入手机号"
            maxlength="11"
            :disabled="!phoneEditable && user.phone"
            v-if="!phoneDialogVisible && (phoneEditable || user.phone)"
            @input="handlePhoneInput"
          />
          <el-button
            type="text"
            class="bind-phone-btn"
            @click="openPhoneDialog"
            v-else-if="!phoneDialogVisible && !user.phone"
          >
            点击绑定
          </el-button>
          <el-button
            type="text"
            class="edit-phone-btn"
            @click="openPhoneDialog"
            v-if="!phoneDialogVisible && user.phone && !phoneEditable"
          >
            修改
          </el-button>
        </div>
      </el-form-item>
      <!-- 邮箱 -->
      <el-form-item label="邮箱" prop="email">
        <div class="email-container">
          <el-input
            v-model="formattedEmail"
            placeholder="请输入邮箱"
            type="email"
            :disabled="!emailEditable && user.email"
            v-if="!emailDialogVisible && (emailEditable || user.email)"
          />
          <el-button
            type="text"
            class="bind-email-btn"
            @click="openEmailDialog"
            v-else-if="!emailDialogVisible && !user.email"
          >
            点击绑定
          </el-button>
          <el-button
            type="text"
            class="edit-email-btn"
            @click="openEmailDialog"
            v-if="!emailDialogVisible && user.email && !emailEditable"
          >
            修改
          </el-button>
        </div>
      </el-form-item>
      <!-- 地区（优化回显和展示） -->
      <el-form-item label="地区" prop="expandVo.area">
        <el-row :gutter="12" class="area-select-row">
          <!-- 省份选择 -->
          <el-col :span="8">
            <el-select
              v-model="user.expandVo.province"
              placeholder="选择省份"
              @change="handleProvinceChange"
              class="area-select"
            >
              <el-option
                v-for="item in provinces"
                :key="item.code"
                :label="item.name"
                :value="item.code"
              />
            </el-select>
          </el-col>
          <!-- 城市选择 -->
          <el-col :span="7">
            <el-select
              v-model="user.expandVo.city"
              :placeholder="user.expandVo.province ? '选择城市' : '请先选省份'"
              :disabled="!user.expandVo.province"
              @change="handleCityChange"
              class="area-select"
            >
              <el-option
                v-for="item in cities"
                :key="item.code"
                :label="item.name"
                :value="item.code"
              />
            </el-select>
          </el-col>
          <!-- 区县选择 -->
          <el-col :span="7">
            <el-select
              v-model="user.expandVo.district"
              :placeholder="user.expandVo.city ? '选择区县' : '请先选城市'"
              :disabled="!user.expandVo.city"
              class="area-select"
            >
              <el-option
                v-for="item in counties"
                :key="item.code"
                :label="item.name"
                :value="item.code"
              />
            </el-select>
          </el-col>
        </el-row>
        <!-- 地区预览文本（直观显示完整地区） -->
        <div class="area-preview">当前选择：{{ formattedArea }}</div>
      </el-form-item>
      <!-- 提交/取消按钮 -->
      <el-form-item>
        <el-button
          type="primary"
          @click="submitEdit"
          :loading="saveLoading"
          class="save-btn"
        >
          <el-icon v-if="saveLoading"><Loading /></el-icon>
          <span v-else>保存</span>
        </el-button>
        <el-button @click="cancelEdit" class="cancel-btn">取消</el-button>
      </el-form-item>
    </el-form>
    <!-- 手机号绑定/修改弹框 -->
    <el-dialog
      title="绑定手机号"
      v-model="phoneDialogVisible"
      :close-on-click-modal="false"
      width="30%"
    >
      <el-form
        ref="phoneFormRef"
        :model="phoneForm"
        :rules="phoneRules"
        label-width="80px"
      >
        <el-form-item label="手机号" prop="phone">
          <el-input
            v-model="phoneForm.phone"
            placeholder="请输入手机号"
            maxlength="11"
            @input="handleDialogPhoneInput"
          />
        </el-form-item>
        <el-form-item label="验证码" prop="code">
          <div class="code-container">
            <el-input
              v-model="phoneForm.code"
              placeholder="请输入验证码"
              maxlength="6"
            />
            <el-button
              type="text"
              @click="sendPhoneCode"
              :disabled="phoneCodeDisabled"
              class="send-code-btn"
            >
              {{ phoneCodeText }}
            </el-button>
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="phoneDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitPhone">提交</el-button>
      </template>
    </el-dialog>
    <!-- 邮箱绑定/修改弹框 -->
    <el-dialog
      title="绑定邮箱"
      v-model="emailDialogVisible"
      :close-on-click-modal="false"
      width="30%"
    >
      <el-form
        ref="emailFormRef"
        :model="emailForm"
        :rules="emailRules"
        label-width="80px"
      >
        <el-form-item label="邮箱" prop="email">
          <el-input
            v-model="emailForm.email"
            placeholder="请输入邮箱"
            type="email"
          />
        </el-form-item>
        <el-form-item label="验证码" prop="code">
          <div class="code-container">
            <el-input
              v-model="emailForm.code"
              placeholder="请输入验证码"
              maxlength="6"
            />
            <el-button
              type="text"
              @click="sendEmailCode"
              :disabled="emailCodeDisabled"
              class="send-code-btn"
            >
              {{ emailCodeText }}
            </el-button>
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="emailDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEmail">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { useRouter, useRoute } from "vue-router";
import { bindPhoneAndEmail, editUser, getUserById } from "@/api/yonghuguanli";
import { ElMessage, ElForm, ElDialog } from "element-plus";
import { onMounted, ref, computed, nextTick } from "vue";
import { useLoginUserStore } from "@/store/loginUser";
import FileUpload from "@/components/FileUpload.vue";
import { User, Loading } from "@element-plus/icons-vue";
import dayjs from "dayjs";
import { sendCode } from "@/api/userRegisterController";
import { baseURL } from "@/request";
const router = useRouter();
const formRef = ref<InstanceType<typeof ElForm>>();
const phoneFormRef = ref<InstanceType<typeof ElForm>>();
const emailFormRef = ref<InstanceType<typeof ElForm>>();
const avatarUploadRef = ref<InstanceType<typeof FileUpload>>();

// 加载状态
const saveLoading = ref(false);
const avatarUploading = ref(false);

// 手机号/邮箱编辑状态
const phoneEditable = ref(false);
const phoneDialogVisible = ref(false);
const emailEditable = ref(false);
const emailDialogVisible = ref(false);

// 表单数据
const phoneForm = ref({
  phone: "",
  code: "",
});
const emailForm = ref({
  email: "",
  code: "",
});

// 验证码配置（1分钟倒计时）
const phoneCodeDisabled = ref(false);
const phoneCodeText = ref("获取验证码");
const emailCodeDisabled = ref(false);
const emailCodeText = ref("获取验证码");
const codeCountdown = 60;

// 用户信息结构
const user = ref<API.UserVO>({
  account: "",
  userName: "",
  phone: "",
  email: "",
  gender: 0,
  avatar: "",
  expandVo: {
    address: "",
    bio: "",
    province: "",
    city: "",
    district: "",
    birthday: "", // 补充生日字段，避免undefined
  },
});

// 省市区假数据
const provinces = ref([
  { code: "110000", name: "北京市" },
  { code: "120000", name: "天津市" },
  { code: "130000", name: "河北省" },
  { code: "310000", name: "上海市" },
  { code: "440100", name: "广东省" },
]);
const cities = ref([]); // 根据省份动态变化
const counties = ref([]); // 根据市动态变化

// 省份选择变化
const handleProvinceChange = (provinceCode: string) => {
  // 模拟根据省份获取市数据
  cities.value =
    provinceCode === "110000"
      ? [{ code: "110100", name: "北京市" }]
      : provinceCode === "310000"
      ? [{ code: "310100", name: "上海市" }]
      : provinceCode === "440100"
      ? [
          { code: "440101", name: "广州市" },
          { code: "440300", name: "深圳市" },
        ]
      : provinceCode === "130000"
      ? [
          { code: "130100", name: "石家庄市" },
          { code: "130200", name: "唐山市" },
        ]
      : [];
  // 清空下级选择
  user.value.expandVo.city = "";
  counties.value = [];
};

// 市选择变化
const handleCityChange = (cityCode: string) => {
  // 模拟根据市获取县数据
  counties.value =
    cityCode === "440101"
      ? [
          { code: "440103", name: "荔湾区" },
          { code: "440104", name: "越秀区" },
          { code: "440106", name: "天河区" },
        ]
      : cityCode === "440300"
      ? [
          { code: "440303", name: "罗湖区" },
          { code: "440304", name: "福田区" },
          { code: "440305", name: "南山区" },
        ]
      : cityCode === "110100"
      ? [
          { code: "110101", name: "东城区" },
          { code: "110102", name: "西城区" },
          { code: "110105", name: "朝阳区" },
        ]
      : cityCode === "310100"
      ? [
          { code: "310101", name: "黄浦区" },
          { code: "310104", name: "徐汇区" },
          { code: "310105", name: "长宁区" },
        ]
      : [];
  // 清空下级选择
  user.value.expandVo.district = "";
};

const loginUserStore = useLoginUserStore();

// 邮箱脱敏处理
const formattedEmail = computed({
  get() {
    if (!user.value.email) return "";
    return emailEditable.value
      ? user.value.email
      : user.value.email.replace(/^(.{2}).*@(.*)$/, "$1***@$2");
  },
  set(val) {
    user.value.email = val;
  },
});

// 手机号脱敏处理
const formattedPhone = computed({
  get() {
    if (!user.value.phone) return "";
    return phoneEditable.value
      ? user.value.phone
      : user.value.phone.replace(/(\d{3})\d{4}(\d{4})/, "$1****$2");
  },
  set(val) {
    user.value.phone = val;
  },
});

// 【新增】根据code获取地区名称（用于预览文本）
const getAreaName = (code: string, list: { code: string; name: string }[]) => {
  const item = list.find((item) => item.code === code);
  return item ? item.name : "";
};

// 【新增】格式化地区预览文本（如：北京市 - 北京市 - 朝阳区）
const formattedArea = computed(() => {
  const { province, city, district } = user.value.expandVo;
  // 根据code获取对应的名称
  const provinceName = getAreaName(province, provinces.value);
  const cityName = getAreaName(city, cities.value);
  const districtName = getAreaName(district, counties.value);
  // 过滤空值并拼接
  const areaParts = [provinceName, cityName, districtName].filter(Boolean);
  return areaParts.length > 0 ? areaParts.join(" - ") : "未选择地区";
});

// 手机号表单验证规则
const phoneRules = ref({
  phone: [
    { required: true, message: "请输入手机号", trigger: "blur" },
    {
      pattern: /^1[3-9]\d{9}$/,
      message: "请输入正确的手机号",
      trigger: "blur",
    },
  ],
  code: [
    { required: true, message: "请输入验证码", trigger: "blur" },
    { pattern: /^\d{6}$/, message: "验证码为6位数字", trigger: "blur" },
  ],
});

// 邮箱表单验证规则
const emailRules = ref({
  email: [
    { required: true, message: "请输入邮箱", trigger: "blur" },
    { type: "email", message: "请输入正确的邮箱格式", trigger: "blur" },
  ],
  code: [
    { required: true, message: "请输入验证码", trigger: "blur" },
    { pattern: /^\d{6}$/, message: "验证码为6位数字", trigger: "blur" },
  ],
});

// 获取用户信息（新增回显处理逻辑）
const getUserInfo = async () => {
  try {
    const res = await getUserById({ id: loginUserStore.loginUser.id });
    if (res.data.code !== 200) return;
    // 赋值用户信息
    user.value = res.data.data;
    if (!user.value.expandVo) {
      user.value.expandVo = {
        address: "",
        bio: "",
        province: "",
        city: "",
        district: "",
        birthday: "",
      };
    }
    const { province, city, district } = user.value.expandVo;

    // 【关键】处理省市区回显：联动加载对应选项
    if (province) {
      // 1. 加载省份对应的城市
      handleProvinceChange(province);
      // 等待城市列表加载完成后，设置城市并加载区县
      await nextTick();
      if (city) {
        // 2. 赋值城市并加载区县
        user.value.expandVo.city = city;
        handleCityChange(city);
        // 等待区县列表加载完成后，设置区县
        await nextTick();
        if (district) {
          user.value.expandVo.district = district;
        }
      }
    }
  } catch (error) {
    console.error("获取用户信息失败", error);
    ElMessage.error("获取用户信息失败");
  }
};

// 头像上传成功处理
const handleAvatarSuccess = (data: any) => {
  avatarUploading.value = true;
  setTimeout(() => {
    user.value.avatar = data;
    avatarUploading.value = false;
    ElMessage.success("头像上传成功");
  }, 500);
};

// 头像上传失败处理
const handleAvatarError = () => {
  avatarUploading.value = false;
  ElMessage.error("头像上传失败，请重试");
};

// 触发头像上传（按钮点击时调用）
const triggerAvatarUpload = () => {
  if (!avatarUploadRef.value) return;
  const uploadInput =
    avatarUploadRef.value.$el.querySelector('input[type="file"]');
  if (uploadInput) uploadInput.click();
};

// 手机号输入处理（过滤非数字）
const handlePhoneInput = (value: string) => {
  user.value.phone = value.replace(/[^\d]/g, "");
};

// 弹框中手机号输入处理
const handleDialogPhoneInput = (value: string) => {
  phoneForm.value.phone = value.replace(/[^\d]/g, "");
};

// 打开手机号弹框
const openPhoneDialog = () => {
  phoneForm.value.phone = user.value.phone || "";
  phoneForm.value.code = "";
  phoneDialogVisible.value = true;
};

// 打开邮箱弹框
const openEmailDialog = () => {
  emailForm.value.email = user.value.email || "";
  emailForm.value.code = "";
  emailDialogVisible.value = true;
};

// 发送手机验证码
const sendPhoneCode = async () => {
  if (!phoneForm.value.phone) {
    ElMessage.warning("请输入手机号");
    return;
  }
  if (!/^1[3-9]\d{9}$/.test(phoneForm.value.phone)) {
    ElMessage.warning("请输入正确的手机号");
    return;
  }
  try {
    const res = await sendCode({ phone: phoneForm.value.phone });
    if (res.data.code !== 200) {
      ElMessage.error(res.data.message);
      return;
    }
    ElMessage.success("验证码发送成功");
    // 开始倒计时
    startPhoneCodeCountdown();
  } catch (error) {
    ElMessage.error("验证码发送失败，请重试");
  }
};

// 发送邮箱验证码
const sendEmailCode = async () => {
  if (!emailForm.value.email) {
    ElMessage.warning("请输入邮箱");
    return;
  }
  const emailReg = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!emailReg.test(emailForm.value.email)) {
    ElMessage.warning("请输入正确的邮箱");
    return;
  }
  try {
    const res = await sendCode({ email: emailForm.value.email });
    if (res.data.code !== 200) {
      ElMessage.error(res.data.message);
      return;
    }
    ElMessage.success("验证码发送成功");
    // 开始倒计时
    startEmailCodeCountdown();
  } catch (error) {
    ElMessage.error("验证码发送失败，请重试");
  }
};

// 手机验证码1分钟倒计时（结束显示“重复发送”）
const startPhoneCodeCountdown = () => {
  let count = codeCountdown;
  phoneCodeDisabled.value = true;
  phoneCodeText.value = `${count}s后重新发送`;
  const timer = setInterval(() => {
    count--;
    phoneCodeText.value = `${count}s后重新发送`;
    if (count <= 0) {
      clearInterval(timer);
      phoneCodeDisabled.value = false;
      phoneCodeText.value = "重复发送";
    }
  }, 1000);
};

// 邮箱验证码1分钟倒计时（结束显示“重复发送”）
const startEmailCodeCountdown = () => {
  let count = codeCountdown;
  emailCodeDisabled.value = true;
  emailCodeText.value = `${count}s后重新发送`;
  const timer = setInterval(() => {
    count--;
    emailCodeText.value = `${count}s后重新发送`;
    if (count <= 0) {
      clearInterval(timer);
      emailCodeDisabled.value = false;
      emailCodeText.value = "重复发送";
    }
  }, 1000);
};

// 提交手机号绑定/修改
const submitPhone = async () => {
  if (!phoneFormRef.value) return;
  try {
    const res = await bindPhoneAndEmail({
      userId: user.value.id,
      phone: phoneForm.value.phone,
      code: phoneForm.value.code,
    });
    if (res.data.code !== 200) {
      ElMessage.error(res.data.message);
      return;
    }
    ElMessage.success("手机号绑定成功");
    // 绑定成功后更新用户信息并关闭弹框
    user.value.phone = phoneForm.value.phone;
    phoneDialogVisible.value = false;
  } catch (error) {
    console.error("提交手机号失败", error);
    ElMessage.error("提交手机号失败，请重试");
  }
};

// 提交邮箱绑定/修改
const submitEmail = async () => {
  if (!emailFormRef.value) return;
  try {
    const res = await bindPhoneAndEmail({
      userId: user.value.id,
      email: emailForm.value.email,
      code: emailForm.value.code,
    });
    if (res.data.code !== 200) {
      ElMessage.error(res.data.message);
      return;
    }
    ElMessage.success("邮箱绑定成功");
    user.value.email = emailForm.value.email;
    emailDialogVisible.value = false;
  } catch (error) {
    console.error("提交邮箱失败", error);
    ElMessage.error("提交邮箱失败，请重试");
  }
};

// 禁止选择未来日期（生日）
const disableFutureDate = (time: Date) => {
  return dayjs(time).isAfter(dayjs());
};

// 提交编辑
const submitEdit = async () => {
  saveLoading.value = true;
  try {
    const updateParams: any = {
      userId: user.value.id,
      avatar: user.value.avatar,
      userName: user.value.userName,
      gender: user.value.gender,
      bio: user.value.expandVo?.bio || "",
      address: user.value.expandVo?.address || "",
      birthday: user.value.expandVo?.birthday,
      province: user.value.expandVo?.province || "",
      city: user.value.expandVo?.city || "",
      district: user.value.expandVo?.district || "",
    };
    const res = await editUser({ ...updateParams });
    if (res.data.code !== 200) {
      ElMessage.error(res.data.message);
      return;
    }
    ElMessage.success("编辑成功");
  } catch (error) {
    ElMessage.error("编辑失败，请重试");
  } finally {
    saveLoading.value = false;
    phoneEditable.value = false;
    emailEditable.value = false;
  }
};

// 取消编辑
const cancelEdit = () => {
  router.back();
};

const handleInput = () => {};

// 页面挂载时获取用户信息
onMounted(() => {
  getUserInfo();
});
</script>
<style scoped>
.edit-container {
  padding: 20px;
  margin: 0 auto;
}
.user-form {
  padding: 15px;
  border-radius: 8px;
  background: #fff;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.05);
}

/* 头像上传区域样式 */
.avatar-upload-container {
  position: relative;
  display: inline-block;
  margin-bottom: 15px;
  text-align: center;
}
.avatar-upload {
  position: absolute;
  top: 0;
  left: 0;
  width: 60px;
  height: 60px;
  opacity: 0;
  cursor: pointer;
  z-index: 2;
}
.avatar-preview-container {
  position: relative;
  width: 60px;
  height: 60px;
  margin: 0 auto;
  z-index: 1;
}
.avatar-upload-btn {
  margin-top: 8px;
  color: #1890ff;
  font-size: 14px;
  padding: 0;
  height: auto;
}
.avatar {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #f5f5f5;
  transition: all 0.3s ease;
  cursor: pointer;
}
.avatar:hover {
  transform: scale(1.03);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}
.avatar-loading::after {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background-color: rgba(255, 255, 255, 0.7);
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='24' height='24' viewBox='0 0 24 24' fill='none' stroke='%231890ff' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3E%3Cline x1='12' y1='2' x2='12' y2='6'%3E%3C/line%3E%3Cline x1='12' y1='18' x2='12' y2='22'%3E%3C/line%3E%3Cline x1='4.93' y1='4.93' x2='7.76' y2='7.76'%3E%3C/line%3E%3Cline x1='16.24' y1='16.24' x2='19.07' y2='19.07'%3E%3C/line%3E%3Cline x1='2' y1='12' x2='6' y2='12'%3E%3C/line%3E%3Cline x1='18' y1='12' x2='22' y2='12'%3E%3C/line%3E%3Cline x1='4.93' y1='19.07' x2='7.76' y2='16.24'%3E%3C/line%3E%3Cline x1='16.24' y1='7.76' x2='19.07' y2='4.93'%3E%3C/line%3E%3C/svg%3E");
  background-repeat: no-repeat;
  background-position: center;
  background-size: 24px;
}
.avatar-placeholder {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background-color: #f5f5f5;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #999;
  border: 2px dashed #ddd;
  transition: all 0.3s ease;
}
.avatar-placeholder:hover {
  background-color: #f0f0f0;
  border-color: #aaa;
}
.placeholder-icon {
  font-size: 16px;
  margin-bottom: 4px;
}
.placeholder-text {
  font-size: 8px;
}

/* 性别单选组样式 */
.gender-radio-group {
  display: flex;
  gap: 20px;
  margin-top: 5px;
}
.gender-radio-group .el-radio {
  font-size: 14px;
  color: #333;
}

/* 手机号/邮箱容器样式 */
.phone-container,
.email-container {
  display: flex;
  align-items: center;
  gap: 10px;
}
.edit-phone-btn,
.bind-phone-btn,
.bind-email-btn,
.edit-email-btn {
  color: #1890ff;
  padding: 0;
  height: auto;
  font-size: 14px;
}

/* 验证码容器样式 */
.code-container {
  display: flex;
  gap: 10px;
}
.send-code-btn {
  color: #1890ff;
  white-space: nowrap;
}

/* 地区选择区域样式（新增/优化） */
.area-select-row {
  margin-bottom: 8px;
  align-items: center;
}
.area-select {
  width: 100%;
  border-radius: 4px;
  font-size: 14px;
  height: 32px; /* 统一高度，优化对齐 */
}
.area-preview {
  margin-top: 4px;
  font-size: 14px;
  color: #666;
  padding-left: 2px;
  line-height: 1.5;
}

/* 文本域样式 */
.el-textarea {
  --el-textarea-min-height: 60px;
}

/* 按钮样式 */
.save-btn,
.cancel-btn {
  min-width: 90px;
  margin-right: 10px;
  font-size: 14px;
}

/* 操作提示样式 */
.operation-toast {
  position: fixed;
  top: 20px;
  right: 20px;
  padding: 12px 20px;
  border-radius: 4px;
  color: white;
  font-size: 14px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  z-index: 1000;
  animation: toastSlideIn 0.3s ease, toastFadeOut 0.3s ease 2.7s forwards;
}
.success {
  background-color: #52c41a;
}
.error {
  background-color: #f5222d;
}

/* 动画效果 */
@keyframes toastSlideIn {
  from {
    transform: translateX(100%);
    opacity: 0;
  }
  to {
    transform: translateX(0);
    opacity: 1;
  }
}
@keyframes toastFadeOut {
  from {
    opacity: 1;
  }
  to {
    opacity: 0;
  }
}

/* 响应式调整 */
@media (max-width: 768px) {
  .edit-container {
    padding: 10px;
  }
  .user-form {
    padding: 10px;
  }
  .phone-container,
  .email-container,
  .code-container {
    flex-direction: column;
    align-items: flex-start;
    gap: 5px;
  }
  .operation-toast {
    left: 20px;
    right: 20px;
    text-align: center;
  }
  .el-dialog {
    width: 90% !important;
  }
  /* 地区选择响应式优化 */
  .area-select-row .el-col {
    flex: 0 0 100% !important;
    max-width: 100% !important;
    margin-bottom: 8px;
  }
  .area-select-row .el-col:last-child {
    margin-bottom: 0;
  }
}
</style>
