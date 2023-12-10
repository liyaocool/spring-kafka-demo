package com.example.kafkademo.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConsumerListener {

    @KafkaListener(topics = {"my-test"}, groupId = "default_group")
    public void ConsumerListen(String msg) {
        log.info("========= my-test topic consumer receive ========= \n" + msg);
    }
}
