// 状态枚举类型定义
interface StatusEnum {
    code: number;
    description: string;
}

// 状态枚举值
const StatusEnums: { [key: string]: StatusEnum } = {
    NORMAL: { code: 1, description: '正常' },
    DISABLE: { code: 0, description: '禁用' }
};


 export {
    StatusEnums,
 };