declare namespace API {
  type ArticleAuditVO = {
    /** 文章id */
    articleId: number;
    /** 审核状态:1-审核通过，2-审核不通过 */
    auditStatus: number;
    /** 审核原因 */
    auditReason?: string;
  };

  type ArticleEditVO = {
    /** 主键 */
    id?: number;
    /** 标题 */
    title: string;
    /** 内容 */
    content: string;
    /** 分类id */
    categoryId?: number;
    /** 标签id列表 */
    tagIds?: number[];
  };

  type ArticleSearchParam = {
    pageNum?: number;
    pageSize?: number;
    /** 标题 */
    title?: string;
    /** 状态，0-草稿，1-已发布 */
    publishStatus?: number;
    /** 审核状态，0-待审核，1-审核通过，2-审核不通过 */
    auditStatus?: number;
    /** 分类id */
    categoryId?: number;
    /** 标签Id集合 */
    tagIds?: number[];
  };

  type ArticleVO = {
    /** 主键 */
    id?: number;
    /** 标题 */
    title?: string;
    /** 内容 */
    content?: string;
    /** 用户id */
    userId?: number;
    /** 用户名 */
    userName?: string;
    /** 状态，0-草稿，1-已发布 */
    publishStatus?: number;
    /** 审核状态，0-待审核，1-审核通过，2-审核不通过 */
    auditStatus?: number;
    /** 审核原因 */
    auditReason?: string;
    /** 浏览量 */
    viewCount?: number;
    /** 点赞量 */
    likeCount?: number;
    /** 分类id */
    categoryId?: number;
    /** 创建时间 */
    createTime?: string;
    /** 标签id */
    tagIds?: number[];
  };

  type CategorySearchParam = {
    pageNum?: number;
    pageSize?: number;
    categoryName?: string;
  };

  type CategoryVO = {
    /** 主键 */
    id?: number;
    /** 父分类Id */
    parentId?: number;
    /** 分类名称 */
    name?: string;
    /** 分类描述 */
    description?: string;
    /** 创建时间 */
    createTime?: string;
  };

  type countParams = {
    date: string;
  };

  type deleteArticleParams = {
    id: number;
  };

  type deleteCategoryParams = {
    id: number;
  };

  type deleteLoginLogParams = {
    id: number;
  };

  type deleteOperationLogParams = {
    id: number;
  };

  type deleteOrderParams = {
    id: number;
  };

  type deletePlanParams = {
    id: number;
  };

  type deleteRoleParams = {
    id: number;
  };

  type deleteTagParams = {
    id: number;
  };

  type deleteUserParams = {
    id: number;
  };

  type EditUserExpandVO = {
    /** 用户id */
    userId?: number;
    /** 用户名 */
    userName?: string;
    /** 手机号 */
    phone?: string;
    /** 邮箱 */
    email?: string;
    /** 性别 0-女，1-男 */
    gender?: number;
    /** 头像 */
    avatar?: string;
    /** 生日 */
    birthday?: string;
    /** 省 */
    province?: string;
    /** 市 */
    city?: string;
    /** 区/县 */
    district?: string;
    /** 详细地址 */
    address?: string;
    /** 个人简介 */
    bio?: string;
  };

  type getCategoryListParams = {
    categoryName: string;
  };

  type getLoginLogByIdParams = {
    id: number;
  };

  type getOperationLogByIdParams = {
    id: number;
  };

  type getOrderByIdParams = {
    id: number;
  };

  type getOrderStatusByIdParams = {
    id: number;
  };

  type getPayPlanParams = {
    userId: number;
  };

  type getPlanInfoByIdParams = {
    id: number;
  };

  type getRolePermissionParams = {
    id: number;
  };

  type getSignRecordParams = {
    year?: number;
  };

  type getTagListParams = {
    tagName: string;
  };

  type getUserByIdParams = {
    id: number;
  };

  type IPageArticleVO = {
    size?: number;
    records?: ArticleVO[];
    total?: number;
    current?: number;
    pages?: number;
  };

  type IPageCategoryVO = {
    size?: number;
    records?: CategoryVO[];
    total?: number;
    current?: number;
    pages?: number;
  };

