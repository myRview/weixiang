// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** 删除权限 POST /permission/delete */
export async function deletePermission(
  body: number[],
  options?: { [key: string]: any }
) {
  return request<API.ResponseResult>("/permission/delete", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 权限列表 POST /permission/list */
export async function getPermissionList(options?: { [key: string]: any }) {
  return request<API.ResponseResultListPermissionVO>("/permission/list", {
    method: "POST",
    ...(options || {}),
  });
}

/** 权限分页列表 POST /permission/page */
export async function getPermissionPage(
  body: API.PermissionSearchParam,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultIPagePermissionVO>("/permission/page", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 添加权限 POST /permission/save */
export async function savePermission(
  body: API.PermissionVO,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResult>("/permission/save", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 删除角色 GET /role/delete */
export async function deleteRole(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteRoleParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResult>("/role/delete", {
    method: "GET",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 查询角色权限详情 GET /role/get */
export async function getRolePermission(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getRolePermissionParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultListPermissionVO>("/role/get", {
    method: "GET",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 查询角色列表 GET /role/list */
export async function selectAllRole(options?: { [key: string]: any }) {
  return request<API.ResponseResultListRoleVO>("/role/list", {
    method: "GET",
    ...(options || {}),
  });
}

/** 角色添加权限 POST /role/permission */
export async function saveRolePermission(
  body: API.RolePermissionVO,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResult>("/role/permission", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 添加角色 POST /role/save */
export async function saveRole(
  body: API.RoleVO,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResult>("/role/save", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}
