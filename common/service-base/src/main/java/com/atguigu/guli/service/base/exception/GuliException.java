package com.atguigu.guli.service.base.exception;

import com.atguigu.guli.service.base.result.ResultCodeEnum;
import lombok.Data;
import lombok.ToString;

/**
 * @author Connor
 * @date 2022/7/23
 */
@Data
@ToString
public class GuliException extends RuntimeException {
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 消息
     */
    private String msg;

    /**
     * 接受状态码和消息
     *
     * @param code    状态码
     * @param message 消息
     */
    public GuliException(Integer code, String message) {
        //将接受的msg设置给父类的message属性
        super(message);
        this.msg = message;
        this.code = code;
    }

    public GuliException(String message, Integer code, Exception e) {
        //将接受的msg设置给父类的message属性
        super(e);
        this.msg = message;
        this.code = code;
    }

    /**
     * 接收枚举类型
     *
     * @param resultCodeEnum 结果
     */
    public GuliException(ResultCodeEnum resultCodeEnum) {
        //将接受的msg设置给父类的message属性
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    /**
     * 参数3：为了接受出现的真正的异常对象，方便异常处理器获取异常的堆栈日志消息输出
     *
     * @param codeEnum 结果
     * @param e        异常
     */
    public GuliException(ResultCodeEnum codeEnum, Exception e) {
        //将接受的msg设置给父类的message属性
        super(e);
        this.msg = codeEnum.getMessage();
        this.code = codeEnum.getCode();
    }
}
