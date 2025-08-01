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
        />
      </el-form-item>

      <el-form-item label="文章分类" prop="categoryId">
        <el-select
          v-model="articleForm.categoryId"
          placeholder="请选择分类"
          clearable
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
          <!-- 这里使用简单的文本域作为编辑器，实际项目中可以替换为富文本编辑器 -->
          <el-input
            v-model="articleForm.content"
            type="textarea"
            placeholder="请输入文章内容"
            :rows="15"
          />
        </div>
      </el-form-item>

      <el-form-item label="发布状态" prop="publishStatus">
        <el-radio-group v-model="articleForm.publishStatus">
          <el-radio :label="0">保存为草稿</el-radio>
          <el-radio :label="1">立即发布</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <div class="action-buttons">
      <el-button @click="handleCancel">取消</el-button>
      <el-button type="primary" @click="handleSubmit">提交</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, reactive, computed } from "vue";
import { useRoute, useRouter } from "vue-router";
import { ElMessage, ElForm } from "element-plus";
import { saveArticle, selectArticleDetail } from "@/api/wenzhangguanli";
import { getCategoryList } from "@/api/wenzhangfenleiguanli";
import { getTagList } from "@/api/wenzhangbiaoqianguanli";

// 路由和导航
const route = useRoute();
const router = useRouter();

// 表单引用
const articleFormRef = ref<InstanceType<typeof ElForm>>();

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
  padding: 20px;
  max-width: 1000px;
  margin: 0 auto;
}

.edit-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.edit-form {
  margin-top: 20px;
}

.editor-container {
  width: 100%;
}

.action-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}

.form-hint {
  color: #909399;
  font-size: 12px;
  margin-top: 5px;
}

@media (max-width: 768px) {
  .edit-article {
    padding: 10px;
  }
}
</style>
