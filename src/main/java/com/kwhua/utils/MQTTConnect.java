package com.kwhua.utils;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Component;

/**
 * mqtt 工具类操作
 */
@Slf4j
@Component
public class MQTTConnect {
    private String HOST = "tcp://127.0.0.1:1883";
    private final String clintId = "DC"+ (int) (Math.random() * 100000000);
    private MqttClient mqttClient;

    public void setMqttClient(String userName, String password, MqttCallback mqttCallback) throws MqttException {
        MqttConnectOptions options = mqttConnectOptions(userName,password);
        if (mqttCallback == null){
            mqttClient.setCallback(new Callback());
        }else {
            mqttClient.setCallback(mqttCallback);
        }
        mqttClient.connect(options);
    }
/**
 * mqtt连接参数设置
 */
private MqttConnectOptions mqttConnectOptions (String username,String password) throws MqttException {
    mqttClient = new MqttClient(HOST,clintId,new MemoryPersistence());
    MqttConnectOptions options = new MqttConnectOptions();
    options.setUserName(username);
    options.setPassword(password.toCharArray());
    options.setConnectionTimeout(10);//默认是30毫秒
    options.setAutomaticReconnect(true);//默认是false，设置断开自动重连
    options.setCleanSession(false); //默认是true
    options.setKeepAliveInterval(20);//默认是60
    return options;
}

/**
 * 关闭mqtt连接
 */
public void close() throws MqttException {
    mqttClient.close();
    mqttClient.disconnect();
}

/**
 * 向某个主题发布消息
 * 默认的QOS为0
 */
public void pub(String topic , String msg) throws MqttException {
    MqttMessage mqttMessage = new MqttMessage();
    mqttMessage.setPayload(msg.getBytes());
    MqttTopic mqttTopic = mqttClient.getTopic(topic);
    MqttDeliveryToken token = mqttTopic.publish(mqttMessage);
    token.waitForCompletion();
}

    /**
     * 向某个主题发布消息
     * qos 消息质量 ，0、1、2
     */
    public void pub(String topic , String msg ,int qos) throws MqttException {
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setQos(qos);
        mqttMessage.setPayload(msg.getBytes());
        MqttTopic mqttTopic = mqttClient.getTopic(topic);
        MqttDeliveryToken token = mqttTopic.publish(mqttMessage);
        token.waitForCompletion();
    }

    /**
     * 订阅某一个主题，此主题的qos等级为1
     * @param topic
     */
    public void sub(String topic) throws MqttException {
        mqttClient.subscribe(topic);
    }

    /**
     * 订阅一个主题，可携带qos
     */
    public void sub(String topic , int qos) throws MqttException {
        mqttClient.subscribe(topic,qos);
    }

    /**
     * main方法测试用
     * @param args
     */
    public static void main(String[] args) throws MqttException {
       MQTTConnect mqttConnect = new MQTTConnect();
       mqttConnect.setMqttClient("kwhua","123456" ,new Callback());
//       mqttConnect.sub("com/kwhua/init");
        mqttConnect.sub("$SYS/brokers/+/clients/#");
        mqttConnect.pub("$SYS/brokers/+/clients/#","kwhua" + (int) (Math.random() * 100000000));

    }
}
