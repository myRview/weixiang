<template>
  <div class="province-manager">
    <el-card class="region-tree-card">
      <template #header>
        <div class="card-header">
          <span>地区数据管理</span>
          <div class="header-right">
            <el-button
              type="primary"
              size="small"
              icon="Plus"
              @click="handleAddProvince"
            >
              添加省份
            </el-button>
            <el-button
              type="danger"
              size="small"
              icon="Delete"
              @click="handleDeleteProvince"
            >
              删除省份
            </el-button>
          </div>
        </div>
      </template>
      <!-- 树内容区域 -->
      <div class="tree-wrapper">
        <el-skeleton
          :loading="loading"
          animated
          :throttle="500"
          :rows="8"
          v-if="loading"
        />
        <el-tree
          ref="treeRef"
          v-else
          :data="filteredTreeData"
          :props="treeProps"
          node-key="id"
          :default-expanded-keys="defaultExpandedKeys"
          class="region-tree"
          :load="loadNode"
          lazy
          highlight-current
          expand-on-click-node
          @node-click="handleNodeClick"
          :filter-node-method="filterNode"
          show-checkbox
          :check-strictly="true"
          @check="handleCheckChange"
        >
          <template #default="{ node, data }">
            <span class="tree-node">
              <el-icon
                class="node-icon"
                size="16"
                :class="
                  data.countyName
                    ? 'county-icon'
                    : data.cityName
                    ? 'city-icon'
                    : 'province-icon'
                "
              >
                <Province v-if="data.provinceName" />
                <City v-if="data.cityName" />
                <Building v-if="data.countyName" />
              </el-icon>
              <span class="node-label">{{ node.label }}</span>
              <span
                v-if="
                  (data.provinceName && data.cityVOS?.length) ||
                  (data.cityName && data.countyVOS?.length)
                "
                class="node-count"
              >
                {{
                  data.provinceName
                    ? data.cityVOS.length
                    : data.countyVOS.length
                }}
              </span>

              <!-- 省份节点添加"添加城市"按钮 -->
              <el-button
                v-if="data.provinceName"
                type="primary"
                size="small"
                icon="Plus"
                @click.stop="handleAddCity(data)"
                class="operation-btn"
              >
                添加城市
              </el-button>

              <!-- 城市节点添加"添加区域"和"删除城市"按钮 -->
              <template v-if="data.cityName">
                <el-button
                  type="primary"
                  size="small"
                  icon="Plus"
                  @click.stop="handleAddRegion(data)"
                  class="operation-btn"
                >
                  添加区域
                </el-button>
                <el-button
                  size="small"
                  type="danger"
                  icon="Delete"
                  @click.stop="handleDeleteCity(data)"
                  class="operation-btn"
                >
                  删除城市
                </el-button>
              </template>

              <!-- 区县节点添加"删除区域"按钮 -->
              <el-button
                v-if="data.countyName"
                size="small"
                type="danger"
                icon="Delete"
                @click.stop="handleDeleteRegion(data)"
                class="operation-btn"
              >
                删除区域
              </el-button>
            </span>
          </template>
        </el-tree>
      </div>
    </el-card>

    <!-- 添加省份弹窗 -->
    <el-dialog
      v-model="addProvinceDialogVisible"
      title="添加省份"
      width="400px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="addProvinceFormRef"
        :model="addProvinceForm"
        :rules="addProvinceFormRules"
        label-width="80px"
      >
        <el-form-item label="省份名称" prop="provinceName">
          <el-input
            v-model="addProvinceForm.provinceName"
            placeholder="请输入省份名称"
            :maxlength="20"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="省份编码" prop="provinceCode">
          <el-input
            v-model="addProvinceForm.provinceCode"
            placeholder="请输入6位数字编码"
            :maxlength="6"
            show-word-limit
            oninput="this.value = this.value.replace(/[^\d]/g, '').slice(0, 6)"
          />
        </el-form-item>
        <el-form-item label="邮政编码" prop="postCode">
          <el-input
            v-model="addProvinceForm.postCode"
            placeholder="请输入6位数字邮编"
            :maxlength="6"
            show-word-limit
            oninput="this.value = this.value.replace(/[^\d]/g, '').slice(0, 6)"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="addProvinceDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitAddProvince">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 添加城市弹窗 -->
    <el-dialog
      v-model="addCityDialogVisible"
      title="添加城市"
      width="400px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="addCityFormRef"
        :model="addCityForm"
        :rules="addCityFormRules"
        label-width="80px"
      >
        <el-form-item label="城市名称" prop="cityName">
          <el-input
            v-model="addCityForm.cityName"
            placeholder="请输入城市名称"
            :maxlength="20"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="城市编码" prop="cityCode">
          <el-input
            v-model="addCityForm.cityCode"
            placeholder="请输入6位数字编码"
            :maxlength="6"
            show-word-limit
            oninput="this.value = this.value.replace(/[^\d]/g, '').slice(0, 6)"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="addCityDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitAddCity">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 添加区域弹窗 -->
    <el-dialog
      v-model="addRegionDialogVisible"
      title="添加区域"
      width="400px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="addRegionFormRef"
        :model="addRegionForm"
        :rules="addRegionFormRules"
        label-width="80px"
      >
        <el-form-item label="区域名称" prop="countyName">
          <el-input
            v-model="addRegionForm.countyName"
            placeholder="请输入区域名称"
            :maxlength="20"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="区域编码" prop="countyCode">
          <el-input
            v-model="addRegionForm.countyCode"
            placeholder="请输入6位数字编码"
            :maxlength="6"
            show-word-limit
            oninput="this.value = this.value.replace(/[^\d]/g, '').slice(0, 6)"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="addRegionDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitAddRegion">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from "vue";
