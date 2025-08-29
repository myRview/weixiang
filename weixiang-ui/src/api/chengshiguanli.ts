// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** 添加区县数据 POST /city/add */
export async function addCity(
  body: API.CityVO,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultObject>("/city/add", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 删除区县数据 POST /city/delete */
export async function deleteCity(
  body: number,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultObject>("/city/delete", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 查询区县数据 GET /city/select */
export async function selectCity(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.selectCityParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultObject>("/city/select", {
    method: "GET",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 更新区县数据 POST /city/update */
export async function updateCity(
  body: API.CityVO,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultObject>("/city/update", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}
