<template>
  <div class="file-upload-container">
    <el-upload
      class="upload-area"
      :accept="acceptTypes"
      :show-file-list="false"
      :before-upload="beforeUpload"
      @change="handleFileChange"
    >
      <div class="upload-area-inner">
        <el-icon class="upload-icon"><UploadFilled /></el-icon>
        <p class="upload-text">点击上传{{ fileTypeText }}文件</p>
      </div>
    </el-upload>

    <div v-if="file" class="file-preview">
      <el-card class="preview-card" shadow="hover">
        <div class="file-preview-content">
          <div v-if="isImage" class="image-preview">
            <el-image
              :src="file.url"
              alt="预览图片"
              fit="contain"
              class="preview-img"
            ></el-image>
          </div>
          <div v-else class="file-info">
            <el-icon :class="getFileIcon()" class="file-icon-large">
              <component :is="getFileIconComponent()"></component>
            </el-icon>
            <div class="file-meta">
              <span class="file-name">{{ file.name }}</span>
              <span class="file-size">{{ formatFileSize(file.size) }}</span>
            </div>
          </div>
        </div>
        <div class="file-actions">
          <el-button
            type="text"
            icon="Delete"
            class="remove-btn"
            @click="removeFile"
          >
            移除
          </el-button>
        </div>
      </el-card>
    </div>

    <el-progress
      v-if="uploading"
      :percentage="progress"
      :status="progress === 100 ? 'success' : 'active'"
      class="upload-progress"
      text-inside
      stroke-width="6"
    ></el-progress>

    <el-alert
      v-if="error"
      :message="error"
      type="error"
      show-icon
      class="upload-error"
    ></el-alert>

    <el-button
      class="upload-btn"
      type="primary"
      :loading="uploading"
      :disabled="!file || uploading"
      @click="uploadFile"
      :icon="UploadFilled"
    >
      {{ uploading ? "上传中..." : "上传文件" }}
    </el-button>
  </div>
</template>

<script setup lang="ts">
import {
  ref,
  computed,
  defineProps,
  defineEmits,
  defineOptions,
} from "vue";
import { upload } from "@/api/wenjianshangchuan";
import {
  UploadFilled,
  Delete,
  Upload,
  Picture,
  VideoCamera,
  Document,
} from "@element-plus/icons-vue";
import {
  ElMessage,
  ElUpload,
  ElProgress,
  ElAlert,
  ElButton,
  ElCard,
  ElImage,
  ElIcon,
} from "element-plus";
import type { UploadFile, UploadFiles } from "element-plus";
// 定义组件名称
defineOptions({
  name: "FileUpload",
});

// 定义props
const props = defineProps({
  fileType: {
    type: String,
    required: true,
    validator: (value: string) => {
      return ["image", "video", "audio", "document"].includes(value);
    },
  },
  // 新增autoUpload属性
  autoUpload: {
    type: Boolean,
    default: false
  },
  // 新增clearAfterUpload属性
  clearAfterUpload: {
    type: Boolean,
    default: true
  }
});

// 定义emits
const emit = defineEmits<{
  (e: "upload-success", data: any): void;
  (e: "upload-fail", error: string): void;
  (e: "file-removed"): void;
}>();

// 响应式变量
const file = ref<File | null>(null);
const uploading = ref(false);
const progress = ref(0);
const error = ref("");

// 计算属性
const fileTypeText = computed(() => {
  const typeMap = {
    image: "图片",
    video: "视频",
    audio: "音频",
    document: "文档",
  };
  return typeMap[props.fileType] || "文件";
});

const acceptTypes = computed(() => {
  const typeMap = {
    image: "image/*",
    video: "video/*",
    audio: "audio/*",
    document: ".doc,.docx,.pdf,.txt,.xls,.xlsx,.ppt,.pptx",
  };
  return typeMap[props.fileType] || "*";
});

const isImage = computed(() => props.fileType === "image");

// 方法定义
const selectFile = () => {
  if (fileInput.value) {
    fileInput.value.click();
  }
};