  type IPageLoginLogVO = {
    size?: number;
    records?: LoginLogVO[];
    total?: number;
    current?: number;
    pages?: number;
  };

  type IPageOperationLogVO = {
    size?: number;
    records?: OperationLogVO[];
    total?: number;
    current?: number;
    pages?: number;
  };

  type IPagePermissionVO = {
    size?: number;
    records?: PermissionVO[];
    total?: number;
    current?: number;
    pages?: number;
  };

  type IPageTagVO = {
    size?: number;
    records?: TagVO[];
    total?: number;
    current?: number;
    pages?: number;
  };

  type LoginLogVO = {
    /** 主键id */
    id?: number;
    /** 用户名 */
    username?: string;
    /** 用户id */
    userId?: number;
    /** IP地址 */
    ipAddress?: string;
    /** 登录地点 */
    location?: string;
    /** 登录设备 */
    device?: string;
    /** 登录时间 */
    loginTime?: string;
    /** 1-登录成功，0-登录失败 */
    status?: number;
  };

  type LogSearchParam = {
    pageNum?: number;
    pageSize?: number;
    /** 用户名 */
    username?: string;
    /** IP地址 */
    ipAddress?: string;
  };

  type MemberPlanVO = {
    /** 套餐id */
    id?: number;
    /** 套餐名称 */
    name?: string;
    /** 价格 */
    price?: number;
    /** 描述 */
    description?: string;
    /** 有效期(天) */
    validityDays?: number;
    /** 0-禁用，1-启用 */
    status?: number;
    /** 创建时间 */
    createTime?: string;
  };

  type OperationLogVO = {
    /** 主键id */
    id?: number;
    /** 用户id */
    userId?: number;
    /** 用户名 */
    username?: string;
    /** 操作内容 */
    operationContent?: string;
    /** 操作模块 */
    operationModule?: string;
    /** IP地址 */
    ipAddress?: string;
    /** 操作地址 */
    operationAddress?: string;
    /** 操作时间 */
    operationTime?: string;
    /** 1-成功，0-失败 */
    status?: number;
  };

  type OrderItem = {
    column?: string;
    asc?: boolean;
  };

  type OrderSearchParam = {
    pageNum?: number;
    pageSize?: number;
    /** 用户id */
    userId?: number;
    /** 订单号 */
    orderNo?: string;
    /** 订单状态 */
    status?: number;
  };

  type OrderStatisticsVO = {
    /** 数量 */
    total?: number;
    /** 订单状态,0未支付，1-已支付,2-已取消 */
    status?: number;
    /** 订单金额 */
    totalAmount?: number;
  };

  type OrderVO = {
    /** 订单id */
    id?: number;
    /** 订单编号 */
    orderNumber?: string;
    /** 用户id */
    userId?: number;
    /** 套餐id */
    planId?: number;
    /** 下单日期 */
    orderDate?: string;
    /** 订单金额 */
    amount?: number;
    /** 订单状态,0未支付，1-已支付 */
    status?: number;
    /** 创建时间 */
    createTime?: string;
    /** 用户信息 */
    userVO?: UserVO;
    /** 套餐信息 */
    planVO?: MemberPlanVO;
    /** 用户名 */
    userName?: string;
    /** 套餐名 */
    planName?: string;
    /** 支付二维码 */
    qrCodeUrl?: string;
  };

  type PageMemberPlanVO = {
    records?: MemberPlanVO[];
    total?: number;
    size?: number;
    current?: number;
    orders?: OrderItem[];
    optimizeCountSql?: any;
    searchCount?: any;
    optimizeJoinOfCountSql?: boolean;
    maxLimit?: number;
    countId?: string;
    pages?: number;
  };

  type PageOrderVO = {
    records?: OrderVO[];
    total?: number;
    size?: number;
    current?: number;
    orders?: OrderItem[];
    optimizeCountSql?: any;
    searchCount?: any;
    optimizeJoinOfCountSql?: boolean;
    maxLimit?: number;
    countId?: string;
    pages?: number;
  };