import {
  ElCard,
  ElTree,
  ElSkeleton,
  ElMessage,
  ElIcon,
  ElButton,
  ElMessageBox,
  ElForm,
  ElFormItem,
  ElInput,
  ElDialog,
} from "element-plus";
import {
  Plus,
  Delete,
} from "@element-plus/icons-vue";
import {
  getAllProvince,
  addProvince,
  deleteProvince,
} from "@/api/shengfenguanli";
import { addCity, deleteCity } from "@/api/chengshiguanli";
import { addCounty, deleteCounty } from "@/api/quxianguanli";

interface RegionNode {
  id: number | string; // 唯一标识（省/市/县通用）
  // 省份专属字段
  provinceName?: string; // 省份名称（如：广东省）
  provinceCode?: string; // 省份编码（如：440000）
  postCode?: string; // 邮政编码（如：510000）
  cityVOS?: RegionNode[]; // 省份下的城市列表（子节点）
  // 城市专属字段
  cityName?: string; // 城市名称（如：广州市）
  cityCode?: string; // 城市编码（如：440100）
  provinceId?: number | string; // 所属省份ID（如：19）
  countyVOS?: RegionNode[]; // 城市下的区县列表（子节点）
  // 区县专属字段
  countyName?: string; // 区县名称（如：荔湾区）
  countyCode?: string; // 区县编码（如：440103）
  cityId?: number | string; // 所属城市ID（如：199）
}

/** 状态管理 */
const loading = ref(true); // 加载态
const regionData = ref<RegionNode[]>([]); // 根节点：省份列表（接口返回）
const defaultExpandedKeys = ref<(number | string)[]>([]); // 默认展开节点
const selectedProvince = ref<RegionNode | null>(null); // 当前点击选中的省份
const checkedProvinces = ref<Set<string | number>>(new Set()); // 勾选的省份ID集合
const treeRef = ref<InstanceType<typeof ElTree>>();

// 弹窗状态管理
const addProvinceDialogVisible = ref(false); // 添加省份弹窗显示状态
const addCityDialogVisible = ref(false); // 添加城市弹窗显示状态
const addRegionDialogVisible = ref(false); // 添加区域弹窗显示状态

// 当前操作的父节点数据
const currentProvince = ref<RegionNode | null>(null);
const currentCity = ref<RegionNode | null>(null);

// 表单数据
const addProvinceForm = ref({
  provinceName: "", // 省份名称
  provinceCode: "", // 省份编码
  postCode: "", // 邮政编码
});

