// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** 查询订单详情 GET /order/info/${param0} */
export async function getOrderById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getOrderByIdParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params;
  return request<API.ResponseResultOrderVO>(`/order/info/${param0}`, {
    method: "GET",
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 删除订单 DELETE /order/info/${param0} */
export async function deleteOrder(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteOrderParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params;
  return request<API.ResponseResult>(`/order/info/${param0}`, {
    method: "DELETE",
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 添加订单 POST /order/info/add */
export async function addOrder(
  body: API.OrderInfoEntityduixiang,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResult>("/order/info/add", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
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

/** 修改订单 POST /order/info/update */
export async function updateOrder(
  body: API.OrderInfoEntityduixiang,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResult>("/order/info/update", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}
