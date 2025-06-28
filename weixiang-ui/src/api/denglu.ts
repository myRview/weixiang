// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** 此处后端没有提供注释 POST /login */
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