const addCityForm = ref({
  cityName: "", // 城市名称
  cityCode: "", // 城市编码
  provinceId: "", // 所属省份ID
});

const addRegionForm = ref({
  countyName: "", // 区域名称
  countyCode: "", // 区域编码
  cityId: "", // 所属城市ID
});

// 表单验证规则
const addProvinceFormRules = {
  provinceName: [
    { required: true, message: "请输入省份名称", trigger: "blur" },
    {
      min: 1,
      max: 20,
      message: "省份名称长度应在 1 到 20 个字符之间",
      trigger: "blur",
    },
  ],
  provinceCode: [
    { required: true, message: "请输入省份编码", trigger: "blur" },
    { pattern: /^\d{6}$/, message: "省份编码应为6位数字", trigger: "blur" },
  ],
  postCode: [
    { required: true, message: "请输入邮政编码", trigger: "blur" },
    { pattern: /^\d{6}$/, message: "邮政编码应为6位数字", trigger: "blur" },
  ],
};

const addCityFormRules = {
  cityName: [
    { required: true, message: "请输入城市名称", trigger: "blur" },
    {
      min: 1,
      max: 20,
      message: "城市名称长度应在 1 到 20 个字符之间",
      trigger: "blur",
    },
  ],
  cityCode: [
    { required: true, message: "请输入城市编码", trigger: "blur" },
    { pattern: /^\d{6}$/, message: "城市编码应为6位数字", trigger: "blur" },
  ],
};

const addRegionFormRules = {
  countyName: [
    { required: true, message: "请输入区域名称", trigger: "blur" },
    {
      min: 1,
      max: 20,
      message: "区域名称长度应在 1 到 20 个字符之间",
      trigger: "blur",
    },
  ],
  countyCode: [
    { required: true, message: "请输入区域编码", trigger: "blur" },
    { pattern: /^\d{6}$/, message: "区域编码应为6位数字", trigger: "blur" },
  ],
};

// 表单引用
const addProvinceFormRef = ref<InstanceType<typeof ElForm>>();
const addCityFormRef = ref<InstanceType<typeof ElForm>>();
const addRegionFormRef = ref<InstanceType<typeof ElForm>>();

const treeProps = {
  label: (node: RegionNode) =>
    node.provinceName || node.cityName || node.countyName,
  children: (node: RegionNode) => {
    if (node.provinceName) return node.cityVOS || []; // 省份子节点=城市列表
    if (node.cityName) return node.countyVOS || []; // 城市子节点=区县列表
    return []; // 区县无子集
  },
  isLeaf: (node: RegionNode) => !!node.countyName, // 区县为叶子节点
};

const loadNode = async (node: any, resolve: (data: RegionNode[]) => void) => {
  if (node.level === 0) {
    resolve(regionData.value);
    return;
  }
  if (node.level === 1 && node.data.provinceName) {
    resolve(node.data.cityVOS || []);
    return;
  }
  if (node.level === 2 && node.data.cityName) {
    resolve(node.data.countyVOS || []);
    return;
  }
  resolve([]);
};

const filterNode = (value: string, data: RegionNode) => {
  if (!value) return true;
  const label =
    (data.provinceName || data.cityName || data.countyName)?.toLowerCase() ||
    "";
  return label.includes(value.toLowerCase());
};

const filteredTreeData = computed(() => {
  const filterNodes = (nodes: RegionNode[]): RegionNode[] => {
    return nodes
      .map((node) => {
        // 判断当前节点是否匹配关键词
        const label =
          (
            node.provinceName ||
            node.cityName ||
            node.countyName
          )?.toLowerCase() || "";
        // 获取当前节点的子节点数组（按层级区分）
        const childSource = node.provinceName
          ? node.cityVOS
          : node.cityName
          ? node.countyVOS
          : [];
        const filteredChildren = filterNodes(childSource || []);

        // 保留规则：当前节点匹配 或 子节点有匹配
        if (filteredChildren.length > 0) {
          // 自动展开有匹配子节点的父节点
          if (
            filteredChildren.length > 0 &&
            !defaultExpandedKeys.value.includes(node.id)
          ) {
            defaultExpandedKeys.value.push(node.id);
          }

          // 复制节点并更新子节点字段（保持原结构）
          const newNode = { ...node };
          if (node.provinceName) newNode.cityVOS = filteredChildren;
          if (node.cityName) newNode.countyVOS = filteredChildren;

          return newNode;
        }
        return null;
      })
      .filter(Boolean) as RegionNode[];
  };

  return filterNodes(regionData.value);
});

