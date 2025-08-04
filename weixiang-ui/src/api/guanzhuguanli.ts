// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** 关注/取消关注 POST /follow/chang/status */
export async function changStatus(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.changStatusParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultBoolean>("/follow/chang/status", {
    method: "POST",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 获取粉丝列表 POST /follow/fans/list */
export async function fansList(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.fansListParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultListUserVO>("/follow/fans/list", {
    method: "POST",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 获取关注列表 POST /follow/list */
export async function list(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultListUserVO>("/follow/list", {
    method: "POST",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 获取关注状态 POST /follow/status */
export async function status(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.statusParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultBoolean>("/follow/status", {
    method: "POST",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}
