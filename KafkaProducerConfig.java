package vn.com.vpbanks.flex.usecase.service.common.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.serializer.JsonSerializer;
import vn.com.vpbanks.flex.usecase.service.common.utils.CommonUtils;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class KafkaProducerConfig {
    @Value("${vpbanks.kafka.server}")
    private String bootstrapServer;

    @Value("${vpbanks.kafka.properties.topics.flex.group}")
    private String groupId;

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        KafkaTemplate<String, String> kt = new KafkaTemplate(producerFactory());
        kt.setProducerListener(new ProducerListener<>() {
            @Override
            public void onSuccess(ProducerRecord<String, String> producerRecord, RecordMetadata recordMetadata) {
                log.debug("Successful push data to kafka, data: {}, topic: {}; partition: {} with offset {}",
                        producerRecord.value(),
                        recordMetadata.topic(),
                        recordMetadata.partition(),
                        recordMetadata.offset());
                ProducerListener.super.onSuccess(producerRecord, recordMetadata);
            }

            @Override
            public void onError(ProducerRecord<String, String> producerRecord, RecordMetadata recordMetadata, Exception exception) {
                log.error("error while push data to kafka, data {}, topic: {}, exception {}",
                        producerRecord.value(),
                        recordMetadata.topic(),
                        CommonUtils.handlerError(exception));
                ProducerListener.super.onError(producerRecord, recordMetadata, exception);
            }
        });

        return kt;
    }


    @Bean
    public KafkaTemplate<String, Object> kafkaObjectTemplate() {
        KafkaTemplate<String, Object> kafkaTemplate = new KafkaTemplate(producerFactory());
        kafkaTemplate.setProducerListener(new ProducerListener<>() {
            @Override
            public void onSuccess(ProducerRecord<String, Object> producerRecord, RecordMetadata recordMetadata) {
                log.debug("Successful push data to kafka, data: {}, topic: {}; partition: {} with offset {}",
                        producerRecord.value(),
                        recordMetadata.topic(),
                        recordMetadata.partition(),
                        recordMetadata.offset());
                ProducerListener.super.onSuccess(producerRecord, recordMetadata);
            }

            @Override
            public void onError(ProducerRecord<String, Object> producerRecord, RecordMetadata recordMetadata, Exception exception) {
                log.error("error while push data to kafka, data {}, topic: {}, exception {}",
                        producerRecord.value(),
                        recordMetadata.topic(),
                        CommonUtils.handlerError(exception));
                ProducerListener.super.onError(producerRecord, recordMetadata, exception);
            }
        });

        return kafkaTemplate;
    }
}
