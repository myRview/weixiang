// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** 支付回调 POST /aliPay/callback */
export async function callback(options?: { [key: string]: any }) {
  return request<string>("/aliPay/callback", {
    method: "POST",
    ...(options || {}),
  });
}

/** 生成支付二维码(测试) GET /aliPay/pay */
export async function pay(options?: { [key: string]: any }) {
  return request<API.ResponseResult>("/aliPay/pay", {
    method: "GET",
    ...(options || {}),
  });
}
