package com.example.kafkademo.config;

public class KafkaConstants {

    /**
     * 默认任务主题
     */
    public static final String TOPIC_DEFAULT = "default_topic";

    /**
     * 默认消费分组
     */
    public static final String DEFAULT_GROUP = "default_group";

    /**
     * 任务主题
     */
    public static final String TOPIC_TASK = "task_topic";

    /**
     * 批量任务主题
     */
    public static final String TOPIC_BATCH_TASK = "batch_task_topic";

    /**
     * 批量消费分组
     */
    public static final String BATCH_GROUP = "batch_group";


    /**
     * 有回调的任务主题
     */
    public static final String TOPIC_CALLBACK_TASK = "callback_task_topic";

    /**
     * 有回调的消费分组
     */
    public static final String CALLBACK_GROUP = "callback_group";


}
