// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** 注册 POST /register */
export async function register(
  body: API.UserRegisterVO,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResult>("/register", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}
