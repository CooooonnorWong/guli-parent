package com.atguigu.guli.service.edu.listener;

import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.service.CourseService;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @author Connor
 * @date 2022/8/6
 */
@Component
@Slf4j
public class MqOrderListener {
    @Autowired
    private CourseService courseService;
    @Autowired
    private Gson gson;

    //一个RabbitListener代表一个消费者方法
    @RabbitListener(
            //如果交换机队列存在并且已绑定，可以直接使用queues
            //如果队列和交换机可能不存在  使用bindings创建交换机队列 并指定绑定的key
            bindings = @QueueBinding(
                    //name:队列名称
                    // durable:是否持久化
                    value = @Queue(name = "edu.course.queue", durable = "true"),
                    //name:交换机名称
                    // ignoreDeclarationExceptions:忽略重复生成的异常
                    // type:交换机类型
                    exchange = @Exchange(name = "guli.order.exchange",
                            ignoreDeclarationExceptions = "true",
                            type = ExchangeTypes.TOPIC),
                    // key:匹配队列,词之间用.分割 #匹配任意个数词 *匹配一个词
                    key = "order.pay.#")
    )
    public void orderPaymentListener(Message message, String msg, Channel channel) throws IOException {
        try {
            //消费消息处理业务
            log.info("接收到消息：" + msg);
            Map map = gson.fromJson(msg, Map.class);
            String courseId = map.get("courseId").toString();
            Course course = courseService.getById(courseId);
            course.setBuyCount(course.getBuyCount() + 1);
            courseService.updateById(course);
            //手动ack
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            e.printStackTrace();
            //消息是重新归队后再次消费的
            if (message.getMessageProperties().isRedelivered()) {
                //重复消费有异常 丢弃消息
                //死信队列：最好绑定死信队列
                channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
            } else {
                //第一次有异常重试
                //参数1：消息id  参数2：是否批量处理  参数3：消息是否重新回调消息队列 true重新归队
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            }
        }
    }
}
