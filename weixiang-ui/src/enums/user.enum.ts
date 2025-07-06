// 用户角色枚举值
 export const USER_ROLE_ENUM = {
  USER: "user",
  ADMIN: "admin"
} as const;

// 用户角色描述映射
 export const USER_ROLE_MAP: Record<string, string> = {
  "user": "普通用户",
  "admin": "管理员"
};

// 用户角色选项（用于下拉选择等场景）
 export const USER_ROLE_OPTIONS = Object.entries(USER_ROLE_MAP).map(([value, label]) => ({
  label,
  value
}));