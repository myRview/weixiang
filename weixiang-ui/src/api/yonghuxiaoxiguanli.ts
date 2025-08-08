// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** 删除用户消息 POST /user/message/delete */
export async function deleteMessage(
  body: number[],
  options?: { [key: string]: any }
) {
  return request<API.ResponseResult>("/user/message/delete", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 查询用户消息详情 GET /user/message/get */
export async function getMessageById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getMessageByIdParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultUserMessageVO>("/user/message/get", {
    method: "GET",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 查询用户消息列表 POST /user/message/page */
export async function selectMessageList(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.selectMessageListParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultListUserMessageVO>("/user/message/page", {
    method: "POST",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 设置用户消息已读 POST /user/message/read */
export async function readMessage(
  body: number[],
  options?: { [key: string]: any }
) {
  return request<API.ResponseResult>("/user/message/read", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}