/** 节点点击事件：记录选中的省份 */
const handleNodeClick = (node: RegionNode) => {
  console.log("选中地区节点:", {
    id: node.id,
    name: node.provinceName || node.cityName || node.countyName,
    code: node.provinceCode || node.cityCode || node.countyCode,
    level: node.provinceName ? "province" : node.cityName ? "city" : "county",
  });
  // 记录选中的省份
  if (node.provinceName) {
    selectedProvince.value = node;
  }
};

/** 复选框状态变化事件 */
const handleCheckChange = (data: any, checkedInfo: any) => {
  // 只处理省份节点的勾选
  if (data.provinceName) {
    const nodeId = data.id;
    if (checkedInfo.checked) {
      checkedProvinces.value.add(nodeId);
    } else {
      checkedProvinces.value.delete(nodeId);
    }
  }
};

/** 加载省份数据：调用接口并初始化根节点 */
const fetchData = async () => {
  loading.value = true;
  try {
    const res = await getAllProvince();
    if (res.data.code === 200) {
      regionData.value = res.data.data || [];
    } else {
      regionData.value = [];
    }
  } catch (error) {
    console.error("加载地区数据失败:", error);
    regionData.value = [];
  } finally {
    loading.value = false;
  }
};

/** 添加省份功能 */
const handleAddProvince = () => {
  // 重置表单
  addProvinceForm.value = {
    provinceName: "",
    provinceCode: "",
    postCode: "",
  };
  // 显示弹窗
  addProvinceDialogVisible.value = true;
};

/** 提交添加省份表单 */
const submitAddProvince = async () => {
  if (!addProvinceFormRef.value) return;

  try {
    // 表单验证
    await addProvinceFormRef.value.validate();

    loading.value = true;
    // 调用添加省份接口
    const res = await addProvince({
      provinceName: addProvinceForm.value.provinceName,
      provinceCode: addProvinceForm.value.provinceCode,
      postCode: addProvinceForm.value.postCode,
    });

    if (res.data.code === 200) {
      ElMessage.success("添加省份成功");
      addProvinceDialogVisible.value = false;
      await fetchData();
      // 重置表单
      addProvinceFormRef.value?.resetFields();
    } else {
      ElMessage.error(res.data.message || "添加省份失败");
    }
  } catch (error) {
    console.error("添加省份失败:", error);
    ElMessage.error("添加省份失败，请重试");
  } finally {
    loading.value = false;
  }
};