  type PageUserVO = {
    records?: UserVO[];
    total?: number;
    size?: number;
    current?: number;
    orders?: OrderItem[];
    optimizeCountSql?: any;
    searchCount?: any;
    optimizeJoinOfCountSql?: boolean;
    maxLimit?: number;
    countId?: string;
    pages?: number;
  };

  type PayPlanVo = {
    /** 用户id */
    userId?: number;
    /** 套餐id */
    planId?: number;
  };

  type PermissionSearchParam = {
    pageNum?: number;
    pageSize?: number;
  };

  type PermissionVO = {
    /** 权限id */
    id?: number;
    /** 权限名称 */
    permissionName?: string;
    /** 权限编码 */
    permissionCode?: string;
  };

  type planCountParams = {
    date: string;
  };

  type PlanSearchParam = {
    pageNum?: number;
    pageSize?: number;
    /** 套餐名称 */
    name?: string;
    /** 0-禁用，1-启用 */
    status?: number;
  };

  type PlanStatisticsVO = {
    /** 套餐名称 */
    planName?: string;
    /** 购买数量 */
    total?: number;
  };

  type publishArticleParams = {
    id: number;
  };

  type resetPasswordParams = {
    userId: number;
  };

  type ResponseResult = {
    code?: number;
    message?: string;
    data?: any;
  };

  type ResponseResultArticleVO = {
    code?: number;
    message?: string;
    data?: ArticleVO;
  };

  type ResponseResultInteger = {
    code?: number;
    message?: string;
    data?: number;
  };

  type ResponseResultIPageArticleVO = {
    code?: number;
    message?: string;
    data?: IPageArticleVO;
  };

  type ResponseResultIPageCategoryVO = {
    code?: number;
    message?: string;
    data?: IPageCategoryVO;
  };

  type ResponseResultIPageLoginLogVO = {
    code?: number;
    message?: string;
    data?: IPageLoginLogVO;
  };

  type ResponseResultIPageOperationLogVO = {
    code?: number;
    message?: string;
    data?: IPageOperationLogVO;
  };

  type ResponseResultIPagePermissionVO = {
    code?: number;
    message?: string;
    data?: IPagePermissionVO;
  };

  type ResponseResultIPageTagVO = {
    code?: number;
    message?: string;
    data?: IPageTagVO;
  };

  type ResponseResultListCategoryVO = {
    code?: number;
    message?: string;
    data?: CategoryVO[];
  };

  type ResponseResultListMemberPlanVO = {
    code?: number;
    message?: string;
    data?: MemberPlanVO[];
  };

  type ResponseResultListOrderStatisticsVO = {
    code?: number;
    message?: string;
    data?: OrderStatisticsVO[];
  };

  type ResponseResultListPermissionVO = {
    code?: number;
    message?: string;
    data?: PermissionVO[];
  };

  type ResponseResultListPlanStatisticsVO = {
    code?: number;
    message?: string;
    data?: PlanStatisticsVO[];
  };

  type ResponseResultListRoleVO = {
    code?: number;
    message?: string;
    data?: RoleVO[];
  };

  type ResponseResultListTagVO = {
    code?: number;
    message?: string;
    data?: TagVO[];
  };

  type ResponseResultLoginLogVO = {
    code?: number;
    message?: string;
    data?: LoginLogVO;
  };

  type ResponseResultMapLocalDateBoolean = {
    code?: number;
    message?: string;
    data?: Record<string, any>;
  };

  type ResponseResultMemberPlanVO = {
    code?: number;
    message?: string;
    data?: MemberPlanVO;
  };

  type ResponseResultObject = {
    code?: number;
    message?: string;
    data?: any;
  };

  type ResponseResultOperationLogVO = {
    code?: number;
    message?: string;
    data?: OperationLogVO;
  };

  type ResponseResultOrderVO = {
    code?: number;
    message?: string;
    data?: OrderVO;
  };

  type ResponseResultPageMemberPlanVO = {
    code?: number;
    message?: string;
    data?: PageMemberPlanVO;
  };

  type ResponseResultPageOrderVO = {
    code?: number;
    message?: string;
    data?: PageOrderVO;
  };

