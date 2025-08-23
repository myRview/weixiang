// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** 添加用户 POST /user/add */
export async function addUser(
  body: API.UserAddVO,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResult>("/user/add", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 绑定手机号/邮箱 POST /user/bind */
export async function bindPhoneAndEmail(
  body: API.UserBindVO,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResult>("/user/bind", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 获取连续签到天数 POST /user/continuous/count */
export async function getContinuousSignCount(options?: { [key: string]: any }) {
  return request<API.ResponseResult>("/user/continuous/count", {
    method: "POST",
    ...(options || {}),
  });
}

/** 删除用户 GET /user/delete */
export async function deleteUser(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteUserParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResult>("/user/delete", {
    method: "GET",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 编辑用户资料 POST /user/edit */
export async function editUser(
  body: API.EditUserExpandVO,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResult>("/user/edit", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 查询用户详情 GET /user/get/info */
export async function getUserById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getUserByIdParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultUserVO>("/user/get/info", {
    method: "GET",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 获取当前用户 POST /user/info */
export async function getLoginUser(options?: { [key: string]: any }) {
  return request<API.ResponseResultUserVO>("/user/info", {
    method: "POST",
    ...(options || {}),
  });
}

/** 是否签到 POST /user/isSigned */
export async function isSigned(options?: { [key: string]: any }) {
  return request<API.ResponseResult>("/user/isSigned", {
    method: "POST",
    ...(options || {}),
  });
}

/** 查询用户列表 POST /user/list */
export async function getUserList(
  body: API.UserSearchParam,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultPageUserVO>("/user/list", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 获取本月签到天数 POST /user/month/count */
export async function getMonthSignCount(options?: { [key: string]: any }) {
  return request<API.ResponseResult>("/user/month/count", {
    method: "POST",
    ...(options || {}),
  });
}

/** 修改密码 POST /user/pwd */
export async function updatePassword(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.updatePasswordParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResult>("/user/pwd", {
    method: "POST",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 重置密码 POST /user/reset/pwd */
export async function resetPassword(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.resetPasswordParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResult>("/user/reset/pwd", {
    method: "POST",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 用户签到 POST /user/sign */
export async function sign(options?: { [key: string]: any }) {
  return request<API.ResponseResult>("/user/sign", {
    method: "POST",
    ...(options || {}),
  });
}

/** 获取签到记录 GET /user/sign/record */
export async function getSignRecord(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getSignRecordParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultMapLocalDateBoolean>("/user/sign/record", {
    method: "GET",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 启用/禁用用户 POST /user/status */
export async function transStatus(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.transStatusParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResult>("/user/status", {
    method: "POST",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 修改用户 POST /user/update */
export async function updateUser(
  body: API.UserEditVO,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResult>("/user/update", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}
