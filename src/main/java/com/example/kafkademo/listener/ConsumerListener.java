package com.example.kafkademo.listener;

import com.example.kafkademo.config.KafkaConstants;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
public class ConsumerListener {
    @Resource
    KafkaTemplate<Object, Object> kafkaTemplate;

    /**
     * 默认单次消费
     *
     * @param record
     */
    @KafkaListener(topics = {KafkaConstants.TOPIC_DEFAULT}, groupId = KafkaConstants.DEFAULT_GROUP)
    public void DefaultConsumerListen(ConsumerRecord<?, ?> record) {
        log.info("========= 默认单次消费  ========= \n" + record.value());
    }

    /**
     * 简单单次消费
     *
     * @param record
     */
    @KafkaListener(topics = {KafkaConstants.TOPIC_TASK}, groupId = KafkaConstants.DEFAULT_GROUP)
    public void ConsumerListen(ConsumerRecord<?, ?> record, Acknowledgment ack) {
        try {
           // 若未手动提交偏移 ack.acknowledge()， 重启服务后，消费者会再次消费
            ack.acknowledge();
            //ack.nack(Duration.ofSeconds(15000));
            log.info("========= 简单 单次消费  ========= \n" + record.value());
        } catch (Exception e) {
            log.error("error", e);
        }

    }

    /**
     * 批量消费
     *
     * @param records
     */
    @KafkaListener(topics = {KafkaConstants.TOPIC_BATCH_TASK}, groupId = KafkaConstants.BATCH_GROUP)
    public void BatchConsumerListen(List<ConsumerRecord<?, ?>> records) {
        for (int i = 0; i < records.size(); i++) {
            log.info("========= 批量消费 " + i + " ========= \n" + records.get(i).value());
        }
    }

    /**
     * 有回调的消费
     *
     * @param record
     */
    @KafkaListener(topics = {KafkaConstants.TOPIC_CALLBACK_TASK}, groupId = KafkaConstants.CALLBACK_GROUP)
    public void CallbackConsumerListen(ConsumerRecord<?, ?> record) {
        log.info("========= 有回调的消费  ========= \n" + record.value());
    }
}
