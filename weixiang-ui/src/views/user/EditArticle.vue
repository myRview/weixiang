<template>
  <div class="edit-article">
    <el-form
      :model="articleForm"
      ref="articleFormRef"
      :rules="rules"
      label-width="80px"
      class="edit-form"
    >
      <el-form-item label="文章标题" prop="title">
        <el-input
          v-model="articleForm.title"
          placeholder="请输入文章标题"
          clearable
          maxLength="100"
          class="form-input"
        />
      </el-form-item>

      <el-form-item label="文章分类" prop="categoryId">
        <el-select
          v-model="articleForm.categoryId"
          placeholder="请选择分类"
          clearable
          class="form-select"
        >
          <el-option
            v-for="category in categoryData"
            :key="category.id"
            :label="category.name"
            :value="category.id"
          ></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="文章标签" prop="tagIds">
        <el-select
          v-model="articleForm.tagIds"
          placeholder="请选择标签"
          multiple
          class="form-select"
        >
          <el-option
            v-for="tag in tagData"
            :key="tag.id"
            :label="tag.name"
            :value="tag.id"
          ></el-option>
        </el-select>
        <div class="form-hint">可多选标签，用于文章分类</div>
      </el-form-item>

      <el-form-item label="文章内容" prop="content">
        <div class="editor-container">
          <el-input
            v-model="articleForm.content"
            type="textarea"
            placeholder="请输入文章内容，支持Markdown格式"
            :rows="18"
            class="content-editor"
          />
          <div class="editor-tip">提示：可使用Markdown语法进行格式化</div>
        </div>
      </el-form-item>

      <el-form-item
        label="发布状态"
        prop="publishStatus"
        class="publish-status"
      >
        <el-radio-group v-model="articleForm.publishStatus">
          <el-radio :label="0" class="radio-option">保存为草稿</el-radio>
          <el-radio :label="1" class="radio-option">立即发布</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>

    <div class="action-buttons">
      <el-button @click="handleCancel" class="cancel-btn">取消</el-button>
      <el-button type="primary" @click="handleSubmit" class="submit-btn">
        <el-icon v-if="submitting" class="loading-icon"><Loading /></el-icon>
        {{ submitting ? "提交中..." : isEdit.value ? "更新文章" : "发布文章" }}
      </el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, reactive, computed } from "vue";
import { useRoute, useRouter } from "vue-router";
import { ElMessage, ElForm, ElLoading } from "element-plus";
import { Loading } from "@element-plus/icons-vue";
import { saveArticle, selectArticleDetail } from "@/api/wenzhangguanli";
import { getCategoryList } from "@/api/wenzhangfenleiguanli";
import { getTagList } from "@/api/wenzhangbiaoqianguanli";

// 路由和导航
const route = useRoute();
const router = useRouter();

// 表单引用
const articleFormRef = ref<InstanceType<typeof ElForm>>();

// 提交状态
const submitting = ref(false);

// 判断是否为编辑模式
const isEdit = computed(() => !!route.query.id);

// 文章表单数据
const articleForm = reactive<API.ArticleVO>({
  id: undefined,
  title: "",
  categoryId: undefined,
  tagIds: [],
  content: "",
  publishStatus: 0,
});

// 验证规则
const rules = {
  title: [
    { required: true, message: "请输入文章标题", trigger: "blur" },
    { max: 100, message: "标题最多100个字符", trigger: "blur" },
  ],
  categoryId: [
    { required: true, message: "请选择文章分类", trigger: "change" },
  ],
  content: [{ required: true, message: "请输入文章内容", trigger: "blur" }],
};

// 分类和标签数据
const categoryData = ref<API.CategoryVO[]>([]);
const tagData = ref<API.TagVO[]>([]);

// 获取分类列表
const getCategoryData = async () => {
  try {
    const res = await getCategoryList({ categoryName: "" });
    if (res.data.code === 200) {
      categoryData.value = res.data.data || [];
    }
  } catch (err) {
    ElMessage.error("获取分类列表失败");
  }
};

// 获取标签列表
const getTagData = async () => {
  try {
    const res = await getTagList({ tagName: "" });
    if (res.data.code === 200) {
      tagData.value = res.data.data || [];
    }
  } catch (err) {
    ElMessage.error("获取标签列表失败");
  }
};

