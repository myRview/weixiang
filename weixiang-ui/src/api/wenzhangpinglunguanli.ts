// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** 添加文章评论 POST /comment/add */
export async function addArticleComment(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.addArticleCommentParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultObject>("/comment/add", {
    method: "POST",
    params: {
      ...params,
      articleCommentVO: undefined,
      ...params["articleCommentVO"],
    },
    ...(options || {}),
  });
}

/** 删除文章评论 POST /comment/delete */
export async function deleteArticleComment(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteArticleCommentParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultObject>("/comment/delete", {
    method: "POST",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 获取文章评论列表 GET /comment/list */
export async function getArticleCommentList(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getArticleCommentListParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultListArticleCommentVO>("/comment/list", {
    method: "GET",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}
