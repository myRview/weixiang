// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** 统计订单数量 POST /order/statistics/count */
export async function count(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.countParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultListOrderStatisticsVO>(
    "/order/statistics/count",
    {
      method: "POST",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** 统计套餐购买 POST /order/statistics/plan/count */
export async function planCount(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.planCountParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultListPlanStatisticsVO>(
    "/order/statistics/plan/count",
    {
      method: "POST",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}
