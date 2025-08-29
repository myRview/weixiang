// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** 添加黑名单ip POST /system/setting/add/black/ip */
export async function addBlackIp(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.addBlackIpParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResult>("/system/setting/add/black/ip", {
    method: "POST",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 获取所有黑名单ip POST /system/setting/all/black/ip */
export async function selectBlackIpAll(options?: { [key: string]: any }) {
  return request<API.ResponseResultListBlackIpVO>(
    "/system/setting/all/black/ip",
    {
      method: "POST",
      ...(options || {}),
    }
  );
}

/** 移除黑名单ip POST /system/setting/remove/black/ip */
export async function removeBlackIp(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.removeBlackIpParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResult>("/system/setting/remove/black/ip", {
    method: "POST",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}
