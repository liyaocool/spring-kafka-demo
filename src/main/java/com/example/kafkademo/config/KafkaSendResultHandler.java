package com.example.kafkademo.config;


import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaSendResultHandler implements ProducerListener {

    @Override
    public void onSuccess(ProducerRecord producerRecord, RecordMetadata recordMetadata) {
        log.info("Listener send success:" + producerRecord.toString());
        ProducerListener.super.onSuccess(producerRecord, recordMetadata);
    }

    @Override
    public void onError(ProducerRecord producerRecord, RecordMetadata recordMetadata, Exception exception) {
        log.error("Listener send error:" + producerRecord.toString());
        ProducerListener.super.onError(producerRecord, recordMetadata, exception);
    }
}