/** 删除选中的省份 */
const handleDeleteProvince = async () => {
  // 关键优化：手动同步树形组件的勾选状态，确保获取最新选中的省份
  const checkedNodes = treeRef.value?.getCheckedNodes() || [];
  const checkedProvinceIds = checkedNodes
    .filter((node) => node.provinceName)
    .map((node) => node.id);
  checkedProvinces.value = new Set(checkedProvinceIds);

  // 获取所有勾选的省份ID
  const checkedIds = [...checkedProvinces.value];
  console.log("选中的省份ID:", checkedIds);

  if (checkedIds.length === 0) {
    ElMessage.warning("请先选择要删除的省份");
    return;
  }

  // 获取选中省份的名称，用于提示信息
  const selectedProvinceNames = regionData.value
    .filter((province) => checkedIds.includes(province.id))
    .map((province) => province.provinceName || "未知省份")
    .join("、");

  ElMessageBox.confirm(
    `确定要删除选中的 ${checkedIds.length} 个省份（${selectedProvinceNames}）吗？删除后将无法恢复，且省份下的所有城市和区域也会被删除。`,
    "删除确认",
    {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    }
  )
    .then(async () => {
      try {
        loading.value = true;
        const successIds: (number | string)[] = [];
        const errorIds: (number | string)[] = [];

        // 逐个删除省份，确保错误处理更精细
        for (const id of checkedIds) {
          try {
            const res = await deleteProvince(Number(id));
            if (res.data.code === 200) {
              successIds.push(id);
            } else {
              errorIds.push(id);
            }
          } catch (error) {
            console.error(`删除省份 ${id} 失败:`, error);
            errorIds.push(id);
          }
        }

        // 重新加载数据
        await fetchData();

        // 清空选中状态
        checkedProvinces.value = new Set();
        if (treeRef.value) {
          treeRef.value.setCheckedKeys([]);
        }

        // 显示结果提示
        let message = "";
        if (successIds.length > 0) {
          message += `成功删除 ${successIds.length} 个省份。`;
        }
        if (errorIds.length > 0) {
          message += ` ${errorIds.length} 个省份删除失败，请稍后重试。`;
        }

        if (successIds.length > 0) {
          ElMessage.success(message);
        } else {
          ElMessage.error(message || "删除省份失败，请重试");
        }
      } catch (error) {
        console.error("删除省份失败:", error);
        ElMessage.error("删除省份过程中发生错误，请重试");
      } finally {
        loading.value = false;
      }
    })
    .catch(() => {
      // 用户取消删除
      ElMessage.info("已取消删除操作");
    });
};

/** 打开添加城市弹窗 */
const handleAddCity = (provinceData: RegionNode) => {
  currentProvince.value = provinceData;
  // 重置表单
  addCityForm.value = {
    cityName: "",
    cityCode: "",
    provinceId: provinceData.id as string,
  };
  // 显示弹窗
  addCityDialogVisible.value = true;
};

/** 提交添加城市表单 */
const submitAddCity = async () => {
  if (!addCityFormRef.value || !currentProvince.value) return;

  try {
    // 表单验证
    await addCityFormRef.value.validate();

    loading.value = true;
    // 调用添加城市接口
    const res = await addCity({
      cityName: addCityForm.value.cityName,
      cityCode: addCityForm.value.cityCode,
      provinceId: currentProvince.value.id as number,
    });

    if (res.data.code === 200) {
      ElMessage.success("添加城市成功");
      addCityDialogVisible.value = false;
      await fetchData();
      // 重置表单
      addCityFormRef.value?.resetFields();
      currentProvince.value = null;
    } else {
      ElMessage.error(res.data.message || "添加城市失败");
    }
  } catch (error) {
    console.error("添加城市失败:", error);
    ElMessage.error("添加城市失败，请重试");
  } finally {
    loading.value = false;
  }
};

/** 打开添加区域弹窗 */
const handleAddRegion = (cityData: RegionNode) => {
  currentCity.value = cityData;
  // 重置表单
  addRegionForm.value = {
    countyName: "",
    countyCode: "",
    cityId: cityData.id as string,
  };
  // 显示弹窗
  addRegionDialogVisible.value = true;
};

/** 提交添加区域表单 */
const submitAddRegion = async () => {
  if (!addRegionFormRef.value || !currentCity.value) return;

  try {
    // 表单验证
    await addRegionFormRef.value.validate();

    loading.value = true;
    // 调用添加区域接口
    const res = await addCounty({
      countyName: addRegionForm.value.countyName,
      countyCode: addRegionForm.value.countyCode,
      cityId: currentCity.value.id as number,
    });

    if (res.data.code === 200) {
      ElMessage.success("添加区域成功");
      addRegionDialogVisible.value = false;
      await fetchData();
      // 重置表单
      addRegionFormRef.value?.resetFields();
      currentCity.value = null;
    } else {
      ElMessage.error(res.data.message || "添加区域失败");
    }
  } catch (error) {
    console.error("添加区域失败:", error);
    ElMessage.error("添加区域失败，请重试");
  } finally {
    loading.value = false;
  }
};

