package com.kwhua.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * mqtt 回调函数
 */
@Slf4j
public class Callback implements MqttCallback {

    /**
     * MQTT断开会执行此方法
     * @param throwable
     */

    @Override
    public void connectionLost(Throwable throwable) {
        log.info("断开了MQTT连接"+ throwable.getMessage());
        log.error(throwable.getMessage(),throwable);
    }

    /**
     * subcribe订阅成功后得到的消息会执行到这里
     */
    @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            log.info("收到来自"+topic+"的消息"+new String(message.getPayload()));
            String msg = new String(message.getPayload());
            try {
                JSONObject jsonObject = JSON.parseObject(msg);
                String clientId = String.valueOf(jsonObject.get("clientid"));
                if (topic.endsWith("disconnected")) {
                    log.info("客户端已掉线：{}",clientId);
                } else {
                    log.info("客户端已上线：{}",clientId);
                }
            } catch (JSONException e) {
                log.error("JSON Format Parsing Exception : {}", msg);
            }
    }

    /**
     * publish发布成功后会执行到这里
     * @param token
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    log.info("发布消息成功");
    }
}
