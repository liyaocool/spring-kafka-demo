package com.example.kafkademo.controller;

import jakarta.annotation.Resource;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Properties;

@RestController
@RequestMapping("/p")
public class ProducerController {
    @Resource
    KafkaTemplate<Object, Object> kafkaTemplate;

    /**
     * spring kafka 发送
     *
     * @param msg
     * @return
     */
    @GetMapping("/easy_send")
    public String easySend(String msg) {

        kafkaTemplate.send("my-test", msg);
        return "easy send msg: " + msg;
    }

    /**
     * 原始发送
     *
     * @param msg
     * @return
     */
    @GetMapping("/send")
    public String send(String msg) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9093,localhost:9094,localhost:9095");
        props.put("linger.ms", 1);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);
        for (int i = 0; i < 100; i++)
            producer.send(new ProducerRecord<String, String>("my-test", Integer.toString(i), Integer.toString(i)));

        producer.close();
        return "easy send msg: " + msg;
    }
}
