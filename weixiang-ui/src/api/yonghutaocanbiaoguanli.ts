// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** 用户购买套餐 POST /user/plan/pay/plan */
export async function payPlan(
  body: API.PayPlanVo,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResult>("/user/plan/pay/plan", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 查询用户套餐详情 GET /user/plan/user/plan */
export async function getPayPlan(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getPayPlanParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultUserPlan>("/user/plan/user/plan", {
    method: "GET",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}
