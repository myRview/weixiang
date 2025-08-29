// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** 删除订单 GET /order/info/delete */
export async function deleteOrder(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteOrderParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResult>("/order/info/delete", {
    method: "GET",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 查询支付宝订单详情 GET /order/info/detail */
export async function getOrderDetail(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getOrderDetailParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultJSONObject>("/order/info/detail", {
    method: "GET",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 查询订单详情 GET /order/info/get */
export async function getOrderById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getOrderByIdParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultOrderVO>("/order/info/get", {
    method: "GET",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 查询订单列表 POST /order/info/page */
export async function selectOrderPage(
  body: API.OrderSearchParam,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultPageOrderVO>("/order/info/page", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 退款 GET /order/info/refund */
export async function refund(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.refundParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultObject>("/order/info/refund", {
    method: "GET",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 查询支付宝退款订单详情 GET /order/info/refund/detail */
export async function getRefundDetail(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getRefundDetailParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultJSONObject>("/order/info/refund/detail", {
    method: "GET",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 查询订单状态 GET /order/info/status */
export async function getOrderStatusById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getOrderStatusByIdParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultInteger>("/order/info/status", {
    method: "GET",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}
