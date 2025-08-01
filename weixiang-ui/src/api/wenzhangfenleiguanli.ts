// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** 添加分类 POST /category/add */
export async function saveCategory(
  body: API.CategoryVO,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResult>("/category/add", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 删除分类 GET /category/delete */
export async function deleteCategory(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteCategoryParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResult>("/category/delete", {
    method: "GET",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 获取分类列表 POST /category/list */
export async function getCategoryList(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getCategoryListParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultListCategoryVO>("/category/list", {
    method: "POST",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 获取分类分页列表 POST /category/page */
export async function selectCategoryPage(
  body: API.CategorySearchParam,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultIPageCategoryVO>("/category/page", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 修改分类 POST /category/update */
export async function updateCategory(
  body: API.CategoryVO,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResult>("/category/update", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}
