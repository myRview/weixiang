export enum PushTypeEnum {
  // 系统消息
  SYSTEM = 'system',
  // 用户消息
  USER = 'user',
  // 文章消息
  ARTICLE = 'article',
  // 评论消息
  COMMENT = 'comment',
  // 订单消息
  ORDER = 'order',
}

// 消息类型描述映射
export const PushTypeDescription: Record<PushTypeEnum, string> = {
  [PushTypeEnum.SYSTEM]: '系统消息',
  [PushTypeEnum.USER]: '用户消息',
  [PushTypeEnum.ARTICLE]: '文章消息',
  [PushTypeEnum.COMMENT]: '评论消息',
  [PushTypeEnum.ORDER]: '订单消息',
};