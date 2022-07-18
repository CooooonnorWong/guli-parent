package com.atguigu.guli.service.base.result;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Connor
 * @date 2022/7/18
 */
@Data
public class R {
    private Integer code;
    private String message;
    private Boolean success;
    /**
     * 如果map没有初始化 以后会有空指针问题
     */
    private Map<String, Object> data = new HashMap<>();

    /**
     * 静态创建成功R对象的方法
     */
    public static R ok() {
        R r = new R();
        r.setSuccess(ResultCodeEnum.SUCCESS.getSuccess());
        r.setCode(ResultCodeEnum.SUCCESS.getCode());
        r.setMessage(ResultCodeEnum.SUCCESS.getMessage());
        return r;
    }

    /**
     * 静态创建失败R对象的方法
     */
    public static R fail() {
        R r = new R();
        r.setSuccess(ResultCodeEnum.UNKNOWN_REASON.getSuccess());
        r.setCode(ResultCodeEnum.UNKNOWN_REASON.getCode());
        r.setMessage(ResultCodeEnum.UNKNOWN_REASON.getMessage());
        return r;
    }

    /**
     * 静态创建指定枚举类对象的R对象的方法
     */
    public static R setResult(ResultCodeEnum resultCodeEnum) {
        R r = new R();
        r.setCode(resultCodeEnum.getCode());
        r.setMessage(resultCodeEnum.getMessage());
        r.setSuccess(resultCodeEnum.getSuccess());
        return r;
    }

    /**
     * 修改R对象的属性值的方法
     */
    public R code(Integer code) {
        this.setCode(code);
        return this;
    }

    public R success(Boolean success) {
        this.setSuccess(success);
        return this;
    }

    public R message(String message) {
        this.setMessage(message);
        return this;
    }

    /**
     * 向data map中存入k-v
     */
    public R data(String key, Object val) {
        //获取data 存入kv
        this.getData().put(key, val);
        return this;
    }

    /**
     * 数据已经封装到一个map中  使用该map设置给data
     */
    public R data(Map<String, Object> map) {
        //获取data 存入kv
        this.setData(map);
        return this;
    }
}
