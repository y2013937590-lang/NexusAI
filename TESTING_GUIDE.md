# NexusAI 测试指南

> 版本: v1.0  
> 最后更新: 2025-01-14

## 🧪 测试环境准备

### 1. 启动MySQL

```bash
# 启动MySQL服务
mysql.server start

# 或使用Docker
docker run -d \
  --name nexusai-mysql \
  -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=nexusai \
  mysql:8.0
```

### 2. 启动Redis

```bash
# 启动Redis服务
redis-server

# 或使用Docker
docker run -d \
  --name nexusai-redis \
  -p 6379:6379 \
  redis:7.0
```

### 3. 配置OpenAI API Key

```bash
# 设置环境变量
export OPENAI_API_KEY="your-openai-api-key-here"

# 或修改配置文件
# nexusai-service/src/main/resources/application.yml
```

## 🚀 启动服务

### 方式一: IDE启动(推荐)

1. 启动 `nexusai-gateway/GatewayApplication` (端口8080)
2. 启动 `nexusai-service/ServiceApplication` (端口8081)
3. 启动 `nexusai-admin/AdminApplication` (端口8082)

### 方式二: 命令行启动

```bash
# Terminal 1 - 启动网关
cd nexusai-gateway && mvn spring-boot:run

# Terminal 2 - 启动核心服务
cd nexusai-service && mvn spring-boot:run

# Terminal 3 - 启动管理服务
cd nexusai-admin && mvn spring-boot:run
```

## ✅ 功能测试

### 1. 健康检查测试

**测试网关**
```bash
curl http://localhost:8080/actuator/health

# 预期结果
{
  "status": "UP",
  "components": {
    "diskSpace": {"status": "UP"},
    "ping": {"status": "UP"},
    "redis": {"status": "UP"}
  }
}
```

**测试核心服务**
```bash
curl http://localhost:8080/api/v1/health

# 预期结果
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

**测试管理服务**
```bash
curl http://localhost:8080/api/v1/admin/health

# 预期结果
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "status": "UP",
    "service": "nexusai-admin",
    "version": "1.0.0",
    "timestamp": 1705228800000
  },
  "timestamp": 1705228800000
}
```

### 2. 连通性测试

```bash
# Ping测试 - 核心服务
curl http://localhost:8080/api/v1/ping

# 预期结果
{
  "code": 200,
  "message": "操作成功",
  "data": "pong",
  "timestamp": 1705228800000
}

# Ping测试 - 管理服务
curl http://localhost:8080/api/v1/admin/ping

# 预期结果
{
  "code": 200,
  "message": "操作成功",
  "data": "pong",
  "timestamp": 1705228800000
}
```

### 3. LLM连接测试

```bash
curl http://localhost:8080/api/v1/chat/test

# 预期结果
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "connected": true,
    "model": "gpt-3.5-turbo",
    "message": "LLM连接成功"
  },
  "timestamp": 1705228800000
}

# 如果失败
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "connected": false,
    "model": "gpt-3.5-turbo",
    "message": "LLM连接失败"
  },
  "timestamp": 1705228800000
}
```

### 4. 简单对话测试

**测试1: Hello**
```bash
curl -X POST http://localhost:8080/api/v1/chat/simple \
  -H "Content-Type: application/json" \
  -d '{"message":"Hello"}'

# 预期结果
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "message": "Hello! How can I assist you today?",
    "model": "gpt-3.5-turbo"
  },
  "timestamp": 1705228800000
}
```

**测试2: 中文对话**
```bash
curl -X POST http://localhost:8080/api/v1/chat/simple \
  -H "Content-Type: application/json" \
  -d '{"message":"你好，请介绍一下你自己"}'

# 预期结果
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "message": "你好！我是一个AI助手...",
    "model": "gpt-3.5-turbo"
  },
  "timestamp": 1705228800000
}
```

**测试3: 复杂问题**
```bash
curl -X POST http://localhost:8080/api/v1/chat/simple \
  -H "Content-Type: application/json" \
  -d '{"message":"用Python实现一个快速排序算法"}'
```

**测试4: 空消息(错误测试)**
```bash
curl -X POST http://localhost:8080/api/v1/chat/simple \
  -H "Content-Type: application/json" \
  -d '{"message":""}'

# 预期结果
{
  "code": 500,
  "message": "消息不能为空",
  "data": null,
  "timestamp": 1705228800000
}
```

### 5. 数据库连接测试

```bash
# 查看数据库连接状态
curl http://localhost:8081/actuator/health | jq '.components.db'

# 查看Druid监控页面
open http://localhost:8081/druid/index.html
# 用户名: admin
# 密码: admin123
```

### 6. Redis连接测试

```bash
# 查看Redis连接状态
curl http://localhost:8081/actuator/health | jq '.components.redis'

