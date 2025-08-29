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
      <!-- 地区选择区域 -->
      <el-form-item label="地区" prop="expandVo.area">
        <el-row :gutter="12" class="area-select-row">
          <el-col :span="8">
            <el-select
              v-model="user.expandVo.province"
              placeholder="选择省份"
              @change="handleProvinceChange"
              class="area-select"
              ref="provinceSelect"
            >
              <el-option
                v-for="item in provinces"
                :key="item.provinceCode"
                :label="item.provinceName"
                :value="item.provinceCode"
              />
            </el-select>
          </el-col>
          <el-col :span="8">
            <el-select
              v-model="user.expandVo.city"
              :placeholder="user.expandVo.province ? '选择城市' : '请先选省份'"
              :disabled="!user.expandVo.province"
              @change="handleCityChange"
              class="area-select"
              ref="citySelect"
            >
              <el-option
                v-for="item in cities"
                :key="item.cityCode"
                :label="item.cityName"
                :value="item.cityCode"
              />
            </el-select>
          </el-col>
          <el-col :span="7">
            <el-select
              v-model="user.expandVo.district"
              :placeholder="user.expandVo.city ? '选择区县' : '请先选城市'"
              :disabled="!user.expandVo.city"
              class="area-select"
              ref="districtSelect"
            >
              <el-option
                v-for="item in counties"
                :key="item.countyCode"
                :label="item.countyName"
                :value="item.countyCode"
              />
            </el-select>
          </el-col>
        </el-row>
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
import { useRouter } from "vue-router";
import { bindPhoneAndEmail, editUser, getUserById } from "@/api/yonghuguanli";
import { ElMessage, ElForm, ElDialog, ElSelect } from "element-plus";
import { onMounted, ref, computed, nextTick, watch } from "vue";
import { useLoginUserStore } from "@/store/loginUser";
import FileUpload from "@/components/FileUpload.vue";
import dayjs from "dayjs";
import { sendCode } from "@/api/userRegisterController";
import { baseURL } from "@/request";
import { getAllProvince } from "@/api/shengfenguanli";

const router = useRouter();
const formRef = ref<InstanceType<typeof ElForm>>();
const phoneFormRef = ref<InstanceType<typeof ElForm>>();
const emailFormRef = ref<InstanceType<typeof ElForm>>();
const avatarUploadRef = ref<InstanceType<typeof FileUpload>>();

// 省市区选择器引用
const provinceSelect = ref<InstanceType<typeof ElSelect>>();
const citySelect = ref<InstanceType<typeof ElSelect>>();
const districtSelect = ref<InstanceType<typeof ElSelect>>();

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
    birthday: "",
  },
});

// 省市区数据
const provinces = ref<API.ProvinceVO[]>([]);
const cities = ref<API.CityVO[]>([]);
const counties = ref<API.CountyVO[]>([]);

// 获取省份数据 - 优化错误处理
const selectProvince = async () => {
  try {
    const res = await getAllProvince();
    if (res.data.code === 200 && res.data.data) {
      provinces.value = res.data.data;
    }
  } catch (error) {
    ElMessage.error("获取地区数据失败，请刷新页面重试");
    provinces.value = [];
  }
};

const handleProvinceChange = async (provinceCode: string) => {
  // 清空下级选择
  user.value.expandVo.city = "";
  user.value.expandVo.district = "";
  cities.value = [];
  counties.value = [];

  try {
    // 查找选中省份的ID
    const selectedProvince = provinces.value.find(
      (p) => p.provinceCode === provinceCode
    );
    if (selectedProvince) {
      cities.value = selectedProvince.cityVOS || [];
    }
  } catch (error) {
    cities.value = [];
    if (citySelect.value) citySelect.value.loading = false;
  }
};

// 城市选择变化 - 优化数据加载和回显
const handleCityChange = async (cityCode: string) => {
  // 清空下级选择
  user.value.expandVo.district = "";
  counties.value = [];
  try {
    // 从当前城市列表查找
    const currentCity = cities.value.find((c) => c.cityCode === cityCode);
    if (currentCity) {
      counties.value = currentCity.countyVOS || [];
    }
  } catch (error) {}
};

