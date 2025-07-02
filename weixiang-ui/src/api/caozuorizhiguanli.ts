// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** 查询操作日志详情 GET /operation/log/${param0} */
export async function getOperationLogById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getOperationLogByIdParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params;
  return request<API.ResponseResultOperationLogVO>(`/operation/log/${param0}`, {
    method: "GET",
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 删除操作日志 DELETE /operation/log/${param0} */
export async function deleteOperationLog(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteOperationLogParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params;
  return request<API.ResponseResult>(`/operation/log/${param0}`, {
    method: "DELETE",
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 添加操作日志 POST /operation/log/add */
export async function addOperationLog(
  body: API.OperationLogEntityduixiang,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResult>("/operation/log/add", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 查询操作日志列表 POST /operation/log/page */
export async function selectOperaLogPage(
  body: API.LogSearchParam,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultIPageOperationLogVO>("/operation/log/page", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 修改操作日志 POST /operation/log/update */
export async function updateOperationLog(
  body: API.OperationLogEntityduixiang,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResult>("/operation/log/update", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}
