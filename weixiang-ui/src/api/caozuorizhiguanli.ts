// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** 删除操作日志 GET /operation/log/delete */
export async function deleteOperationLog(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteOperationLogParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResult>("/operation/log/delete", {
    method: "GET",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 查询操作日志详情 GET /operation/log/get */
export async function getOperationLogById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getOperationLogByIdParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultOperationLogVO>("/operation/log/get", {
    method: "GET",
    params: {
      ...params,
    },
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