// 获取文章详情（编辑模式）
const getArticleData = async () => {
  if (!isEdit.value) return;
  try {
    const res = await selectArticleDetail({ id: route.query.id });
    if (res.data.code === 200) {
      const article = res.data.data || ({} as API.ArticleVO);
      // 填充表单数据
      articleForm.id = article.id;
      articleForm.title = article.title || "";
      articleForm.categoryId = article.categoryId;
      articleForm.tagIds = article.tagIds || [];
      articleForm.content = article.content || "";
      articleForm.publishStatus = article.publishStatus || 0;
    }
  } catch (err) {
    ElMessage.error("获取文章详情失败");
  }
};

// 提交表单
const handleSubmit = async () => {
  if (!articleFormRef.value) return;
  try {
    submitting.value = true;
    await articleFormRef.value.validate();
    const res = await saveArticle({
      id: articleForm.id,
      title: articleForm.title,
      categoryId: articleForm.categoryId,
      tagIds: articleForm.tagIds,
      content: articleForm.content,
      publishStatus: articleForm.publishStatus,
    });
    if (res.data.code === 200) {
      ElMessage.success(isEdit.value ? "编辑成功" : "创建成功");
      router.push("/my-articles");
    } else {
      ElMessage.error(res.data.message || "操作失败");
    }
  } catch (err) {
    // 表单验证失败或提交出错
    ElMessage.error("请检查表单数据是否正确");
  } finally {
    submitting.value = false;
  }
};

// 取消操作
const handleCancel = () => {
  router.push("/my-articles");
};

// 页面加载时获取数据
onMounted(async () => {
  await Promise.all([getCategoryData(), getTagData()]);
  if (isEdit.value) {
    await getArticleData();
  }
});
</script>

<style scoped>
.edit-article {
  background-color: #f9fafb85;
}

.edit-form {
  padding: 24px;
}

.form-input,
.form-select {
  width: 100%;
  transition: border-color 0.2s ease-in-out, box-shadow 0.2s ease-in-out;
}

.form-input:focus-within,
.form-select:focus-within {
  border-color: #4096ff;
  box-shadow: 0 0 0 2px rgba(64, 150, 255, 0.2);
}

.el-form-item {
  margin-bottom: 20px;
}

.el-form-item__label {
  font-weight: 500;
  color: #374151;
}

.editor-container {
  width: 100%;
  position: relative;
}

.content-editor {
  width: 100%;
  resize: vertical;
  border-radius: 6px;
  transition: all 0.2s ease;
  line-height: 1.6;
}

.content-editor:focus {
  border-color: #4096ff;
  box-shadow: 0 0 0 2px rgba(64, 150, 255, 0.2);
}

.editor-tip {
  position: absolute;
  right: 12px;
  bottom: 12px;
  color: #9ca3af;
  font-size: 12px;
  pointer-events: none;
}

.form-hint {
  color: #909399;
  font-size: 12px;
  margin-top: 5px;
  line-height: 1.5;
}

.publish-status {
  margin-top: 10px;
  margin-bottom: 0;
}

.radio-option {
  margin-right: 20px;
}

.action-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 24px;
  padding: 16px 0;
}

.cancel-btn {
  transition: all 0.2s ease;
}

.cancel-btn:hover {
  background-color: #f3f4f6;
}

.submit-btn {
  transition: all 0.2s ease;
  padding-left: 20px;
  padding-right: 20px;
}

.submit-btn:hover {
  transform: translateY(-1px);
}

.loading-icon {
  margin-right: 6px;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

/* 响应式调整 */
@media (max-width: 768px) {
  .edit-article {
    padding: 16px;
  }

  .page-title h2 {
    font-size: 20px;
  }

  .edit-form {
    padding: 16px;
  }

  .el-form-item {
    margin-bottom: 16px;
  }

  .action-buttons {
    flex-direction: column;
    gap: 8px;
  }

  .cancel-btn,
  .submit-btn {
    width: 100%;
  }

  .radio-option {
    display: block;
    margin-bottom: 10px;
  }
}
</style>
