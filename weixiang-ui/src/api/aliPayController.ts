// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** 此处后端没有提供注释 POST /aliPay/callback */
export async function callback(options?: { [key: string]: any }) {
  return request<string>("/aliPay/callback", {
    method: "POST",
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 GET /aliPay/pay */
export async function pay(options?: { [key: string]: any }) {
  return request<API.ResponseResult>("/aliPay/pay", {
    method: "GET",
    ...(options || {}),
  });
}
