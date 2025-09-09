
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
1. **用户认证与授权**：基于JWT的身份验证和权限控制，使用security权限校验框架实现方法级别的校验
2. **操作日志记录**：记录用户操作行为，支持审计追踪，使用Elasticsearch进行日志存储和检索，通过定时任务更新
3. **支付套餐管理**：提供不同套餐的订阅与支付功能，对接支付宝实现支付，退款等功能
4. **错误处理机制**：使用全局异常处理器，统一的错误码和异常处理
5. **文件存储**：支持阿里云OSS和腾讯云COS等对象存储服务
6. **API文档**：自动生成的接口文档，便于前后端协作
7. **Nginx反向代理和限流**：使用Nginx实现系统的反向代理功能，隐藏项目的实际接口地址以及对象存储的访问地址，并且通过限流功能增强系统的可用性
## 系统介绍
维享空间是一个基于Spring Boot和Vue 3开发的资源共享平台。系统采用Elasticsearch进行文章全文搜索，支持滚动分页。
用户可以注册、登录、发布文章、评论、点赞、签到等功能。系统还集成了支付功能，用户可以购买会员套餐，享受更多特权。
## 系统架构图
## 视频效果展示
https://vimeo.com/1116977446
## 界面效果
#### 登录注册页面展示（支持账号、短信、邮箱三种方式注册登录）
<img width="639" height="392" alt="login" src="https://github.com/user-attachments/assets/7b833537-daa9-431a-abfb-bba4054734ce" />
<img width="633" height="375" alt="register" src="https://github.com/user-attachments/assets/b263d54d-c7a9-45ca-a49e-134cabb710f0" />

#### 系统首页展示（支持滚动分页）
<img width="637" height="383" alt="home" src="https://github.com/user-attachments/assets/06276a1b-5458-417f-8906-1ef38ea7f61f" />
<img width="624" height="357" alt="admin-home" src="https://github.com/user-attachments/assets/499114b7-aade-4e74-91a3-70a28353e29b" />

#### 文章审批和我的文章页面
<img width="615" height="305" alt="article-approval" src="https://github.com/user-attachments/assets/a1c637b6-bbbf-4043-b014-f53013a7e7fa" />
<img width="606" height="240" alt="my-articles" src="https://github.com/user-attachments/assets/10bcb571-66b8-416d-8e26-bfee17eef761" />

#### 用户详情页面和用户主页
<img width="535" height="281" alt="User-detail" src="https://github.com/user-attachments/assets/5bda5d8f-b995-442d-8a46-4854cbb3b817" />
<img width="535" height="289" alt="User-home" src="https://github.com/user-attachments/assets/aacab049-9d4f-4dcd-9a0e-260d55182a78" />

#### 订单数据统计页面和订单管理页面
<img width="543" height="322" alt="data-analyse" src="https://github.com/user-attachments/assets/ea169c7b-68f7-44a2-8af0-bb356c947377" />
<img width="615" height="305" alt="order-manager" src="https://github.com/user-attachments/assets/009a391f-b390-436b-950b-17ad313fb3f4" />

#### 文章详情页面
<img width="440" height="261" alt="article-detail" src="https://github.com/user-attachments/assets/02c6179f-513e-40b9-80af-f932b573a484" />

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
