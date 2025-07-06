// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** 登陆 POST /login */
export async function login(
  body: API.UserLoginVO,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultObject>("/login", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 退出登录 POST /logout */
export async function logout(options?: { [key: string]: any }) {
  return request<API.ResponseResultObject>("/logout", {
    method: "POST",
    ...(options || {}),
  });
}
