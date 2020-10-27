package com.kwhua.mqtt;

import com.kwhua.utils.Callback;
import com.kwhua.utils.MQTTConnect;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MQTTListener implements ApplicationListener<ContextRefreshedEvent> {
    private final MQTTConnect server;
    @Autowired
    public  MQTTListener(MQTTConnect server){
        this.server = server;
    }
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            server.setMqttClient("kwhua","123456",new Callback());
            server.sub("$SYS/brokers/+/clients/#");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
