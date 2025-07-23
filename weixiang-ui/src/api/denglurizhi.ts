// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** 删除登录日志表 GET /login/log/delete */
export async function deleteLoginLog(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteLoginLogParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResult>("/login/log/delete", {
    method: "GET",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 查询登录日志详情 GET /login/log/get */
export async function getLoginLogById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getLoginLogByIdParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultLoginLogVO>("/login/log/get", {
    method: "GET",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 查询列表 POST /login/log/page */
export async function selectLoginPage(
  body: API.LogSearchParam,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultIPageLoginLogVO>("/login/log/page", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}