// 初始化地区数据并处理回显 - 重点修复回显问题
const initAreaData = async () => {
  try {
    await selectProvince();
    const { province, city, district } = user.value.expandVo;
    if (province && provinces.value.length > 0) {
      const provinceExists = provinces.value.some(
        (p) => p.provinceCode === province
      );
      if (!provinceExists) {
        console.warn(`保存的省份代码 ${province} 不在省份列表中，可能已过期`);
        return;
      }
      user.value.expandVo.province = province;
      await handleProvinceChange(province);
      await nextTick();
      if (city && cities.value.length > 0) {
        // 验证城市是否存在
        const cityExists = cities.value.some((c) => c.cityCode === city);
        if (!cityExists) {
          console.warn(`保存的城市代码 ${city} 不在城市列表中，可能已过期`);
          return;
        }
        user.value.expandVo.city = city;
        await nextTick();
        await handleCityChange(city);
        await nextTick();
        if (district && counties.value.length > 0) {
          const districtExists = counties.value.some(
            (d) => d.countyCode === district
          );
          if (!districtExists) {
            console.warn(
              `保存的区县代码 ${district} 不在区县列表中，可能已过期`
            );
          } else {
            user.value.expandVo.district = district;
          }
        }
      }
    }
  } catch (error) {
    console.error("地区数据初始化失败:", error);
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
    startEmailCodeCountdown();
  } catch (error) {
    ElMessage.error("验证码发送失败，请重试");
  }
};

// 手机验证码倒计时
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

// 邮箱验证码倒计时
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
  }
};

// 取消编辑
const cancelEdit = () => {
  router.back();
};

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
// 监听数据变化，确保选择器正确更新
watch(
  () => user.value.expandVo.province,
  (newVal) => {
    if (newVal && provinceSelect.value) {
      // 强制刷新选择器
      provinceSelect.value.blur();
      provinceSelect.value.focus();
    }
    // 省份变化后更新地址
    updateAddress();
  }
);

watch(
  () => user.value.expandVo.city,
  (newVal) => {
    if (newVal && citySelect.value) {
      citySelect.value.blur();
      citySelect.value.focus();
    }
    // 城市变化后更新地址
    updateAddress();
  }
);

watch(
  () => user.value.expandVo.district,
  (newVal) => {
    if (newVal && districtSelect.value) {
      districtSelect.value.blur();
      districtSelect.value.focus();
    }
    // 区县变化后更新地址
    updateAddress();
  }
);

// 获取用户信息
const getUserInfo = async () => {
  try {
    const loginUserStore = useLoginUserStore();
    const res = await getUserById({ id: loginUserStore.loginUser.id });
    if (res.data.code !== 200) return;
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
  } catch (error) {
    ElMessage.error("获取用户信息失败");
  }
};

// 计算并更新详细地址
const updateAddress = () => {
  let addressParts = [];
  
  // 根据省份代码查找省份名称
  if (user.value.expandVo.province) {
    const selectedProvince = provinces.value.find(
      (p) => p.provinceCode === user.value.expandVo.province
    );
    if (selectedProvince) {
      addressParts.push(selectedProvince.provinceName);
    }
  }
  
  // 根据城市代码查找城市名称
  if (user.value.expandVo.city) {
    const selectedCity = cities.value.find(
      (c) => c.cityCode === user.value.expandVo.city
    );
    if (selectedCity) {
      addressParts.push(selectedCity.cityName);
    }
  }
  
  // 根据区县代码查找区县名称
  if (user.value.expandVo.district) {
    const selectedDistrict = counties.value.find(
      (d) => d.countyCode === user.value.expandVo.district
    );
    if (selectedDistrict) {
      addressParts.push(selectedDistrict.countyName);
    }
  }
  
  // 更新详细地址
  user.value.expandVo.address = addressParts.join("");
};

const handleInput = () => {};

onMounted(async () => {
  await getUserInfo();
  // 等待用户信息加载完成后再初始化地区数据
  await nextTick();
  await initAreaData();
  // 初始化地区数据后更新地址
  await nextTick();
  updateAddress();
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

/* 地区选择区域样式 */
.area-select-row {
  margin-bottom: 8px;
  align-items: center;
}
.area-select {
  width: 100%;
  border-radius: 4px;
  font-size: 14px;
  height: 32px;
}
.area-preview {
  margin-top: 4px;
  font-size: 14px;
  color: #666;
  padding-left: 2px;
  line-height: 1.5;
}

/* 为选择器添加加载状态样式 */
.el-select__input.is-loading {
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='%23999' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3E%3Cline x1='12' y1='2' x2='12' y2='6'%3E%3C/line%3E%3Cline x1='12' y1='18' x2='12' y2='22'%3E%3C/line%3E%3Cline x1='4.93' y1='4.93' x2='7.76' y2='7.76'%3E%3C/line%3E%3Cline x1='16.24' y1='16.24' x2='19.07' y2='19.07'%3E%3C/line%3E%3Cline x1='2' y1='12' x2='6' y2='12'%3E%3C/line%3E%3Cline x1='18' y1='12' x2='22' y2='12'%3E%3C/line%3E%3Cline x1='4.93' y1='19.07' x2='7.76' y2='16.24'%3E%3C/line%3E%3Cline x1='16.24' y1='7.76' x2='19.07' y2='4.93'%3E%3C/line%3E%3C/svg%3E");
  background-repeat: no-repeat;
  background-position: right 10px center;
  background-size: 16px;
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

/* 地区选择框宽度设置 */
.area-select {
  width: 120px !important;
}
</style>
