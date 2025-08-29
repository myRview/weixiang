// 订单状态枚举值
 export const ORDER_STATUS_ENUM = {
  WAIT_PAY: 0,
  FINISHED: 1,
  CANCEL: 2
} as const;

// 订单状态描述映射
 export const ORDER_STATUS_MAP: Record<number, string> = {
  0: '待支付',
  1: '已完成',
  2: '已取消',
  3: '已退款',
};

// 订单状态选项（用于下拉选择等场景）
 export const ORDER_STATUS_OPTIONS = Object.entries(ORDER_STATUS_MAP).map(([value, label]) => ({
  label,
  value: Number(value)
}));