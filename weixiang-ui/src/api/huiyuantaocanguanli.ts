// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** 添加会员套餐 POST /member/plan/add */
export async function addPlan(
  body: API.MemberPlanVO,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResult>("/member/plan/add", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 删除会员套餐 GET /member/plan/delete */
export async function deletePlan(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deletePlanParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResult>("/member/plan/delete", {
    method: "GET",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 查询会员套餐详情 GET /member/plan/get */
export async function getPlanInfoById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getPlanInfoByIdParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultMemberPlanVO>("/member/plan/get", {
    method: "GET",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 查询会员套餐列表 POST /member/plan/list */
export async function selectPlanList(options?: { [key: string]: any }) {
  return request<API.ResponseResultListMemberPlanVO>("/member/plan/list", {
    method: "POST",
    ...(options || {}),
  });
}

/** 查询会员套餐列表 POST /member/plan/page */
export async function selectPlanPage(
  body: API.PlanSearchParam,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultPageMemberPlanVO>("/member/plan/page", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 修改会员套餐 POST /member/plan/update */
export async function updatePlan(
  body: API.MemberPlanVO,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResult>("/member/plan/update", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}
