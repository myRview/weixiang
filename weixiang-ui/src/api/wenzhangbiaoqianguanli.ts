// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** 添加标签 POST /tag/add */
export async function saveTag(
  body: API.TagVO,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResult>("/tag/add", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 删除标签 GET /tag/delete */
export async function deleteTag(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteTagParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResult>("/tag/delete", {
    method: "GET",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 获取标签列表 POST /tag/list */
export async function getTagList(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getTagListParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultListTagVO>("/tag/list", {
    method: "POST",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 获取标签分页列表 POST /tag/page */
export async function selectTagPage(
  body: API.TagSearchParam,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultIPageTagVO>("/tag/page", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 修改标签 POST /tag/update */
export async function updateTag(
  body: API.TagVO,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResult>("/tag/update", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}