const beforeUpload = (rawFile: File) => {
  file.value = rawFile;
  error.value = "";
  // 如果是图片，创建预览URL
  if (isImage.value && file.value) {
    file.value.url = URL.createObjectURL(file.value);
  }
  
  // 新增：如果设置了自动上传，则立即触发上传
  if (props.autoUpload) {
    uploadFile();
  }
  
  // 返回false阻止自动上传
  return false;
};

const handleFileChange = (uploadFile: UploadFile, uploadFiles: UploadFiles) => {
  // 处理上传状态变化
  if (uploadFile.status === "error") {
    error.value = "文件上传失败，请重试";
    emit("upload-fail", error.value);
  }
};

const removeFile = () => {
  if (file.value && (file.value as File & { url?: string }).url) {
    URL.revokeObjectURL((file.value as File & { url?: string }).url!);
  }
  file.value = null;
  emit("file-removed");
};

const getFileIconComponent = (): Component => {
  const iconMap = {
    image: Picture,
    video: VideoCamera,
    document: Document,
  };
  return iconMap[props.fileType] || Document;
};

const getFileIcon = () => {
  return "file-icon";
};

const formatFileSize = (bytes: number) => {
  if (bytes < 1024) return bytes + " B";
  else if (bytes < 1048576) return (bytes / 1024).toFixed(1) + " KB";
  else return (bytes / 1048576).toFixed(1) + " MB";
};

const uploadFile = async () => {
  if (!file.value) return;

  uploading.value = true;
  progress.value = 0;
  error.value = "";

  try {
    const formData = new FormData();
    formData.append("file", file.value);

    const response = await upload({ category: props.fileType }, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
      onUploadProgress: (e) => {
        if (e.total) {
          progress.value = Math.round((e.loaded * 100) / e.total);
        }
      },
    });

    if (response.data.code === 200) {
      emit("upload-success", response.data.data);
      // 上传成功后根据设置清除文件
      if (props.clearAfterUpload) {
        removeFile();
      }
    } else {
      error.value = response.data.message || "上传失败，请重试";
      emit("upload-fail", error.value);
    }
  } catch (err) {
    error.value = "上传失败，请检查网络连接";
    emit("upload-fail", error.value);
  } finally {
    uploading.value = false;
    progress.value = 0;
  }
};
</script>

<style scoped>
.file-upload-container {
  max-width: 500px;
  margin: 20px auto;
}

.upload-area {
  border: 2px dashed #ccc;
  border-radius: 8px;
  padding: 40px 20px;
  text-align: center;
  cursor: pointer;
  transition: border-color 0.3s;
}

.upload-area:hover {
  border-color: #007bff;
}

.upload-icon {
  font-size: 48px;
  color: #ccc;
  margin-bottom: 10px;
}

.upload-text {
  color: #666;
  margin: 0;
}

.file-input {
  display: none;
}

.file-preview {
  margin: 15px 0;
  padding: 15px;
  border: 1px solid #eee;
  border-radius: 4px;
  position: relative;
  display: flex;
  align-items: center;
}

.image-preview img {
  max-width: 150px;
  max-height: 100px;
  border-radius: 4px;
}

.file-info {
  display: flex;
  align-items: center;
  flex: 1;
}

.file-icon {
  font-size: 24px;
  margin-right: 10px;
  color: #666;
}

.file-name {
  margin-right: 15px;
  flex: 1;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.file-size {
  color: #999;
  margin-right: 15px;
}

.remove-btn {
  position: absolute;
  top: 5px;
  right: 5px;
  background: #f56c6c;
  color: white;
  border: none;
  border-radius: 50%;
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 12px;
}

.upload-progress {
  height: 8px;
  background-color: #eee;
  border-radius: 4px;
  overflow: hidden;
  margin: 15px 0;
}

.progress-bar {
  height: 100%;
  background-color: #007bff;
  transition: width 0.3s;
}

.progress-text {
  display: block;
  text-align: center;
  margin-top: 5px;
  color: #666;
}

.upload-error {
  color: #f56c6c;
  margin: 15px 0;
  text-align: center;
}

.upload-btn {
  width: 100%;
  padding: 10px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s;
}

.upload-btn:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}
</style>