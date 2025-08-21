# 维享空间(Weixiang)
### 后端技术
- **jdk**: Java 17
- **核心框架**: Spring Boot 3.5.3
- **安全框架**: Spring Security
- **ORM框架**: MyBatis-Plus
- **数据库**: MySQL
- **缓存**: Redis
- **文件存储**: 阿里云OSS, 腾讯云COS
- **消息队列**: RabbitMQ
- **支付**:  支付宝沙箱支付
- **认证授权**: JWT
- **API文档**: Knife4j (OpenAPI)
- **工具库**: Lombok, Hutool, Apache Commons
- **构建工具**: Maven

### 前端技术
- **框架**: Vue 3
- **语言**: TypeScript
- **UI组件库**: Element Plus
- **状态管理**: Pinia
- **网络请求**: Axios
- **图表库**: ECharts
- **构建工具**: npm

## 功能特点
1. **用户认证与授权**：基于JWT的身份验证和权限控制
2. **操作日志记录**：记录用户操作行为，支持审计追踪
3. **支付套餐管理**：提供不同套餐的订阅与支付功能
4. **错误处理机制**：统一的错误码和异常处理
5. **文件存储**：支持阿里云OSS和腾讯云COS等对象存储服务
6. **API文档**：自动生成的接口文档，便于前后端协作

## 快速开始

### 后端启动
1. 确保安装了JDK 17+和Maven
2. 导入SQL脚本 `sql/weixiang_resource.sql`
3. 配置 `bootstrap/src/main/resources/application.yml` 中的数据库和其他服务参数
4. 运行 `BootstrapApplication` 类启动后端服务

### 前端启动
1. 确保安装了Node.js 18+
2. 进入前端目录 `cd weixiang-ui`
3. 安装依赖 `npm install`
4. 启动开发服务器 `npm run serve`
5. 访问 http://localhost:8080

## API文档
- **作者**: huangkun
- **版本**: 1.0.0
- **文档**: 访问 http://localhost:8081/doc.html 查看API文档