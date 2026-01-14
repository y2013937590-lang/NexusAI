package io.github.fyy.nexusai.common.model;

import io.github.fyy.nexusai.common.constant.CommonConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 统一响应结果
 * 
 * @author fyy
 * @since 2025-01-14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 时间戳
     */
    private Long timestamp;

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 成功返回结果
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 成功返回结果
     */
    public static <T> Result<T> success(T data) {
        return success(CommonConstants.SUCCESS_MSG, data);
    }

    /**
     * 成功返回结果
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(CommonConstants.SUCCESS, message, data);
    }

    /**
     * 失败返回结果
     */
    public static <T> Result<T> fail() {
        return fail(CommonConstants.FAIL_MSG);
    }

    /**
     * 失败返回结果
     */
    public static <T> Result<T> fail(String message) {
        return fail(CommonConstants.FAIL, message);
    }

    /**
     * 失败返回结果
     */
    public static <T> Result<T> fail(Integer code, String message) {
        return new Result<>(code, message, null);
    }
}
