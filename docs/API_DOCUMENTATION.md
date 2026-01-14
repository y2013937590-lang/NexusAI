# NexusAI API 文档

> 版本: v1.0  
> 最后更新: 2025-01-14

## 基础信息

### 接入地址
- **开发环境**: `http://localhost:8080`
- **测试环境**: `待部署`
- **生产环境**: `待部署`

### 认证方式
当前版本暂未启用认证，后续版本将支持：
- AppKey/AppSecret 认证
- JWT Token 认证

### 统一响应格式

所有API返回格式统一为：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {},
  "timestamp": 1705228800000
}
```

**字段说明：**
- `code`: 状态码，200表示成功，其他表示失败
- `message`: 响应消息
- `data`: 响应数据
- `timestamp`: 时间戳(毫秒)

## 核心服务 API

### 1. 健康检查

**接口**: `GET /api/v1/health`

**描述**: 检查服务健康状态

**请求示例**:
```bash
curl http://localhost:8080/api/v1/health
```

**响应示例**:
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

### 2. 连通性测试

**接口**: `GET /api/v1/ping`

**描述**: 简单的连通性测试

**请求示例**:
```bash
curl http://localhost:8080/api/v1/ping
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": "pong",
  "timestamp": 1705228800000
}
```

### 3. 简单对话

**接口**: `POST /api/v1/chat/simple`

**描述**: 与AI进行简单对话

**请求参数**:
```json
{
  "message": "你好，请介绍一下你自己"
}
```

**请求示例**:
```bash
curl -X POST http://localhost:8080/api/v1/chat/simple \
  -H "Content-Type: application/json" \
  -d '{"message":"你好，请介绍一下你自己"}'
```

**响应示例**:
```json
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

### 4. LLM连接测试

**接口**: `GET /api/v1/chat/test`

**描述**: 测试LLM连接状态

**请求示例**:
```bash
curl http://localhost:8080/api/v1/chat/test
```

**响应示例**:
```json
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
```

## 管理服务 API

### 1. 健康检查

**接口**: `GET /api/v1/admin/health`

**描述**: 检查管理服务健康状态

**请求示例**:
```bash
curl http://localhost:8080/api/v1/admin/health
```

**响应示例**:
```json
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

## 监控端点

### 1. 健康检查

**接口**: `GET /actuator/health`

**描述**: Spring Boot Actuator健康检查

**请求示例**:
```bash
curl http://localhost:8080/actuator/health
```

### 2. Prometheus指标

**接口**: `GET /actuator/prometheus`

**描述**: Prometheus格式的监控指标

**请求示例**:
```bash
curl http://localhost:8080/actuator/prometheus
```

### 3. 所有指标

**接口**: `GET /actuator/metrics`

**描述**: 查看所有可用的监控指标

**请求示例**:
```bash
curl http://localhost:8080/actuator/metrics
```

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 403 | 无权限 |
| 404 | 资源不存在 |
| 429 | 请求过于频繁 |
| 500 | 服务器内部错误 |
| 503 | 服务暂时不可用 |

## 使用示例

### Python
```python
import requests

# 健康检查
response = requests.get('http://localhost:8080/api/v1/health')
print(response.json())

# 对话
response = requests.post(
    'http://localhost:8080/api/v1/chat/simple',
    json={'message': '你好'}
)
print(response.json())
```

### Java
```java
// 使用OkHttp
OkHttpClient client = new OkHttpClient();

// 健康检查
Request request = new Request.Builder()
    .url("http://localhost:8080/api/v1/health")
    .build();

Response response = client.newCall(request).execute();
System.out.println(response.body().string());
```

### JavaScript
```javascript
// 健康检查
fetch('http://localhost:8080/api/v1/health')
  .then(response => response.json())
  .then(data => console.log(data));

// 对话
fetch('http://localhost:8080/api/v1/chat/simple', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    message: '你好'
  })
})
  .then(response => response.json())
  .then(data => console.log(data));
```

## 注意事项

1. **API Key配置**: 使用对话功能前需要配置OpenAI API Key
2. **速率限制**: 当前版本未启用限流，生产环境建议配置
3. **数据安全**: 请不要在请求中包含敏感信息
4. **超时设置**: 建议客户端设置60秒超时

## 更新日志

### v1.0 (2025-01-14)
- ✅ 基础健康检查接口
- ✅ 简单对话接口
- ✅ LLM连接测试接口
- ✅ 监控端点

---

**联系方式**  
技术支持: [待填写]  
问题反馈: [待填写]