  type ResponseResultPageUserVO = {
    code?: number;
    message?: string;
    data?: PageUserVO;
  };

  type ResponseResultUserPlan = {
    code?: number;
    message?: string;
    data?: UserPlan;
  };

  type ResponseResultUserVO = {
    code?: number;
    message?: string;
    data?: UserVO;
  };

  type RolePermissionVO = {
    /** 角色id */
    roleId?: number;
    /** 权限id列表 */
    permissionIds?: number[];
  };

  type RoleVO = {
    /** 角色id */
    id?: number;
    /** 角色名称 */
    roleName?: string;
    /** 角色编码 */
    roleCode?: string;
    /** 角色权限 */
    permissionVOList?: PermissionVO[];
  };

  type selectArticleDetailParams = {
    id: number;
  };

  type TagSearchParam = {
    pageNum?: number;
    pageSize?: number;
    tagName?: string;
  };

  type TagVO = {
    /** 主键 */
    id?: number;
    /** 标签名称 */
    name?: string;
    /** 创建时间 */
    createTime?: string;
  };

  type transStatusParams = {
    userId: number;
    status: number;
  };

  type updatePasswordParams = {
    password: string;
  };

  type uploadParams = {
    category: string;
  };

  type UserAddVO = {
    /** 账号 */
    account?: string;
    /** 用户名 */
    userName?: string;
    /** 邮箱 */
    email?: string;
    /** 手机号 */
    phone?: string;
    /** 密码 */
    password?: string;
    /** 确认密码 */
    confirmPassword?: string;
    /** 角色id */
    roleId?: number;
  };

  type UserEditVO = {
    /** 用户ID */
    userId?: number;
    /** 账号 */
    account?: string;
    /** 用户名 */
    userName?: string;
    /** 邮箱 */
    email?: string;
    /** 手机号 */
    phone?: string;
  };

  type UserExpandVo = {
    /** 主键id */
    id?: number;
    /** 用户id */
    userId?: number;
    /** 生日 */
    birthday?: string;
    /** 省 */
    province?: string;
    /** 市 */
    city?: string;
    /** 区/县 */
    district?: string;
    /** 详细地址 */
    address?: string;
    /** 个人简介 */
    bio?: string;
  };

  type UserLoginVO = {
    /** 账号 */
    account?: string;
    /** 邮箱 */
    email?: string;
    /** 手机号 */
    phone?: string;
    /** 密码 */
    password?: string;
    /** 验证码 */
    code?: string;
  };

  type UserPlan = {
    id?: number;
    /** 用户id */
    userId?: number;
    /** 套餐id */
    planId?: number;
    /** 订单id */
    orderId?: number;
    /** 起效日期 */
    startDate?: string;
    /** 失效日期 */
    endDate?: string;
    /** 0-失效，1-有效 */
    status?: number;
  };

  type UserRegisterVO = {
    /** 账号 */
    account?: string;
    /** 邮箱 */
    email?: string;
    /** 手机号 */
    phone?: string;
    /** 密码 */
    password?: string;
    /** 确认密码 */
    confirmPassword?: string;
  };

  type UserSearchParam = {
    pageNum?: number;
    pageSize?: number;
    /** 账号 */
    account?: string;
    /** 用户名 */
    userName?: string;
    /** 邮箱 */
    email?: string;
    /** 手机号 */
    phone?: string;
    /** 0-禁用，1-启用 */
    status?: number;
  };

  type UserVO = {
    /** 用户Id */
    id?: number;
    /** 账号 */
    account?: string;
    /** 用户名 */
    userName?: string;
    /** 邮箱 */
    email?: string;
    /** 手机号 */
    phone?: string;
    /** 0-禁用，1-启用 */
    status?: number;
    /** 创建时间 */
    createTime?: string;
    /** 角色代码 */
    roleCode?: string;
    /** 角色 */
    roleVO?: RoleVO;
    /** 性别 0-女，1-男 */
    gender?: number;
    /** 头像 */
    avatar?: string;
    /** 扩展信息 */
    expandVo?: UserExpandVo;
  };
}
