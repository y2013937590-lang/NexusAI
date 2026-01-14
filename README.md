# NexusAI - 企业级LLM服务中间件平台

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.1-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![LangChain4j](https://img.shields.io/badge/LangChain4j-0.25.0-blue.svg)](https://github.com/langchain4j/langchain4j)

## 📖 项目简介

NexusAI是一个开箱即用的LLM服务中间件，为不同业务类型的应用提供标准化、可靠、易用的AI能力接入。它基于LangChain4j框架构建，补齐了企业级生产环境所需的治理、编排、监控等能力。

## ✨ 核心特性

- 🚀 **开箱即用**: RESTful API + Web控制台，零配置快速接入
- 🔐 **多租户隔离**: 支持多应用接入，独立配额和数据隔离
- 🛡️ **企业级可靠性**: 内置限流、熔断、降级、缓存等高可用机制
- 💰 **成本可控**: Token计量统计、语义缓存、智能路由
- ⚡ **创新编排引擎**: 基于DAG的任务编排，实现复杂AI工作流自动化

## 🏗️ 项目结构

```
NexusAI/
├── nexusai-gateway/           # API网关 - 认证、限流、路由
├── nexusai-service/           # 核心服务 - 对话、知识库、编排引擎
├── nexusai-admin/             # 管理服务 - 应用管理、模型管理
├── nexusai-engine/            # LangChain4j引擎封装
├── nexusai-common/            # 通用组件 - 安全、缓存、限流
└── nexusai-console/           # Web控制台 (Vue3) [待开发]
```

## 🚀 快速开始

### 环境要求

- JDK 17+
- Maven 3.8+
- MySQL 8.0+
- Redis 7.0+

### 编译项目

```bash
mvn clean install
```

### 启动服务

**1. 启动API网关**
```bash
cd nexusai-gateway
mvn spring-boot:run
```
访问: http://localhost:8080

**2. 启动核心服务**
```bash
cd nexusai-service
mvn spring-boot:run
```
访问: http://localhost:8081

**3. 启动管理服务**
```bash
cd nexusai-admin
mvn spring-boot:run
```
访问: http://localhost:8082

### 健康检查

```bash
# 通过网关访问核心服务
curl http://localhost:8080/api/v1/health

# 通过网关访问管理服务
curl http://localhost:8080/api/v1/admin/health
```

### 监控端点

- Prometheus指标: http://localhost:8080/actuator/prometheus
- 健康检查: http://localhost:8080/actuator/health

## 📚 技术栈

### 后端框架
- Spring Boot 3.2.1
- Spring Cloud Gateway
- LangChain4j 0.25.0

### 数据存储
- MySQL 8.0 (关系数据)
- Redis 7.0 (缓存/限流)
- Milvus (向量数据库) [待集成]

### 监控
- Prometheus + Grafana
- Micrometer Tracing

## 📅 开发进度

当前状态: **Phase 1 - 基础设施搭建** ✅ Week 1 完成

- [x] 创建多模块Maven工程结构
- [x] 配置Spring Boot 3.x + Java 17环境
- [x] 集成Spring Cloud Gateway
- [x] 设计数据库表结构(6张表)
- [x] 集成MySQL + Flyway
- [x] 集成Redis + 工具类
- [x] 集成LangChain4j
- [x] 实现简单对话功能
- [x] 集成Prometheus监控
- [x] 完善项目文档
- [ ] 实现API认证鉴权 (Week 2)
- [ ] 应用管理功能 (Week 2)

详细开发计划请查看: [DEVELOPMENT_ROADMAP.md](DEVELOPMENT_ROADMAP.md)

## 📝 文档

- [项目立项文档](PROJECT_PROPOSAL.md)
- [开发路线图](DEVELOPMENT_ROADMAP.md)
- [开发日志](DEVELOPMENT_LOG.md)

## 👥 贡献指南

欢迎贡献代码、提出问题或建议！

## 📄 许可证

[待定]

## 📧 联系方式

项目负责人: [待填写]

---

**当前版本**: v1.0-SNAPSHOT  
**最后更新**: 2025-01-14
