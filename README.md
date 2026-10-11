# edu-admin-system

前后端分离的教务管理系统，覆盖部门、员工、班级、学员四类主体的日常管理，
包含登录鉴权、条件分页查询、操作日志与文件上传。

## 技术栈

- 后端：Spring Boot 3、Spring MVC、MyBatis、PageHelper、Spring AOP、JWT、MySQL 8、Maven 多模块
- 前端：Vue 3（组合式 API）、Vue Router、axios、Element Plus、Vite

## 功能

- 登录鉴权：账号密码登录签发 JWT，`LoginFilter` 统一校验请求头令牌，校验失败返回 401
- 部门管理：增删改查；删除前校验该部门下是否仍有员工，违规抛出自定义业务异常
- 员工管理：条件分页查询（动态 SQL）；新增/修改含多段工作经历并开启事务；支持批量删除与头像上传
- 班级管理：条件分页查询，班主任与学科维护，班级状态按开课/结课时间实时计算
- 学员管理：条件分页查询，违纪扣分累加并记录次数，批量删除
- 操作日志：自定义 `@Log` 注解 + 环绕通知，记录类名、方法、参数、返回值与耗时；日志写入使用独立事务
- 统一响应：接口统一返回 `{code, msg, data}`，分页统一 `{total, rows}`；全局异常处理器把异常翻译为可读信息

## 快速开始

### 1. 准备数据库

创建数据库并导入初始化脚本，然后修改 `eduadmin-web-management/src/main/resources/application.yml` 中的连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/your_db
    username: your_user
    password: your_password
```

### 2. 启动后端

```bash
mvn -f eduadmin-parent/pom.xml clean package
```

或在 IDE 中直接运行 `EduAdminApplication`，默认端口 8080。

### 3. 启动前端

```bash
cd frontend
npm install
npm run dev
```

开发环境下 Vite 会把 `/api` 开头的请求代理到 `http://localhost:8080`。

## 接口设计（节选）

| 方法 | 路径 | 用途 |
| --- | --- | --- |
| POST | /login | 登录并签发 JWT |
| GET | /depts | 查询全部部门 |
| POST / PUT / DELETE | /depts | 新增 / 修改 / 删除部门 |
| GET | /emps | 条件分页查询员工 |
| GET | /emps/{id} | 查询员工详情（含多段工作经历） |
| POST / PUT | /emps | 新增 / 修改员工 |
| DELETE | /emps?ids= | 批量删除员工 |
| GET | /clazzs | 条件分页查询班级 |
| GET | /students | 条件分页查询学员 |
| PUT | /students/violation/{id}/{score} | 学员违纪扣分 |
| POST | /upload | 上传头像至对象存储 |
| GET | /log/page | 分页查询操作日志 |

## 实现要点

- **统一响应体**：所有接口返回 `{code, msg, data}`，分页统一为 `{total, rows}`，前端只需一套解析逻辑
- **鉴权**：登录签发 JWT，`LoginFilter` 统一校验请求头中的令牌，失败返回 401
- **操作日志**：自定义 `@Log` 注解配合环绕通知记录操作留痕；日志写入使用独立事务，主业务回滚不影响日志
- **分页**：PageHelper 拦截查询自动追加 limit 并统计总数
- **动态 SQL**：条件查询使用 `<where>` 与 `<if>` 组合，避免为每种条件组合各写一个方法
- **一对多映射**：员工与其多段工作经历通过 `resultMap` 与 `<collection>` 一次查出
- **多模块**：pojo / utils / web 三层拆分，依赖方向单向，便于复用与维护

## 项目结构

```text
├── frontend                      # Vue 3 前端
│   └── src
│       ├── api                   # 接口封装
│       ├── router                # 路由与登录守卫
│       ├── utils/request.js      # axios 实例：统一携带 token、拆响应、处理 401
│       └── views                 # 页面组件（部门 / 员工 / 班级 / 学员）
├── eduadmin-parent               # 聚合 POM，统一版本管理
├── eduadmin-pojo                 # 实体类、统一响应体、查询参数对象
├── eduadmin-utils                # JWT 工具、对象存储封装
└── eduadmin-web-management       # 业务模块
    └── src/main
        ├── java/com/az9374/eduadmin
        │   ├── anno              # 自定义 @Log 注解
        │   ├── aop               # 操作日志切面
        │   ├── config            # MVC 配置（拦截器注册）
        │   ├── controller        # 对外接口
        │   ├── exception         # 全局异常处理与业务异常
        │   ├── filter            # 登录令牌过滤器
        │   ├── interceptor       # Token 拦截器
        │   ├── mapper            # MyBatis Mapper
        │   └── service           # 业务接口与实现
        └── resources
            ├── application.yml
            ├── logback.xml
            └── com/az9374/eduadmin/mapper   # 自定义 SQL
```
