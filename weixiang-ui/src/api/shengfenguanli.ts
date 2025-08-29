// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** 添加省份数据 POST /province/add */
export async function addProvince(
  body: API.ProvinceVO,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultObject>("/province/add", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 获取所有省份数据（包含下级数据） POST /province/all */
export async function getAllProvince(options?: { [key: string]: any }) {
  return request<API.ResponseResultListProvinceVO>("/province/all", {
    method: "POST",
    ...(options || {}),
  });
}

/** 删除省份数据 POST /province/delete */
export async function deleteProvince(
  body: number,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultObject>("/province/delete", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 查询省份数据 GET /province/select */
export async function selectProvince(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.selectProvinceParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultObject>("/province/select", {
    method: "GET",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 更新省份数据 POST /province/update */
export async function updateProvince(
  body: API.ProvinceVO,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultObject>("/province/update", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}
