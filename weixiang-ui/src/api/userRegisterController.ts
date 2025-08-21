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

/** 获取验证码 POST /send/code */
export async function sendCode(
  body: API.UserRegisterVO,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultObject>("/send/code", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}
