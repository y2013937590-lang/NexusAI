-- NexusAI 数据库初始化脚本
-- Version: 1.0
-- Author: fyy
-- Date: 2025-01-14

-- =============================================
-- 1. 应用信息表
-- =============================================
CREATE TABLE IF NOT EXISTS `app_info` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '应用ID',
    `app_name` VARCHAR(100) NOT NULL COMMENT '应用名称',
    `app_key` VARCHAR(64) NOT NULL COMMENT '应用Key(唯一标识)',
    `app_secret` VARCHAR(128) NOT NULL COMMENT '应用Secret(加密存储)',
    `description` VARCHAR(500) COMMENT '应用描述',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    `owner` VARCHAR(100) COMMENT '应用负责人',
    `owner_email` VARCHAR(100) COMMENT '负责人邮箱',
    `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_app_key` (`app_key`),
    KEY `idx_status` (`status`),
    KEY `idx_created_time` (`created_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='应用信息表';

-- =============================================
-- 2. 应用配置表
-- =============================================
CREATE TABLE IF NOT EXISTS `app_config` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '配置ID',
    `app_id` BIGINT NOT NULL COMMENT '应用ID',
    `token_quota` BIGINT NOT NULL DEFAULT 1000000 COMMENT 'Token配额(每月)',
    `token_used` BIGINT NOT NULL DEFAULT 0 COMMENT '已使用Token数',
    `request_quota` INT NOT NULL DEFAULT 10000 COMMENT '请求配额(每天)',
    `request_used` INT NOT NULL DEFAULT 0 COMMENT '已使用请求数',
    `qps_limit` INT NOT NULL DEFAULT 10 COMMENT 'QPS限制',
    `concurrent_limit` INT NOT NULL DEFAULT 5 COMMENT '并发数限制',
    `enable_cache` TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用缓存: 0-否, 1-是',
    `enable_rag` TINYINT NOT NULL DEFAULT 0 COMMENT '是否启用RAG: 0-否, 1-是',
    `enable_tools` TINYINT NOT NULL DEFAULT 0 COMMENT '是否启用工具调用: 0-否, 1-是',
    `quota_reset_time` DATETIME COMMENT '配额重置时间',
    `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_app_id` (`app_id`),
    KEY `idx_quota_reset_time` (`quota_reset_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='应用配置表';

-- =============================================
-- 3. 模型配置表
-- =============================================
CREATE TABLE IF NOT EXISTS `model_config` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '模型配置ID',
    `model_name` VARCHAR(100) NOT NULL COMMENT '模型名称',
    `model_type` VARCHAR(50) NOT NULL COMMENT '模型类型: openai, azure, qwen, ernie',
    `model_version` VARCHAR(50) COMMENT '模型版本: gpt-4, gpt-3.5-turbo等',
    `api_endpoint` VARCHAR(200) NOT NULL COMMENT 'API端点',
    `api_key` VARCHAR(256) NOT NULL COMMENT 'API密钥(加密存储)',
    `max_tokens` INT DEFAULT 4096 COMMENT '最大Token数',
    `temperature` DECIMAL(3,2) DEFAULT 0.70 COMMENT '温度参数',
    `top_p` DECIMAL(3,2) DEFAULT 1.00 COMMENT 'Top-P参数',
    `timeout` INT DEFAULT 60000 COMMENT '超时时间(毫秒)',
    `retry_times` INT DEFAULT 3 COMMENT '重试次数',
    `priority` INT DEFAULT 0 COMMENT '优先级: 数字越大优先级越高',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    `is_default` TINYINT NOT NULL DEFAULT 0 COMMENT '是否默认: 0-否, 1-是',
    `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_model_type` (`model_type`),
    KEY `idx_status` (`status`),
    KEY `idx_priority` (`priority`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='模型配置表';

-- =============================================
-- 4. 会话表
-- =============================================
CREATE TABLE IF NOT EXISTS `chat_session` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '会话ID',
    `session_id` VARCHAR(64) NOT NULL COMMENT '会话标识(UUID)',
    `app_id` BIGINT NOT NULL COMMENT '应用ID',
    `user_id` VARCHAR(100) COMMENT '用户ID(业务系统用户标识)',
    `title` VARCHAR(200) COMMENT '会话标题',
    `model_id` BIGINT COMMENT '使用的模型ID',
    `message_count` INT NOT NULL DEFAULT 0 COMMENT '消息数量',
    `token_used` BIGINT NOT NULL DEFAULT 0 COMMENT '消耗Token数',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-已结束, 1-进行中',
    `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_session_id` (`session_id`),
    KEY `idx_app_id` (`app_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_created_time` (`created_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会话表';

-- =============================================
-- 5. 消息表
-- =============================================
CREATE TABLE IF NOT EXISTS `chat_message` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '消息ID',
    `message_id` VARCHAR(64) NOT NULL COMMENT '消息标识(UUID)',
    `session_id` VARCHAR(64) NOT NULL COMMENT '会话标识',
    `role` VARCHAR(20) NOT NULL COMMENT '角色: user, assistant, system',
    `content` TEXT NOT NULL COMMENT '消息内容',
    `model_id` BIGINT COMMENT '使用的模型ID',
    `prompt_tokens` INT DEFAULT 0 COMMENT '输入Token数',
    `completion_tokens` INT DEFAULT 0 COMMENT '输出Token数',
    `total_tokens` INT DEFAULT 0 COMMENT '总Token数',
    `cost` DECIMAL(10,6) DEFAULT 0.000000 COMMENT '成本(美元)',
    `duration` INT DEFAULT 0 COMMENT '耗时(毫秒)',
    `finish_reason` VARCHAR(50) COMMENT '结束原因: stop, length, content_filter',
    `error_message` VARCHAR(500) COMMENT '错误信息',
    `metadata` JSON COMMENT '扩展元数据',
    `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_message_id` (`message_id`),
    KEY `idx_session_id` (`session_id`),
    KEY `idx_role` (`role`),
    KEY `idx_created_time` (`created_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息表';

-- =============================================
-- 6. 用量记录表
-- =============================================
CREATE TABLE IF NOT EXISTS `usage_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    `app_id` BIGINT NOT NULL COMMENT '应用ID',
    `session_id` VARCHAR(64) COMMENT '会话标识',
    `user_id` VARCHAR(100) COMMENT '用户ID',
    `api_path` VARCHAR(200) NOT NULL COMMENT 'API路径',
    `method` VARCHAR(10) NOT NULL COMMENT '请求方法',
    `model_id` BIGINT COMMENT '模型ID',
    `model_name` VARCHAR(100) COMMENT '模型名称',
    `prompt_tokens` INT DEFAULT 0 COMMENT '输入Token数',
    `completion_tokens` INT DEFAULT 0 COMMENT '输出Token数',
    `total_tokens` INT DEFAULT 0 COMMENT '总Token数',
    `cost` DECIMAL(10,6) DEFAULT 0.000000 COMMENT '成本(美元)',
    `duration` INT DEFAULT 0 COMMENT '耗时(毫秒)',
    `status_code` INT COMMENT 'HTTP状态码',
    `error_message` VARCHAR(500) COMMENT '错误信息',
    `ip_address` VARCHAR(50) COMMENT '客户端IP',
    `user_agent` VARCHAR(500) COMMENT 'User-Agent',
    `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_app_id` (`app_id`),
    KEY `idx_session_id` (`session_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_api_path` (`api_path`),
    KEY `idx_created_time` (`created_time`),
    KEY `idx_cost` (`cost`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用量记录表';

-- =============================================
-- 初始化数据
-- =============================================

-- 插入默认模型配置(示例,需要替换实际的API Key)
INSERT INTO `model_config` 
(`model_name`, `model_type`, `model_version`, `api_endpoint`, `api_key`, `max_tokens`, `temperature`, `status`, `is_default`, `priority`) 
VALUES 
('GPT-4', 'openai', 'gpt-4', 'https://api.openai.com/v1', 'your-api-key-here', 8192, 0.7, 1, 1, 100),
('GPT-3.5 Turbo', 'openai', 'gpt-3.5-turbo', 'https://api.openai.com/v1', 'your-api-key-here', 4096, 0.7, 1, 0, 90);

-- 插入测试应用
INSERT INTO `app_info` 
(`app_name`, `app_key`, `app_secret`, `description`, `owner`, `owner_email`, `status`) 
VALUES 
('测试应用', 'test_app_key_001', 'test_app_secret_001', '用于开发测试的应用', 'Admin', 'admin@nexusai.io', 1);

-- 为测试应用创建配置
INSERT INTO `app_config` 
(`app_id`, `token_quota`, `request_quota`, `qps_limit`, `concurrent_limit`) 
VALUES 
(1, 10000000, 100000, 100, 10);
