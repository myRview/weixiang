// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** 保存文章 POST /article/add */
export async function saveArticle(
  body: API.ArticleEditVO,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResult>("/article/add", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 审核文章(管理员) POST /article/audit */
export async function auditArticle(
  body: API.ArticleAuditVO,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultObject>("/article/audit", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 获取当前作者的文章分页列表(作者) POST /article/author/page */
export async function selectArticlePageByAuthor(
  body: API.ArticleSearchParam,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultIPageArticleVO>("/article/author/page", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 删除文章(作者) GET /article/delete */
export async function deleteArticle(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteArticleParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResult>("/article/delete", {
    method: "GET",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 获取文章详情 GET /article/detail */
export async function selectArticleDetail(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.selectArticleDetailParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultArticleVO>("/article/detail", {
    method: "GET",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 点赞和取消点赞 POST /article/like */
export async function likeArticle(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.likeArticleParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultObject>("/article/like", {
    method: "POST",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 点赞数量 GET /article/like/count */
export async function articleLikeCount(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.articleLikeCountParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultInteger>("/article/like/count", {
    method: "GET",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 点赞状态 GET /article/like/status */
export async function articleLikeStatus(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.articleLikeStatusParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultBoolean>("/article/like/status", {
    method: "GET",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 获取已发布文章分页列表(管理员) POST /article/page */
export async function selectArticlePage(
  body: API.ArticleSearchParam,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultIPageArticleVO>("/article/page", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 获取审批通过的文章分页列表 POST /article/pass/page */
export async function selectPassArticlePage(
  body: API.ArticleSearchParam,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultIPageArticleVO>("/article/pass/page", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 发布文章 POST /article/publish */
export async function publishArticle(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.publishArticleParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResult>("/article/publish", {
    method: "POST",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 添加阅读数量 GET /article/view/add */
export async function addArticleViewCount(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.addArticleViewCountParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultBoolean>("/article/view/add", {
    method: "GET",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 阅读数量 GET /article/view/count */
export async function articleViewCount(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.articleViewCountParams,
  options?: { [key: string]: any }
) {
  return request<API.ResponseResultInteger>("/article/view/count", {
    method: "GET",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}
