// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** 文件上传 POST /file/upload */
export async function upload(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.uploadParams,
  body: {},
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultObject>("/file/upload", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    params: {
      ...params,
    },
    data: body,
    ...(options || {}),
  });
}
