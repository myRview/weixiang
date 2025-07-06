// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** 查询用户套餐表详情 GET /user/plan/${param0} */
export async function getById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getByIdParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params;
  return request<API.UserPlanEntityduixiang>(`/user/plan/${param0}`, {
    method: "GET",
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 删除用户套餐表 DELETE /user/plan/${param0} */
export async function deleteUsingDelete(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteUsingDELETEParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params;
  return request<boolean>(`/user/plan/${param0}`, {
    method: "DELETE",
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 添加用户套餐表 POST /user/plan/add */
export async function add(
  body: API.UserPlanEntityduixiang,
  options?: { [key: string]: any }
) {
  return request<boolean>("/user/plan/add", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 查询用户套餐表列表 GET /user/plan/list */
export async function list(options?: { [key: string]: any }) {
  return request<API.UserPlanEntityduixiang[]>("/user/plan/list", {
    method: "GET",
    ...(options || {}),
  });
}

/** 修改用户套餐表 POST /user/plan/update */
export async function update(
  body: API.UserPlanEntityduixiang,
  options?: { [key: string]: any }
) {
  return request<boolean>("/user/plan/update", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}