# 使用redis-cli测试
redis-cli ping
# 应该返回: PONG

# 查看Redis键
redis-cli KEYS "nexusai:*"
```

### 7. 监控指标测试

**Prometheus指标**
```bash
# 网关指标
curl http://localhost:8080/actuator/prometheus | grep "http_server"

# 服务指标
curl http://localhost:8081/actuator/prometheus | grep "jvm_memory"

# 查看所有可用指标
curl http://localhost:8081/actuator/metrics
```

**JVM指标**
```bash
# 内存使用
curl http://localhost:8081/actuator/metrics/jvm.memory.used

# GC次数
curl http://localhost:8081/actuator/metrics/jvm.gc.pause

# 线程数
curl http://localhost:8081/actuator/metrics/jvm.threads.live
```

**HTTP指标**
```bash
# 请求总数
curl http://localhost:8081/actuator/metrics/http.server.requests

# 响应时间
curl http://localhost:8081/actuator/metrics/http.server.requests | jq
```

## 📊 性能测试

### 1. 使用Apache Bench

```bash
# 健康检查接口压测
ab -n 1000 -c 10 http://localhost:8080/api/v1/health

# 查看结果
# Requests per second: XXX [#/sec] (mean)
# Time per request: XXX [ms] (mean)
```

### 2. 使用wrk

```bash
# 安装wrk
brew install wrk  # macOS
# apt-get install wrk  # Ubuntu

# 压测健康检查
wrk -t4 -c100 -d30s http://localhost:8080/api/v1/health

# 压测对话接口
wrk -t4 -c10 -d30s -s post.lua http://localhost:8080/api/v1/chat/simple
```

## 🐛 常见问题排查

### 1. 服务启动失败

**检查端口占用**
```bash
# macOS/Linux
lsof -i :8080
lsof -i :8081
lsof -i :8082

# Windows
netstat -ano | findstr :8080
```

**检查日志**
```bash
# 查看控制台输出
# 或查看日志文件
tail -f logs/nexusai-*.log
```

### 2. 数据库连接失败

**检查MySQL**
```bash
# 测试连接
mysql -h localhost -u root -p

# 查看数据库
SHOW DATABASES;
USE nexusai;
SHOW TABLES;
```

**检查Flyway迁移**
```bash
# 查看迁移历史
mysql -u root -p nexusai -e "SELECT * FROM flyway_schema_history;"
```

### 3. Redis连接失败

**检查Redis**
```bash
# 测试连接
redis-cli ping

# 查看配置
redis-cli CONFIG GET "*"

# 查看键
redis-cli KEYS "*"
```

### 4. OpenAI API调用失败

**检查API Key**
```bash
# 验证环境变量
echo $OPENAI_API_KEY

# 测试API Key
curl https://api.openai.com/v1/models \
  -H "Authorization: Bearer $OPENAI_API_KEY"
```

**检查网络**
```bash
# 测试连接
ping api.openai.com

# 如果需要代理
export https_proxy=http://your-proxy:port
```

## 📝 测试清单

### Week 1 功能测试清单

- [x] 网关启动成功
- [x] 核心服务启动成功
- [x] 管理服务启动成功
- [x] 健康检查接口可用
- [x] 连通性测试通过
- [x] 数据库连接成功
- [x] Redis连接成功
- [x] Flyway迁移成功(6张表)
- [x] LLM连接测试通过
- [x] 简单对话功能可用
- [x] Prometheus指标可访问
- [x] 跨域配置生效
- [x] 异常处理正常
- [x] 响应时间统计正常
- [x] 请求日志记录正常

### Week 2 功能测试清单(待完成)

- [ ] 应用注册接口
- [ ] AppKey认证
- [ ] JWT Token生成
- [ ] 鉴权过滤器
- [ ] 限流功能
- [ ] 审计日志

## 🔧 测试工具推荐

### REST API测试
- **Postman** - 图形化接口测试
- **curl** - 命令行测试
- **HTTPie** - 更友好的curl

### 性能测试
- **Apache Bench (ab)** - 简单压测
- **wrk** - 高性能压测
- **JMeter** - 完整压测方案

### 监控查看
- **Prometheus** - 指标收集
- **Grafana** - 可视化面板
- **Druid** - 数据库监控

## 📚 参考资料

- [Spring Boot Testing](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing)
- [REST API Testing](https://www.postman.com/api-testing/)
- [Performance Testing](https://github.com/wg/wrk)

---

**编写人**: AI Assistant  
**审核人**: [待填写]  
**最后更新**: 2025-01-14
