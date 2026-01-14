# NexusAI 快速开始指南

## 📦 项目结构

```
NexusAI/
├── nexusai-gateway/           # API网关 (端口: 8080)
│   ├── 路由配置
│   ├── 跨域处理
│   ├── 全局异常处理
│   └── 统一响应格式
├── nexusai-service/           # 核心服务 (端口: 8081)
│   └── 对话、知识库、编排引擎 [待开发]
├── nexusai-admin/             # 管理服务 (端口: 8082)
│   └── 应用管理、模型管理 [待开发]
├── nexusai-engine/            # LangChain4j引擎封装 [待开发]
└── nexusai-common/            # 通用组件
    ├── 统一响应格式 (Result)
    └── 通用常量
```

## 🚀 启动步骤

### 方式一: 使用IDE (推荐)

1. **导入项目**
   - 打开IntelliJ IDEA
   - File → Open → 选择项目根目录
   - 等待Maven依赖下载完成

2. **启动服务**
   
   按以下顺序启动:
   
   ① **nexusai-gateway** (必须)
   ```
   运行: io.github.fyy.nexusai.gateway.GatewayApplication
   端口: 8080
   ```
   
   ② **nexusai-service** (必须)
   ```
   运行: io.github.fyy.nexusai.service.ServiceApplication
   端口: 8081
   ```
   
   ③ **nexusai-admin** (可选)
   ```
   运行: io.github.fyy.nexusai.admin.AdminApplication
   端口: 8082
   ```

### 方式二: 使用Maven命令

```bash
# 1. 编译项目
mvn clean install -DskipTests

# 2. 启动各个服务 (新开终端窗口)
cd nexusai-gateway && mvn spring-boot:run
cd nexusai-service && mvn spring-boot:run
cd nexusai-admin && mvn spring-boot:run
```

## ✅ 验证服务

### 1. 检查服务启动状态

**网关健康检查:**
```bash
curl http://localhost:8080/actuator/health
```

**核心服务健康检查 (通过网关):**
```bash
curl http://localhost:8080/api/v1/health
```
预期响应:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "status": "UP",
    "service": "nexusai-service",
    "version": "1.0.0",
    "timestamp": 1705228800000
  },
  "timestamp": 1705228800000
}
```

**管理服务健康检查 (通过网关):**
```bash
curl http://localhost:8080/api/v1/admin/health
```

### 2. 查看监控指标

**Prometheus指标:**
```bash
curl http://localhost:8080/actuator/prometheus
curl http://localhost:8081/actuator/prometheus
curl http://localhost:8082/actuator/prometheus
```

## 🎯 当前可用接口

### 核心服务 (nexusai-service)

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 健康检查 | GET | /api/v1/health | 服务状态检查 |
| Ping | GET | /api/v1/ping | 简单连通性测试 |

### 管理服务 (nexusai-admin)

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 健康检查 | GET | /api/v1/admin/health | 服务状态检查 |
| Ping | GET | /api/v1/admin/ping | 简单连通性测试 |

## 📊 监控端点

| 端点 | URL | 说明 |
|------|-----|------|
| 健康检查 | http://localhost:{port}/actuator/health | 服务健康状态 |
| 应用信息 | http://localhost:{port}/actuator/info | 应用基本信息 |
| 指标监控 | http://localhost:{port}/actuator/metrics | 所有可用指标 |
| Prometheus | http://localhost:{port}/actuator/prometheus | Prometheus格式指标 |

## 🔧 配置说明

### 端口配置
- Gateway: 8080
- Service: 8081
- Admin: 8082

### 路由规则
- `/api/v1/**` → nexusai-service
- `/api/v1/admin/**` → nexusai-admin

### CORS配置
- 已配置全局跨域支持
- 允许所有来源 (开发环境)
- 生产环境需要配置具体域名

## 📝 下一步开发

当前完成 **Week 1 Day 1-2** 任务:
- [x] 多模块Maven工程结构
- [x] Spring Boot 3.2.1 + Java 17
- [x] Spring Cloud Gateway集成
- [x] 统一响应格式
- [x] 基础监控

**即将开发 (Week 1 Day 3):**
- [ ] 数据库表结构设计
- [ ] MySQL + Flyway集成
- [ ] Redis集成
- [ ] LangChain4j集成

详见: [DEVELOPMENT_ROADMAP.md](DEVELOPMENT_ROADMAP.md)

## 🐛 常见问题

### 1. 端口已被占用

错误: `Port 8080 was already in use`

解决:
```bash
# 查找占用端口的进程
lsof -i :8080
# 或
netstat -ano | findstr :8080

# 关闭进程或修改配置文件端口
```

### 2. Maven依赖下载失败

解决:
```bash
# 使用阿里云镜像 (修改 ~/.m2/settings.xml)
# 或强制更新依赖
mvn clean install -U
```

### 3. 服务间调用失败

检查:
- 确保Gateway已启动
- 确保目标服务已启动
- 检查端口配置是否正确

## 📚 相关文档

- [项目立项文档](PROJECT_PROPOSAL.md) - 项目背景和核心功能
- [开发路线图](DEVELOPMENT_ROADMAP.md) - 24周详细开发计划
- [开发日志](DEVELOPMENT_LOG.md) - 每日开发记录

---

**更新时间**: 2025-01-14  
**当前版本**: v1.0-SNAPSHOT  
**当前阶段**: Phase 1 - 基础设施搭建
