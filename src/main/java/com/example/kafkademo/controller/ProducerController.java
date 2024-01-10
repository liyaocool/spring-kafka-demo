package com.example.kafkademo.controller;

import com.example.kafkademo.config.KafkaConstants;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

@RestController
@Slf4j
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
    @GetMapping("/send/{msg}")
    public String send(@PathVariable String msg) {
        try {
            HashMap<String, String> msgMap = new HashMap<>();
            msgMap.put("orderId", "1");
            msgMap.put("taskName", msg);

//            kafkaTemplate.send(KafkaConstants.TOPIC_TASK, objectMapper.writeValueAsString(msgMap));
//            kafkaTemplate.sendDefault(msgMap);
            kafkaTemplate.send(KafkaConstants.TOPIC_TASK, msgMap).get();

            return "default send msg: " + msg;
        } catch (Exception e) {
            return "error";
        }
    }

    /**
     * 批量发送
     *
     * @param msg
     * @return
     */
    @GetMapping("/sendBatch/{msg}")
    public String sendBatch(@PathVariable String msg) {
        try {
            HashMap<String, String> msgMap = new HashMap<>();
            msgMap.put("orderId", "1");
            msgMap.put("taskName", msg);
            kafkaTemplate.send(KafkaConstants.TOPIC_BATCH_TASK, msgMap);

            HashMap<String, String> msgMap2 = new HashMap<>();
            msgMap2.put("orderId", "2");
            msgMap2.put("taskName", msg);
            kafkaTemplate.send(KafkaConstants.TOPIC_BATCH_TASK, msgMap2);

            HashMap<String, String> msgMap3 = new HashMap<>();
            msgMap3.put("orderId", "3");
            msgMap3.put("taskName", msg);
            kafkaTemplate.send(KafkaConstants.TOPIC_BATCH_TASK, msgMap3);
            return "batch send msg: " + msg;
        } catch (Exception e) {
            return "error";
        }
    }

    /**
     * 有回调的发送
     *
     * @param msg
     * @return
     */
    @GetMapping("/sendCallback/{msg}")
    public String sendCallback(@PathVariable String msg) {
        try {
            HashMap<String, String> msgMap = new HashMap<>();
            msgMap.put("orderId", "1");
            msgMap.put("taskName", msg);
            kafkaTemplate.send(KafkaConstants.TOPIC_CALLBACK_TASK, msg);
            return "callback send msg: " + msg;
        } catch (Exception e) {
            return "error";
        }
    }
}
