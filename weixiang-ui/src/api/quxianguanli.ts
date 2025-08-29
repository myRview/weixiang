// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** 添加区县数据 POST /county/add */
export async function addCounty(
  body: API.CountyVO,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultObject>("/county/add", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 删除区县数据 POST /county/delete */
export async function deleteCounty(
  body: number,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultObject>("/county/delete", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 查询区县数据 GET /county/select */
export async function selectCounty(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.selectCountyParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultObject>("/county/select", {
    method: "GET",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 更新区县数据 POST /county/update */
export async function updateCounty(
  body: API.CountyVO,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultObject>("/county/update", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}
