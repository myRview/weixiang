// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** 查询详情 GET /login/log/${param0} */
export async function getLoginLogById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getLoginLogByIdParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params;
  return request<API.ResponseResultLoginLogVO>(`/login/log/${param0}`, {
    method: "GET",
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 删除登录日志表 DELETE /login/log/${param0} */
export async function deleteLoginLog(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteLoginLogParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params;
  return request<API.ResponseResult>(`/login/log/${param0}`, {
    method: "DELETE",
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 添加登录日志表 POST /login/log/add */
export async function addLoginLog(
  body: API.LoginLogEntityduixiang,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResult>("/login/log/add", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 查询列表 POST /login/log/page */
export async function selectLoginPage(
  body: API.LogSearchParam,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultPageLoginLogVO>("/login/log/page", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 修改登录日志表 POST /login/log/update */
export async function updateLoginLog(
  body: API.LoginLogEntityduixiang,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResult>("/login/log/update", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}
