package com.example.kafkademo.listener;

import com.example.kafkademo.config.KafkaConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ConsumerListener {

    /**
     * 批量消费
     *
     * @param records
     */
    @KafkaListener(topics = {KafkaConstants.TOPIC_TASK}, groupId = KafkaConstants.DEFAULT_GROUP)
    public void ConsumerListen(List<ConsumerRecord<?, ?>> records) {

        for (int i = 0; i < records.size(); i++) {
            log.info("========= consumer 消费了  ========= \n" + records.get(i).value());
        }


    }

    /**
     * 单次消费
     *
     * @param record
     */
    @KafkaListener(topics = {KafkaConstants.TOPIC_TASK}, groupId = KafkaConstants.DEFAULT_GROUP)
    public void ConsumerListen(ConsumerRecord<?, ?> record) {

        log.info("========= consumer 消费了  ========= \n" + record.value());


    }
}
