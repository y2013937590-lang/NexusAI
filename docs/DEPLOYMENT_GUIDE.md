# NexusAI 部署指南

> 版本: v1.0  
> 最后更新: 2025-01-14

## 环境要求

### 基础环境
- **JDK**: 17 或更高版本
- **Maven**: 3.8+ 
- **MySQL**: 8.0+
- **Redis**: 7.0+

### 推荐配置

**开发环境**:
- CPU: 2核+
- 内存: 4GB+
- 磁盘: 20GB+

**生产环境**:
- CPU: 4核+
- 内存: 8GB+
- 磁盘: 100GB+

## 安装步骤

### 1. 克隆代码

```bash
git clone https://github.com/your-org/NexusAI.git
cd NexusAI
```

### 2. 配置MySQL数据库

```sql
-- 创建数据库
CREATE DATABASE nexusai CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建用户(可选)
CREATE USER 'nexusai'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON nexusai.* TO 'nexusai'@'localhost';
FLUSH PRIVILEGES;
```

### 3. 配置Redis

确保Redis服务已启动：
```bash
# 启动Redis
redis-server

# 检查Redis状态
redis-cli ping
# 应该返回: PONG
```

### 4. 配置OpenAI API Key

#### 方式一: 环境变量(推荐)
```bash
export OPENAI_API_KEY="your-openai-api-key"
```

#### 方式二: 修改配置文件
编辑 `nexusai-service/src/main/resources/application.yml`:
```yaml
langchain4j:
  openai:
    api-key: your-openai-api-key
```

### 5. 编译项目

```bash
mvn clean install -DskipTests
```

### 6. 启动服务

#### 方式一: 使用Maven(开发环境)

**启动API网关**:
```bash
cd nexusai-gateway
mvn spring-boot:run
```

**启动核心服务**:
```bash
cd nexusai-service
mvn spring-boot:run
```

**启动管理服务**:
```bash
cd nexusai-admin
mvn spring-boot:run
```

#### 方式二: 使用Jar包(生产环境)

**编译Jar包**:
```bash
mvn clean package -DskipTests
```

**启动服务**:
```bash
# 启动网关
java -jar nexusai-gateway/target/nexusai-gateway-1.0-SNAPSHOT.jar &

# 启动核心服务
java -jar nexusai-service/target/nexusai-service-1.0-SNAPSHOT.jar &

# 启动管理服务
java -jar nexusai-admin/target/nexusai-admin-1.0-SNAPSHOT.jar &
```

## 配置说明

### 数据库配置

编辑 `application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/nexusai?...
    username: root
    password: your_password
```

### Redis配置

```yaml
spring:
  redis:
    host: localhost
    port: 6379
    password: your_redis_password  # 如果没有密码可以留空
```

### JVM参数

**开发环境**:
```bash
java -Xms512m -Xmx1024m -jar app.jar
```

**生产环境**:
```bash
java -Xms2g -Xmx4g \
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=200 \
  -XX:+HeapDumpOnOutOfMemoryError \
  -XX:HeapDumpPath=/var/log/nexusai/heapdump.hprof \
  -jar app.jar
```

## 验证部署

### 1. 检查服务状态

```bash
# 网关健康检查
curl http://localhost:8080/actuator/health

# 核心服务健康检查
curl http://localhost:8080/api/v1/health

# 管理服务健康检查
curl http://localhost:8080/api/v1/admin/health
```

### 2. 测试对话功能

```bash
curl -X POST http://localhost:8080/api/v1/chat/simple \
  -H "Content-Type: application/json" \
  -d '{"message":"Hello"}'
```

### 3. 查看监控指标

访问: `http://localhost:8080/actuator/prometheus`

## Docker部署

### 1. 构建镜像

```bash
# 构建网关镜像
docker build -t nexusai-gateway:latest -f nexusai-gateway/Dockerfile .

# 构建服务镜像
docker build -t nexusai-service:latest -f nexusai-service/Dockerfile .

# 构建管理镜像
docker build -t nexusai-admin:latest -f nexusai-admin/Dockerfile .
```

### 2. 使用Docker Compose

创建 `docker-compose.yml`:
```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: nexusai
    ports:
      - "3306:3306"
    volumes:
      - mysql-data:/var/lib/mysql

  redis:
    image: redis:7.0
    ports:
      - "6379:6379"

  nexusai-gateway:
    image: nexusai-gateway:latest
    ports:
      - "8080:8080"
    depends_on:
      - redis

  nexusai-service:
    image: nexusai-service:latest
    ports:
      - "8081:8081"
    environment:
      OPENAI_API_KEY: ${OPENAI_API_KEY}
    depends_on:
      - mysql
      - redis

  nexusai-admin:
    image: nexusai-admin:latest
    ports:
      - "8082:8082"
    depends_on:
      - mysql
      - redis

volumes:
  mysql-data:
```

启动:
```bash
docker-compose up -d
```

## 监控配置

### Prometheus

`prometheus.yml`:
```yaml
scrape_configs:
  - job_name: 'nexusai-gateway'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['localhost:8080']

  - job_name: 'nexusai-service'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['localhost:8081']

  - job_name: 'nexusai-admin'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['localhost:8082']
```

### Grafana

1. 添加Prometheus数据源
2. 导入Spring Boot Dashboard (ID: 11378)
3. 自定义业务指标面板

## 常见问题

### 1. 数据库连接失败

**错误**: `Communications link failure`

**解决**:
- 检查MySQL是否启动
- 检查连接地址和端口
- 检查用户名密码
- 检查防火墙设置

### 2. Redis连接失败

**错误**: `Unable to connect to Redis`

**解决**:
- 检查Redis是否启动
- 检查连接地址和端口
- 检查密码配置

### 3. OpenAI API调用失败

**错误**: `Unauthorized` 或 `Invalid API Key`

**解决**:
- 检查API Key是否正确
- 检查API Key是否过期
- 检查网络连接
- 检查是否需要代理

### 4. 端口被占用

**错误**: `Port 8080 already in use`

**解决**:
```bash
# 查找占用端口的进程
lsof -i :8080

# 杀死进程
kill -9 <PID>

# 或修改配置文件端口
```

## 日志管理

### 日志位置
- 默认: 控制台输出
- 生产: `/var/log/nexusai/`

### 日志配置

在 `application.yml` 中配置:
```yaml
logging:
  level:
    root: INFO
    io.github.fyy.nexusai: DEBUG
  file:
    path: /var/log/nexusai
    name: ${spring.application.name}.log
  pattern:
    file: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n"
```

## 性能优化

### 1. JVM调优
```bash
-XX:+UseG1GC
-XX:MaxGCPauseMillis=200
-XX:InitiatingHeapOccupancyPercent=45
-XX:G1HeapRegionSize=16m
```

### 2. 数据库连接池
```yaml
spring:
  datasource:
    druid:
      initial-size: 10
      max-active: 50
      min-idle: 10
```

### 3. Redis连接池
```yaml
spring:
  redis:
    lettuce:
      pool:
        max-active: 16
        max-idle: 8
        min-idle: 0
```

## 备份与恢复

### 数据库备份
```bash
# 备份
mysqldump -u root -p nexusai > nexusai_backup_$(date +%Y%m%d).sql

# 恢复
mysql -u root -p nexusai < nexusai_backup_20250114.sql
```

### Redis备份
```bash
# 手动保存
redis-cli SAVE

# 备份RDB文件
cp /var/lib/redis/dump.rdb /backup/dump_$(date +%Y%m%d).rdb
```

---

**技术支持**  
Email: [待填写]  
文档更新: [待填写]