/** 删除城市 */
const handleDeleteCity = (cityData: RegionNode) => {
  ElMessageBox.confirm(
    `确定要删除城市“${cityData.cityName}”吗？删除后该城市下的所有区域也将被删除。`,
    "删除确认",
    {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    }
  )
    .then(async () => {
      try {
        loading.value = true;
        const res = await deleteCity(Number(cityData.id));

        if (res.data.code === 200) {
          ElMessage.success(`成功删除城市“${cityData.cityName}”`);
          await fetchData();
        } else {
          ElMessage.error(res.data.message || "删除城市失败");
        }
      } catch (error) {
        console.error("删除城市失败:", error);
        ElMessage.error("删除城市失败，请重试");
      } finally {
        loading.value = false;
      }
    })
    .catch(() => {
      // 用户取消删除
      ElMessage.info("已取消删除操作");
    });
};

/** 删除区域 */
const handleDeleteRegion = (regionData: RegionNode) => {
  ElMessageBox.confirm(
    `确定要删除区域“${regionData.countyName}”吗？删除后将无法恢复。`,
    "删除确认",
    {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    }
  )
    .then(async () => {
      try {
        loading.value = true;
        const res = await deleteCounty(Number(regionData.id));

        if (res.data.code === 200) {
          ElMessage.success(`成功删除区域“${regionData.countyName}”`);
          await fetchData();
        } else {
          ElMessage.error(res.data.message || "删除区域失败");
        }
      } catch (error) {
        console.error("删除区域失败:", error);
        ElMessage.error("删除区域失败，请重试");
      } finally {
        loading.value = false;
      }
    })
    .catch(() => {
      // 用户取消删除
      ElMessage.info("已取消删除操作");
    });
};

// 组件挂载时初始化加载数据
onMounted(() => fetchData());
</script>

<style scoped>
.region-tree-card {
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}
.region-tree-card:hover {
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.12);
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  border-bottom: 1px solid #ebeef5;
  background-color: #f9fafb;
  border-radius: 12px 12px 0 0;
}
.card-header span {
  font-size: 18px;
  font-weight: 600;
  color: #333;
}
.header-right {
  display: flex;
  align-items: center;
  gap: 10px;
}
.tree-wrapper {
  padding: 20px;
  overflow: auto;
  transition: all 0.3s ease;
}
/* 自定义滚动条 */
.tree-wrapper::-webkit-scrollbar {
  width: 6px;
}
.tree-wrapper::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}
.tree-wrapper::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 4px;
}
.tree-wrapper::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
/* 树节点样式：图标+名称，间距优化 */
.region-tree {
  --el-tree-node-indent: 24px; /* 调整层级缩进，更清晰 */
  --el-tree-node-hover-bg-color: #f5f7fa;
}
:deep(.el-tree-node__content) {
  height: 40px;
  transition: background-color 0.2s ease;
  border-radius: 6px;
  margin: 2px 0;
}
:deep(.el-tree-node:focus > .el-tree-node__content) {
  background-color: #ecf5ff;
}
:deep(.el-tree-node__content:hover) {
  background-color: #f5f7fa;
}
.tree-node {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 0;
  transition: all 0.2s ease;
  width: 100%;
}
/* 节点图标颜色区分 */
.province-icon {
  color: #f56c6c;
}
.city-icon {
  color: #67c23a;
}
.county-icon {
  color: #409eff;
}
/* 节点名称样式 */
.node-label {
  font-size: 14px;
  color: #333;
  flex: 1;
}
/* 节点计数样式 */
.node-count {
  background-color: #f0f2f5;
  color: #909399;
  border-radius: 10px;
  padding: 0 8px;
  font-size: 12px;
  height: 20px;
  line-height: 20px;
  min-width: 20px;
  text-align: center;
}
/* 操作按钮样式 */
.operation-btn {
  margin-left: 8px;
  padding: 2px 8px;
  height: 24px;
  font-size: 12px;
}
/* 空状态样式 */
.empty-state {
  text-align: center;
  padding: 40px 0;
  color: #999;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
}
@media (max-width: 768px) {
  .card-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
  .header-right {
    width: 100%;
  }
  .region-tree-card {
    margin: 10px;
  }
  .tree-wrapper {
    padding: 10px;
    max-height: 500px;
  }
}
</style>
