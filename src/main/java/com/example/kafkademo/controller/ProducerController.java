package com.example.kafkademo.controller;

import com.example.kafkademo.config.KafkaConstants;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Properties;

@RestController
@RequestMapping("/p")
public class ProducerController {
    @Resource
    KafkaTemplate<Object, Object> kafkaTemplate;

    @Resource
    ObjectMapper objectMapper;

    /**
     * 原始发送
     *
     * @param msg
     * @return
     */
    @GetMapping("/send")
    public String send(String msg) {
        try {
            HashMap<String, String> msgMap = new HashMap<>();
            msgMap.put("orderId", "1");
            msgMap.put("taskName", msg);

//            kafkaTemplate.send(KafkaConstants.TOPIC_TASK, objectMapper.writeValueAsString(msgMap));
            kafkaTemplate.send(KafkaConstants.TOPIC_TASK, msgMap);

            // //测试批量生产消费
//            HashMap<String, String> msgMap2 = new HashMap<>();
//            msgMap2.put("orderId", "2");
//            msgMap2.put("taskName", msg);
//            kafkaTemplate.send(KafkaConstants.TOPIC_TASK, msgMap2);
//
//            HashMap<String, String> msgMap3 = new HashMap<>();
//            msgMap3.put("orderId", "3");
//            msgMap3.put("taskName", msg);
//            kafkaTemplate.send(KafkaConstants.TOPIC_TASK, msgMap3);
            return "easy send msg: " + msg;
        } catch (Exception e) {
            return "error";
        }


    }
}
