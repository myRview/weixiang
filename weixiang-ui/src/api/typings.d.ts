declare namespace API {
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

  type deleteUserParams = {
    id: number;
  };

  type deleteUsingDELETEParams = {
    id: number;
  };

  type getByIdParams = {
    id: number;
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

  type getPlanInfoByIdParams = {
    id: number;
  };

  type getRolePermissionParams = {
    id: number;
  };

  type getUserByIdParams = {
    id: number;
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

  type LoginLogEntityduixiang = {
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
    /** 创建时间 */
    createTime?: string;
    /** 更新时间 */
    updateTime?: string;
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

  type OperationLogEntityduixiang = {
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
    /** 创建时间 */
    createTime?: string;
    /** 更新时间 */
    updateTime?: string;
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

  type OrderInfoEntityduixiang = {
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
    /** 更新时间 */
    updateTime?: string;
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

  type PlanSearchParam = {
    pageNum?: number;
    pageSize?: number;
    /** 套餐名称 */
    name?: string;
    /** 0-禁用，1-启用 */
    status?: number;
  };

  type resetPasswordParams = {
    userId: number;
  };

  type ResponseResult = {
    code?: number;
    message?: string;
    data?: any;
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

  type ResponseResultListMemberPlanVO = {
    code?: number;
    message?: string;
    data?: MemberPlanVO[];
  };

  type ResponseResultListPermissionVO = {
    code?: number;
    message?: string;
    data?: PermissionVO[];
  };

  type ResponseResultListRoleVO = {
    code?: number;
    message?: string;
    data?: RoleVO[];
  };

  type ResponseResultLoginLogVO = {
    code?: number;
    message?: string;
    data?: LoginLogVO;
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

  type transStatusParams = {
    userId: number;
    status: number;
  };

  type updatePasswordParams = {
    password: string;
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

  type UserPlanEntityduixiang = {
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
    status?: string;
    /** 创建时间 */
    createTime?: string;
    /** 更新时间 */
    updateTime?: string;
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
  };
}
